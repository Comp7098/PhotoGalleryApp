package ca.bcit.c7098.photogalleryapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchPictureTest {

    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule = new IntentsTestRule<>(MainActivity.class);

//    @Rule
//    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);
//
//    @Rule
//    public ActivityTestRule<SearchActivity> mSearchActivityTestRule = new ActivityTestRule<>(SearchActivity.class);

//    @Before
//    public void initIntent() {
//        Intents.init();
//    }

    @Test
    public void searchPictureButton_opensSearchActivity() {
        onView(withId(R.id.button_filter)).perform(click());
        //Intents.init();
        intended(hasComponent(SearchActivity.class.getName()));
    }

    @Test
    public void startDate_opensDatePicker() {
        onView(withId(R.id.start_date)).perform(click());

        // check that click() displays a DatePickerDialog
        onView(withClassName(Matchers.equalTo(DatePickerDialog.class.getName()))).perform(PickerActions.setDate(1994, 3, 26));
    }

    @Test
    public void todayAsStartDate() {
        Helper.DatePickerHelper();
    }

    private static class Helper {
        public static void DatePickerHelper(int year, int month, int day)  {
            onView(withClassName(Matchers.equalTo(DatePickerDialog.class.getName()))).perform(PickerActions.setDate(year, month, day));
        }

        public static void DatePickerHelper() {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR) - 1900;
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerHelper(year, month, day);
        }
    }
}