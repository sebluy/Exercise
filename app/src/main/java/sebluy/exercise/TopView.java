package sebluy.exercise;

import android.content.Context;
import android.view.Menu;
import android.view.View;

public class TopView {

    public static View view(MainActivity a, MainState state) {
        MainState.Page page = state.page();
        switch (page.ID()) {
            case CALISTHENIC_EXERCISE:
                return CalisthenicView.view(a, page.calisthenicPageState());
            default:
                return MainView.view(a);
        }
    }

    public static void updateMenu(MainActivity a, Menu menu, MainState state) {
        menu.clear();
        if (state.page().ID() == MainState.Page.ID.CALISTHENIC_EXERCISE) {
            CalisthenicView.updateMenu(a, menu);
        }
    }
}
