package sebluy.exercise;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

import auto.parcelgson.AutoParcelGson;

@AutoParcelGson
public abstract class MainState {

    @AutoValue
    static abstract class Page {
        enum ID {HOME, CALISTHENIC_EXERCISE}

        interface State {

            @AutoValue
            abstract class Empty implements State {
                public static Empty create() {
                    return new AutoValue_MainState_Page_State_Empty();
                }
            }

            @AutoParcelGson
            abstract class Calisthenic implements State {
                public abstract List<CalisthenicExercise> workout();
                public abstract int exerciseIndex();

                public static Calisthenic create(List<CalisthenicExercise> w, int i) {
                    return new AutoParcelGson_MainState_Page_State_Calisthenic(w, i);
                }
            }

        }

        public State.Calisthenic calisthenicPageState() {
            return (State.Calisthenic)pageState();
        }

        public abstract ID ID();
        public abstract State pageState();

        public static Page create(ID id, State state) {
            return new AutoValue_MainState_Page(id, state);
        }
    }

    static MainState create(Page p, fj.data.List<Page> history) {
        return new AutoParcelGson_MainState(p, history);
    }

    public abstract Page page();
    public abstract fj.data.List<Page> history();

    public static MainState init() {
        return MainState.create(
                Page.create(Page.ID.HOME, Page.State.Empty.create()),
                fj.data.List.nil());
    }

    public MainState navigate(Page.ID id) {
        Page p;
        switch (id) {
            case CALISTHENIC_EXERCISE:
                p = Page.create(Page.ID.CALISTHENIC_EXERCISE,
                        Page.State.Calisthenic.create(CalisthenicExercise.generateWorkout(), 0));
                break;
            default:
                p = Page.create(Page.ID.HOME, Page.State.Empty.create());
                break;
        }
        return MainState.create(p, history().cons(page()));
    }

    public MainState setCalisthenicExercise(int position) {
        if (page().ID() == Page.ID.CALISTHENIC_EXERCISE) {
            Page.State.Calisthenic pageState = page().calisthenicPageState();
            return MainState.create(
                    Page.create(
                            Page.ID.CALISTHENIC_EXERCISE,
                            Page.State.Calisthenic.create(pageState.workout(), position)),
                    history());
        } else {
            return this;
        }
    }

    public MainState back() {
        return MainState.create(history().head(), history().tail());
    }

}
