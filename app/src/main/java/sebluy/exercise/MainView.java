package sebluy.exercise;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static sebluy.exercise.MainState.Page.Id;

public class MainView {

    private static final List<String> workoutNames =
            Collections.unmodifiableList(Arrays.asList("Calisthenic"));

    private static final List<Id> workoutPageIds =
            Collections.unmodifiableList(Arrays.asList(Id.CALISTHENIC_WORKOUT));

    public static View view(MainActivity a) {
        ListView list = new ListView(a);

        list.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        list.setOnItemClickListener((parent, v, pos, id) -> {
            a.navigate(workoutPageIds.get(pos));
        });
        list.setAdapter(new ArrayAdapter<>(a, android.R.layout.simple_list_item_1, workoutNames));

        return list;

    }
}
