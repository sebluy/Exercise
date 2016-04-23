package sebluy.exercise;

import com.google.auto.value.AutoValue;

import java.util.List;
import java.util.Map;

import auto.parcelgson.AutoParcelGson;

import static sebluy.exercise.CalisthenicExercise.Template;
import static sebluy.exercise.CalisthenicExercise.Type;
import static sebluy.exercise.CalisthenicExercise.generateWorkout;
import static sebluy.exercise.CalisthenicExercise.templates;

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
            abstract class Calisthenic implements State {
                public abstract Map<Type, Template> templates();
                public abstract List<CalisthenicExercise> workout();
                public abstract int exerciseIndex();

                public static
                Calisthenic create(Map<Type, Template> t, List<CalisthenicExercise> w, int i) {
                    return new AutoParcelGson_MainState_Page_State_Calisthenic(t, w, i);
                }
            }

            @AutoParcelGson
            abstract class CalisthenicFeedback implements State {
                public abstract Map<Type, Template> templates();

                public static CalisthenicFeedback create(Map<Type, Template> t) {
                    return new AutoParcelGson_MainState_Page_State_CalisthenicFeedback(t);
                }
            }

        }

        public State.Calisthenic calisthenicPageState() {
            return (State.Calisthenic)pageState();
        }

        public abstract Id id();
        public abstract State pageState();

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
                Page.create(Page.Id.HOME, Page.State.Empty.create()),
                fj.data.List.nil());
    }

    public MainState navigate(Page.Id id) {
        Page p;
        switch (id) {
            case CALISTHENIC_WORKOUT:
                p = Page.create(Page.Id.CALISTHENIC_WORKOUT,
                        Page.State.Calisthenic.create(templates, generateWorkout(), 0));
                break;
            case CALISTHENIC_FEEDBACK:
                p = Page.create(Page.Id.CALISTHENIC_FEEDBACK,
                        Page.State.CalisthenicFeedback.create(templates));
                break;
            default:
                p = Page.create(id, Page.State.Empty.create());
                break;
        }
        return MainState.create(p, history().cons(page()));
    }

    public MainState setCalisthenicExercise(int position) {
        if (page().id() == Page.Id.CALISTHENIC_WORKOUT) {
            Page.State.Calisthenic pageState = page().calisthenicPageState();
            return MainState.create(
                    Page.create(
                            Page.Id.CALISTHENIC_WORKOUT,
                            Page.State.Calisthenic.create(
                                    pageState.templates(), pageState.workout(), position)),
                    history());
        } else {
            return this;
        }
    }

    public MainState back() {
        return MainState.create(history().head(), history().tail());
    }

}
