package ruc.irm.similarity.sentence;

import java.io.IOException;

import org.junit.Test;
import ruc.irm.similarity.sentence.morphology.MorphoSimilarity;

public class MorphoSimilarityTest {

    @Test
    public void test() {
        String s1 = "A cowboy doll is profoundly threatened and jealous when a new spaceman figure supplants him as top toy in a boy,s room.";
        String s2 = "James Bond teams up with the lone survivor of a destroyed Russian research center to stop the hijacking of a nuclear space weapon by a fellow agent formerly believed to be dead.";

   
        MorphoSimilarity similarity = MorphoSimilarity.getInstance();
        double sim = similarity.getSimilarity(s1, s2);
        System.out.println("sim ==> " + sim);
    }
    public static void main(String[] args) throws IOException {
		new MorphoSimilarityTest().test();
		
	}

}
