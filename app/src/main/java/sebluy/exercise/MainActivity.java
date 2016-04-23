package sebluy.exercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.orhanobut.hawk.GsonParser;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import static sebluy.exercise.MainState.init;
import static sebluy.exercise.MainState.Page.Id;
import static sebluy.exercise.MainState.Page.Id.CALISTHENIC_FEEDBACK;

public class MainActivity extends AppCompatActivity {

    private MainState state;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Hawk.isBuilt()) {
            Hawk.init(this)
                    .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                    .setStorage(HawkBuilder.newSharedPrefStorage(this))
                    .setLogLevel(LogLevel.NONE)
                    .setParser(new GsonParser(GsonConverter.buildGson()))
                    .build();
        }

        state = Hawk.get("main-state", init());

        /* intial render in onCreateOptionsMenu because android calls onCreate before
         * onCreateOptionsMenu and we need the menu to render.
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m) ;
        m.add("Commit").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu = m;
        render();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (state.history().isEmpty()) {
            finish();
        } else {
            state = state.back();
            render();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        Hawk.put("main-state", state);
        super.onSaveInstanceState(b);
    }

    private void render() {
        setContentView(TopView.view(this, state));
        TopView.updateMenu(this, menu, state);
    }

    public void navigate(Id id) {
        state = state.navigate(id);
        render();
    }

    public void setCalisthenicExercise(int position) {
        state = state.setCalisthenicExercise(position);
    }

    public void finishCalisthenicWorkout() {
        navigate(CALISTHENIC_FEEDBACK);
    }
}
