package sebluy.exercise;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.orhanobut.hawk.Hawk;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import sebluy.exercise.CalisthenicExercise.Template;
import sebluy.exercise.CalisthenicExercise.Type;
import sebluy.exercise.MainState.Page.State.CalisthenicWorkout;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

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
    public void calisthenicWorkout() throws Exception {
        MainActivity a = (MainActivity)activityRule.getActivity();
        onView(withText("Calisthenic")).perform(click());
        MainState state = a.getState();
        List<CalisthenicExercise> workout = ((CalisthenicWorkout)state.page().state()).workout();
        checkWorkoutMatchesView(workout);
    }

    @Test
    public void calisthenicFeedback() throws Exception {
        onView(withText("Calisthenic")).perform(click());
        onView(withText("Finish")).perform(click());
        onData(anything()).atPosition(0).check(matches(withText("Push ups")));
        onData(anything()).atPosition(1).check(matches(withText("Squat")));
        onData(anything()).atPosition(2).check(matches(withText("Core")));
        onData(anything()).atPosition(3).check(matches(withText("Pull ups")));
        onData(anything()).atPosition(4).check(matches(withText("Lunge")));
    }

    // kind of a hack
    public void pressBack() {
        activityRule.getActivity().runOnUiThread(() ->
                activityRule.getActivity().onBackPressed()
        );
    }

    @Test
    public void navigation() throws Exception {
        onView(withText("Calisthenic")).perform(click());
        pressBack();

        onView(withText("Calisthenic")).perform(click());
        onView(withText("Finish")).perform(click());
        pressBack();
        pressBack();

        onView(withText("Calisthenic")).perform(click());
        onView(withText("Finish")).perform(click());
        onView(withText("Commit")).perform(click());
        onView(withText("Calisthenic")).check(matches(isDisplayed()));

    }

    @Test
    public void resetCalisthenicTemplates() throws Exception {
        Hawk.put(MainActivity.HAWK_CALISTHENIC,
                CalisthenicExercise.nextTemplates(
                        CalisthenicExercise.TEMPLATES,
                        Arrays.asList(true, true, true, true, true)));
        onView(withText("Calisthenic")).perform(longClick());
        onView(withText("Reset to defaults")).perform(click());
        assertEquals(
                CalisthenicExercise.TEMPLATES,
                Hawk.get(MainActivity.HAWK_CALISTHENIC, CalisthenicExercise.TEMPLATES));
    }


}