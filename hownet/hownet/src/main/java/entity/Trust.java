package entity;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DBSQLManager;

public class Trust {
	// 用户a曾经最大的评论
	public int getcomment_max(String user_a) throws SQLException {
		DBSQLManager dbsm = new DBSQLManager();

		String comments_len1 = null;
		String comments_len2 = null;
		int comment_max1 = 0;
		int comment_max2 = 0;
		int comment_max = 0;
		try {
			String sql1 = "select comments_len  from statuses_bak where commenter_id = '"
					+ user_a + "' order by cast(comments_len as int) desc ";
			dbsm.setSqlStr(sql1);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();

			if (rs.next())
				comments_len1 = rs.getString("comments_len").trim();
			// System.out.println("用户"+a+"的最大评论:"+comments_len);
			if (comments_len1 == null) {
				comment_max1 = 0;
			} else {
				comment_max1 = Integer.parseInt(comments_len1);
				comment_max = comment_max1;
			}

			String sql2 = "select comments_len  from statuses_bak where true_statuseser = '"
					+ user_a + "' order by cast(comments_len as int) desc ";
			dbsm.setSqlStr(sql2);
			dbsm.executeQuery();
			rs = dbsm.getRs();
			if (rs.next())
				comments_len2 = rs.getString("comments_len").trim();
			if (comments_len2 == null) {
				comment_max2 = 0;
			} else {
				comment_max2 = Integer.parseInt(comments_len2);
			}

			dbsm.close();
		} catch (Exception e) {
		}

		if (comment_max1 < comment_max2) {
			comment_max = comment_max2;
		}
		return comment_max;
	}

