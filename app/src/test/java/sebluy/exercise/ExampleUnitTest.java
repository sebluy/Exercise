package sebluy.exercise;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
        state = state.navigate(MainState.Page.ID.CALISTHENIC_EXERCISE);
        assertGsonEquals(gson, state, MainState.class);
        state = gson.fromJson(gson.toJson(state), MainState.class);
        List<CalisthenicExercise> w = ((MainState.Page.State.Calisthenic)state.page().pageState()).workout();
    }

    @Test
    public void setCalisthenicExercise() throws Exception {
        MainState state = MainState
                .init()
                .navigate(MainState.Page.ID.CALISTHENIC_EXERCISE)
                .setCalisthenicExercise(4);

        assertEquals(state.page().calisthenicPageState().exerciseIndex(), 4);
    }

}