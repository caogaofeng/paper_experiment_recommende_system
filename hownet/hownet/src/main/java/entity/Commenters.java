package entity;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import dao.DBSQLManager;

public class Commenters{
	protected int no = 0;		
	protected String name = null;		
	protected String age = null;		
	protected String tel = null;		
	/*得到评论表中的所有用户*/
	public ArrayList<String> commenters() throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		int i=0;
		ArrayList<String> commenters = new ArrayList<String>();
		try
		{
			String sql="  SELECT comment_id FROM comments_bak_2 group by comment_id";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			while(rs.next())
				commenters.add(rs.getString("comment_id").trim());
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		return commenters;
	}
	public int getno() {
		return no;
	}
	public String getname() {
		return name;
	}
	public String getage() {
		return age;
	}
	public String gettel() {
		return tel;
	}
	 public static void main(String[] args) {
		 try {
			ArrayList<String> commenters = new Commenters().commenters();
			for(int i =0;i<commenters.size();i++)
				System.out.println(commenters.get(i));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		 
		}
}
