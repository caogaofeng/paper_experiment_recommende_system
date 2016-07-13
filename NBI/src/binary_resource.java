import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class binary_resource {
	
	/*计算用户的度*/
	public static int userDegree(int user_id, int[][]  arr)  {
		int degree = 0; 
		for (int i = 0; i < arr[0].length; i++) {
			if (arr[user_id-1][i] != 0) {
				degree ++;
			} 
		}
		return degree;
	}

	/*计算项目的度*/
	public static int itemDegree(int item_id, int[][]  arr)  {

		int degree = 0; 
		for (int i = 0; i < arr.length; i++) {
			if (arr[i][item_id-1] != 0) {
				degree ++;
			} 
		}
		return degree;
	}


	public static void main(String[] args){
		
		String BASE = "u1.base"; //训练集
		int[][] user_movie_base = new int[943][1682];
		user_movie_base = new ReadFile().readFile(BASE);
		Map<Integer, Double> itemhashmap = new HashMap<Integer, Double>();
		Map<Integer, Double> userhashmap = new HashMap<Integer, Double>();
		
		/*第一趟  得到项目的资源 itemhashmap<item, resource> 初始用户的资源数为1*/
		for (int i = 0; i < user_movie_base.length; i++) {
			for (int j = 0; j < user_movie_base[0].length; j++){
			
				if (user_movie_base[i][j] != 0) {
					if (itemhashmap.containsKey(j+1)) {
						itemhashmap.put(j+1, itemhashmap.get(j+1) + (1.0 / userDegree(i + 1, user_movie_base)));
					}else {
						itemhashmap.put(j+1, (1.0 / userDegree(i + 1, user_movie_base)));
					}
				}
				}
		}
		
		/*第二趟 得到用户的资源 userhashmap<item, resource>*/
		for (int j = 0; j < user_movie_base[0].length; j++){
			for (int i = 0; i < user_movie_base.length; i++) {
			
				if (user_movie_base[i][j] != 0) {
					if (userhashmap.containsKey(i+1)) {
						userhashmap.put(i+1, userhashmap.get(i+1) + (1.0 / itemDegree(j + 1, user_movie_base)*itemhashmap.get(j+1)));
					}else {
						userhashmap.put(i+1, (1.0 / itemDegree(j + 1, user_movie_base)*itemhashmap.get(j+1)));	
					}
				}
				}
		}
		
		// 对核心用户根据resource 排序
		List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
				userhashmap.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
					Map.Entry<Integer, Double> o2) {
				// 根据value排序
				if ((o2.getValue() - o1.getValue()) > 0) {
					return 1;
				} else if ((o2.getValue() - o1.getValue()) < 0) {
					return -1;
				} else {
					return 0;
				}
				// return (o1.getKey()).toString().compareTo(o2.getKey()); //
				// 根据key排序
			}
		});
		// 排序后
		for (int i = 0; i < infoIds.size(); i++) {
			String id = infoIds.get(i).toString();
			System.out.println(id);
		}
		
	}
}
