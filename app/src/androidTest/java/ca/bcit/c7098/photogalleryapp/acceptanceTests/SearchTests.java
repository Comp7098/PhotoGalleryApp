package ca.bcit.c7098.photogalleryapp.acceptanceTests;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.ui.MainActivity;
import ca.bcit.c7098.photogalleryapp.ui.SearchActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.IsNot.not;

/**
 * Project: photogalleryapp
 * Date: 10/10/2018
 * Author: William Murphy
 * Description:
 */
public class SearchTests {
//    @Rule
//    public ActivityTestRule<SearchActivity> mSearchActivityTestRule = new ActivityTestRule<>(SearchActivity.class);

    @Rule
    public IntentsTestRule<SearchActivity> mIntentsRuleSearch = new IntentsTestRule<>(SearchActivity.class);

    @Before
    public void stubSearchIntent() {

        // stubs the intent
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void startDate_opensDatePicker() {
        onView(withId(R.id.start_date)).perform(click());

        // check that click() displays a DatePickerDialog
    }

    @Test
    public void endDate_opensDatePicker() {
        onView(withId(R.id.end_date)).perform(click());
    }

    @Test
    public void searchByCaption() {
        // Given I am at the Search screen of the app
        // And I see an editable text view labeled "Keywords"
        String caption = "My test caption";
        onView(withHint("Keywords")).check(matches(isDisplayed()));
        // When I type "My test caption" into the editable view
        onView(withHint("Keywords")).perform(typeText(caption), closeSoftKeyboard());
        // And I press the button labeled "Search"
        onView(withId(R.id.button_search)).perform(click());
        // Then I see the main screen of the app

        // And I see the pictures with the caption "My test caption"

    }

    @Test
    public void searchByLocation() {
        // Given I am at the Search screen of the app
        // And I see the field labeled Top left corner of the search area
        // And I see the field labeled Bottom right corner of the search area
        // When I enter the current location in the Top Left corner of the search area
        // And I enter the current location in the Bottom right corner of the search area
        // And I press the button labeled "Search"
        // Then I see the main screen of the app
        // And I see the pictures that were taken at the current location
    }
    @Test
    public void searchByDate() {
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
