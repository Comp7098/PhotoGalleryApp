package ca.bcit.c7098.photogalleryapp.integrationTests;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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


/**
 * Project: photogalleryapp
 * Date: 10/6/2018
 * Author: William Murphy
 * Description:
 */
@RunWith(AndroidJUnit4.class)
public class PhotoGalleryTest {
    @Test
    public void recyclerView_scrollsOneAtATime() {

    }

    @Test
    public void recyclerView_snapsToScreen() {
        onView(withId(R.id.image_gallery)).perform(RecyclerViewActions.scrollToPosition(2));
    }
}
