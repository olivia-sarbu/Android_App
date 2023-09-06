package com.example.myapplication;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import com.example.myapplication.R;
import com.example.myapplication.Register;

public class RegisterTest {

    @Rule
    public ActivityScenarioRule<Register> activityScenarioRule = new ActivityScenarioRule<>(Register.class);

    @Test
    public void testEmptyEmailValidation() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_register)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Introduceti un email")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testEmptyPasswordValidation() {
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("test@example.com"));
        Espresso.onView(ViewMatchers.withId(R.id.btn_register)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Introduceti o parola")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testInvalidEmailValidation() {
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("invalidemail"));
        Espresso.onView(ViewMatchers.withId(R.id.btn_register)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.email)).check(ViewAssertions.matches(ViewMatchers.hasErrorText("Introduceti un email valid!")));
    }

    @Test
    public void testPasswordMismatchValidation() {
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("test@example.com"));
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText("password123"));
        Espresso.onView(ViewMatchers.withId(R.id.confirm_password)).perform(ViewActions.typeText("differentpassword"));
        Espresso.onView(ViewMatchers.withId(R.id.btn_register)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.confirm_password)).check(ViewAssertions.matches(ViewMatchers.hasErrorText("Parolele nu se potrivesc!")));
    }

    @Test
    public void testValidInputValidation() {
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("test@example.com"));
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText("password123"));
        Espresso.onView(ViewMatchers.withId(R.id.confirm_password)).perform(ViewActions.typeText("password123"));
        Espresso.onView(ViewMatchers.withId(R.id.btn_register)).perform(ViewActions.click());
    }
}
