package de.eichstaedt.todos.infrastructure.persistence;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import de.eichstaedt.todos.domain.ToDo;
import java.time.LocalDateTime;
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
  public void testSaveToDo() {
    ToDo neueAufgabe = new ToDo("Test Aufgabe","Aufgabe für einen Test", LocalDateTime.now(),true);

    toDoDAO.insertToDo(neueAufgabe);

    Assert.assertEquals(1,toDoDAO.getAll().size());
  }
}
