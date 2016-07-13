package entity;

import java.sql.ResultSet;
import dao.DBSQLManager;
import java.sql.SQLException;
import java.util.ArrayList;



public class Apps {

	/**
	 * @param args
	 */
	protected String no ;		
	protected String type ;		
	protected String state ;		
	protected String price ;
	/*得到所有软件名称*/
	public ArrayList<String> apps(String col) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		int i=0;
		ArrayList<String> apps = new ArrayList<String>();
		try
		{
			String sql="SELECT distinct("+col+") apps FROM apps";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			while(rs.next())
				apps.add( rs.getString("apps").trim());
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		return apps;
	}
	/*得到软件的简介*/
	public String Intro(String app_id) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		int i=0;
		String intro = null;
		try
		{
			String sql="SELECT intro FROM apps where app_id = '"+app_id+"'";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			if(rs.next())
				intro = rs.getString("intro").trim();
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		//System.out.print(app_id+"的简介:"+intro);
		return new Deal_String().deal_string(intro);
	}
	/*判断两个应用是否在一个簇*/
	public boolean IsClustering(String A_App,String B_App) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		int Clustering[] = new int[2];
		int i = 0,temp;
		String apps[] = null;
		if(A_App.equals(B_App))
			return true;
		try
		{
			String sql="SELECT clustering FROM apps where app_id ='"+A_App+"' or app_id ='"+B_App+"'";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			while(rs.next())
				Clustering[i++] = Integer.valueOf(rs.getString("clustering").trim());
			if(Clustering[0] == Clustering[1])
				return true;
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		return false;
	}
	/*得到软件相似软件集合*/
	public ArrayList<String> apps_simi(String app_id) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		int i=0;
		ArrayList<String> apps_simi = new ArrayList<String>();
		try
		{
			String sql=" select app_id from apps where clustering = (select clustering from apps where app_id ='"+app_id+"')";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			while(rs.next())
			{
				if(!app_id.equals(rs.getString("app_id").trim()))
					apps_simi.add(rs.getString("app_id").trim());
			}
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		return apps_simi;
	}
	 public static void main(String[] args) {
		 try {
			/*String apps[] = new Apps().apps("name");
			for(int i =0;i<apps.length;i++)
				System.out.println(apps[i]);*/
			 System.out.println(new Apps().IsClustering("11454413", "11454413"));
			 //System.out.println(new Apps().Intro("22620054"));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		 
		}

}
