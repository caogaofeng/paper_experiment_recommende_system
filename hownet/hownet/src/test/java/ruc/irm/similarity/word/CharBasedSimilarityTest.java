package ruc.irm.similarity.word;

import java.io.IOException;

import junit.framework.TestCase;

public class CharBasedSimilarityTest extends TestCase {
    public void test() {
        CharBasedSimilarity sim = new CharBasedSimilarity();
        String s1 = "手机";
        String s2 = "微信";
        System.out.print(sim.getSimilarity(s1, s2));
        assertTrue(sim.getSimilarity(s1, s2) > 0);
    }
    public static void main(String[] args) throws IOException {
		new CharBasedSimilarityTest().test();
	}
}
