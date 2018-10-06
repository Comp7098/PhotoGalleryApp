package ca.bcit.c7098.photogalleryapp.acceptanceTests;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.ui.MainActivity;

import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AssignCaptionTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void assignCaptionTest() {

        // Given that I am at the main screen of the app
        // And I see an editable text view labeled "caption" next to the picture
        onView(withId(R.id.caption)).check(matches(withHint("Caption")));

        // When I edit the text by typing "My Text Caption"
        String caption = "My Text Caption";
        onView(withId(R.id.caption)).perform(clearText());
        onView(withId(R.id.caption)).perform(typeText(caption));

        // Then I see the new caption "My Text Caption"
        onView(withId(R.id.caption)).check(matches(withText(caption)));

        // When I scroll away from the picture
        onView(withId(R.id.image_gallery)).perform(RecyclerViewActions.actionOnItemAtPosition(2, scrollTo()));

        // And scroll back to the picture

        // Then I see the new caption "My Test Caption'
    }
}