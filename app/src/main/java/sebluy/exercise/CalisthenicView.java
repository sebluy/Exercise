package sebluy.exercise;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CalisthenicView {

    /* replace with anvil-app-compat-v4 once pushed */

    public static class ExerciseFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater li, ViewGroup g, Bundle b) {
            CalisthenicExercise e = getArguments().getParcelable("exercise");
            Context c = getContext();

            LinearLayout layout = new LinearLayout(c);
            layout.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);

            layout.addView(text(c, "Set " + e.set()));
            layout.addView(text(c, e.name()));
            layout.addView(text(c, e.repetitions() + " reps"));

            return layout;
        }

        public static TextView text(Context c, String msg) {
            TextView t = new TextView(c);
            t.setText(msg);
            t.setGravity(Gravity.CENTER);
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            return t;
        }

        public static ExerciseFragment create(CalisthenicExercise e) {
            Bundle args = new Bundle();
            args.putParcelable("exercise", e);
            ExerciseFragment fragment = new ExerciseFragment();
            fragment.setArguments(args);
            return fragment;
        }
    }

    public static View view(MainActivity a, MainState.Page.CalisthenicPageState pageState) {
        List<CalisthenicExercise> workout = pageState.workout();
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(a.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ExerciseFragment.create(workout.get(position));
            }

            @Override
            public int getCount() {
                return workout.size();
            }
        };
        ViewPager pager = new ViewPager(a);
        pager.setAdapter(adapter);
        pager.setId(View.generateViewId());
        return pager;
    }
}