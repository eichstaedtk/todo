package de.eichstaedt.todos.infrastructure.persistence;

import static androidx.core.content.ContextCompat.getSystemService;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.*;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapFirebaseDocumentToToDo;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapToDoToFirebaseDocument;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapUserToFirebaseDocument;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import de.eichstaedt.todos.ReloadViewCallback;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.domain.User;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Class which handle local or remote persistence solution for ToDos entities
 */

public class DataService {

  private static DataService service;

  protected static final String logger = DataService.class.getName();

  public static final String TODO_COLLECTION_PATH = "todos";

  public static final String USER_COLLECTION_PATH = "user";

  private final Context context;

  private DataService(@NonNull ToDoDatabase toDoDatabase, Context context) {
    this.localDatabase = toDoDatabase;
    this.context = context;
    this.firestore = FirebaseFirestore.getInstance();
  }

  private DataService(@NonNull ToDoDatabase toDoDatabase, Context context,FirebaseFirestore firestore) {
    this.localDatabase = toDoDatabase;
    this.context = context;
    this.firestore = firestore;
  }

  public static DataService instance(@NonNull Context context) {

    if(service == null){
      service = new DataService(ToDoRepository.getInstance(context),context);
    }

    return service;
  }

  public static DataService instance(@NonNull Context context,FirebaseFirestore firestore) {

    if(service == null){
      service = new DataService(ToDoRepository.getInstance(context),context, firestore);
    }

    return service;
  }

  private boolean offline = true;

  protected static final String LOGGER = DataService.class.getName();

  private final FirebaseFirestore firestore;

  private final ToDoDatabase localDatabase;

