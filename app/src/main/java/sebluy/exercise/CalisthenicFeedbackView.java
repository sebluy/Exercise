package sebluy.exercise;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalisthenicFeedbackView {

    public static View view(MainActivity a, MainState.Page.State.CalisthenicFeedback state) {
        ListView list = new ListView(a);

        list.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        Map<CalisthenicExercise.Type, CalisthenicExercise.Template> templates =
                state.templates();

        List<String> typeNames = new ArrayList<>(templates.size());
        for (CalisthenicExercise.Type t : templates.keySet()) {
            typeNames.add(t.toString());
        }

//        list.setOnItemClickListener((parent, v, pos, id) -> a.navigate(workoutNames.get(pos)));
        list.setAdapter(new ArrayAdapter<>(a, android.R.layout.simple_list_item_1, typeNames));

        return list;

    }

    public static void updateMenu(MainActivity a, Menu menu) {
        menu.add("Commit")
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                .setOnMenuItemClickListener(item -> {
//                    a.finishCalisthenicWorkout();
                    return true;
                });
    }

}
