package de.eichstaedt.todos.infrastructure.persistence;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.google.firebase.firestore.DocumentSnapshot;
import de.eichstaedt.todos.domain.ToDo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseDocumentMapperTest {

  @Test
  public void testmapToDoToFirebaseDocument() {

    LocalDateTime today = LocalDateTime.of(2021,4,18,13,20,00);

    ToDo einkaufen = new ToDo("1","Einkaufen","Wocheneinkauf", today);

    Map<String,String> firebaseObject = FirebaseDocumentMapper.mapToDoToFirebaseDocument(einkaufen);

    assertEquals(firebaseObject.get(FirebaseDocumentMapper.TODO_ID),einkaufen.getId());
    assertEquals(firebaseObject.get(FirebaseDocumentMapper.TODO_NAME),einkaufen.getName());
    assertEquals(firebaseObject.get(FirebaseDocumentMapper.TODO_BESCHREIBUNG),einkaufen.getBeschreibung());
    assertEquals(firebaseObject.get(FirebaseDocumentMapper.TODO_FAELLIG),today.format(
        DateTimeFormatter.ofPattern(DATE_FORMAT)));
  }

  @Test
  public void testmapFirebaseDocumentToToDo() {

    LocalDateTime today = LocalDateTime.of(2021,4,18,13,20,00);

    DocumentSnapshot documentSnapshot = mock(DocumentSnapshot.class);
    when(documentSnapshot.getString(FirebaseDocumentMapper.TODO_ID)).thenReturn("1");
    when(documentSnapshot.getString(FirebaseDocumentMapper.TODO_NAME)).thenReturn("Einkaufen");
    when(documentSnapshot.getString(FirebaseDocumentMapper.TODO_BESCHREIBUNG)).thenReturn("Wocheneinkauf");
    when(documentSnapshot.getString(FirebaseDocumentMapper.TODO_FAELLIG)).thenReturn(today.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));

    ToDo einkaufen = FirebaseDocumentMapper.mapFirebaseDocumentToToDo(documentSnapshot);

    assertNotNull(einkaufen);
    assertEquals("1",einkaufen.getId());
    assertEquals("Einkaufen",einkaufen.getName());
    assertEquals("Wocheneinkauf",einkaufen.getBeschreibung());
    assertEquals(today,einkaufen.getFaellig());
  }
}