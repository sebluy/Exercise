package sebluy.exercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import trikita.anvil.RenderableView;

import static trikita.anvil.DSL.*;

public class MainActivity extends AppCompatActivity {

    private MainState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = MainState.create(MainState.Page.HOME);
        setContentView(new View(this));
    }

    public MainState state() {
        return state;
    }

    public void navigate(String pageName) {
        state = MainState.create(MainState.Page.CALISTHENIC_EXERCISE);
    }
}
