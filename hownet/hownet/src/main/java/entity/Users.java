package entity;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;

import ruc.irm.similarity.sentence.morphology.SemanticSimilarity;
import dao.DBSQLManager;

public class Users {

	/**
	 * @param args
	 */
	protected String no ;		
	protected String type ;		
	protected String state ;		
	protected String price ;
	/*得到评论表中的所有用户 comment_id*/
	public ArrayList<String> users(String col) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		int i=0;
		ArrayList<String> comments = new ArrayList<String>();
		try
		{
			String sql="SELECT distinct("+col+") comments FROM comments_bak_2";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			while(rs.next())
				comments.add(rs.getString("comments").trim());
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		return comments;
	}
	/*得到某用户评分的所有软件与对应评分*/
	public ArrayList<String> TolApps(String name) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		int i=0;
		ArrayList<String> tolapps= new ArrayList<String>();
		try
		{
			String sql="SELECT app_id,score FROM comments_bak_2 where comment_id = '"+name+"'";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			while(rs.next())
				tolapps.add(rs.getString("app_id").trim()+"$"+rs.getString("score").trim());
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		return tolapps;
	}
	/*得到某用户对某应用的评分*/
	public int GetScore(String name,String app) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		int Score = 0;
		try
		{
			String sql="SELECT score FROM comments_bak_2 where comment_id = '"+name+"' and app_id ='"+app+"'";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			if(rs.next())
				Score = Integer.valueOf(rs.getString("score").trim());
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		return Score;
	}
	/*得到某用户打的所有软件总平均分*/
	public double TolScore(String name) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();
		double TolScore = 0;
		double i=0.0;
		try
		{
			String sql="SELECT score FROM comments_bak_2 where comment_id = '"+name+"'";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			while(rs.next())
			{
				TolScore += Integer.valueOf(rs.getString("score").trim());
				i++;
			}
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		if(i == 0)
			return 0;
		return TolScore/i;
	}
	/*求某用户对某应用相似应用的评分*/
	public double Score_S_to_I(String user_id,String app_id) throws SQLException
	{
		Users user = new Users();
		Apps app = new Apps();
		ArrayList<String> apps = app.apps_simi(app_id);
		double score_fenzi = 0,score_fenmu = 0,simi = 0,temp_score;
		if(user.GetScore(user_id, app_id) != 0)
			return user.GetScore(user_id, app_id);
		else
		{
			for(int i = 0;i<apps.size();i++)
			{
				String app_temp = apps.get(i);
				simi = SemanticSimilarity.getInstance().getSimilarity(app_id,app_temp);
				temp_score = user.GetScore(user_id, app_temp);
				score_fenzi += temp_score * simi;
				if (temp_score != 0)
					score_fenmu += simi;
			}
		}
		if(score_fenmu == 0)
			return 0;
		return score_fenzi/score_fenmu;
	}
	public static void main(String[] args) {
		 try {
			 ArrayList<String> commenter = new Commenters().commenters();
			for(int i =0;i<commenter.size();i++)
			{
				ArrayList<String> tolapps = new Users().TolApps(commenter.get(i));
				for(int j=0;j<tolapps.size();j++)
					System.out.println(commenter.get(i)+"评分的应用：："+tolapps.get(j));
				System.out.println(commenter.get(i)+"的总评分："+new Users().TolScore(commenter.get(i)));
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		 
		}

}
