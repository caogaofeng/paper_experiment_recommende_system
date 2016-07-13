

class PrintArray {
	public static void myPrint(double[][] a){
		for(int i=0 ; i <a.length ; i++) { 
	        for(int j=0 ; j<a[i].length ; j++) { 
	            System.out.print("a[" + (i+1) + "][" + (j+1) + "]=" + a[i][j]+" ") ; 
	        } 
	        System.out.println();
	    }
	}
	
	public static void myPrint(char[][] a){
		for(int i=0 ; i <a.length ; i++) { 
	        for(int j=0 ; j<a[i].length ; j++) { 
	            System.out.print("a[" + (i+1) + "][" + (j+1) + "]=" + a[i][j]+" ") ; 
	        } 
	        System.out.println();
	    }
	}
	

	public static void myPrint(int[][] a) {
		// TODO Auto-generated method stub
		for(int i=0 ; i <a.length ; i++) { 
	        for(int j=0 ; j<a[i].length ; j++) { 
	            System.out.print("a[" + (i+1) + "][" + (j+1) + "]=" + a[i][j]+" ") ; 
	        } 
	        System.out.println();
	    }
		
	}

}
