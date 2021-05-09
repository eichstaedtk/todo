package de.eichstaedt.todos.infrastructure.persistence;

import com.google.firebase.firestore.DocumentSnapshot;
import de.eichstaedt.todos.domain.ToDo;
import de.eichstaedt.todos.domain.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FirebaseDocumentMapper {

  public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String BESCHREIBUNG = "beschreibung";
  public static final String FAELLIG = "faellig";
  public static final String EMAIL = "email";
  public static final String PASSWORD = "password";
  public static final String MOBIL = "mobil";

  public static Map<String, String> mapUserToFirebaseDocument(User user) {
    Map<String,String> value = new HashMap<>();
    value.put(ID,user.getId());
    value.put(NAME,user.getName());
    value.put(EMAIL,user.getEmail());
    value.put(PASSWORD,String.valueOf(user.getPasswort()));
    value.put(MOBIL,user.getMobilnummer());

    return value;
  }


  public static Map<String, String> mapToDoToFirebaseDocument(ToDo todo) {
    Map<String,String> value = new HashMap<>();
    value.put(ID,todo.getId());
    value.put(NAME,todo.getName());
    value.put(BESCHREIBUNG,todo.getBeschreibung());
    if(todo.getFaellig() != null) {
      value.put(FAELLIG, todo.getFaellig().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
    }
    return value;
  }

  public static ToDo mapFirebaseDocumentToToDo(DocumentSnapshot documentSnapshot) {
     return  new ToDo(documentSnapshot.getString(ID), documentSnapshot.getString(NAME),
        documentSnapshot.getString(BESCHREIBUNG),
        LocalDateTime.parse(documentSnapshot.getString(FAELLIG),
            DateTimeFormatter.ofPattern(DATE_FORMAT)));
  }

}
