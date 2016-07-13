package ruc.irm.similarity.sentence.editdistance;

import java.util.ArrayList;
import java.util.List;

public class Split {
	public static boolean MERGE_FLAG = true;
	@SuppressWarnings("unchecked")
	public static Object[] split(SuperString<? extends EditUnit> X, SuperString<? extends EditUnit> Y){
		Block<? extends EditUnit> LX = new Block(X);
		Block<? extends EditUnit> LY = new Block(Y);
		split(LX,LY);
		while(LY.getPrev()!=null){
			LY = LY.getPrev();
		}
		while(LX.getPrev()!=null){
			LX = LX.getPrev();
		}
		List<ChunkEditUnit> first = new ArrayList<ChunkEditUnit>();
		List<ChunkEditUnit> second = new ArrayList<ChunkEditUnit>();
		while(LX!=null){
			first.add(new ChunkEditUnit(LX.getData()));
			LX = LX.getNext();
		}

		while(LY!=null){
			second.add(new ChunkEditUnit(LY.getData()));
			LY = LY.getNext();
		}
		SuperString<ChunkEditUnit> s1 = new SuperString<ChunkEditUnit>(first);
		SuperString<ChunkEditUnit> s2 = new SuperString<ChunkEditUnit>(second);
		Object[] obj = new Object[]{s1, s2};
		return obj;
	}
	
	private static void split(Block<?> bx, Block<?> LY){
		LCS maxLCS = null;
		Block<?> by = LY;
		while(by.getPrev()!=null){
			by = by.getPrev();
		}
		Block<?> maxMatchedBy = by;
		while(by!=null){
			if(by.isDivideFlag()){
				by = by.getNext();
				continue;
			}
			
			LCS lcs = LCS.parse(bx.getData(), by.getData());
			if(maxLCS==null || maxLCS.length<lcs.length){
				maxLCS = lcs;
				maxMatchedBy = by;
			}
			
			by = by.getNext();
		}
		
		if(maxLCS!=null && maxLCS.length>0){
			bx.divide(maxLCS.x_pos, maxLCS.length);
			maxMatchedBy.divide(maxLCS.y_pos, maxLCS.length);
		}		
		
		if(bx.getPrev()!=null && !bx.isDivideFlag()){
			split(bx.getPrev(), LY);
		}
		
		if(bx.getNext()!=null &&!bx.getNext().isDivideFlag()){
			split(bx.getNext(), LY);
		}
		
	}
	
	/** 
	 * longest common string
	 * @author Gavin
	 *
	 */
	public static class LCS {
		public int length = 0;  //LCS匹配的最长结果
		public int x_pos = 0;	//LCS匹配的X的位置
		public int y_pos = 0;	//LCS匹配的Y的位置
		
		public static LCS parse(SuperString<?> X, SuperString<?> Y){
			LCS lcs = new LCS();
			for(int start=0; start<X.length(); start++){
				for(int end=start+1; end<=X.length(); end++){
					SuperString<?> tempX = X.substring(start, end);
					
					int pos = Y.indexOf(tempX);					
					if(pos>=0 && tempX.length()>lcs.length){
						lcs.length = tempX.length();
						lcs.x_pos = start;
						lcs.y_pos = pos;
					}					
				}
			}
			return lcs;
		}
		
		public String toString(){
			return "length=" + length + ", x_pos=" + x_pos + ", y_pos=" + y_pos;
		}
	}
	
	public static void main(String[] args) {
		String s1 = "abcdefghijkabc";
		String s2 = "cdefghijklabccc";
//		s2 = "fgabcdehijklkdslfkasdflak";
//		s1 = "abcdefgxyzoxyjasdkfjjjaldsfa";
//		s1 = "I like the book";
//		s2 = "the book I like";
		s1 = "什么是计算机病毒";
		s2 = "电脑病毒是什么";

//		SuperString<CharEditUnit> ss1 = SuperString.createCharSuperString(s1);
//		SuperString<CharEditUnit> ss2 = SuperString.createCharSuperString(s2);
		
		SuperString<WordEditUnit> ss1 = SuperString.createWordSuperString(s1);
		SuperString<WordEditUnit> ss2 = SuperString.createWordSuperString(s2);
		Split.split(ss1, ss2);
//		LCS lcs = LCS.parse(ss1, ss2);
//		System.out.println(lcs);
	}
}
