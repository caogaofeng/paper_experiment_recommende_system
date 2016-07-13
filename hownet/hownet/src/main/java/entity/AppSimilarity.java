package entity;

import java.sql.SQLException;
import java.util.ArrayList;

import ruc.irm.similarity.sentence.morphology.SemanticSimilarity;

public class AppSimilarity {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public double[][] appsimilarity() throws SQLException
	{
		ArrayList<String> apps = new Apps().apps("name");
		double appsimilarity[][] = new double[apps.size()][apps.size()];
		for(int i = 0;i<apps.size();i++)
			for(int j = 0;j<apps.size();j++)
			{
				appsimilarity[i][j] = SemanticSimilarity.getInstance().getSimilarity(apps.get(i), apps.get(j));
			}
		return appsimilarity;
	}
	public static void main(String[] args) throws SQLException {
		// TODO 自动生成的方法存根
		double appsimilarity[][] = new AppSimilarity().appsimilarity();
		for(int i = 0;i<appsimilarity[0].length;i++)
		{
			for(int j = 0;j<appsimilarity[0].length;j++)
				System.out.print(appsimilarity[i][j]+" ");
			System.out.println();
		}
	}

}
