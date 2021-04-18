package de.eichstaedt.todos.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import org.junit.Test;

public class ToDoTest {

  @Test
  public void testToDoCreationWithoutId() {
    ToDo einkaufen = new ToDo("Einkaufen","Wocheneinkauf", LocalDateTime.now());

    assertFalse(einkaufen.getId().isEmpty());
    assertEquals("Einkaufen",einkaufen.getName());
    assertEquals("Wocheneinkauf",einkaufen.getBeschreibung());
    assertTrue(LocalDateTime.now().isAfter(einkaufen.getFaellig()));
  }

  @Test
  public void testToDOCreationWithID() {
    ToDo einkaufen = new ToDo("1","Einkaufen","Wocheneinkauf", LocalDateTime.now());

    assertEquals("1",einkaufen.getId());
    assertEquals("Einkaufen",einkaufen.getName());
    assertEquals("Wocheneinkauf",einkaufen.getBeschreibung());
    assertTrue(LocalDateTime.now().isAfter(einkaufen.getFaellig()));
  }
}