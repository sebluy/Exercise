package sebluy.exercise;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sebluy.exercise.MainState.Page.Id;
import static sebluy.exercise.MainState.Page.State.Empty;
import static sebluy.exercise.MainState.Page.State.CalisthenicFeedback;
import static sebluy.exercise.MainState.Page.State.CalisthenicWorkout;

public class ExampleUnitTest {

    @Test
    public void generateCalisthenicWorkout() throws Exception {
        List<CalisthenicExercise> w =
                CalisthenicExercise.generateWorkout(CalisthenicExercise.TEMPLATES);
        for (CalisthenicExercise exercise : w) {
            System.out.println(exercise);
        }
    }

    @Test
    public void interleave() throws Exception {
        assertEquals(CalisthenicExercise.interleave(Arrays.asList(
                Arrays.asList(1,2,3),
                Arrays.asList(4,5),
                Arrays.asList(6,7,8))),
                Arrays.asList(1,4,6,2,5,7,3,8));
    }

    public <T> T gsonThereAndBack(Gson gson, Object o, Class<T> c) {
        return gson.fromJson(gson.toJson(o), c);
    }

    @Test
    public void mainStateGson() throws Exception {
        Gson gson = GsonConverter.buildGson();

        MainState state = MainState.init();
        MainState gsonState = gsonThereAndBack(gson, state, MainState.class);
        assertTrue(state.page().state() instanceof Empty);
        assertEquals(state, gsonState);

        state = state.navigate(Id.CALISTHENIC_WORKOUT);
        gsonState = gsonThereAndBack(gson, state, MainState.class);
        assertTrue(state.page().state() instanceof CalisthenicWorkout);
        assertEquals(state, gsonState);

        state = state.navigate(Id.CALISTHENIC_FEEDBACK);
        gsonState = gsonThereAndBack(gson, state, MainState.class);
        assertTrue(state.page().state() instanceof CalisthenicFeedback);
        assertEquals(state, gsonState);
    }

    @Test
    public void setCalisthenicExercise() throws Exception {
        MainState state =
                MainState.init().navigate(Id.CALISTHENIC_WORKOUT).setCalisthenicExercise(4);

        assertEquals(4, ((CalisthenicWorkout)state.page().state()).exerciseIndex());
    }

    @Test
    public void setCalisthenicFeedback() throws Exception {
        MainState state = MainState.init()
                .navigate(Id.CALISTHENIC_WORKOUT)
                .navigate(Id.CALISTHENIC_FEEDBACK)
                .setCalisthenicFeedback(3, true);

        assertEquals(true, ((CalisthenicFeedback)state.page().state()).results().get(3));
    }

    @Test
    public void calisthenicExerciseTemplateNext() throws Exception {

        CalisthenicExercise.Template original =
                CalisthenicExercise.Template.create(
                        CalisthenicExercise.DEFAULT_REPS_TO_SETS_F,
                        Arrays.asList(14, 14, 15, 15, 15, 15),
                        Arrays.asList("Anything"));

        CalisthenicExercise.Template expected =
                CalisthenicExercise.Template.create(
                        original.repsToSetsF(),
                        Arrays.asList(18, 18, 18, 19, 19),
                        original.variations());

        assertEquals(expected, original.next());
    }

    @Test
    public void pullUpTemplateNext() throws Exception {

        CalisthenicExercise.Template original =
                CalisthenicExercise.Template.create(
                        CalisthenicExercise.PULL_UPS_REPS_TO_SETS_F,
                        Arrays.asList(6, 6, 6, 6, 6),
                        Arrays.asList("Anything"));

        CalisthenicExercise.Template expected =
                CalisthenicExercise.Template.create(
                        original.repsToSetsF(),
                        Arrays.asList(8, 8, 8, 8),
                        original.variations());

        assertEquals(expected, original.next());
    }

}