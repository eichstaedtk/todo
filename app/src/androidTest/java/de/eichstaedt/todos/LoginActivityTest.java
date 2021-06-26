package de.eichstaedt.todos;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule
        = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testEmailWithProperEMail() {

        onView(withId(R.id.emailTextInput))
            .perform(typeText("test@gmx.de"), closeSoftKeyboard());

        onView(withId(R.id.emailTextInput))
            .check(matches(withHint("E-Mail")));

        onView(withId(R.id.emailTextInput))
            .check(matches(withText("test@gmx.de")));
    }

    @Test
    public void testEmailWithInProperEMail() {

        onView(withId(R.id.emailTextInput))
            .perform(typeText("test@gmx"), closeSoftKeyboard());

        onView(withId(R.id.passwordTextInput)).perform(click());

        onView(withId(R.id.emailTextInput))
            .check(matches(hasErrorText("E-Mail Adresse ungültig!")));
    }

    @Test
    public void testPasswordWithInProper() {

        onView(withId(R.id.passwordTextInput))
            .perform(typeText("123"), closeSoftKeyboard());

        onView(withId(R.id.emailTextInput)).perform(click());

        onView(withId(R.id.passwordTextInput))
            .check(matches(hasErrorText("Passwort ungültig!")));
    }
}