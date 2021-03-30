package com.example.e0202;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void TestMessageInfo() throws Exception{
        String testName = "~!@#$%^&*()_+}{POIQWERTYU    GHm,m./  iwu`1234567890-= hjkl;'zxvbnm,./" +
                "kpp[[[ 35678 ";
        String voiceInput = "quick brown fox";

        //Typing the message in Edittext field
        Espresso.onView(withId(R.id.etMessage)).perform(typeText(testName));
        //Closing the soft keyboard
        Espresso.closeSoftKeyboard();
        //Clicking the send button
        Espresso.onView(withId(R.id.bsSend)).perform(click());
        //Checking the intent is targeted to contactDetails
        intended(hasComponent(contactDetails.class.getName()));
        //Checking intent got the actual message or not
        intended(allOf(toPackage("com.example.e0202"), hasExtra("message", testName)));
        //Clicking the navigate up button
        Espresso.onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        //Clearing the edittext field
        Espresso.onView(withId(R.id.etMessage)).perform(clearText());
        //Clicking the voice input button
        Espresso.onView(withId(R.id.bviVoice)).perform(click());
        //checking the voice input is matched or not
        Espresso.onView(withId(R.id.etMessage)).check(matches(withText(voiceInput.toLowerCase())));
    }

    @After
    public void tearDown() throws Exception {
    }
}