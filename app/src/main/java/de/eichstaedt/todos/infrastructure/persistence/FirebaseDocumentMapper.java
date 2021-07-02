package de.eichstaedt.todos.infrastructure.persistence;

import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import de.eichstaedt.todos.domain.entities.ToDo;
import de.eichstaedt.todos.domain.entities.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FirebaseDocumentMapper {

  public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String BESCHREIBUNG = "beschreibung";
  public static final String FAELLIG = "faellig";
  public static final String EMAIL = "email";
  public static final String PASSWORD = "password";
  public static final String MOBIL = "mobil";
  public static final String WICHTIG = "wichtig";
  public static final String ERLEDIGT = "erledigt";
  public static final String KONTAKTE = "kontakte";

  private static final String logger = FirebaseDocumentMapper.class.getName();

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
    value.put(WICHTIG,todo.isWichtig() ? "true": "false");
    value.put(ERLEDIGT,todo.isErledigt() ? "true": "false");
    value.put(KONTAKTE, new Gson().toJson(todo.getKontakte()));
    if(todo.getFaellig() != null) {
      value.put(FAELLIG, todo.getFaellig().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
    }
    return value;
  }

  public static ToDo mapFirebaseDocumentToToDo(DocumentSnapshot documentSnapshot) {

    Log.i(logger,"Map Firebase Document"+documentSnapshot.toString());

     return  new ToDo(documentSnapshot.getString(ID), documentSnapshot.getString(NAME),
        documentSnapshot.getString(BESCHREIBUNG),
        LocalDateTime.parse(documentSnapshot.getString(FAELLIG),
            DateTimeFormatter.ofPattern(DATE_FORMAT)), Boolean.parseBoolean(documentSnapshot.getString(WICHTIG)),
         Boolean.parseBoolean(documentSnapshot.getString(ERLEDIGT)),new Gson().fromJson(documentSnapshot.getString(KONTAKTE),
         Set.class));
  }

  public static User mapFirebaseDocumentToUser(DocumentSnapshot documentSnapshot) {
    return  new User(documentSnapshot.getString(ID), documentSnapshot.getString(NAME),
        documentSnapshot.getString(EMAIL),
        Integer.parseInt(documentSnapshot.getString(PASSWORD)), documentSnapshot.getString(MOBIL));
  }

}
