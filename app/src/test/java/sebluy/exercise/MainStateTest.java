package sebluy.exercise;

public class MainStateTest {

    /*
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

        state = state.navigateCalisthenicWorkout(CalisthenicExercise.TEMPLATES);
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
        MainState state = MainState.init()
                .navigateCalisthenicWorkout(CalisthenicExercise.TEMPLATES)
                .setCalisthenicExercise(4);

        assertEquals(4, ((CalisthenicWorkout)state.page().state()).position());
    }

    @Test
    public void setCalisthenicFeedback() throws Exception {
        MainState state = MainState.init()
                .navigateCalisthenicWorkout(CalisthenicExercise.TEMPLATES)
                .navigate(Id.CALISTHENIC_FEEDBACK)
                .setCalisthenicFeedback(3, true);

        assertEquals(true, ((CalisthenicFeedback)state.page().state()).results().get(3));
    }

    @Test
    public void back() throws Exception {
        MainState state = MainState.init()
                .navigateCalisthenicWorkout(CalisthenicExercise.TEMPLATES)
                .navigate(Id.CALISTHENIC_FEEDBACK)
                .back()
                .back();
        assertEquals(MainState.init(), state);
    }
    */

}