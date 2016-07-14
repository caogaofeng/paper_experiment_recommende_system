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

	/* �����û��Ķ� */
	public static int userDegree(int user_id, int[][] arr) {
		int degree = 0;
		for (int i = 0; i < arr[0].length; i++) {
			if (arr[user_id - 1][i] != 0) {
				degree++;
			}
		}
		return degree;
	}

	/* ������Ŀ�Ķ� */
	public static int itemDegree(int item_id, int[][] arr) {
		int degree = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i][item_id - 1] != 0) {
				degree++;
			}
		}
		return degree;
	}

	/* �������û���ص���Ŀ���� */
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

	/* ��������Ŀ��ص��û����� */
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
		// �õ��û�ֱ����ص�item���ϣ�Ŀ���û���Դ����ȥ����Ŀ��
		itemSet = itemAssociatedWithUser(user_id, user_movie_base);

		// System.out.println("itemSet�Ĵ�С��" + itemSet.size());

		// �õ������û�����

		// System.out.println("�����û����ϵĴ�С��" + coreuser.size());

		for (int i : itemSet) {

			temp.clear();
			// �õ�ÿ����Ŀ��ص��û����ϣ���Ŀ��Դ��������������û���
			temp = userAssociatedWithItem(i, user_movie_base);
			for (int u : temp) {
				// �õ�����Ŀ��ص������û�����
				if (coreuser.contains(u) && !userSet.contains(u)) {
					userSet.add(u);
				}
				// �õ��û�����Դ������Ŀ����������Դ��������û���
				userSource = 1.0 / itemDegree(i, user_movie_base);
				if (userhashmap.containsKey(u)) {
					userhashmap.put(u, userSource + userhashmap.get(u));
				} else {
					userhashmap.put(u, userSource);
				}
			}
		}

		// System.out.println("userSet���ϵĴ�С:" + userSet.size());

		for (int u : userSet) {

			temp.clear();
			temp = itemAssociatedWithUser(u, user_movie_base);

			for (int i : temp) {
				// �õ������Ŀ����Դ�����û�����ȥ����Դ�����Ƽ�����Ŀ��
				itemSource = 1.0 / userDegree(u, user_movie_base)
						* userhashmap.get(u);
				if (itemhashmap.containsKey(i)) {
					itemhashmap.put(i, itemSource + itemhashmap.get(i));

				} else {
					itemhashmap.put(i, itemSource);

				}
			}
		}

		// System.out.println("���Ƽ���Ŀ���ϵĴ�С��" + itemhashmap.size());
		// System.out.println(itemhashmap);

		List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
				itemhashmap.entrySet());

		// �Ժ����û�����rank�÷� ����
		Collections.sort(infoIds,
				new Comparator<Map.Entry<Integer, Double>>() {
					public int compare(Map.Entry<Integer, Double> o1,
							Map.Entry<Integer, Double> o2) {
						// ����value����
						if ((o2.getValue() - o1.getValue()) > 0) {
							return 1;
						} else if ((o2.getValue() - o1.getValue()) < 0) {
							return -1;
						} else {
							return 0;
						}
						// return
						// (o1.getKey()).toString().compareTo(o2.getKey());
						// // ����key����
					}
				});
		return infoIds;
	}
	/* ��ȡ�����û������ļ� */
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
					// ��ȡȫ�������û�
					if (lineTxt.split("=").length > 1) {
						int core_user_id = Integer
								.valueOf(lineTxt.split("=")[0]);
						S_coreuser.add(core_user_id);

					} else {
						S_coreuser.add(Integer.valueOf(lineTxt));

					}

					// ���ٷֱȶ�ȡ�����û�
					flag++;
					if (flag > (943 * rate)) {
						break;
					}
				}
				read.close();
			} else {
				System.out.println("�ļ�������");
			}
		} catch (Exception e) {
			System.out.println("");
			e.printStackTrace();
		}
		return S_coreuser;
	}

	public static void main(String[] args) throws SQLException, IOException {

		String BASE = "u1.base"; // ѵ����
		int[][] user_movie_base = new int[943][1682];
		double[] recall = new double[943]; // �ٻ���

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
			// �õ��û�ֱ����ص�item���ϣ�Ŀ���û���Դ����ȥ����Ŀ��
			itemSet = itemAssociatedWithUser(user_id, user_movie_base);

			// System.out.println("itemSet�Ĵ�С��" + itemSet.size());

			// �õ������û�����

			// System.out.println("�����û����ϵĴ�С��" + coreuser.size());

			for (int i : itemSet) {

				temp.clear();
				// �õ�ÿ����Ŀ��ص��û����ϣ���Ŀ��Դ��������������û���
				temp = userAssociatedWithItem(i, user_movie_base);
				for (int u : temp) {
					// �õ�����Ŀ��ص������û�����
					if (coreuser.contains(u) && !userSet.contains(u)) {
						userSet.add(u);
					}
					// �õ��û�����Դ������Ŀ����������Դ��������û���
					userSource = 1.0 / itemDegree(i, user_movie_base);
					if (userhashmap.containsKey(u)) {
						userhashmap.put(u, userSource + userhashmap.get(u));
					} else {
						userhashmap.put(u, userSource);
					}
				}
			}

			// System.out.println("userSet���ϵĴ�С:" + userSet.size());

			for (int u : userSet) {

				temp.clear();
				temp = itemAssociatedWithUser(u, user_movie_base);

				for (int i : temp) {
					// �õ������Ŀ����Դ�����û�����ȥ����Դ�����Ƽ�����Ŀ��
					itemSource = 1.0 / userDegree(u, user_movie_base)
							* userhashmap.get(u);
					if (itemhashmap.containsKey(i)) {
						itemhashmap.put(i, itemSource + itemhashmap.get(i));

					} else {
						itemhashmap.put(i, itemSource);

					}
				}
			}

			// System.out.println("���Ƽ���Ŀ���ϵĴ�С��" + itemhashmap.size());
			// System.out.println(itemhashmap);

			List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
					itemhashmap.entrySet());

			// �Ժ����û�����rank�÷� ����
			Collections.sort(infoIds,
					new Comparator<Map.Entry<Integer, Double>>() {
						public int compare(Map.Entry<Integer, Double> o1,
								Map.Entry<Integer, Double> o2) {
							// ����value����
							if ((o2.getValue() - o1.getValue()) > 0) {
								return 1;
							} else if ((o2.getValue() - o1.getValue()) < 0) {
								return -1;
							} else {
								return 0;
							}
							// return
							// (o1.getKey()).toString().compareTo(o2.getKey());
							// // ����key����
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
		System.out.println("ƽ��Recall:" + sum_recall / 943.0);
		double sum_novelty = 0.0;
		for (int i = 0; i < novelty.length; i++) {
			sum_novelty += novelty[i];
		}
		System.out.println("ƽ��novelty��" + sum_novelty / 943.0);
	}
}
