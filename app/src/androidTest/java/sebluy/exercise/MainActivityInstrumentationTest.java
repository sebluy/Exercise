package sebluy.exercise;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.orhanobut.hawk.Hawk;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest {


    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testHome() throws InterruptedException {
        /* wipe out any existing state */
        MainActivity.initializeHawk(mActivityRule.getActivity());
        Hawk.remove(MainActivity.HAWK_MAIN_STATE, MainActivity.HAWK_CALISTHENIC);

        onView(withText("Calisthenic")).perform(click());
        onView(withText("9 reps")).check(matches(isDisplayed()));
        onView(withText("Finish")).perform(click());
        onView(withText("Push ups")).perform(click());
        onView(withText("Commit")).perform(click());
        onView(withText("Calisthenic")).perform(click());

        /* swipe through until 4th set of push ups */
        for (int i = 0; i < 5*3; i++) {
            onView(withId(R.id.view_pager)).perform(swipeLeft());
        }

        onView(withText("10 reps")).check(matches(isDisplayed()));

        /* kind of a hack */
        mActivityRule.getActivity().runOnUiThread(() ->
                mActivityRule.getActivity().onBackPressed()
        );

        onView(withText("Calisthenic")).check(matches(isDisplayed()));
    }

}