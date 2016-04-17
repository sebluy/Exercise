package sebluy.exercise;

import android.widget.LinearLayout;

import trikita.anvil.RenderableView;

import static trikita.anvil.DSL.FILL;
import static trikita.anvil.DSL.MATCH;
import static trikita.anvil.DSL.WRAP;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.size;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textView;

public class View extends RenderableView {

    private final MainActivity activity;

    public View(MainActivity a) {
        super(a);
        activity = a;
    }

    @Override
    public void view() {
        MainState state = activity.state();
        if (state.page() == MainState.Page.HOME) {
            MainView.view(activity);
        } else {
            linearLayout(() -> {
                size(MATCH, MATCH);
                orientation(LinearLayout.VERTICAL);
                textView(() -> {
                    size(FILL, WRAP);
                    text("Not implemented");
                });
            });
        }
    }
}
