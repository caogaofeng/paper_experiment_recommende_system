

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetScore implements Base {
	static Map<Integer, Double> neighborMap = new HashMap<Integer, Double>();
	public static double [][]getScore(int[][] user_movie_base,double[][] combineMatrix ){
	
	double[][] matrix = new double[PREFROWCOUNT][PREFROWCOUNT];
	// 进行评分预测
	List<Integer> neighborSerial = new ArrayList<Integer>();
	for (int i = 0; i < KNEIGHBOUR; i++) {
		neighborSerial.clear();
		int j = 0;
		int itemId = 0;
		for (; j < PREFROWCOUNT; j++) {
			if (user_movie_base[i][j] == 0) {
				double similaritySum = 0;
				double sum = 0;
				double score = 0;
				// 该方法有三个参数，score表示某一用户对所有项目的评分；i表示某个项目的序号;combineMatrix表示项目间的相似性
				neighborSerial = new FindKNeighbors().findKNeighbors(user_movie_base[i], j, combineMatrix);
				// System.out.println(neighborSerial);
				
				for(int z = 0; z < neighborSerial.size(); z++){
					if (neighborMap.containsKey(neighborSerial.get(z)+1)) {
						neighborMap.put(neighborSerial.get(z)+1, neighborMap.get(neighborSerial.get(z) +1) + 1.0/(z+1));
					}else {
						neighborMap.put(neighborSerial.get(z)+1, 1.0 / (z+1));
					}
				}
				
				for (int m = 0; m < neighborSerial.size(); m++) {
					sum += combineMatrix[j][neighborSerial.get(m)]* user_movie_base[i][neighborSerial.get(m)];
					similaritySum += combineMatrix[j][neighborSerial.get(m)];
				}
				if (similaritySum == 0)
					score = 0;
				else
					score = sum / similaritySum;
				itemId = j;
				matrix[i][itemId] = score;
			}
			
		}
	}
	
	List<Map.Entry<Integer, Double>> infoIds =
		    new ArrayList<Map.Entry<Integer, Double>>(neighborMap.entrySet());

		// 对核心用户根据rank得分 排序
		Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {   
		    public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
		        if ((o2.getValue() - o1.getValue()) > 0) {
		        	return 1;
				}else if ((o2.getValue() - o1.getValue()) < 0) {
					return -1;
				}else {
					return 0;
				}
		    	 
		        //return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		}); 

		StringBuffer IO= new StringBuffer();
		for(int i = 0; i < infoIds.size(); i++){
			IO.append(infoIds.get(i));
			System.out.println(infoIds.get(i));
			IO.append("\n");
		}
		try{
			File file = new File("./core_user_2_rank_base.txt");
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

		//排序后
		
	return matrix;
	}
}
