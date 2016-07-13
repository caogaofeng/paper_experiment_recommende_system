package entity;
import ruc.irm.similarity.sentence.morphology.MorphoSimilarity;

import java.sql.ResultSet;

import dao.DBSQLManager;

import java.sql.SQLException;





import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;

import org.junit.Test;

import dao.DBSQLManager;

public class similarityOfItme {
	public String getDescription(int movie_id) throws Exception{
		
		String str = null;
		DBSQLManager dbsm = new DBSQLManager();
		String sql="SELECT description  FROM [MovieLens].[dbo].[Movies_item_1] where movie_id = '" +movie_id + "'";
		dbsm.setSqlStr(sql);
		dbsm.executeQuery();
		
		ResultSet rs =  dbsm.getRs();
		if(rs.next())
			str = rs.getString("description").trim();
		return str;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		similarityOfItme sof = new similarityOfItme();
		MorphoSimilarity similarity = MorphoSimilarity.getInstance();
		StringBuffer IO= new StringBuffer();
	
		try{
			File file = new File("./movie_similarity_matrix.txt");
//			if (file.exists()) {
//				file.delete();
//			}
//			file.createNewFile();
			BufferedWriter output = new BufferedWriter(new FileWriter(file)); 
			IO.append("\n");
			double[][] sim_item = new double[1682][1682];
			for (int i = 0; i < 100; i++) {
				for (int j = 0; j < 1682; j++) {
					if (i == j) {
						sim_item[i][j] = 1;
//						System.out.println(sim_item[i][j]);
						IO.append(sim_item[i][j] + "\t");
						
					}else {
						sim_item[i][j] = similarity.getSimilarity(sof.getDescription(i+1), sof.getDescription(j+1));
//						System.out.println(sim_item[i][j]);
						IO.append(sim_item[i][j] + "\t");
					
					}
				}
				IO.append("\n");
				output.write(IO.toString());
			}
			
			
			System.out.println("write done!");
			output.close();
		
		}catch (Exception ex) {
			System.out.println(ex);
		}
	}

}






 


