

import java.util.List;

public class PearsonCorrelation implements Base {
	
	public double pearsonCorrelation(List<Integer> a,List<Integer> b) {
		int num = a.size();
		int sum_prefOne = 0;
		int sum_prefTwo = 0;
		int sum_squareOne = 0;
		int sum_squareTwo = 0;
		int sum_product = 0;
		for (int i = 0; i < num; i++) {
			sum_prefOne += a.get(i);
			sum_prefTwo += b.get(i);
			sum_squareOne += Math.pow(a.get(i), 2);
			sum_squareTwo += Math.pow(b.get(i), 2);
			sum_product += a.get(i) * b.get(i);
		}
		double sum = num * sum_product - sum_prefOne * sum_prefTwo;
		double den = Math.sqrt((num * sum_squareOne - Math.pow(sum_squareOne, 2)) * (num * sum_squareTwo - Math.pow(sum_squareTwo, 2)));
		double result;
		if(den==0) result=0;
		else result = sum / den;
	   
		return result;
	}

}
