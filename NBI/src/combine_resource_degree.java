import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class combine_resource_degree {

	public static ArrayList<Integer> read_data(String filePath) {
		ArrayList<Integer> S = new ArrayList<Integer>();
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;

				while ((lineTxt = bufferedReader.readLine()) != null) {

					if (lineTxt.split("=").length > 1) {
						Integer temp = Integer.valueOf(lineTxt.split("=")[0]);
						S.add(temp);
					}else {
						 S.add(Integer.valueOf(lineTxt));
					}
				}
				read.close();
			} else {
				System.out.println("文件不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return S;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<Integer> resource_user = new ArrayList<Integer>();
		ArrayList<Integer> user_degree = new ArrayList<Integer>();
		resource_user = read_data("./resource_user.txt");
		
		user_degree = read_data("./user_degree.txt");
	
		Map<Integer, Double> user_P = new HashMap<Integer, Double>();
		float alp = 6.0f;
		for (int i = 0; i < user_degree.size(); i++) {
			for (int j = 0; j < resource_user.size(); j++) {
				if (user_degree.get(i).equals(resource_user.get(j))) {
					double p_v = (1 - alp)*(943 - i - 1)/(943 - 1) + alp * (943 - j - 1)/(943 - 1);
					user_P.put(i + 1, p_v);
				}
			}
		}
		
		// 对核心用户根据resource 排序
				List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
						user_P.entrySet());
				Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {
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
						// return (o1.getKey()).toString().compareTo(o2.getKey()); //
						// 根据key排序
					}
				});
				// 排序后
				for (int i = 0; i < infoIds.size(); i++) {
					String id = infoIds.get(i).toString();
					System.out.println(id);
				}
		
	}
}
