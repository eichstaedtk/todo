package de.eichstaedt.todos.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class ToDoTest {

  @Test
  public void testToDoCreationWithoutId() {
    ToDo einkaufen = new ToDo("Einkaufen","Wocheneinkauf", LocalDateTime.now());

    assertFalse(einkaufen.getId().isEmpty());
    assertEquals("Einkaufen",einkaufen.getName());
    assertEquals("Wocheneinkauf",einkaufen.getBeschreibung());
  }
}
