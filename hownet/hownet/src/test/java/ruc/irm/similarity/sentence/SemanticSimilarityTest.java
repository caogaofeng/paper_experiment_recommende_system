package ruc.irm.similarity.sentence;

import java.io.IOException;

import org.junit.Test;

import ruc.irm.similarity.sentence.morphology.SemanticSimilarity;

public class SemanticSimilarityTest {

    @Test
    public void test() {
    	String s1 = "A cowboy doll is profoundly threatened and jealous when a new spaceman figure supplants him as top toy in a boy,s room.";
        String s2 = "James Bond teams up with the lone survivor of a destroyed Russian research center to stop the hijacking of a nuclear space weapon by a fellow agent formerly believed to be dead.";

//        s1="修改下密码";
//        s2="密码修改";
        SemanticSimilarity similarity = SemanticSimilarity.getInstance();
        //double sim = similarity.getSimilarity(s1, s2);
        String[] s = similarity.segment(s1);
        for(int i =0;i<s.length;i++)
        	System.out.print(s[i]+"/");
        
        
    }
    public static void main(String[] args) throws IOException {
		new SemanticSimilarityTest().test();
	}

}
