package de.eichstaedt.todos.domain;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class ToDoSorterTest {


  @Test
  public void testsortByErledigt() {

    ToDo einkaufen = new ToDo("Einkaufen","Wocheneinkauf", LocalDateTime.now(),false);
    ToDo garten = new ToDo("Garten","Rasen mähen", LocalDateTime.now(),false);
    einkaufen.setErledigt(true);

    List<ToDo> todos = Arrays.asList(einkaufen,garten);

    assertEquals(todos.get(0),einkaufen);

    ToDoSorter.sortByErledigt(todos);

    assertEquals(todos.get(0),garten);
  }
}
