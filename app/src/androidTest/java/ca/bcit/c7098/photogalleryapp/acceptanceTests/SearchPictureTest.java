package ca.bcit.c7098.photogalleryapp.acceptanceTests;

import android.app.DatePickerDialog;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.ui.MainActivity;
import ca.bcit.c7098.photogalleryapp.ui.SearchActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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
        // Given that I am at the main screen of the photo gallery app
        // When I press the filter button
        onView(ViewMatchers.withId(R.id.button_filter)).perform(click());
        //Intents.init();
        intended(hasComponent(SearchActivity.class.getName()));
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
        // When I type "My test caption" into the editable view
        // And I press the button labeled "Search"
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