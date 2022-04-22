package data_hidding;

import java.util.Arrays;
import java.util.Comparator;

public class function {
	static 	int time=0;
	static int total = 0;
	static int total_sec = 0;
	
	public static int second_hiding(int[] pixel, int center, int b)
	{
		int max = 0;
		int level = 0;
		int end = 0;
		for(int i : pixel)
		{
			max = Math.max(max, i - center);
		}//(int)(Math.log(Math.abs(pixel[1]-pixel[2])) / Math.log(2));   (int)(Math.log(max) / Math.log(2))
		max = (int)(Math.log(max) / Math.log(2));
		level = Math.abs(pixel[1]-pixel[2]) <= 0 ? 0 : (int)(Math.log(Math.abs(pixel[1]-pixel[2])) / Math.log(2)); //Á×§Klog(0)
		end = max <= level || max+level==1? center : hd(max, level, center);
		time++;
		return end;
	}
	//
	private static int hd(int max, int level, int center)
	{
		int b;
		for(b = level ; b < max-1 ; b++)
		{}
		total_sec+=b;
//		System.out.println(max+" "+level+" "+b+" "+center);
		//b =  get_b(b, center);
		b = (int) (Math.random()*Math.pow(2, b) + center);
//		b =  (int)(Math.pow(2, b) + center);
		return b;
		
	}
	//¨ú±oB
	 public static int get_b(int b, int pixel)
	 {
		total+=b;
		b = (int) (Math.random()*Math.pow(2, b) + pixel);
		return b; 
	 }
	//2D°}¦C±Æ§Ç
    public static void Sort2DArrayBasedOnColumnNumber (int[][] array, final int columnNumber)
    {
        Arrays.sort(array, new Comparator<int[]>() {
            @Override
            public int compare(int[] first, int[] second) {
               if(first[columnNumber-1] < second[columnNumber-1]) return 1;
               else return -1;
            }
        });
    }
    public static void get_total(int t)
    {
    	System.out.printf("new one:% 6d, one: % 6d, two:% 6d, total:% 8d\n",total,t,total_sec,(total+t+total_sec));
    	total=0;
    	total_sec=0;
    }

}