  public void readToDos(RepositoryCallback callback) {

    Observable.fromCallable(() -> localDatabase.toDoDAO().getAll()).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(localToDos -> {
          if(!isOffline()) {
            firestore.collection(TODO_COLLECTION_PATH).get().addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                Log.i(LOGGER,
                    "Got Successful Documents from Google Firebase " + task.getResult().size()
                        + " and local todos " + localToDos.size());

                List<ToDo> result = new ArrayList<>();

                if (localToDos.isEmpty()) {
                  Log.i(logger, "UseCase Online but no local todos " + localToDos.size());
                  result
                      .addAll(saveFireBaseDocumentInLocalDatabase(task.getResult().getDocuments()));
                } else {
                  Log.i(logger, "UseCase Online but local todos");
                  Log.i(logger, "Delete All Remote " + task.getResult().getDocuments().size());
                  deleteFirebaseToDos(task.getResult().getDocuments());
                  Log.i(logger, "Save local to Firebase " + localToDos.size());
                  saveLocalToDoInFirebase(localToDos);
                  result.addAll(localToDos);
                }

                callback.onComplete(result, "Online: Daten erfolgreich geladen");

              } else {
                Log.e(LOGGER, "Error Loading Data from Google Firebase ", task.getException());
                callback.onComplete(localToDos,
                    "Offline: " + task.getException().getMessage() + " Daten lokal geladen");
              }
            });
          }else {
            Log.e(LOGGER, "Error Loading Data from Google Firebase ");
            callback.onComplete(localToDos,
                "Offline: Daten lokal geladen");
          }
        });

  }

  public void deleteToDo(ToDo toDo, ReloadViewCallback callback) {
    Completable.fromAction(()-> localDatabase.toDoDAO().delete(toDo)).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            deleteToDoInFirebase(toDo);
      if(callback != null) {
        callback.onComplete("ToDo gelöscht " + toDo.getName());
      }
    });
  }

  public void saveToDo(ToDo toDo, ReloadViewCallback callback) {
    Completable.fromAction(()-> localDatabase.toDoDAO().insertToDo(toDo)).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
      saveInFirebase(toDo);
      if(callback != null) {
        callback.onComplete("ToDo gespeichert " + toDo.getName());
      }
    });
  }

  public void findUserByEmailAndPasswort(String email,String password, UserRepositoryCallback callback) {
    if (!isOffline()) {
      firestore.collection(USER_COLLECTION_PATH)
          .whereEqualTo(EMAIL, email)
          .whereEqualTo(PASSWORD, password)
          .get()
          .addOnCompleteListener(task -> {

            Log.i(logger,
                "Finding user in firebase " + task.isSuccessful() + " Docs " + task.getResult()
                    .size());
            if (task.isSuccessful() && task.getResult().size() > 0) {
              for (QueryDocumentSnapshot document : task.getResult()) {
                callback.onComplete(Optional.of(mapFirebaseDocumentToUser(document)));
              }
            } else {
              callback.onComplete(Optional.empty());
            }
          }).addOnFailureListener((task) -> {

        Log.i(logger, "Error finding user in firebase ", task.getCause());
        callback.onComplete(Optional.empty());
      });
    }
  }


  public void updateToDo(ToDo toDo, ReloadViewCallback callback) {
    Completable.fromAction(()-> localDatabase.toDoDAO().update(toDo)).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
      saveInFirebase(toDo);
      if(callback != null) {
        callback.onComplete("ToDo gespeichert " + toDo.getName());
      }
    });
  }

  private void saveInFirebase(ToDo toDo) {
    if (!isOffline()) {
      firestore.collection(TODO_COLLECTION_PATH).add(mapToDoToFirebaseDocument(toDo))
          .addOnSuccessListener(
              documentReference -> Log
                  .d(LOGGER, "Save ToDo on Firebase " + documentReference.getId()))
          .addOnFailureListener(e -> Log.w(LOGGER, "Error saving ToDo on Firebase", e));
    }
  }

  public void saveUserInFirebase(User user) {
    if (!isOffline()) {
      Log.i(LOGGER, "Save User on Firebase " + user);

      firestore.collection(USER_COLLECTION_PATH).add(mapUserToFirebaseDocument(user))
          .addOnSuccessListener(
              documentReference -> Log
                  .d(LOGGER, "Save User on Firebase " + documentReference.getId()))
          .addOnFailureListener(e -> Log.w(LOGGER, "Error saving User on Firebase", e));
    }

  }


  private List<ToDo> saveFireBaseDocumentInLocalDatabase(
      List<DocumentSnapshot> documents) {

    List<ToDo> todos = documents.stream().map(d -> mapFirebaseDocumentToToDo(d)).collect(Collectors.toList());

    if(todos.isEmpty())
    {
      todos.add(new ToDo("Test Aufgabe","Schnell was erledigen", LocalDateTime.now().plusDays(3),true));
      saveLocalToDoInFirebase(todos);
    }

    Log.i(logger,"Save todos into local db .... "+todos.size());

    Completable.fromAction(() -> localDatabase.toDoDAO().insertTodos(todos)).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(()-> Log.i(logger,"Save firebase documents to local database"));

    return todos;
  }

  private void saveLocalToDoInFirebase(List<ToDo> localToDos) {
    localToDos.stream().map(todo -> {
      Map<String, String> value = mapToDoToFirebaseDocument(todo);
      return  value;
    }).forEach(d -> firestore.collection(TODO_COLLECTION_PATH).add(d).addOnSuccessListener(
        documentReference -> Log.d(LOGGER, "Save ToDo on Firebase " + documentReference.getId()))
        .addOnFailureListener(e -> Log.w(LOGGER, "Error saving ToDo on Firebase", e)));
  }

  public void deleteAllLokalToDos(RepositoryCallback callback) {
    Log.i(logger,"Delete all lokal todos ...");
    Completable.fromAction(() -> {localDatabase.toDoDAO().deleteAll();})
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> {Log.i(logger,"Deleted all lokal todos");
          if(callback != null) {
            callback.onComplete(new ArrayList<>(), "Es wurden alle Daten lokal gelöscht.");
          }
        });

  }

  private void deleteToDoInFirebase(ToDo toDo) {
    if(!isOffline()) {
      firestore.collection(TODO_COLLECTION_PATH).document(toDo.getId()).delete();
    }
  }

  private void deleteFirebaseToDos(
      List<DocumentSnapshot> documents) {
    if (!isOffline()) {
      documents.stream().forEach(d ->
          firestore.collection(TODO_COLLECTION_PATH).document(d.getId()).delete());
      Log.i(logger, "Deleted all firebase documents");
    }

  }


  public void deleteAllFirebaseToDos() {
    if(!isOffline()) {
      firestore.collection(TODO_COLLECTION_PATH)
          .get()
          .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              deleteFirebaseToDos(task.getResult().getDocuments());
            }
          });
    }
  }

  public boolean isOffline() {
    return offline;
  }

  public Future<Boolean> checkOfflineState() {

    ConnectivityManager connMgr = getSystemService(context, ConnectivityManager.class);
    NetworkCapabilities networkCapabilities = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
    CompletableFuture<Boolean> result = new CompletableFuture<>();

    if(networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI )
    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))) {

      Observable.fromCallable(() -> firestore.getFirestoreSettings().isPersistenceEnabled())
          .observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread())
          .subscribe((enabled) -> {
            Log.i(logger,"FIREBASE is ENABLED "+enabled);
            offline = !enabled;});
    }

    result.complete(offline);

    return result;
  }
}
