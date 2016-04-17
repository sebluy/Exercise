package sebluy.exercise;

import org.junit.Test;

import java.util.Iterator;

import fj.data.Array;
import fj.data.List;
import fj.data.Seq;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void generateCalisthenicWorkout() throws Exception {
        Seq<CalisthenicExercise> w = CalisthenicExercise.generateWorkout();
        assert(w.length() >= 20);
    }

    @Test
    public void interleave() throws Exception {
        assert(CalisthenicExercise.interleave(Seq.arraySeq(
                Seq.arraySeq(1,2,3),
                Seq.arraySeq(4,5),
                Seq.arraySeq(6,7,8))).
                equals(Seq.arraySeq(1,4,6,2,5,7,3,8)));
    }

}