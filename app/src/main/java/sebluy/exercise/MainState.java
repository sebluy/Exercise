package sebluy.exercise;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import auto.parcelgson.AutoParcelGson;

import static sebluy.exercise.CalisthenicExercise.Template;
import static sebluy.exercise.CalisthenicExercise.Type;
import static sebluy.exercise.CalisthenicExercise.generateWorkout;
import static sebluy.exercise.MainState.Page.Id;
import static sebluy.exercise.MainState.Page.Id.CALISTHENIC_FEEDBACK;
import static sebluy.exercise.MainState.Page.Id.CALISTHENIC_WORKOUT;
import static sebluy.exercise.MainState.Page.Id.HOME;
import static sebluy.exercise.MainState.Page.State.CalisthenicFeedback;
import static sebluy.exercise.MainState.Page.State.CalisthenicWorkout;
import static sebluy.exercise.MainState.Page.State.Empty;

@AutoParcelGson
public abstract class MainState {

    @AutoValue
    public static abstract class Page {
        enum Id {HOME, CALISTHENIC_WORKOUT, CALISTHENIC_FEEDBACK}

        interface State {

            @AutoValue
            abstract class Empty implements State {
                public static Empty create() {
                    return new AutoValue_MainState_Page_State_Empty();
                }
            }

            @AutoParcelGson
            abstract class CalisthenicWorkout implements State {
                public abstract List<CalisthenicExercise> workout();
                public abstract int position();

                public static CalisthenicWorkout create(List<CalisthenicExercise> w, int i) {
                    return new AutoParcelGson_MainState_Page_State_CalisthenicWorkout(w, i);
                }

                public static CalisthenicWorkout init(Map<Type, Template> templates) {
                    return create(generateWorkout(templates), 0);
                }

                public CalisthenicWorkout updatePosition(int position) {
                    return create(workout(), position);
                }
            }

            @AutoParcelGson
            abstract class CalisthenicFeedback implements State {
                public abstract List<Boolean> results();

                public static CalisthenicFeedback create(List<Boolean> r) {
                    return new AutoParcelGson_MainState_Page_State_CalisthenicFeedback(r);
                }

                public static CalisthenicFeedback init() {
                    int length = CalisthenicExercise.ORDER.size();
                    List<Boolean> r = new ArrayList<>(length);
                    for (int i = 0; i < length; i++) {
                        r.add(i, false);
                    }
                    return create(Collections.unmodifiableList(r));
                }

                public CalisthenicFeedback updateResults(int position, boolean b) {
                    List<Boolean> r = new ArrayList<>(results());
                    r.set(position, b);
                    return create(Collections.unmodifiableList(r));
                }
            }

        }

        public abstract Id id();
        public abstract State state();

        public static Page create(Id id, State state) {
            return new AutoValue_MainState_Page(id, state);
        }

        public Page updateState(State state) {
            return create(id(), state);
        }
    }

    public static MainState init() {
        return MainState.create(
                Page.create(HOME, Empty.create()),
                fj.data.List.nil());
    }

    public static MainState create(Page p, fj.data.List<Page> history) {
        return new AutoParcelGson_MainState(p, history);
    }

    public abstract Page page();
    public abstract fj.data.List<Page> history();

    public MainState navigate(Id id) {
        Page p;
        switch (id) {
            case CALISTHENIC_FEEDBACK:
                p = Page.create(id, CalisthenicFeedback.init());
                break;
            default:
                p = Page.create(id, Empty.create());
                break;
        }
        return MainState.create(p, history().cons(page()));
    }

    public MainState navigateCalisthenicWorkout(Map<Type, Template> templates) {
        Page p = Page.create(CALISTHENIC_WORKOUT, CalisthenicWorkout.init(templates));
        return MainState.create(p, history().cons(page()));
    }

    public MainState setCalisthenicExercise(int position) {
        CalisthenicWorkout state = (CalisthenicWorkout)page().state();
        return setPage(page().updateState(state.updatePosition(position)));
    }

    public MainState setCalisthenicFeedback(int position, boolean b) {
        CalisthenicFeedback state = (CalisthenicFeedback)page().state();
        return setPage(page().updateState(state.updateResults(position, b)));
    }

    private MainState setPage(Page p) {
        return create(p, history());
    }

    public MainState back() {
        return MainState.create(history().head(), history().tail());
    }

}
