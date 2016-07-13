

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindKNeighbors implements Base{
	/**
	 * This method is used to find the nearest K neighbors to the un_scored item 
	 * @param score
	 * @param i
	 * @param similarityMatrix
	 * @return
	 */
	//该方法有三个参数，score表示某一用户对所有项目的评分；i表示某个项目的序号
	public  List<Integer> findKNeighbors(int[] score,int i,double[][] similarityMatrix) {	
		List<Integer> neighborSerial = new ArrayList<Integer>();
		double[] similarity = new double[similarityMatrix.length];
		for (int j = 0; j < similarityMatrix.length; j++) {
			if(score[j] != 0) {
				similarity[j] = similarityMatrix[j][i];
			} 
			else {
				similarity[j] = 0;
			}
		}
		double[] temp = new double[similarity.length];
		for (int j = 0; j < temp.length; j++) {
			temp[j] = similarity[j];
		}
		Arrays.sort(temp);

		
		for (int m = temp.length - 1; m >= temp.length - KNEIGHBOUR; m--) {
			for(int j = 0; j < similarity.length; j++) {
				if (similarity[j] == temp[m] && similarity[j] != 0.0)
					neighborSerial.add(new Integer(j));/*添加一个整型数据对象*/
			}	
		}
		return neighborSerial; 
	}

}
