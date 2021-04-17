package de.eichstaedt.todos.infrastructure.persistence;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import de.eichstaedt.todos.domain.ToDo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

  protected static final String logger = ToDoDataService.class.getName();

  private final FirebaseFirestore
      firestore = FirebaseFirestore.getInstance();

  private final ToDoDatabase localDatabase;

  private static final String DATE_FORMAT = "dd.mm.yyyy HH:MM:ss";

  public void readData(RepositoryCallback callback) {

    localDatabase.toDoDAO().getAllAsync().toObservable().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(localToDos -> firestore.collection(COLLECTION_PATH)
        .get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            Log.i(logger,"Got Successful Documents from Google Firebase "+task.getResult().size());

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
            Log.e(logger,"Error Loading Data from Google Firebase ",task.getException());
            callback.onComplete(localToDos, "Offline: "+task.getException().getMessage()+" Daten lokal geladen");
          }
        }));

  }

  private Function<DocumentSnapshot, ToDo> mapDocumentSnapshotToDo() {
    return (d) -> {
      Log.i(logger,"MAP Document "+d.toString());
      return new ToDo(d.getId(),d.getString("name"),d.getString("beschreibung"),
        LocalDateTime.parse(d.getString("faellig"),DateTimeFormatter.ofPattern(DATE_FORMAT)));};
  }

  private List<ToDo> saveFireBaseDocumentInLocalDatabase(
      List<DocumentSnapshot> documents) {

    List<ToDo> todos = documents.stream().map(mapDocumentSnapshotToDo()).collect(Collectors.toList());

    localDatabase.toDoDAO().insertAllAsync(todos);

    return todos;
  }

  private void saveLocalToDoInFirebase(List<ToDo> localToDos) {
    localToDos.stream().map(todo -> {
      Map<String,String> value = new HashMap<>();
      value.put("id",todo.getId());
      value.put("name",todo.getName());
      value.put("beschreibung",todo.getBeschreibung());
      value.put("faellig",todo.getFaellig().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
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
