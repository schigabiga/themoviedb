package com.schigabiga.themoviedb;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity>  mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private String queryFilter = "fast";

    @Before
    public void setUp() throws Exception {

    }

    //megnyitja a keresőt
    //beírja a keresett szöveget
    //keres, lekéri a filmeket a keresett szöveg alapján
    //vár amíg letölti
    //majd megvizsgálja, hogy a recycleview összes elemének title view-ja tartalmazza e a beírt szöveget
    @Test
    public void testSearch(){
        onView(withId(R.id.toolbar_search)).perform(click());
        onView(withId(R.id.toolbar_edit)).perform(typeText(queryFilter));
        onView(withId(R.id.toolbar_search)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = mainActivityActivityTestRule.getActivity().findViewById(R.id.recycleview);
        for (int i=0;i<recyclerView.getChildCount();i++){
            TextView textView = recyclerView.getChildAt(i).findViewById(R.id.movie_item_title);
            onView(toMatcher(textView)).check(matches(withLowercaseText(containsString(queryFilter))));
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    //makes textview text to lowercase
    private static Matcher<View> withLowercaseText(final Matcher<String> stringMatcher) {
        checkNotNull(stringMatcher);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text (ignoring case): ");
                stringMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(TextView textView) {
                return stringMatcher.matches(textView.getText().toString().toLowerCase());
            }
        };
    }

    //makes a matcher from a view
    public static Matcher<View> toMatcher(final View v) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return item == v;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(v.toString().toLowerCase());
            }
        };
    }
}