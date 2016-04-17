package sebluy.exercise;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.google.auto.value.AutoValue;

import static trikita.anvil.DSL.FILL;
import static trikita.anvil.DSL.MATCH;
import static trikita.anvil.DSL.WRAP;
import static trikita.anvil.DSL.adapter;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.listView;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.size;

@AutoValue
public abstract class MainState {

    enum Page {HOME, CALISTHENIC_EXERCISE}

    static MainState create(Page p) {
        return new AutoValue_MainState(p);
    }

    public abstract Page page();

    public MainState setPage(Page p) {
        return create(p);
    }

}
