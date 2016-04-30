package sebluy.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sebluy.exercise.MainState.Page.State.CalisthenicFeedback;

import static sebluy.exercise.CalisthenicExercise.Type;

public class CalisthenicFeedbackView {

    public static View view(MainActivity a, CalisthenicFeedback state) {
        ListView list = new ListView(a);

        list.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        List<String> typeNames = new ArrayList<>(CalisthenicExercise.ORDER.size());
        for (Type t : CalisthenicExercise.ORDER) {
            typeNames.add(CalisthenicExercise.names.get(t));
        }

        list.setAdapter(new Adapter(a, typeNames, state.results()));
        list.setOnItemClickListener((parent, v, pos, id) -> {
            CheckedTextView t = (CheckedTextView)v;
            a.setCalisthenicFeedback(pos, !t.isChecked());
            t.setChecked(!t.isChecked());
        });

        return list;

    }

    private static class Adapter extends ArrayAdapter<String> {

        private final List<Boolean> checked;

        public Adapter(Context c, List<String> names, List<Boolean> chkd) {
            super(c, 0, names);
            checked = chkd;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String name = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater
                        .from(getContext())
                        .inflate(android.R.layout.simple_list_item_checked, parent, false);
            }
            CheckedTextView t = (CheckedTextView)convertView.findViewById(android.R.id.text1);
            t.setText(name);
            t.setChecked(checked.get(position));
            return convertView;
        }

    }

    public static void updateMenu(MainActivity a, Menu menu) {
        menu.add("Commit")
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                .setOnMenuItemClickListener(item -> {
                    a.commitCalisthenicWorkout();
                    return true;
                });
    }

}
