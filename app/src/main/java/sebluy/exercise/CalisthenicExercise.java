package sebluy.exercise;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import fj.data.Seq;
import fj.data.Stream;

@AutoValue
public abstract class CalisthenicExercise {

    public enum Type {PUSH_UP, PULL_UP, CORE, SQUAT, LUNGE}

    @AutoValue
    abstract static class Template {
        public abstract int repetitions();
        public abstract int sets();
        public abstract Seq<String> variations();

        static Template create(int r, int s, Seq<String> v) {
            return new AutoValue_CalisthenicExercise_Template(r, s, v);
        }
    }

    private static final Template pushUp = Template.create(6, 6, Seq.arraySeq(
            "Normal Push-up",
            "Knuckle Push-up",
            "Wide Push-up",
            "Diamond Push-up",
            "T Push-up",
            "Pike Push-Up",
            "Dive Bomber Push-Up"
    ));

    private static final Template pullUp = Template.create(4, 6, Seq.arraySeq(
            "Normal Pull-Up",
            "Narrow Pull-Up",
            "Parallel Pull-Up",
            "Wide Pull-Up",
            "Normal Chin-Up",
            "Narrow Chin-Up"
    ));

    private static final Template core = Template.create(14, 6, Seq.arraySeq(
            "Normal Crunch",
            "Reverse Crunch",
            "Double Crunch",
            "Bicycle Crunch",
            "Wiper",
            "Flutter Kick"
    ));

    private static final Template squat = Template.create(12, 6, Seq.arraySeq(
            "Squat",
            "Vertical Jump",
            "Forward Jump",
            "Lateral Jump"
    ));

    private static final Template lunge = Template.create(14, 6, Seq.arraySeq(
            "Forward Lunge",
            "Backward Lunge",
            "Side Lunge"
    ));

    private static final HashMap<Type, Template> templates = new HashMap<>();
    static {
        templates.put(Type.PUSH_UP, pushUp);
        templates.put(Type.PULL_UP, pullUp);
        templates.put(Type.CORE, core);
        templates.put(Type.SQUAT, squat);
        templates.put(Type.LUNGE, lunge);
    }

    private static <E> ArrayList<E> seqToArrayList(Seq<E> s) {
        ArrayList<E> l = new ArrayList<>();
        for (E value : s) {
            l.add(value);
        }
        return l;
    }

    /* Returns an infinite stream of random variations given a type.
    * The variations are shuffled into a random order and then repeated
    * infinitely.
    */
    private static Stream<String> randomVariations(Seq<String> variations) {
        ArrayList<String> l = seqToArrayList(variations);
        Collections.shuffle(l);
        return Stream.cycle(Stream.iterableStream(l));
    }

    /* Returns a seq formed by taking a single element in order from each sub-seq
    * until all sub-seqs are empty.
    */
    public static <E> Seq<E> interleave(Seq<Seq<E>> ss) {
        Seq<E> result = Seq.empty();
        int maxLength = ss.map(Seq::length).foldLeft((b, a) -> b > a ? b : a, 0);
        for (int j = 0; j < maxLength; j++) {
            for (int i = 0; i < ss.length(); i++) {
                Seq<E> s = ss.index(i);
                if (j < s.length()) {
                    result = result.snoc(s.index(j));
                }
            }
        }
        return result;
    }

    private static Seq<CalisthenicExercise> buildExerciseSeq(Type type) {
        Template template = templates.get(type);
        return Seq.iterableSeq(randomVariations(template.variations()).
                take(template.sets()).
                map(variation -> CalisthenicExercise.create(type, template.repetitions(), variation)));
    }

    public static Seq<CalisthenicExercise> generateWorkout() {
        return interleave(
                Seq.arraySeq(Type.PUSH_UP, Type.SQUAT, Type.CORE, Type.PULL_UP, Type.LUNGE).
                map(CalisthenicExercise::buildExerciseSeq));
    }

    public abstract Type type();
    public abstract int repetitions();
    public abstract String name();

    public static CalisthenicExercise create(Type t, int r, String n) {
        return new AutoValue_CalisthenicExercise(t, r, n);
    }

}
