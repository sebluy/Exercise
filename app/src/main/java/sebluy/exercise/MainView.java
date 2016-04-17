package sebluy.exercise;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import fj.data.Seq;

import static trikita.anvil.DSL.FILL;
import static trikita.anvil.DSL.MATCH;
import static trikita.anvil.DSL.WRAP;
import static trikita.anvil.DSL.adapter;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.listView;
import static trikita.anvil.DSL.onItemClick;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.size;

public class MainView {

    private static final Seq<String> workoutNames = Seq.arraySeq("Calisthenic");

    public static void view(MainActivity a) {
        linearLayout(() -> {
            size(MATCH, MATCH);
            orientation(LinearLayout.VERTICAL);
            listView(() -> {
                size(FILL, WRAP);
                onItemClick((parent, v, pos, id) -> a.navigate(workoutNames.index(pos)));
                adapter(new ArrayAdapter<>(a,
                        android.R.layout.simple_list_item_1,
                        workoutNames.toJavaList()));
            });
        });
    }
}
