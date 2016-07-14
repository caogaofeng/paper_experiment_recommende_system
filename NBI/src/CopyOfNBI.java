import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CopyOfNBI {

	/* 计算用户的度 */
	public static int userDegree(int user_id, int[][] arr) {
		int degree = 0;
		for (int i = 0; i < arr[0].length; i++) {
			if (arr[user_id - 1][i] != 0) {
				degree++;
			}
		}
		return degree;
	}

	/* 计算项目的度 */
	public static int itemDegree(int item_id, int[][] arr) {
		int degree = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i][item_id - 1] != 0) {
				degree++;
			}
		}
		return degree;
	}

	/* 计算与用户相关的项目集合 */
	public static ArrayList<Integer> itemAssociatedWithUser(int user_id,
			int[][] arr) {
		ArrayList<Integer> s = new ArrayList<Integer>();
		for (int i = 0; i < arr[0].length; i++) {
			if (arr[user_id - 1][i] != 0) {
				s.add(i + 1);
			}
		}
		return s;
	}

	/* 计算与项目相关的用户集合 */
	public static ArrayList<Integer> userAssociatedWithItem(int item_id,
			int[][] arr) {
		ArrayList<Integer> s = new ArrayList<Integer>();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i][item_id - 1] != 0) {
				s.add(i + 1);
			}
		}
		return s;
	}

	public static List<Map.Entry<Integer, Double>> resource_flow(int user_id, int[][] user_movie_base){
		double userSource, itemSource;
		ArrayList<Integer> itemSet = new ArrayList<Integer>();
		ArrayList<Integer> userSet = new ArrayList<Integer>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		HashMap<Integer, Double> userhashmap = new HashMap<Integer, Double>();
		Map<Integer, Double> itemhashmap = new HashMap<Integer, Double>();
		itemSet.clear();
		userSet.clear();
		temp.clear();
		userhashmap.clear();
		itemhashmap.clear();
		
		ArrayList<Integer> coreuser = new ArrayList<Integer>();
		coreuser = ReadDate_CoreUser("./p_v(0.4).txt", 0.8);
		// 得到用户直接相关的item集合（目标用户资源流出去到项目）
		itemSet = itemAssociatedWithUser(user_id, user_movie_base);

		// System.out.println("itemSet的大小：" + itemSet.size());

		// 得到核心用户集合

		// System.out.println("核心用户集合的大小：" + coreuser.size());

		for (int i : itemSet) {

			temp.clear();
			// 得到每个项目相关的用户集合（项目资源流回来到最近邻用户）
			temp = userAssociatedWithItem(i, user_movie_base);
			for (int u : temp) {
				// 得到与项目相关的所有用户集合
				if (coreuser.contains(u) && !userSet.contains(u)) {
					userSet.add(u);
				}
				// 得到用户的资源（从项目流过来的资源到最近邻用户）
				userSource = 1.0 / itemDegree(i, user_movie_base);
				if (userhashmap.containsKey(u)) {
					userhashmap.put(u, userSource + userhashmap.get(u));
				} else {
					userhashmap.put(u, userSource);
				}
			}
		}

		// System.out.println("userSet集合的大小:" + userSet.size());

		for (int u : userSet) {

			temp.clear();
			temp = itemAssociatedWithUser(u, user_movie_base);

			for (int i : temp) {
				// 得到相关项目的资源（从用户流回去的资源到待推荐的项目）
				itemSource = 1.0 / userDegree(u, user_movie_base)
						* userhashmap.get(u);
				if (itemhashmap.containsKey(i)) {
					itemhashmap.put(i, itemSource + itemhashmap.get(i));

				} else {
					itemhashmap.put(i, itemSource);

				}
			}
		}

		// System.out.println("待推荐项目集合的大小：" + itemhashmap.size());
		// System.out.println(itemhashmap);

		List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
				itemhashmap.entrySet());

		// 对核心用户根据rank得分 排序
		Collections.sort(infoIds,
				new Comparator<Map.Entry<Integer, Double>>() {
					public int compare(Map.Entry<Integer, Double> o1,
							Map.Entry<Integer, Double> o2) {
						// 根据value排序
						if ((o2.getValue() - o1.getValue()) > 0) {
							return 1;
						} else if ((o2.getValue() - o1.getValue()) < 0) {
							return -1;
						} else {
							return 0;
						}
						// return
						// (o1.getKey()).toString().compareTo(o2.getKey());
						// // 根据key排序
					}
				});
		return infoIds;
	}
	/* 读取核心用户集合文件 */
	public static ArrayList<Integer> ReadDate_CoreUser(String url, double rate) {
		ArrayList<Integer> S_coreuser = new ArrayList<Integer>();

		int flag = 1;
		try {
			String encoding = "GBK";
			File file = new File(url);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// 读取全部核心用户
					if (lineTxt.split("=").length > 1) {
						int core_user_id = Integer
								.valueOf(lineTxt.split("=")[0]);
						S_coreuser.add(core_user_id);

					} else {
						S_coreuser.add(Integer.valueOf(lineTxt));

					}

					// 按百分比读取核心用户
					flag++;
					if (flag > (943 * rate)) {
						break;
					}
				}
				read.close();
			} else {
				System.out.println("文件不存在");
			}
		} catch (Exception e) {
			System.out.println("");
			e.printStackTrace();
		}
		return S_coreuser;
	}

	public static void main(String[] args) throws SQLException, IOException {

		String BASE = "u1.base"; // 训练集
		int[][] user_movie_base = new int[943][1682];
		double[] recall = new double[943]; // 召回率

		user_movie_base = new ReadFile().readFile(BASE);
		double userSource, itemSource;
		ArrayList<Integer> itemSet = new ArrayList<Integer>();
		ArrayList<Integer> userSet = new ArrayList<Integer>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		HashMap<Integer, Double> userhashmap = new HashMap<Integer, Double>();
		Map<Integer, Double> itemhashmap = new HashMap<Integer, Double>();
		ArrayList<Integer> recommdList = new ArrayList<Integer>();
		ArrayList<Integer> coreuser = new ArrayList<Integer>();
		double[] novelty = new double[943];
		coreuser = ReadDate_CoreUser("./p_v(0.4).txt", 0.8);
		System.out.println(coreuser.size());
		for (int p = 1; p <= 943; p++) {

			int user_id = p;
			itemSet.clear();
			userSet.clear();
			temp.clear();
			userhashmap.clear();
			itemhashmap.clear();
			recommdList.clear();
			double right_Num_of_list = 0.0;
			// 得到用户直接相关的item集合（目标用户资源流出去到项目）
			itemSet = itemAssociatedWithUser(user_id, user_movie_base);

			// System.out.println("itemSet的大小：" + itemSet.size());

			// 得到核心用户集合

			// System.out.println("核心用户集合的大小：" + coreuser.size());

			for (int i : itemSet) {

				temp.clear();
				// 得到每个项目相关的用户集合（项目资源流回来到最近邻用户）
				temp = userAssociatedWithItem(i, user_movie_base);
				for (int u : temp) {
					// 得到与项目相关的所有用户集合
					if (coreuser.contains(u) && !userSet.contains(u)) {
						userSet.add(u);
					}
					// 得到用户的资源（从项目流过来的资源到最近邻用户）
					userSource = 1.0 / itemDegree(i, user_movie_base);
					if (userhashmap.containsKey(u)) {
						userhashmap.put(u, userSource + userhashmap.get(u));
					} else {
						userhashmap.put(u, userSource);
					}
				}
			}

			// System.out.println("userSet集合的大小:" + userSet.size());

			for (int u : userSet) {

				temp.clear();
				temp = itemAssociatedWithUser(u, user_movie_base);

				for (int i : temp) {
					// 得到相关项目的资源（从用户流回去的资源到待推荐的项目）
					itemSource = 1.0 / userDegree(u, user_movie_base)
							* userhashmap.get(u);
					if (itemhashmap.containsKey(i)) {
						itemhashmap.put(i, itemSource + itemhashmap.get(i));

					} else {
						itemhashmap.put(i, itemSource);

					}
				}
			}

			// System.out.println("待推荐项目集合的大小：" + itemhashmap.size());
			// System.out.println(itemhashmap);

			List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
					itemhashmap.entrySet());

			// 对核心用户根据rank得分 排序
			Collections.sort(infoIds,
					new Comparator<Map.Entry<Integer, Double>>() {
						public int compare(Map.Entry<Integer, Double> o1,
								Map.Entry<Integer, Double> o2) {
							// 根据value排序
							if ((o2.getValue() - o1.getValue()) > 0) {
								return 1;
							} else if ((o2.getValue() - o1.getValue()) < 0) {
								return -1;
							} else {
								return 0;
							}
							// return
							// (o1.getKey()).toString().compareTo(o2.getKey());
							// // 根据key排序
						}
					});

			for (int i = 0; i < 15; i++) {

				recommdList.add(infoIds.get(i).getKey());
				// System.out.println(recommdList.get(i));
			}
			double sum_degree_rlist = 0;
			for (int i = 0; i < recommdList.size(); i++) {
				sum_degree_rlist += itemDegree(recommdList.get(i), user_movie_base);
			}
			novelty[p - 1] = sum_degree_rlist / 15.0;
			// System.out.println(recommdList);
			for (int i = 0; i < recommdList.size(); i++) {
				if (itemSet.contains(recommdList.get(i))) {
					right_Num_of_list++;
				}
			}

			recall[p - 1] = right_Num_of_list / itemSet.size();

		}

		double sum_recall = 0.0;
		Arrays.sort(recall);
		for (int i = 0; i < recall.length; i++) {
			sum_recall += recall[i];
		}
		System.out.println("平均Recall:" + sum_recall / 943.0);
		double sum_novelty = 0.0;
		for (int i = 0; i < novelty.length; i++) {
			sum_novelty += novelty[i];
		}
		System.out.println("平均novelty：" + sum_novelty / 943.0);
	}
}
