package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DBSQLManager;

public class Base {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public String getComm(Object name) throws SQLException
	{
		String comment = null;
		DBSQLManager dbsm = new DBSQLManager();
		try
		{
			String sql="select comments from base where field = '"+String.valueOf(name)+"'";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			if(rs.next())
				comment = rs.getString("comments");
		}catch (Exception e) {
			System.out.print(e.toString());
		}
		finally{
			dbsm.close();
		}
		return comment;
	}
	public static void main(String[] args) {
		
		try {
			new Base().getComm("no");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

}
