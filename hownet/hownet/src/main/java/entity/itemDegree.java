package entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;

import dao.DBSQLManager;

public class itemDegree {
	

	public static void main(String[] args) {
		
		ArrayList<String> S = new ArrayList<String>();
		DBSQLManager dbsm = new DBSQLManager();
		double avgFreq = 0;
		String sql = "select a.degree from (select itemId,count(distinct userId) degree from [spider].[dbo].[TM] where itemId in (select itemId from [spider].[dbo].[TM] ) group by itemId) a order by a.degree desc";
		
		try {
			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			while(rs.next())
				S.add(rs.getString("degree").trim());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		StringBuffer IO= new StringBuffer();
		for(int i = 0; i < S.size(); i++){
			
			
			IO.append(i + "\t" + S.get(i));
			IO.append("\n");
		}
		try{
			File file = new File("./item_degree_TM.txt");
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
