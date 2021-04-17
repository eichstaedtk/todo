package de.eichstaedt.todos.infrastructure.persistence;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapFirebaseDocumentToToDo;
import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.mapToDoToFirebaseDocument;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import de.eichstaedt.todos.domain.ToDo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class which handle local or remote persistence solution for ToDos entities
 */

public class ToDoDataService {

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

            List<DocumentSnapshot> firebaseDocuments = task.getResult().getDocuments();
            List<ToDo> result = new ArrayList<>();

            if(!localToDos.isEmpty())
            {
              deleteAllFirebaseToDos(firebaseDocuments);
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

    localDatabase.toDoDAO().insertAllAsync(todos);

    return todos;
  }

  private void saveLocalToDoInFirebase(List<ToDo> localToDos) {
    localToDos.stream().map(todo -> {
      Map<String, String> value = mapToDoToFirebaseDocument(todo);
      return  value;
    }).forEach(d -> firestore.collection(COLLECTION_PATH).add(d));
  }


  private void deleteAllFirebaseToDos(
      List<DocumentSnapshot> firebaseDocuments) {
    firebaseDocuments.stream().forEach(d ->
        firestore.collection(COLLECTION_PATH).document(d.getId()).delete());
  }

  public boolean isOffline() {
    return offline;
  }
}
