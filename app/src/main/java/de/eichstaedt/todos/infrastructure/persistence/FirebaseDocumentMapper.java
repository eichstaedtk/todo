package de.eichstaedt.todos.infrastructure.persistence;

import com.google.firebase.firestore.DocumentSnapshot;
import de.eichstaedt.todos.domain.ToDo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FirebaseDocumentMapper {

  public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
  public static final String TODO_ID = "id";
  public static final String TODO_NAME = "name";
  public static final String TODO_BESCHREIBUNG = "beschreibung";
  public static final String TODO_FAELLIG = "faellig";

  public static Map<String, String> mapToDoToFirebaseDocument(ToDo todo) {
    Map<String,String> value = new HashMap<>();
    value.put(TODO_ID,todo.getId());
    value.put(TODO_NAME,todo.getName());
    value.put(TODO_BESCHREIBUNG,todo.getBeschreibung());
    if(todo.getFaellig() != null) {
      value.put(TODO_FAELLIG, todo.getFaellig().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
    }
    return value;
  }

  public static ToDo mapFirebaseDocumentToToDo(DocumentSnapshot documentSnapshot) {
     return  new ToDo(documentSnapshot.getString(TODO_ID), documentSnapshot.getString(TODO_NAME),
        documentSnapshot.getString(TODO_BESCHREIBUNG),
        LocalDateTime.parse(documentSnapshot.getString(TODO_FAELLIG),
            DateTimeFormatter.ofPattern(DATE_FORMAT)));
  }

}
