package entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ruc.irm.similarity.sentence.morphology.SemanticSimilarity;

public class Forecast_Score {

	/**
	 * @param args
	 * @throws Exception 
	 */
	/*计算预测公式相关系数B*/
	public double forecast_B(String user_id,String app_id,double score) throws Exception
	{
		Apps app = new Apps();
		Users user = new Users();
		Trust trust = new Trust();
		UserSimilarity US = new UserSimilarity();
		double average_score = user.TolScore(user_id);
		double forecast_score = 0,forecast_score_simi = 0,forecast_score_tru = 0;
		double weight = 0.8;
		double B = 0;
		//得到user_id的相似用户集合
		List<String> list_simi = US.User_Simi_Set(user_id);
		//得到user_id的信任用户集合
		List<String> list_tru = trust.S(user_id);
		if(list_simi.size() != 0&&list_tru.size() != 0)
		{
			double score_fenzi = 0,score_fenmu = 0;
			for(int i =0;i<list_simi.size();i++)
			{
				String user_temp = list_simi.get(i).split("\\$")[0];
				Double user_score = Double.valueOf(list_simi.get(i).split("\\$")[1]);
				score_fenzi += user_score * (user.Score_S_to_I(user_temp, app_id) - user.TolScore(user_temp));
				score_fenmu += user_score;
			}
			forecast_score_simi = score_fenzi/score_fenmu;
			//forecast_score += average_score + weight * score_fenzi/score_fenmu;
			score_fenzi = 0;
			score_fenmu = 0;
			for(int i =0;i<list_tru.size();i++)
			{
				String user_temp = list_tru.get(i);
				score_fenzi += trust.trush(user_id, user_temp) * (user.GetScore(user_temp, app_id) - user.TolScore(user_temp));
				score_fenmu += trust.trush(user_id, user_temp);
			}
			forecast_score_tru = score_fenzi/score_fenmu;
			//forecast_score += (1 - weight) * score_fenzi/score_fenmu;
		}
		return (score - average_score - forecast_score_tru)/(forecast_score_simi - forecast_score_tru) ;
	}
	/*预测用户user_id对应用app_id的评分*/
	public double forecast_score(String user_id,String app_id,List<String> list_simi,int simi_num) throws Exception
	{
		Apps app = new Apps();
		Users user = new Users();
		Trust trust = new Trust();
		UserSimilarity US = new UserSimilarity();
		double average_score = user.TolScore(user_id);
		double forecast_score = 0;
		double weight = 0.0;
		int list_simi_size = list_simi.size();
		//得到user_id的相似用户集合
		//List<String> list_simi = US.User_Simi_Set(user_id);
		//得到user_id的信任用户集合
		if(list_simi_size >= simi_num)
			list_simi_size = simi_num;
		List<String> list_tru = trust.S(user_id);
		if(list_simi.size() != 0&&list_tru.size() != 0)
		{
			double score_fenzi = 0,score_fenmu = 0;
			for(int i =0;i<list_simi_size;i++)
			{
				String user_temp = list_simi.get(i).split("\\$")[0];
				Double user_score = Double.valueOf(list_simi.get(i).split("\\$")[1]);
				Double simi_score = user.Score_S_to_I(user_temp, app_id);
				if(simi_score != 0)
				{
					score_fenzi += user_score * (simi_score - user.TolScore(user_temp));
					score_fenmu += user_score;
				}
			}
			if(score_fenmu == 0)
				forecast_score += average_score;
			else
				forecast_score += average_score + weight * score_fenzi/score_fenmu;
			score_fenzi = 0;
			score_fenmu = 0;
			for(int i =0;i<list_tru.size();i++)
			{
				String user_temp = list_tru.get(i);
				if(user.GetScore(user_temp, app_id) != 0)
				{
					score_fenzi += trust.trush(user_id, user_temp) * (user.GetScore(user_temp, app_id) - user.TolScore(user_temp));
					score_fenmu += trust.trush(user_id, user_temp);
				}
			}
			if(score_fenmu == 0)
			{
				forecast_score += 0;
				return forecast_score;
			}
			forecast_score += (1 - weight) * score_fenzi/score_fenmu;
		}
		if(list_simi.size() != 0&&list_tru.size() == 0)
		{
			double score_fenzi = 0,score_fenmu = 0;
			for(int i =0;i<list_simi_size;i++)
			{
				String user_temp = list_simi.get(i).split("\\$")[0];
				Double user_score = Double.valueOf(list_simi.get(i).split("\\$")[1]);
				Double simi_score = user.Score_S_to_I(user_temp, app_id);
				if(simi_score != 0)
				{
					score_fenzi += user_score * (simi_score - user.TolScore(user_temp));
					score_fenmu += user_score;
				}
			}
			if(score_fenmu == 0)
			{
				forecast_score += 0;
				return forecast_score;
			}
			forecast_score += average_score + score_fenzi/score_fenmu;
		}
		if(list_simi.size() == 0 &&list_tru.size() != 0)
		{
			double score_fenzi = 0,score_fenmu = 0;
			for(int i =0;i<list_tru.size();i++)
			{
				String user_temp = list_tru.get(i);
				double score_temp = user.GetScore(user_temp, app_id);
				double score_1 = user.TolScore(user_temp);
				if(score_temp != 0)
				{
					score_fenzi += trust.trush(user_id, user_temp) * (score_temp - score_1);
					score_fenmu += trust.trush(user_id, user_temp);
				}
			}
			if(score_fenmu == 0)
			{
				forecast_score += 0;
				return forecast_score;
			}
			forecast_score += average_score + score_fenzi/score_fenmu;
		}
		return forecast_score;
	}
	/*基于语意相似度的预测*/
	public double forecast_score_yuyi(String user_id,String app_id,List<String> list_simi,int simi_num) throws Exception
	{
		Apps app = new Apps();
		Users user = new Users();
		UserSimilarity US = new UserSimilarity();
		double average_score = user.TolScore(user_id);
		double forecast_score = 0;
		int list_simi_size = list_simi.size();
		//得到user_id的相似用户集合
		//List<String> list_simi = US.User_Simi_Set(user_id);
		//得到user_id的信任用户集合
		if(list_simi_size >= simi_num)
			list_simi_size = simi_num;
		if(list_simi.size() != 0)
		{
			double score_fenzi = 0,score_fenmu = 0;
			for(int i =0;i<list_simi_size;i++)
			{
				String user_temp = list_simi.get(i).split("\\$")[0];
				Double user_score = Double.valueOf(list_simi.get(i).split("\\$")[1]);
				Double simi_score = user.Score_S_to_I(user_temp, app_id);
				if(simi_score != 0)
				{
					score_fenzi += user_score * (simi_score - user.TolScore(user_temp));
					score_fenmu += user_score;
				}
			}
			if(score_fenmu == 0)
			{
				forecast_score += 0;
				return forecast_score;
			}
			forecast_score += average_score + score_fenzi/score_fenmu;
		}
		return forecast_score;
	}
	/*通常预测用户user_id对应用app_id的评分*/
	public double forecast_score_pearson(String user_id,String app_id,List<String> list_simi,int simi_num) throws Exception
	{
		Apps app = new Apps();
		Users user = new Users();
		UserSimilarity US = new UserSimilarity();
		double average_score = user.TolScore(user_id);
		double forecast_score = 0;
		int list_simi_size = list_simi.size();
		//得到user_id的相似用户集合
		//List<String> list_simi = US.User_Simi_Set(user_id);
		//得到user_id的信任用户集合
		if(list_simi_size >= simi_num)
			list_simi_size = simi_num;
		if(list_simi.size() != 0)
		{
			double score_fenzi = 0,score_fenmu = 0;
			for(int i =0;i<list_simi_size;i++)
			{
				String user_temp = list_simi.get(i).split("\\$")[0];
				Double user_score = Double.valueOf(list_simi.get(i).split("\\$")[1]);
				Double simi_score = (double) user.GetScore(user_temp, app_id);
				if(simi_score != 0)
				{
					if(user.TolScore(user_temp) == 0)
						continue;
					else
					{
						score_fenzi += user_score * (simi_score - user.TolScore(user_temp));
						score_fenmu += user_score;
					}
				}
			}
			if(score_fenmu == 0)
			{
				forecast_score += 0;
				return forecast_score;
			}
			forecast_score += average_score + score_fenzi/score_fenmu;
		}
		return forecast_score;
	}
	public double MAE_pearson(int num) throws Exception
	{
		ArrayList<String> commenters = new Commenters().commenters();
		Forecast_Score FS = new Forecast_Score(); 
		Users user = new Users();
		UserSimilarity US = new UserSimilarity();
		double res = 0;
		for(int i = 0;i<commenters.size();i++)
		{
			ArrayList<String> apps = user.TolApps(commenters.get(i));
			List<String> list_simi = US.User_Simi_Set(commenters.get(i));
			for(int j = 0;j<apps.size();j++)
			{
				res += Math.abs(FS.forecast_score_pearson(commenters.get(i), apps.get(j).split("\\$")[0],list_simi,num) - Double.valueOf(apps.get(j).split("\\$")[1]));
				System.out.println("res:"+res);
			}
		}
		return res/154;
	}
	public double MAE(int num) throws Exception
	{
		ArrayList<String> commenters = new Commenters().commenters();
		Forecast_Score FS = new Forecast_Score(); 
		Users user = new Users();
		UserSimilarity US = new UserSimilarity();
		double res = 0;
		for(int i = 0;i<commenters.size();i++)
		{
			ArrayList<String> apps = user.TolApps(commenters.get(i));
			List<String> list_simi = US.User_Simi_Set(commenters.get(i));
			for(int j = 0;j<apps.size();j++)
			{
				res += Math.abs(FS.forecast_score(commenters.get(i), apps.get(j).split("\\$")[0],list_simi,num) - Double.valueOf(apps.get(j).split("\\$")[1]));
				System.out.println("res:"+res);
			}
		}
		return res/154;
	}
	public double MAE_yuyi(int num) throws Exception
	{
		ArrayList<String> commenters = new Commenters().commenters();
		Forecast_Score FS = new Forecast_Score(); 
		Users user = new Users();
		UserSimilarity US = new UserSimilarity();
		double res = 0;
		for(int i = 0;i<commenters.size();i++)
		{
			ArrayList<String> apps = user.TolApps(commenters.get(i));
			List<String> list_simi = US.User_Simi_Set(commenters.get(i));
			for(int j = 0;j<apps.size();j++)
			{
				res += Math.abs(FS.forecast_score_yuyi(commenters.get(i), apps.get(j).split("\\$")[0],list_simi,num) - Double.valueOf(apps.get(j).split("\\$")[1]));
				System.out.println("res:"+res);
			}
		}
		return res/154;
	}
	public static void main(String[] args) throws Exception {
		// TODO 自动生成的方法存根
		//System.out.print(new Forecast_Score().forecast_score("1576590", "22616865"));
		System.out.println(new Forecast_Score().MAE(10));/*2.2896229931825047  0.927940261307527(0.1) 0.9420770480364882(0.5)*/
		//System.out.println(new Forecast_Score().MAE_pearson(11));/*5.1896229931825047 2.936334547905935*/
		//System.out.println(new Forecast_Score().MAE_yuyi(13));
		//List<String> list_simi = new UserSimilarity().User_Simi_Set("kimikostyleT");
		//System.out.println(new Forecast_Score().forecast_score("kimikostyleT", "23009526",list_simi));/*4.599571840188757  5*/
		//System.out.println(new Forecast_Score().forecast_score("Obsessive", "22608936"));/*4.205971834182898  4*/
		//System.out.print(new Forecast_Score().forecast_score("slowlemon", "11486034"));/*4.205971834182898  5*/
		//System.out.print(new Forecast_Score().forecast_score("slowlemon", "11455837"));/*4.205971834182898  5*/
		//System.out.println(new Forecast_Score().forecast_score("slowlemon", "11451328"));/*4.205971834182898  4*/
		//select * from comments_bak a where a.comment_id ='1576590'
	}

}
