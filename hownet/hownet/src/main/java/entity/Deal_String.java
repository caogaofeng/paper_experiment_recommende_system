package entity;

public class Deal_String {

	/**
	 * @param args
	 */
	
	public String deal_string(String str) {
		StringBuffer sb = new StringBuffer();
		int max = 30;
		if(str.length() < max)
			max = str.length();
		try{
        //System.out.println(str);
        for (int i = 0; i < max; i++) {
            if ((str.charAt(i) > 'a'&&str.charAt(i) < 'z')||(str.charAt(i) > 'A'&&str.charAt(i) < 'Z')||(str.charAt(i)+"").getBytes().length>1) {
                sb.append(str.charAt(i));
            }
        }
        //System.out.println(sb.toString());
		}catch(Exception e)
		{
			System.out.print(str);
		}
        return sb.toString();
	}

}
