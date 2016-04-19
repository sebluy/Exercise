package sebluy.exercise;

import android.view.View;

public class TopView {

    public static View view(MainActivity activity) {
        MainState.Page page = activity.state().page();
        switch (page.ID()) {
            case CALISTHENIC_EXERCISE:
                return CalisthenicView.view(activity,
                        (MainState.Page.CalisthenicPageState)page.pageState());
            default:
                return MainView.view(activity);
        }
    }
}
