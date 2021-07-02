package de.eichstaedt.todos.infrastructure.persistence;

import static de.eichstaedt.todos.infrastructure.persistence.FirebaseDocumentMapper.DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.google.firebase.firestore.DocumentSnapshot;
import de.eichstaedt.todos.domain.entities.ToDo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseDocumentMapperTest {

  @Test
  public void testmapToDoToFirebaseDocument() {

    LocalDateTime today = LocalDateTime.of(2021,4,18,13,20,00);

    ToDo einkaufen = new ToDo("1","Einkaufen","Wocheneinkauf", today, false, true, new HashSet<>());

    Map<String,String> firebaseObject = FirebaseDocumentMapper.mapToDoToFirebaseDocument(einkaufen);

    assertEquals(firebaseObject.get(FirebaseDocumentMapper.ID),einkaufen.getId());
    assertEquals(firebaseObject.get(FirebaseDocumentMapper.NAME),einkaufen.getName());
    assertEquals(firebaseObject.get(FirebaseDocumentMapper.BESCHREIBUNG),einkaufen.getBeschreibung());
    assertEquals(firebaseObject.get(FirebaseDocumentMapper.FAELLIG),today.format(
        DateTimeFormatter.ofPattern(DATE_FORMAT)));
  }

  @Test
  public void testmapFirebaseDocumentToToDo() {

    LocalDateTime today = LocalDateTime.of(2021,4,18,13,20,00);

    DocumentSnapshot documentSnapshot = Mockito.mock(DocumentSnapshot.class);

    when(documentSnapshot.getString(FirebaseDocumentMapper.ID)).thenReturn("1");
    when(documentSnapshot.getString(FirebaseDocumentMapper.NAME)).thenReturn("Einkaufen");
    when(documentSnapshot.getString(FirebaseDocumentMapper.BESCHREIBUNG)).thenReturn("Wocheneinkauf");
    when(documentSnapshot.getString(FirebaseDocumentMapper.FAELLIG)).thenReturn(today.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));

    ToDo einkaufen = FirebaseDocumentMapper.mapFirebaseDocumentToToDo(documentSnapshot);

    assertNotNull(einkaufen);
    assertEquals("1",einkaufen.getId());
    assertEquals("Einkaufen",einkaufen.getName());
    assertEquals("Wocheneinkauf",einkaufen.getBeschreibung());
    assertEquals(today,einkaufen.getFaellig());
  }
}
