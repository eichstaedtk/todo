package de.eichstaedt.todos.infrastructure.persistence;

import com.google.firebase.firestore.DocumentSnapshot;
import de.eichstaedt.todos.domain.ToDo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FirebaseDocumentMapper {

  private static final String DATE_FORMAT = "dd.mm.yyyy HH:MM:ss";
  public static final String TODO_ID = "id";
  public static final String TODO_NAME = "name";
  public static final String TODO_BESCHREIBUNG = "beschreibung";
  public static final String TODO_FAELLIG = "faellig";

  public static Map<String, String> mapToDoToFirebaseDocument(ToDo todo) {
    Map<String,String> value = new HashMap<>();
    value.put(TODO_ID,todo.getId());
    value.put(TODO_NAME,todo.getName());
    value.put(TODO_BESCHREIBUNG,todo.getBeschreibung());
    value.put(TODO_FAELLIG,todo.getFaellig().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
    return value;
  }

  public static ToDo mapFirebaseDocumentToToDo(DocumentSnapshot documentSnapshot) {
   return  new ToDo(documentSnapshot.getId(), documentSnapshot.getString("name"),
        documentSnapshot.getString("beschreibung"),
        LocalDateTime.parse(documentSnapshot.getString("faellig"),
            DateTimeFormatter.ofPattern(DATE_FORMAT)));
  }

}
