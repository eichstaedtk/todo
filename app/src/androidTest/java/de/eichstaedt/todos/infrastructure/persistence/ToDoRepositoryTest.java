package de.eichstaedt.todos.infrastructure.persistence;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import de.eichstaedt.todos.domain.ToDo;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ToDoRepositoryTest {

  ToDoDatabase db;
  ToDoDAO toDoDAO;

  @Before
  public void createDb() {
    Context context = ApplicationProvider.getApplicationContext();
    db = Room.inMemoryDatabaseBuilder(context, ToDoDatabase.class).build();
    toDoDAO = db.toDoDAO();
  }

  @After
  public void closeDb() {
    db.close();
  }

  @Test
  public void testinsertToDo() {
    ToDo neueAufgabe = new ToDo("Test Aufgabe","Aufgabe für einen Test", LocalDateTime.now(),true);

    toDoDAO.insertToDo(neueAufgabe);

    assertEquals(1,toDoDAO.getAll().size());
  }

  @Test
  public void testinsertToDos() {
    ToDo alteAufgabe = new ToDo("Test Aufgabe","Aufgabe für einen Test", LocalDateTime.now(),true);
    ToDo neueAufgabe = new ToDo("Test Aufgabe","Aufgabe für einen Test", LocalDateTime.now(),true);

    toDoDAO.insertTodos(Arrays.asList(alteAufgabe,neueAufgabe));

    assertEquals(2,toDoDAO.getAll().size());
  }

  @Test
  public void testFindById() {
    ToDo neueAufgabe = new ToDo("Test Aufgabe","Aufgabe für einen Test", LocalDateTime.now(),true);

    toDoDAO.insertToDo(neueAufgabe);

    Assert.assertEquals(neueAufgabe,toDoDAO.findById(neueAufgabe.getId()).blockingFirst());
  }
}
