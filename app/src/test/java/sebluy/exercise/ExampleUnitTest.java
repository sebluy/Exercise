package sebluy.exercise;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest {

    @Test
    public void generateCalisthenicWorkout() throws Exception {
        List<CalisthenicExercise> w = CalisthenicExercise.generateWorkout();
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

    public void assertGsonEquals(Gson gson, Object o, Class<?> c) throws Exception {
        assertEquals(gson.fromJson(gson.toJson(o), c), o);
    }

    @Test
    public void mainStateGson() throws Exception {
        Gson gson = GsonConverter.buildGson();
        MainState state = MainState.init();
        assertGsonEquals(gson, state, MainState.class);
        state = state.navigate(MainState.Page.Id.CALISTHENIC_WORKOUT);
        assertGsonEquals(gson, state, MainState.class);
        state = state.navigate(MainState.Page.Id.CALISTHENIC_FEEDBACK);
        assertGsonEquals(gson, state, MainState.class);
    }

    @Test
    public void setCalisthenicExercise() throws Exception {
        MainState state = MainState
                .init()
                .navigate(MainState.Page.Id.CALISTHENIC_WORKOUT)
                .setCalisthenicExercise(4);

        assertEquals(4, state.page().calisthenicPageState().exerciseIndex());
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