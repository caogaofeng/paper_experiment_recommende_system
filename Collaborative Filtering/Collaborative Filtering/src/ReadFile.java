

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile implements Base{
	
		public int[][] readFile(int rowCount,String fileName) {	
		//	public static final int COLUMNCOUNT = 1682;	//number of items
		//	public static final int PREFROWCOUNT = 943;	//number of users
		//建立数组，用户为列，项目为行，用户总共943，项目1682
		//PREFROWCOUNT 用户数目，COLUMNCOUNT 项目数目
		int [][] user_movie=new int [PREFROWCOUNT][COLUMNCOUNT];
		
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			int i = 0;
			while (br.ready()) {
				line = br.readLine();
				String[] data = line.split("\t");/*用 号分隔数字*/
				int[] ddd = new int[4]; 
				  for (int j = 0; j < data.length; j++) {					   
					  ddd[j] = Integer.parseInt(data[j]);
				}
			 user_movie[ddd[0]-1][ddd[1]-1]=ddd[2];
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*return preference;*/
		return user_movie;
		
	}

}
