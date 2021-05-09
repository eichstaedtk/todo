package de.eichstaedt.todos.infrastructure.persistence;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapFirebaseDocumentToToDo;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapToDoToFirebaseDocument;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapUserToFirebaseDocument;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.stream.Collectors;

/**
 * Class which handle local or remote persistence solution for ToDos entities
 */

public class ToDoDataService {

  protected static final String logger = ToDoDataService.class.getName();

  public static final String COLLECTION_PATH = "todos";

  public ToDoDataService(@NonNull ToDoDatabase toDoDatabase) {
    this.localDatabase = toDoDatabase;
  }

  private boolean offline = true;

  protected static final String LOGGER = ToDoDataService.class.getName();

  private final FirebaseFirestore
      firestore = FirebaseFirestore.getInstance();

  private final ToDoDatabase localDatabase;

  public void readToDos(RepositoryCallback callback) {

    Observable.fromCallable(() -> localDatabase.toDoDAO().getAll()).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(localToDos -> firestore.collection(COLLECTION_PATH).get().addOnCompleteListener(task -> {
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

  public void saveUser(User user, ReloadViewCallback callback) {
    Completable.fromAction(()-> localDatabase.toDoDAO().insertUser(user)).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
      saveUserInFirebase(user);
      if(callback != null) {
        callback.onComplete("ToDo gespeichert " + user.getName());
      }
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
      firestore.collection(COLLECTION_PATH).add(mapToDoToFirebaseDocument(toDo))
          .addOnSuccessListener(
              documentReference -> Log
                  .d(LOGGER, "Save ToDo on Firebase " + documentReference.getId()))
          .addOnFailureListener(e -> Log.w(LOGGER, "Error saving ToDo on Firebase", e));
    }
  }

  private void saveUserInFirebase(User user) {
    if (!offline) {
      firestore.collection(COLLECTION_PATH).add(mapUserToFirebaseDocument(user))
          .addOnSuccessListener(
              documentReference -> Log
                  .d(LOGGER, "Save ToDo on Firebase " + documentReference.getId()))
          .addOnFailureListener(e -> Log.w(LOGGER, "Error saving ToDo on Firebase", e));
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
    }).forEach(d -> firestore.collection(COLLECTION_PATH).add(d).addOnSuccessListener(
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
    firestore.collection(COLLECTION_PATH).document(toDo.getId()).delete();
  }

  private void deleteAllFirebaseToDos(
      List<DocumentSnapshot> documents) {
            documents.stream().forEach(d ->
                firestore.collection(COLLECTION_PATH).document(d.getId()).delete());
            Log.i(logger,"Deleted all firebase documents");

  }

  public void deleteAllFirebaseToDos() {
    firestore.collection(COLLECTION_PATH)
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
