package sebluy.exercise;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class MainState {

    @AutoValue
    static abstract class Page {
        enum ID {HOME, CALISTHENIC_EXERCISE}

        interface PageState {
            static final PageState empty = new PageState() {};
        }

        @AutoValue
        static abstract class CalisthenicPageState implements PageState {
            public abstract List<CalisthenicExercise> workout();

            public static CalisthenicPageState create(List<CalisthenicExercise> w) {
                return new AutoValue_MainState_Page_CalisthenicPageState(w);
            }
        }

        public abstract ID ID();
        public abstract PageState pageState();

        public static Page create(ID id, PageState state) {
            return new AutoValue_MainState_Page(id, state);
        }
    }

    public abstract Page page();
    public abstract fj.data.List<Page> history();

    static MainState create(Page p, fj.data.List<Page> history) {
        return new AutoValue_MainState(p, history);
    }

    public static MainState init() {
        return MainState.create(
                Page.create(Page.ID.HOME, Page.PageState.empty),
                fj.data.List.nil());
    }

    public MainState navigate(Page.ID id) {
        Page p;
        switch (id) {
            case CALISTHENIC_EXERCISE:
                p = Page.create(Page.ID.CALISTHENIC_EXERCISE,
                        Page.CalisthenicPageState.create(CalisthenicExercise.generateWorkout()));
                break;
            default:
                p = Page.create(Page.ID.HOME, Page.PageState.empty);
                break;
        }
        return MainState.create(p, history().cons(page()));
    }

    public MainState back() {
        return MainState.create(history().head(), history().tail());
    }

}
