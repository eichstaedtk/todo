package de.eichstaedt.todos.infrastructure.persistence;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.*;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapFirebaseDocumentToToDo;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapToDoToFirebaseDocument;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapUserToFirebaseDocument;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.stream.Collectors;

/**
 * Class which handle local or remote persistence solution for ToDos entities
 */

public class DataService {

  protected static final String logger = DataService.class.getName();

  public static final String TODO_COLLECTION_PATH = "todos";

  public static final String USER_COLLECTION_PATH = "user";

  public DataService(@NonNull ToDoDatabase toDoDatabase) {
    this.localDatabase = toDoDatabase;
  }

  private boolean offline = true;

  protected static final String LOGGER = DataService.class.getName();

  private final FirebaseFirestore
      firestore = FirebaseFirestore.getInstance();

  private final ToDoDatabase localDatabase;

  public void readToDos(ToDoRepositoryCallback callback) {

    Observable.fromCallable(() -> localDatabase.toDoDAO().getAll()).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(localToDos -> firestore.collection(TODO_COLLECTION_PATH).get().addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            Log.i(LOGGER,"Got Successful Documents from Google Firebase "+task.getResult().size() + " and local todos "+localToDos.size());

            List<ToDo> result = new ArrayList<>();

            if(localToDos.isEmpty())
            {
              Log.i(logger,"UseCase Online but no local todos "+localToDos.size());
              result.addAll(saveFireBaseDocumentInLocalDatabase(task.getResult().getDocuments()));
            }else {
              Log.i(logger,"UseCase Online but local todos");
              Log.i(logger,"Delete All Remote "+task.getResult().getDocuments().size());
              deleteAllFirebaseToDos(task.getResult().getDocuments());
              Log.i(logger,"Save local to Firebase "+ localToDos.size());
              saveLocalToDoInFirebase(localToDos);
              result.addAll(localToDos);
            }

            callback.onComplete(result,"Online: Daten erfolgreich geladen");
            offline = false;

          } else {
            Log.e(LOGGER,"Error Loading Data from Google Firebase ",task.getException());
            callback.onComplete(localToDos, "Offline: "+task.getException().getMessage()+" Daten lokal geladen");
          }
        }));

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

  public void findUserByEmail(String email, UserRepositoryCallback callback) {
    firestore.collection(USER_COLLECTION_PATH)
        .whereEqualTo("email", email)
        .get()
        .addOnCompleteListener(task -> {
          Log.i(logger,"Finding user in firebase "+task.isSuccessful()+ " Docs "+task.getResult().size());
          if (task.isSuccessful() && task.getResult().size() > 0) {
            for (QueryDocumentSnapshot document : task.getResult()) {
              callback.onComplete(Optional.of(mapFirebaseDocumentToUser(document)));
            }
          } else {
            callback.onComplete(Optional.empty());
          }
        }).addOnFailureListener((task) -> {

          Log.i(logger,"Error finding user in firebase ", task.getCause());
          callback.onComplete(Optional.empty());
        });

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
    if (!offline) {
      firestore.collection(TODO_COLLECTION_PATH).add(mapToDoToFirebaseDocument(toDo))
          .addOnSuccessListener(
              documentReference -> Log
                  .d(LOGGER, "Save ToDo on Firebase " + documentReference.getId()))
          .addOnFailureListener(e -> Log.w(LOGGER, "Error saving ToDo on Firebase", e));
    }
  }

  public void saveUserInFirebase(User user) {
    Log.i(LOGGER, "Save User on Firebase " +user);

      firestore.collection(USER_COLLECTION_PATH).add(mapUserToFirebaseDocument(user))
          .addOnSuccessListener(
              documentReference -> Log
                  .d(LOGGER, "Save User on Firebase " + documentReference.getId()))
          .addOnFailureListener(e -> Log.w(LOGGER, "Error saving User on Firebase", e));

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

  public void deleteAllLokalToDos(ToDoRepositoryCallback callback) {
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
    firestore.collection(TODO_COLLECTION_PATH).document(toDo.getId()).delete();
  }

  private void deleteAllFirebaseToDos(
      List<DocumentSnapshot> documents) {
            documents.stream().forEach(d ->
                firestore.collection(TODO_COLLECTION_PATH).document(d.getId()).delete());
            Log.i(logger,"Deleted all firebase documents");

  }

  private void deleteAllFirebaseUser(
      List<DocumentSnapshot> documents) {
    documents.stream().forEach(d ->
        firestore.collection(USER_COLLECTION_PATH).document(d.getId()).delete());
    Log.i(logger,"Deleted all firebase user");

  }

  public void deleteAllFirebaseToDos() {
    firestore.collection(TODO_COLLECTION_PATH)
        .get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            deleteAllFirebaseToDos(task.getResult().getDocuments());
          }
        });
  }

  public boolean isOffline() {
    return offline;
  }
}
