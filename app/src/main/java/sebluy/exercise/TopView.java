package sebluy.exercise;

import android.content.Context;
import android.view.Menu;
import android.view.View;

public class TopView {

    public static View view(MainActivity activity, MainState state) {
        MainState.Page page = state.page();
        switch (page.ID()) {
            case CALISTHENIC_EXERCISE:
                return CalisthenicView.view(activity, page.calisthenicPageState());
            default:
                return MainView.view(activity);
        }
    }

    public static void updateMenu(Menu menu, MainState state) {
        menu.clear();
        if (state.page().ID() == MainState.Page.ID.CALISTHENIC_EXERCISE) {
            CalisthenicView.updateMenu(menu);
        }
    }
}
