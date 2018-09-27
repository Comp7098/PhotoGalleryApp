package ca.bcit.c7098.photogalleryapp;

import android.app.DatePickerDialog;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

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
        onView(withId(R.id.button_filter)).perform(click());
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

    }

    @Test
    public void searchByLocation() {

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