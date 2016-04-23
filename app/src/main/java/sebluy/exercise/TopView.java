package sebluy.exercise;

import android.view.Menu;
import android.view.View;

import static sebluy.exercise.MainState.Page;
import static sebluy.exercise.MainState.Page.State.CalisthenicWorkout;
import static sebluy.exercise.MainState.Page.State.CalisthenicFeedback;

public class TopView {

    public static View view(MainActivity a, MainState state) {
        Page page = state.page();
        switch (page.id()) {
            case CALISTHENIC_WORKOUT:
                return CalisthenicView.view(a, (CalisthenicWorkout)page.state());
            case CALISTHENIC_FEEDBACK:
                return CalisthenicFeedbackView.view(a, (CalisthenicFeedback)page.state());
            default:
                return MainView.view(a);
        }
    }

    public static void updateMenu(MainActivity a, Menu menu, MainState state) {
        menu.clear();
        switch (state.page().id()) {
            case CALISTHENIC_WORKOUT:
                CalisthenicView.updateMenu(a, menu);
                break;
            case CALISTHENIC_FEEDBACK:
                CalisthenicFeedbackView.updateMenu(a, menu);
                break;
        }
    }
}
