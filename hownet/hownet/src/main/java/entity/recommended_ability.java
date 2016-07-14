package entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;

import dao.DBSQLManager;


public class recommended_ability {
	public static int degreeOfUser(String user){
		int degree = 0;
		DBSQLManager dbsm = new DBSQLManager();
		String sql = "select count(distinct item_id) degree from [MovieLens].[dbo].[u] where user_id ='" + user + "'";
		
		try {
			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			if(rs.next())
				degree = rs.getInt("degree");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return degree;
		
	}
	public static int Freq(String user){
		int freq = 0;
		DBSQLManager dbsm = new DBSQLManager();
		String sql = "select count(user_id) freq from [MovieLens].[dbo].[u] "
				+ "where item_id in (select distinct item_id from [MovieLens].[dbo].[u] "
				+ "where user_id='" + user + "')";
		
		try {
			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			if(rs.next())
				freq = rs.getInt("freq");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
//		System.out.println(freq);
		return freq;
		
	}

	
	public static void main(String[] args) {
		
		ArrayList<String> S = new ArrayList<String>();
		DBSQLManager dbsm = new DBSQLManager();
		double avgFreq = 0;
		String sql = "select distinct user_id from [MovieLens].[dbo].[u]";
		
		try {
			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			while(rs.next())
				S.add(rs.getString("user_id").trim());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		StringBuffer IO= new StringBuffer();
		for(String user : S){
			double degreeOfUser = degreeOfUser(user);
			double freq = Freq(user);
			avgFreq = freq/degreeOfUser;
//			System.out.println(degreeOfUser+" " + freq + " "+ avgFreq);
			IO.append(user +"\t" + degreeOfUser + "\t" + avgFreq);
			IO.append("\n");
		}
		try{
			File file = new File("./avgfreq_U.txt");
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

	}

}
