package de.eichstaedt.todos;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static de.eichstaedt.todos.TestUtils.withRecyclerView;

import android.content.Context;
import androidx.room.Room;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import de.eichstaedt.todos.infrastructure.persistence.DataService;
import de.eichstaedt.todos.infrastructure.persistence.ToDoDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device for Main Activity.
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
        = new ActivityScenarioRule<>(MainActivity.class);

    DataService dataService;
    ToDoDatabase db;

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(appContext, ToDoDatabase.class).build();
        dataService = DataService.instance(appContext);
        dataService.setLocalDatabase(db);
        dataService.getLocalDatabase().toDoDAO().deleteAll();
    }

    @After
    public void cleanup() {
        dataService.getLocalDatabase().toDoDAO().deleteAll();
        db.close();
    }

    @Test
    public void testHeader() {

        onView(withId(R.id.todoNameHeader))
            .check(matches(withText("Name")));

        onView(withId(R.id.todoFaelligHeader))
            .check(matches(withText("Fällig")));

        onView(withId(R.id.todoErledigtHeader))
            .check(matches(withText("Erledigt")));

        onView(withId(R.id.todoWichtigHeader))
            .check(matches(withText("Wichtig")));
    }

    @Test
    public void testHeadLine(){

        onView(withId(R.id.headline))
            .check(matches(withText("Daten werden geladen ....")));

    }

    @Test
    public void testHeadLineOffline(){
        dataService.setOffline(true);
        onView(withId(R.id.headline))
            .check(matches(withText("Offline: Daten lokal geladen")));
    }

    @Test
    public void testHeadLineOnline(){
        dataService.setOffline(false);
        IdlingRegistry.getInstance().register(dataService.getCountingResource());
        onView(withId(R.id.headline))
            .check(matches(withText("Online: Daten erfolgreich geladen")));
    }
}