	// 用户a 和 b 之间的评论长度
	public int getcommentlength(String user_a, String user_b) throws Exception {

		String commentsAB_len1 = null;
		String commentsAB_len2 = null;
		int lengthAB1;
		int lengthAB2;
		int lengthAB = 0;
		DBSQLManager dbsm = new DBSQLManager();

		String sql1 = "select comments_len from statuses_bak where true_statuseser ='"
				+ user_b
				+ "' and commenter_id ='"
				+ user_a
				+ "' order by cast(comments_len as int) desc ";
		String sql2 = "select comments_len from statuses_bak where true_statuseser ='"
				+ user_a
				+ "' and commenter_id ='"
				+ user_b
				+ "' order by cast(comments_len as int) desc ";

		try {
			dbsm.setSqlStr(sql1);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			if (rs.next())
				commentsAB_len1 = rs.getString("comments_len").trim();
			// System.out.println("用户"+a+" 和"+ b+" 之间的评论长度"+commentsAB_len1);
			if (commentsAB_len1 == null) {
				lengthAB1 = 0;
				// System.out.println("用户"+a+" 和"+ b+" 之间的评论长度"+lengthAB1);
			} else {
				lengthAB1 = Integer.parseInt(commentsAB_len1);
				lengthAB = lengthAB1;
				// System.out.println("用户"+a+" 和"+ b+" 之间的评论长度"+lengthAB1);
			}

			dbsm.setSqlStr(sql2);
			dbsm.executeQuery();
			rs = dbsm.getRs();
			if (rs.next())
				commentsAB_len2 = rs.getString("comments_len").trim();
			if (commentsAB_len2 == null) {
				lengthAB2 = 0;
				// System.out.println("用户"+a+" 和"+ b+" 之间的评论长度"+lengthAB2);
			} else {
				lengthAB2 = Integer.parseInt(commentsAB_len2);
				// System.out.println("用户"+a+" 和"+ b+" 之间的评论长度"+lengthAB2);
			}
			if (lengthAB1 < lengthAB2) {
				lengthAB = lengthAB2;
			}
			dbsm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lengthAB;
	}

	// 用户a和 b 之间的最评论数
	public int getFrequency(String user_a, String user_b) throws Exception {

		int FrequencyAB1 = 0;
		int FrequencyAB2 = 0;
		int FrequencyAB = 0;
		DBSQLManager dbsm = new DBSQLManager();
		String sql1 = "select count(*) num1 from statuses_bak where true_statuseser ='"
				+ user_b + "' and commenter_id ='" + user_a + "'";
		String sql2 = "select count(*) num2 from statuses_bak where true_statuseser ='"
				+ user_a + "' and commenter_id ='" + user_b + "'";

		try {
			dbsm.setSqlStr(sql1);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			if (rs.next())
				FrequencyAB1 = Integer.parseInt(rs.getString("num1").trim());
			// System.out.println("用户"+a+"和"+ b+" 之间的评论频率"+FrequencyAB1);
			FrequencyAB = FrequencyAB1;

			dbsm.setSqlStr(sql2);
			dbsm.executeQuery();
			rs = dbsm.getRs();
			if (rs.next())
				FrequencyAB2 = Integer.parseInt(rs.getString("num2").trim());
			// System.out.println("用户"+a+"和"+ b+" 之间的评论频率"+FrequencyAB2);
			dbsm.close();
			if (FrequencyAB1 < FrequencyAB2) {
				FrequencyAB = FrequencyAB2;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return FrequencyAB;
	}

	// 用户a曾经评论的最大频
	public int getFrequency_max(String user_a) throws Exception {
		String Frequency1 = null;
		String Frequency2 = null;
		int frequency1 = 0;
		int frequency2 = 0;
		int max_frequency = 0;
		DBSQLManager dbsm = new DBSQLManager();
		String sql1 = "select count(true_statuseser ) cou  from statuses_bak  where commenter_id='"
				+ user_a + "'group by true_statuseser order by cou desc";
		try {
			dbsm.setSqlStr(sql1);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			if (rs.next())
				Frequency1 = rs.getString("cou").trim();
			if (Frequency1 == null) {
				frequency1 = 0;
				// System.out.println("用户a曾经评论的最大频"+frequency);
			} else {
				frequency1 = Integer.parseInt(Frequency1);
				max_frequency = frequency1;
				// System.out.println("用户a曾经评论的最大频"+frequency);
			}

			String sql2 = "select count( commenter_id) cou1 from statuses_bak  where true_statuseser='"
					+ user_a + "'group by commenter_id order by cou1 desc";

			dbsm.setSqlStr(sql2);
			dbsm.executeQuery();
			rs = dbsm.getRs();
			if (rs.next())
				Frequency2 = rs.getString("cou1").trim();
			if (Frequency2 == null) {
				frequency2 = 0;
				// System.out.println("用户a曾经评论的最大频"+frequency);
			} else {
				frequency2 = Integer.parseInt(Frequency2);
				// System.out.println("用户a曾经评论的最大频"+frequency);
			}

			if (frequency2 > frequency1) {
				max_frequency = frequency2;
			}
			dbsm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return max_frequency;
	}

	// 信任度
	public double trush(String user_a, String user_b) throws Exception {
		double alf = 0.5;
		double trushAB = 0.0;
		Trust t = new Trust();
		if (user_a == user_b) {
			trushAB = 1;
		} else if (t.getcomment_max(user_a) == 0
				|| t.getFrequency_max(user_a) == 0) {
			trushAB = 0;
		} else {
			trushAB = alf * t.getcommentlength(user_a, user_b)
					/ t.getcomment_max(user_a) + (1 - alf)
					* t.getFrequency(user_a, user_b)
					/ t.getFrequency_max(user_a);
		}
		// System.out.println(trushAB);
		return trushAB;
	}

	// 信任关系集合
	public ArrayList<String> S(String user_a) {

		ArrayList<String> S = new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		DBSQLManager dbsm = new DBSQLManager();
		String sql1 = "select distinct commenter_id from statuses_bak where true_statuseser= '"
				+ user_a + "'";
		String sql2 = "select distinct true_statuseser from statuses_bak where commenter_id= '"
				+ user_a + "'";

		try {
			dbsm.setSqlStr(sql1);
			dbsm.executeQuery();
			ResultSet rs = dbsm.getRs();
			while (rs.next()) {
				list1.add(rs.getString("commenter_id").trim());
			}

			dbsm.setSqlStr(sql2);
			dbsm.executeQuery();
			rs = dbsm.getRs();
			while (rs.next()) {
				list2.add(rs.getString("true_statuseser").trim());
			}
			for (int i = 0; i < list1.size(); i++) {
				list.add(list1.get(i));
			}
			for (int i = 0; i < list2.size(); i++) {

				list.add(list2.get(i));

			}

			for (int i = 1; i < list.size(); i++) {
				if (list.get(i) == list.get(i - 1)) {
					list.remove(i);
				}
			}
			for (int i = 0; i < list.size(); i++) {
				S.add(list.get(i));

			}
			dbsm.close();
		} catch (Exception e) {
		}
		return S;
	}

	public static void main(String[] args) throws Exception {
		ArrayList<String> T = new ArrayList<String>();
		DBSQLManager dbsm = new DBSQLManager();
		String sql = "select true_statuseser, commenter_id from statuses_bak ";

		dbsm.setSqlStr(sql);
		dbsm.executeQuery();
		ResultSet rs = dbsm.getRs();
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();

		while (rs.next()) {
			list1.add(rs.getString("true_statuseser"));
			list2.add(rs.getString("commenter_id"));
		}

		dbsm.close();
		Trust t = new Trust();
		for (int i = 0; i < list1.size(); i++) {

			// System.out.println("用户"+list1.get(i)+"的最大交流信息长度："+t.getcomment_max(list1.get(i)));
			// System.out.println("用户"+list1.get(i)+"和"+list2.get(i)+"的交流信息长度："+t.getcommentlength(list1.get(i),list2.get(i)));
			// System.out.println("用户"+list1.get(i)+"和"+list2.get(i)+"的交流频率"+t.getFrequency(list1.get(i),list2.get(i)));
			// System.out.println("用户"+list1.get(i)+"的最大交流频率"+t.getFrequency_max(list1.get(i)));
			System.out.println("用户" + list1.get(i) + "与" + list2.get(i)
					+ "用户的信任度：" + t.trush(list1.get(i), list2.get(i)));

			System.out.println("用户" + list1.get(i) + "的信任用户集合"
					+ t.S(list1.get(i)));

			// if (t.trush(list1.get(i),list2.get(i))!=0) {
			// T.add(list2.get(i));
			// System.out.println("用户"+list1.get(i)+"的信任用户集合："+T);
			// }

		}

		// for (int i = 0; i < T.size(); i++) {
		// System.out.println("用户"+list1.get(i)+"的信任用户集合："+T);
		// }

	}
}
