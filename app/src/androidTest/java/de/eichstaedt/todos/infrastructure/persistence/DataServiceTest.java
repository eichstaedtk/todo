package de.eichstaedt.todos.infrastructure.persistence;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import de.eichstaedt.todos.domain.ToDo;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DataServiceTest {

  DataService dataService;

  @Before
  public void setup() {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    dataService = DataService.instance(appContext);
    dataService.getLocalDatabase().toDoDAO().deleteAll();
  }

  @After
  public void cleanup() {
    dataService.getLocalDatabase().toDoDAO().deleteAll();
  }

  @Test
  public void testCreation() {
    Assert.assertNotNull(dataService);
  }

  @Test
  public void testSaveToDo() {

    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    DataService dataService = DataService.instance(appContext);

    ToDo neueAufgabe = new ToDo("Test Aufgabe","Aufgabe für einen Test", LocalDateTime.now(),true);

    dataService.saveToDo(neueAufgabe,
        message -> Assert.assertEquals(neueAufgabe,dataService.getLocalDatabase().toDoDAO().findById(neueAufgabe.getId()).blockingFirst()));

  }

}
