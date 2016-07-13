package entity;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;

import dao.DBSQLManager;


public class recommended_ability_Coo8 {
	public static int degreeOfUser(String user){
		int degree = 0;
		DBSQLManager dbsm = new DBSQLManager();
		String sql = "select count(distinct itemId) degree from [spider].[dbo].[TM] where userId='" + user + "'";
		
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
		String sql = "select count(distinct userId) freq from [spider].[dbo].[TM] "
				+ "where itemId in (select distinct itemId from [spider].[dbo].[TM] "
				+ "where userId='" + user + "')";
		
		try {
			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			if(rs.next())
				freq = rs.getInt("freq");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return freq;
		
	}

	
	public static void main(String[] args) {
		
		ArrayList<String> S = new ArrayList<String>();
		DBSQLManager dbsm = new DBSQLManager();
		double avgFreq = 0;
		String sql = "select distinct userId from [spider].[dbo].[TM]";
		
		try {
			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			while(rs.next())
				S.add(rs.getString("userId").trim());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		StringBuffer IO= new StringBuffer();
		for(String user : S){
			
			avgFreq = Freq(user)/degreeOfUser(user);
			IO.append(degreeOfUser(user) + "\t" + avgFreq);
			IO.append("\n");
		}
		try{
			File file = new File("./avgfreq_TM.txt");
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

