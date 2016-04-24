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
import static sebluy.exercise.CalisthenicExercise.names;
import static sebluy.exercise.CalisthenicExercise.templates;
import static sebluy.exercise.MainState.Page.Id;
import static sebluy.exercise.MainState.Page.Id.HOME;
import static sebluy.exercise.MainState.Page.Id.CALISTHENIC_FEEDBACK;
import static sebluy.exercise.MainState.Page.Id.CALISTHENIC_WORKOUT;
import static sebluy.exercise.MainState.Page.State.Empty;
import static sebluy.exercise.MainState.Page.State.CalisthenicWorkout;
import static sebluy.exercise.MainState.Page.State.CalisthenicFeedback;

@AutoParcelGson
public abstract class MainState {

    @AutoValue
    static abstract class Page {
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
                public abstract Map<Type, Template> templates();
                public abstract List<CalisthenicExercise> workout();
                public abstract int exerciseIndex();

                public static CalisthenicWorkout
                create(Map<Type, Template> t, List<CalisthenicExercise> w, int i) {
                    return new AutoParcelGson_MainState_Page_State_CalisthenicWorkout(t, w, i);
                }
            }

            @AutoParcelGson
            abstract class CalisthenicFeedback implements State {
                public abstract Map<Type, Template> templates();
                public abstract List<Boolean> results();

                public static CalisthenicFeedback create(Map<Type, Template> t, List<Boolean> r) {
                    return new AutoParcelGson_MainState_Page_State_CalisthenicFeedback(t, r);
                }

                public static CalisthenicFeedback init(Map<Type, Template> t) {
                    List<Boolean> r = new ArrayList<>(t.size());
                    for (int i = 0; i < t.size(); i++) {
                        r.add(i, false);
                    }
                    return create(t, Collections.unmodifiableList(r));
                }
            }

        }

        public CalisthenicWorkout calisthenicPageState() {
            return (State.CalisthenicWorkout) state();
        }

        public abstract Id id();
        public abstract State state();

        public static Page create(Id id, State state) {
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
                Page.create(HOME, Empty.create()),
                fj.data.List.nil());
    }

    public MainState navigate(Id id) {
        Page p;
        switch (id) {
            case CALISTHENIC_WORKOUT:
                p = Page.create(CALISTHENIC_WORKOUT,
                        CalisthenicWorkout.create(templates, generateWorkout(), 0));
                break;
            case CALISTHENIC_FEEDBACK:
                p = Page.create(CALISTHENIC_FEEDBACK, CalisthenicFeedback.init(templates));
                break;
            default:
                p = Page.create(id, Empty.create());
                break;
        }
        return MainState.create(p, history().cons(page()));
    }

    public MainState setCalisthenicExercise(int position) {
        if (page().id() == CALISTHENIC_WORKOUT) {
            CalisthenicWorkout state = (CalisthenicWorkout)page().state();
            return MainState.create(
                    Page.create(
                            CALISTHENIC_WORKOUT,
                            Page.State.CalisthenicWorkout.create(
                                    state.templates(), state.workout(), position)),
                    history());
        } else {
            return this;
        }
    }

    public MainState setCalisthenicFeedback(int position, boolean b) {
        CalisthenicFeedback state = (CalisthenicFeedback)page().state();
        List<Boolean> newResults = new ArrayList<>(state.results());
        newResults.set(position, b);
        return MainState.create(
                Page.create(
                        CALISTHENIC_FEEDBACK,
                        CalisthenicFeedback.create(
                                state.templates(),
                                Collections.unmodifiableList(newResults))),
                history());
    }


    public MainState back() {
        return MainState.create(history().head(), history().tail());
    }

}
