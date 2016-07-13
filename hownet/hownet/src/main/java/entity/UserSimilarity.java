package entity;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.DBSQLManager;

import ruc.irm.similarity.sentence.morphology.SemanticSimilarity;

public class UserSimilarity{

	/**
	 * @param args
	 * @throws SQLException 
	 */
	/*计算用户相似度*/
	public double usersimilarity(String A_User,String B_User) throws SQLException
	{
		Users users = new Users();
		Apps app = new Apps();
		ArrayList<String> A_Apps = users.TolApps(A_User);
		ArrayList<String> B_Apps = users.TolApps(B_User);
		double A_TolScore = users.TolScore(A_User);
		double B_TolScore = users.TolScore(B_User);
		double A_Score[] = new double[100];
		double B_Score[] = new double[100];
		double result_member = 0.0,result_denominator = 0.0,result_denominator_1 = 0.0,result_denominator_2 = 0;
		double temp;
		int za=0,zb=0;
		for(int i =0 ;i<A_Apps.size();i++)
		{
			temp = Double.valueOf(A_Apps.get(i).split("\\$")[1]);
			if(temp == 0)
				continue;
			A_Score[za] = temp - A_TolScore;
			for(int j =0 ;j<B_Apps.size();j++)
			{
				temp = Double.valueOf(B_Apps.get(j).split("\\$")[1]);
				if(temp == 0)
					continue;
				if(app.IsClustering(A_Apps.get(i).split("\\$")[0], B_Apps.get(j).split("\\$")[0]))
				{
					if(A_Apps.get(i).split("\\$")[0].equals( B_Apps.get(j).split("\\$")[0]))
					{
						B_Score[zb] = temp - B_TolScore;
						result_member += A_Score[za] * B_Score[zb] * 1;/*相同应用的相似度为1*/
						zb++;
						za++;
					}
					else
					{
						B_Score[zb] = temp - B_TolScore;
						result_member += A_Score[za] * B_Score[zb] * SemanticSimilarity.getInstance().getSimilarity(app.Intro(A_Apps.get(i).split("\\$")[0]), app.Intro(B_Apps.get(j).split("\\$")[0]));/*相似应用的相似度*/
						zb++;
						za++;
					}
				}
			}
		}
		for(int i=0;i<zb;i++)
			result_denominator_1 += Math.pow(A_Score[i], 2);
		for(int i=0;i<zb;i++)
			result_denominator_2 += Math.pow(B_Score[i], 2);
		result_denominator = Math.pow(result_denominator_1 * result_denominator_2, 0.5);
		if(result_member == 0&&result_denominator ==0)
			return 0;
		return result_member/result_denominator;
	}
	/*得到某个用户的相似用户集和相似度.*/
	public ArrayList<String> User_Simi_Set(String name) throws SQLException, InterruptedException
	{
		ArrayList<String> list = new ArrayList<String>();
		UserSimilarity US = new UserSimilarity();
		ArrayList<String> commenter = US.User_Simi_Set2(name);
		double simi_score;
		//simi_thread t[]=new simi_thread[commenter.length];
		//for(int i = 0;i<commenter.length;i++)
		//{
			//t[i]=US.new simi_thread(name,commenter[i]);
			//t[i].start();
		//}
		//System.out.println(commenter.size());
		for(int i = 0;i<commenter.size();i++)
		{
			//t[i].join();
			//simi_score = t[i].get_simi();
			if (!name.equals(commenter.get(i)))
			{
				simi_score = US.usersimilarity(name,commenter.get(i));
				System.out.println(name+"和"+commenter.get(i)+"的相似度："+simi_score);
				if(simi_score > 0)
					list.add(commenter.get(i)+"$"+String.valueOf(simi_score));
			}
		}
		return User_Simi_Set_Sort(list);
	}
	/*排序相似度得到最近邻*/
	public ArrayList<String> User_Simi_Set_Sort(ArrayList<String> s)
	{
		String temp;
		for(int i =0;i<s.size();i++)
		{
			for(int j=i+1;j<s.size();j++)
			{
				if(Double.valueOf(s.get(i).split("\\$")[1]) <= Double.valueOf(s.get(j).split("\\$")[1]))
				{
					temp = s.get(i);
					s.set(i, s.get(j));
					s.set(j, temp);
				}
			}
		}
		return s;
	}
	/*得到某个用户的相似用户集 2. 用户 -软件集合-相似软件集合-相似用户集合*/
	public ArrayList<String> User_Simi_Set2(String name) throws SQLException, InterruptedException
	{
		DBSQLManager dbsm = new DBSQLManager();
		ArrayList<String> list = new ArrayList<String>();
		try
		{
			String sql="select distinct comment_id from comments_bak_2 where app_id in(select distinct app_id from apps where clustering in(select distinct clustering from apps where app_id in(select distinct app_id from comments_bak_2 where comment_id = '"+name+"')))";
 			dbsm.setSqlStr(sql);
			dbsm.executeQuery();
			ResultSet rs =  dbsm.getRs();
			while(rs.next())
			{
				String s = rs.getString("comment_id").trim();
				if(name.equals(rs.getString("comment_id").trim()))
					continue;
				list.add(rs.getString("comment_id").trim());
			}
			//System.out.print(name.equals(null));
		}
		catch (Exception e) {
		}
		finally
		{
			dbsm.close();
		}
		return list;
	}
	class simi_thread extends Thread
	{
		String user_id1,user_id2;
		double simi;
		simi_thread(String user_id1,String user_id2)
		{
			this.user_id1 = user_id1;
			this.user_id2 = user_id2;
		}
		public void run(){
			try {
				simi= new UserSimilarity().usersimilarity(user_id1, user_id2);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}		
		}
		public double get_simi()
		{
			return simi;
		}
	}
	public static void main(String[] args) throws SQLException, InterruptedException {
		// TODO 自动生成的方法存根
		ArrayList<String> commenter = new Commenters().commenters();
		UserSimilarity US = new UserSimilarity();
		System.out.println(US.usersimilarity("64414832","67093640"));
		for(int i = 0;i<commenter.size();i++)
		{
		}
			//System.out.println(US.usersimilarity("58163617","forrest_chou_v"));
	}
}
