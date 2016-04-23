package sebluy.exercise;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import auto.parcelgson.AutoParcelGson;

@AutoParcelGson
public abstract class CalisthenicExercise implements Parcelable {

    public enum Type {PUSH_UP, PULL_UP, CORE, SQUAT, LUNGE}

    @AutoValue
    abstract static class Template {
        public abstract int repetitions();
        public abstract int sets();
        public abstract List<String> variations();

        static Template create(int r, int s, List<String> v) {
            return new AutoValue_CalisthenicExercise_Template(r, s, v);
        }
    }

    private static final Template pushUp = Template.create(9, 6,
            Collections.unmodifiableList(Arrays.asList(
                    "Normal Push-up",
                    "Knuckle Push-up",
                    "Wide Push-up",
                    "Diamond Push-up",
                    "T Push-up",
                    "Pike Push-Up",
                    "Dive Bomber Push-Up"
    )));

    private static final Template pullUp = Template.create(8, 4,
            Collections.unmodifiableList(Arrays.asList(
                    "Normal Pull-Up",
                    "Narrow Pull-Up",
                    "Parallel Pull-Up",
                    "Wide Pull-Up",
                    "Normal Chin-Up",
                    "Narrow Chin-Up"
    )));

    private static final Template core = Template.create(20, 5,
            Collections.unmodifiableList(Arrays.asList(
                    "Normal Crunch",
                    "Reverse Crunch",
                    "Double Crunch",
                    "Bicycle Crunch",
                    "Wiper",
                    "Flutter Kick"
    )));

    private static final Template squat = Template.create(18, 5,
            Collections.unmodifiableList(Arrays.asList(
                    "Squat",
                    "Vertical Jump",
                    "Forward Jump",
                    "Lateral Jump"
    )));

    private static final Template lunge = Template.create(20, 5,
            Collections.unmodifiableList(Arrays.asList(
                    "Forward Lunge",
                    "Backward Lunge",
                    "Side Lunge"
    )));

    private static final Map<Type, Template> templates;
    static {
        Map<Type, Template> t = new HashMap<>();
        t.put(Type.PUSH_UP, pushUp);
        t.put(Type.PULL_UP, pullUp);
        t.put(Type.CORE, core);
        t.put(Type.SQUAT, squat);
        t.put(Type.LUNGE, lunge);
        templates = Collections.unmodifiableMap(t);
    }

    private static final List<Type> order =
            Collections.unmodifiableList(
                    Arrays.asList(Type.PUSH_UP, Type.SQUAT, Type.CORE, Type.PULL_UP, Type.LUNGE));

    private static class VariationStream {

        private final List<String> variations;
        private int index;

        public VariationStream(List<String> v) {
            variations = v;
            index = 0;
        }

        public String next() {
            String ret = variations.get(index);
            if (index == variations.size() - 1) {
                index = 0;
            } else {
                index++;
            }
            return ret;
        }
    }

    /* Returns an infinite stream of random variations.
    * The variations are shuffled into a random order and then repeated
    * infinitely.
    */
    private static VariationStream randomVariations(List<String> variations) {
        List<String> l = new ArrayList<>(variations);
        Collections.shuffle(l);
        return new VariationStream(l);
    }

    /* Returns a list formed by taking a single element in order from each sub-list
    * until all sub-lists are empty.
    */
    public static <E> List<E> interleave(List<List<E>> ll) {
        List<E> result = new ArrayList<>();
        int maxSize = 0;
        for (List<E> l : ll) {
            maxSize = maxSize > l.size() ? maxSize : l.size();
        }
        for (int j = 0; j < maxSize; j++) {
            for (int i = 0; i < ll.size(); i++) {
                List<E> l = ll.get(i);
                if (j < l.size()) {
                    result.add(l.get(j));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    public static List<CalisthenicExercise> buildExercises(Type type) {
        Template template = templates.get(type);
        List<CalisthenicExercise> exercises = new ArrayList<>();
        VariationStream variations = randomVariations(template.variations());
        int sets = template.sets();
        for (int set = 1; set <= sets; set++) {
            String variation = variations.next();
            int extraReps = (set == sets ? sets : 0);
            int reps = template.repetitions() + extraReps;
            exercises.add(CalisthenicExercise.create(type, reps, set, variation));
        }
        return Collections.unmodifiableList(exercises);
    }

    public static List<CalisthenicExercise> generateWorkout() {
        List<List<CalisthenicExercise>> ll = new ArrayList<>();
        for (Type t : order) {
            ll.add(buildExercises(t));
        }
        return interleave(ll);
    }

    public abstract Type type();
    public abstract int repetitions();
    public abstract int set();
    public abstract String name();

    public static CalisthenicExercise create(Type t, int r, int s, String n) {
        return new AutoParcelGson_CalisthenicExercise(t, r, s, n);
    }

}
