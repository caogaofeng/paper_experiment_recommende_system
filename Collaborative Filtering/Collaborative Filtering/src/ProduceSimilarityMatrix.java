import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;



public class ProduceSimilarityMatrix implements Base{
		public double[][] produceSimilarityMatrix(int[][] preference) {
		double[][] similarityMatrix = new double[PREFROWCOUNT][PREFROWCOUNT];
		for (int i = 0; i < PREFROWCOUNT; i++) {
			for (int j = 0; j < PREFROWCOUNT; j++) {
				if (i == j) {
					similarityMatrix[i][j] = 1;
				}
				else {
					similarityMatrix[i][j] = 
							new ComputeSimilarity().computeSimilarity(preference[i], preference[j]);
				}			
			}
		}
		return similarityMatrix;
	}

		
	public static void main(String[] args){
		int[] coverage = new int[943];
		double avg_coverage_rat = 0;
		int[][] user_movie_base = new int[PREFROWCOUNT][COLUMNCOUNT];
		user_movie_base = new ReadFile().readFile(BASE_LINE, BASE);
		double[][] similarityMatrix = new ProduceSimilarityMatrix()
		.produceSimilarityMatrix(user_movie_base);
		
		String url = "./core_user_2_rank_base.txt";
	    ArrayList<String> S = new ArrayList<String>();
	    String core_user_id = null;
	    try {
            String encoding="GBK";
            File file=new File(url);
            if(file.isFile() && file.exists()){ 
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
            	while((lineTxt = bufferedReader.readLine()) != null)
            	{
            		core_user_id = lineTxt.split("=")[0];
            		S.add(core_user_id);
                }
            	System.out.println(S);
                read.close();
            }else{
            	System.out.println("文件不存在");
            }
	    } catch (Exception e) {
        System.out.println("");
        e.printStackTrace();
    }

		
		for (String j : S) {
			for (int i = 0; i < similarityMatrix.length; i++) {
				if (similarityMatrix[Integer.valueOf(j)][i] > 0) {
					coverage[Integer.valueOf(j)] ++;
				}
			}
		}
		int[] coverage_1 = new int[943];
		for (int i = 0; i < coverage.length; i++) {
			if ((coverage[i] / 943.0) > 0.6) {
				coverage_1[i] = i;
			}
		}
		
		try{
			
			StringBuffer IO= new StringBuffer();
			for (int i = 0; i < coverage_1.length; i++) {
				if (coverage_1[i] > 0) {
					IO.append(i);
					IO.append("\n");
				}
			}
			
			File file = new File("./core_user_2_rank_base_coverage(0.6).txt");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			BufferedWriter output = new BufferedWriter(new FileWriter(file)); 
			output.write(IO.toString());
			System.out.println("write done!");
			output.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}		 

		// 最小覆盖率（64）的用户
		for (int i = 0; i < coverage.length; i++) {
			if (coverage[i] == 64) {
				System.out.println(" zuixiaode :" + S.get(i));
			}
		}
		
		Arrays.sort(coverage);
		for (int i = 0; i < coverage.length; i++) {
			System.out.println(coverage[i] + "  " + coverage[i] / 943.0);
			
			avg_coverage_rat += coverage[i] / 943.0;
			
		}
		System.out.println();
		System.out.println(avg_coverage_rat / 188);
	}

}
