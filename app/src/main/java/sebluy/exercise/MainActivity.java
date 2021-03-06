package sebluy.exercise;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.hawk.GsonParser;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.Map;

import sebluy.exercise.CalisthenicExercise.Template;
import sebluy.exercise.CalisthenicExercise.Type;
import sebluy.exercise.MainState.Page.State.CalisthenicFeedback;

import static sebluy.exercise.MainState.Page.Id;
import static sebluy.exercise.MainState.Page.Id.CALISTHENIC_FEEDBACK;
import static sebluy.exercise.MainState.init;

public class MainActivity extends AppCompatActivity {

    private MainState state;
    private Menu menu;

    public static final String HAWK_MAIN_STATE = "main-state";
    public static final String HAWK_CALISTHENIC = "calisthenic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeHawk(this);

        /* for bad states in development */
//        Hawk.remove(HAWK_CALISTHENIC);
//        Hawk.remove(HAWK_MAIN_STATE);

        state = Hawk.get(HAWK_MAIN_STATE, init());

        /* intial render in onCreateOptionsMenu because android calls onCreate before
         * onCreateOptionsMenu and we need the menu to render.
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m) ;
//        m.add("Commit").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
        Hawk.put(HAWK_MAIN_STATE, state);
        super.onSaveInstanceState(b);
    }

    private void render() {
        setContentView(TopView.view(this, state));
        TopView.updateMenu(this, menu, state);
    }

    public void navigate(Id id) {
        switch (id) {
            case CALISTHENIC_WORKOUT:
                Map<Type, Template> templates =
                        Hawk.get(HAWK_CALISTHENIC, CalisthenicExercise.TEMPLATES);
                state = state.navigateCalisthenicWorkout(templates);
                break;
            default:
                state = state.navigate(id);
                break;
        }
        render();
    }

    public void setCalisthenicExercise(int position) {
        state = state.setCalisthenicExercise(position);
    }

    public void finishCalisthenicWorkout() {
        navigate(CALISTHENIC_FEEDBACK);
    }

    public void setCalisthenicFeedback(int pos, boolean b) {
        state = state.setCalisthenicFeedback(pos, b);
    }

    public void commitCalisthenicWorkout() {
        CalisthenicFeedback feedback = (CalisthenicFeedback)state.page().state();
        Map<Type, Template> last = Hawk.get(HAWK_CALISTHENIC, CalisthenicExercise.TEMPLATES);
        Map<Type, Template> next =
                CalisthenicExercise.nextTemplates(last, feedback.results());
        Hawk.put(HAWK_CALISTHENIC, next);
        state = state.back().back(); /* find a better way */
        render();
    }

    public static void initializeHawk(Context c) {
        if (!Hawk.isBuilt()) {
            Hawk.init(c)
                    .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                    .setStorage(HawkBuilder.newSharedPrefStorage(c))
                    .setLogLevel(LogLevel.NONE)
                    .setParser(new GsonParser(GsonConverter.buildGson()))
                    .build();
        }
    }

    public MainState getState() {
        return state;
    }

    @Override
    public void onCreateContextMenu(ContextMenu m, View v, ContextMenuInfo i) {
        super.onCreateContextMenu(m, v, i);
        MenuItem item = m.add("Reset to defaults");
        item.setOnMenuItemClickListener((menuItem) -> Hawk.remove(HAWK_CALISTHENIC));
    }
}
