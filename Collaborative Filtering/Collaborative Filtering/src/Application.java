

public class Application extends PrintArray implements Base {

	public static void main(String[] args) {
		
		int[][] user_movie_base = new int[PREFROWCOUNT][COLUMNCOUNT];
		user_movie_base = new ReadFile().readFile(BASE_LINE, BASE); // base����943���û���1682����Ŀ������
		
		int[][] test = new ReadFile().readFile(TEST_LINE, TEST); // 462���û���ʵ������
		double[][] similarityMatrix = new ProduceSimilarityMatrix()
				.produceSimilarityMatrix(user_movie_base);
		double[][] matrix = GetScore
				.getScore(user_movie_base, similarityMatrix);
		
		/**
		double[] mae = new ProduceMAE().produceMAE(matrix, test);
		double Mae = 0.0, MAE = 0.0;
		for (int k = 0; k < mae.length; k++) {
			Mae += mae[k];
		}
		MAE = Mae / TESTROWCOUNT;
		System.out.println("MAE=:" + MAE);
		*/
	}

}
