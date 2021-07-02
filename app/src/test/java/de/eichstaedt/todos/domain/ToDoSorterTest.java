package de.eichstaedt.todos.domain;

import static org.junit.Assert.assertEquals;

import de.eichstaedt.todos.domain.entities.ToDo;
import de.eichstaedt.todos.domain.entities.ToDoSorter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

  @Test
  public void testsortByErledigtAndWichtigDatum() {

    ToDo einkaufen = new ToDo("Einkaufen","Wocheneinkauf", LocalDateTime.now(),false);
    ToDo garten = new ToDo("Garten","Rasen mähen", LocalDateTime.now(),false);

    List<ToDo> todos = Arrays.asList(einkaufen,garten);

    assertEquals(todos.get(0),einkaufen);

    garten.setWichtig(true);

    ToDoSorter.sortByErledigtAndWichtigDatum(todos);

    assertEquals(todos.get(0),garten);
  }

  @Test
  public void testsortByErledigtAndDatumWichtig() {
    ToDo einkaufen = new ToDo("Einkaufen","Wocheneinkauf", LocalDateTime.now(),false);
    ToDo garten = new ToDo("Garten","Rasen mähen", LocalDateTime.now(),false);

    List<ToDo> todos = Arrays.asList(einkaufen,garten);

    assertEquals(todos.get(0),einkaufen);

    garten.setFaellig(garten.getFaellig().minusDays(2));

    ToDoSorter.sortByErledigtAndDatumWichtig(todos);

    assertEquals(todos.get(0),garten);

  }
}
