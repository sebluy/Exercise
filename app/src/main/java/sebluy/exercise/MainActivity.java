package sebluy.exercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import trikita.anvil.RenderableView;

import static trikita.anvil.DSL.*;

public class MainActivity extends AppCompatActivity {

    private static final String[] workoutNames = {"Static Core", "Calisthenic"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new RenderableView(this) {
            @Override
            public void view() {
                linearLayout(() -> {
                    size(MATCH, MATCH);
                    orientation(LinearLayout.VERTICAL);
                    listView(() -> {
                        size(FILL, WRAP);
                        adapter(new ArrayAdapter<>(
                                MainActivity.this, android.R.layout.simple_list_item_1, workoutNames));
                    });
                });
            }
        });
    }
}
