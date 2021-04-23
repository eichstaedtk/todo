package de.eichstaedt.todos.infrastructure.persistence;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapFirebaseDocumentToToDo;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapToDoToFirebaseDocument;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import de.eichstaedt.todos.MainActivity;
import de.eichstaedt.todos.domain.ToDo;
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

    localDatabase.toDoDAO().getAllAsync().toObservable().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(localToDos -> firestore.collection(COLLECTION_PATH)
        .get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            Log.i(LOGGER,"Got Successful Documents from Google Firebase "+task.getResult().size());

            List<ToDo> result = new ArrayList<>();

            if(!localToDos.isEmpty())
            {
              deleteAllFirebaseToDos();
              saveLocalToDoInFirebase(localToDos);
              result.addAll(localToDos);
            }else {
              result.addAll(saveFireBaseDocumentInLocalDatabase(task.getResult().getDocuments()));
            }

            callback.onComplete(result,"Online: Daten erfolgreich geladen");
            offline = false;

          } else {
            Log.e(LOGGER,"Error Loading Data from Google Firebase ",task.getException());
            callback.onComplete(localToDos, "Offline: "+task.getException().getMessage()+" Daten lokal geladen");
          }
        }));

  }

  public void saveToDo(ToDo toDo) {
    localDatabase.toDoDAO().insertAsync(toDo).doOnComplete(() -> {
      if(!offline) {
        firestore.collection(COLLECTION_PATH).add(mapToDoToFirebaseDocument(toDo)).addOnSuccessListener(
            documentReference -> Log.d(LOGGER, "Save ToDo on Firebase " + documentReference.getId()))
            .addOnFailureListener(e -> Log.w(LOGGER, "Error saving ToDo on Firebase", e));
      }
    });
  }

  private List<ToDo> saveFireBaseDocumentInLocalDatabase(
      List<DocumentSnapshot> documents) {

    List<ToDo> todos = documents.stream().map(d -> mapFirebaseDocumentToToDo(d)).collect(Collectors.toList());

    if(todos.isEmpty())
    {
      todos.add(new ToDo("Einkaufen","bei Kaufland", LocalDateTime.now().plusDays(7),true));
    }

    localDatabase.toDoDAO().insertAllAsync(todos);

    return todos;
  }

  private void saveLocalToDoInFirebase(List<ToDo> localToDos) {
    localToDos.stream().map(todo -> {
      Map<String, String> value = mapToDoToFirebaseDocument(todo);
      return  value;
    }).forEach(d -> firestore.collection(COLLECTION_PATH).add(d));
  }

  public void deleteAllLokalToDos(RepositoryCallback callback) {
    Log.i(logger,"Delete all lokal todos ...");
    localDatabase.toDoDAO().deleteAll().doOnComplete(() ->{
      Log.i(logger,"Deleted all lokal todos");
      callback.onComplete(new ArrayList<>(),"Es wurden alle Daten lokal gelöscht.");
    }).doOnError(e -> Log.e(logger,"Error during delete all lokal todos ...",e));
  }

  public void deleteAllFirebaseToDos() {

    firestore.collection(COLLECTION_PATH)
        .get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            task.getResult().getDocuments().stream().forEach(d ->
                firestore.collection(COLLECTION_PATH).document(d.getId()).delete());
            Log.i(logger,"Deleted all firebase documents");
          }else {
            Log.e(logger,"Error during delete all firebase documents!",task.getException());
          }
        });
  }

  public boolean isOffline() {
    return offline;
  }
}
