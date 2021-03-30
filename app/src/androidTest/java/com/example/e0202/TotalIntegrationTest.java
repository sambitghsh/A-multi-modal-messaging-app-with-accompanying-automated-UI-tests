package com.example.e0202;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;

public class TotalIntegrationTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    public UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void TestOverallInfo() throws Throwable {

        String testToast = "";
        String chooserTest = "";
        int i = 0;

        String[] testMessage = {"hello world", "welcome home", "all fine"};
        String[] validContacts = {"9903457528","sambit@gmail.cm","9051797308"};
        String[] inValidContacts = {"990", "@G.C", "90517973081"};
        String errorMessage = "Invalid contact details.";
        String[] mode = {"type", "voice", "type"};

        for(String s: mode){

            //clear the textfield
            Espresso.onView(withId(R.id.etMessage)).perform(clearText());

            if (s.equals("type")){
                //type a message in the editext
                Espresso.onView(withId(R.id.etMessage)).perform(typeText(testMessage[i]));
                //close the softkeyboard
                Espresso.closeSoftKeyboard();
                testToast = "Sending SMS..";
                chooserTest = "Choose a phone client";
            }
            else{
                //click the voice input button
                Espresso.onView(withId(R.id.bviVoice)).perform(click());
                //check the voice message is right or not
                Espresso.onView(withId(R.id.etMessage)).check(matches(withText(testMessage[i].toLowerCase())));
                testToast = "Sending Email..";
                chooserTest = "Choose an email client";
            }
            //press the send button
            Espresso.onView(withId(R.id.bsSend)).perform(click());
            //Checking intent got the actual message or not
            intended(hasExtra("message", testMessage[i]));
            //type a wrong phone number or email
            Espresso.onView(withId(R.id.etContact)).perform(typeText(inValidContacts[i]));
            //close the keyboard
            Espresso.closeSoftKeyboard();
            //press the confirm button
            Espresso.onView(withId(R.id.bcConfirm)).perform(click());
            //check the error message is displayed or not
            Espresso.onView(withId(R.id.etContact)).check(matches(hasErrorText(errorMessage)));
            //check there is no toast message or not
            Espresso.onView(withText(testToast)).inRoot(withDecorView(not(getCurrentActivity().getWindow().getDecorView()))).check(doesNotExist());
            //clear the editetxt
            Espresso.onView(withId(R.id.etContact)).perform(clearText());
            //type a valid phone number or email
            Espresso.onView(withId(R.id.etContact)).perform(typeText(validContacts[i]));
            //close the keyboard
            Espresso.closeSoftKeyboard();
            //press the confirm button
            Espresso.onView(withId(R.id.bcConfirm)).perform(click());
            //press back
            mDevice.pressBack();
            //check the error has not occured
            Espresso.onView(withId(R.id.etContact)).check(matches(not(hasErrorText(errorMessage))));
            //check there is a toast message or not
            Espresso.onView(withText(testToast)).inRoot(withDecorView(not(getCurrentActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
            //clear text
            Espresso.onView(withId(R.id.etContact)).perform(clearText());
            //press navigateup
            Espresso.onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
            //check the message content
            Espresso.onView(withId(R.id.etMessage)).check(matches(withText(testMessage[i])));
            i = i+1;
        }

    }

    //https://stackoverflow.com/questions/24517291/get-current-activity-in-espresso-android to get the current activity
    public static Activity getCurrentActivity() {
        final Activity[] activity = new Activity[1];
        Espresso.onView(isRoot()).check((view, noViewFoundException) -> {
            View checkedView = view;
            while (checkedView instanceof ViewGroup && ((ViewGroup) checkedView).getChildCount() > 0) {
                checkedView = ((ViewGroup) checkedView).getChildAt(0);
                if (checkedView.getContext() instanceof Activity) {
                    activity[0] = (Activity) checkedView.getContext();
                    return;
                }
            }
        });
        return activity[0];
    }

    @After
    public void tearDown() throws Exception {
    }
}