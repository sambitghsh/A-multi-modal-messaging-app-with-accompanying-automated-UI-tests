package com.example.e0202;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class contactDetailsTest {

    private View decorView;
    
    @Rule
    public ActivityTestRule<contactDetails> mActivityRule = new ActivityTestRule<>(contactDetails.class);

    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void TestContactInfo() throws Exception{
        String[] inValidNumber ={"99034","123 678", "12345678901", "12345 7899"};
        String[] inValidEmail = {"Sambit$gmail.com", "1@&.c", "1@  h,j.",  "f$g.c"};

        //Validation for phone number
        for(String s : inValidNumber) {
            //typing the invalid number
            Espresso.onView(withId(R.id.etContact)).perform(typeText(s));
            //Closing the keyboard
            Espresso.closeSoftKeyboard();
            //Clicking the confirm button
            Espresso.onView(withId(R.id.bcConfirm)).perform(click());
            //Checking the error has occured or not
            Espresso.onView(withId(R.id.etContact)).check(matches(hasErrorText("Invalid contact details.")));
            //testing the toast has not yet appeared
            Espresso.onView(withText(R.string.TOAST_SMS)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(doesNotExist());
            //Clearing the edit text field
            Espresso.onView(withId(R.id.etContact)).perform(clearText());
        }

        //Validation for email
        for(String s:inValidEmail){
            //typing the invalid email
            Espresso.onView(withId(R.id.etContact)).perform(typeText(s));
            //Closing the keyboard
            Espresso.closeSoftKeyboard();
            //Clicking the Confirm button
            Espresso.onView(withId(R.id.bcConfirm)).perform(click());
            //Checking the error has occured or not
            Espresso.onView(withId(R.id.etContact)).check(matches(hasErrorText("Invalid contact details.")));
            //testing the toast has not yet appeared
            Espresso.onView(withText(R.string.TOAST_EMAIL)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(doesNotExist());
            //Clearing the edit text field
            Espresso.onView(withId(R.id.etContact)).perform(clearText());
        }
    }

    @After
    public void tearDown() throws Exception {
    }
}