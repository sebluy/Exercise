package sebluy.exercise;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.orhanobut.hawk.Hawk;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import sebluy.exercise.CalisthenicExercise.Type;
import sebluy.exercise.CalisthenicExercise.Template;
import sebluy.exercise.MainState.Page.State.CalisthenicWorkout;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest {

    private Map<Type, Template> calisthenicTemplates;
    private MainState state;

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);

    @After
    public void restoreStorage() {
        Hawk.put(MainActivity.HAWK_MAIN_STATE, state);
        Hawk.put(MainActivity.HAWK_CALISTHENIC, calisthenicTemplates);
    }

    @Before
    public void saveAndClearStorage() {
        MainActivity.initializeHawk(activityRule.getActivity());
        state = Hawk.get(MainActivity.HAWK_MAIN_STATE);
        calisthenicTemplates = Hawk.get(MainActivity.HAWK_CALISTHENIC);
        Hawk.remove(MainActivity.HAWK_MAIN_STATE, MainActivity.HAWK_CALISTHENIC);
    }

    public void checkWorkoutMatchesView(List<CalisthenicExercise> workout) throws Exception {
        for (CalisthenicExercise e : workout) {
            onView(allOf(
                    withText(e.name()),
                    hasSibling(withText("Set " + e.set())),
                    hasSibling(withText(e.repetitions() + " reps")))).check(matches(isDisplayed()));
            onView(withId(R.id.view_pager)).perform(swipeLeft());
        }
    }

    @Test
    public void testCalisthenic() throws Exception {
        MainActivity a = (MainActivity)activityRule.getActivity();
        onView(withText("Calisthenic")).perform(click());
        MainState state = a.getState();
        List<CalisthenicExercise> workout = ((CalisthenicWorkout)state.page().state()).workout();
        checkWorkoutMatchesView(workout);
        onView(withText("Finish")).perform(click());
        onView(withText("Push ups")).perform(click());
        onView(withText("Commit")).perform(click());
        onView(withText("Calisthenic")).perform(click());

        // kind of a hack
        activityRule.getActivity().runOnUiThread(() ->
                activityRule.getActivity().onBackPressed()
        );

        onView(withText("Calisthenic")).check(matches(isDisplayed()));
    }


}