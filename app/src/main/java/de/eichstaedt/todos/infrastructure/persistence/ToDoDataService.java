package de.eichstaedt.todos.infrastructure.persistence;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.FirebaseFirestore;
import de.eichstaedt.todos.MainActivity;
import de.eichstaedt.todos.domain.ToDo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Class which handle local or remote persistence solution for ToDos entities
 */

public class ToDoDataService {

  public ToDoDataService(@NonNull ToDoDatabase toDoDatabase) {
    this.localDatabase = toDoDatabase;
  }

  private boolean offline = true;

  protected static final String logger = ToDoDataService.class.getName();

  private final FirebaseFirestore
      firestore = FirebaseFirestore.getInstance();

  private final ToDoDatabase localDatabase;

  public void readData(RepositoryCallback callback) {

    firestore.collection("todos")
        .get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            Log.i(logger,"Got Successful Documents from Google Firebase "+task.getResult().size());
            callback.onComplete(task.getResult().getDocuments()
                .stream()
                .map(d -> new ToDo(d.getId(),d.getString("name"),d.getString("beschreibung"),LocalDateTime.now()))
                .collect(Collectors.toList()),"Online: Daten erfolgreich geladen");
            offline = false;

          } else {
            Log.e(logger,"Error Loading Data from Google Firebase ",task.getException());
            callback.onComplete(localDatabase.toDoDAO().getAllAsync().blockingGet(), "Offline: "+task.getException().getMessage()+" Daten lokal geladen");
          }
        });
  }

  public boolean isOffline() {
    return offline;
  }
}
