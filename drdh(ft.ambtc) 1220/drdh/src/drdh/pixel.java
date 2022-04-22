package drdh;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.imageio.ImageIO;


public class pixel 
{
	//參數
	static int gray = 255; //灰階大小
	static int image_h = 512; //圖片長度
	static int image_w = 512; //圖片寬度
	static int range = 4; //判斷陣列大小(2-16)
	static int[] add = {1, 1}; //是否要額外增加容量 0:case1 1:case2
   	static String key = "11001110";//密鑰吧:">   	0:2 1:1 
	//
	static int[][] original_image = new int[image_h][image_w];
	static int[][] s1_image = new int[image_h][image_w];
	static int[][] s2_image = new int[image_h][image_w];
	static int[][] s1_image_switch = new int[image_h][image_w];
	static int[][] s2_image_switch = new int[image_h][image_w];
	public static int[][] st_arr = new int[image_h][image_w];
   	static ArrayList<Integer> point_x =  new ArrayList<Integer>();//紀錄座標X
   	static ArrayList<Integer> point_y =  new ArrayList<Integer>();//紀錄座標Y
   	static ArrayList<Integer> block_point_x =  new ArrayList<Integer>();//紀錄座標X
   	static ArrayList<Integer> block_point_y =  new ArrayList<Integer>();//紀錄座標Y
   	static long[] timeMilli = new long[2];//時間:">
   	static int total_st = 0;
   	//
   	static double ttt = 0;
   	public static int num = 0;
   	//
   	function fc = new function();
   	static int ed = function.ed;
   	Date date;
   	//
//   	static String[] img_n = {"Alan512.raw","Baboon512.raw","Barbara512.raw","Boat512.raw","body512_2.raw","F16_512.raw","flower512.raw","fruits512.raw","GoldHill512.raw","head512.raw",
//			"ImageBook512.raw","ImgBook512.raw","Jet(F14)512.raw","Lena512.raw","Magazine512.raw","palyBoat512.raw","pens512.raw","Pepper512.raw","playboat512.raw",
//			"Sailboat512.raw","splash512.raw","Tiffany512.raw","Toys512.raw","watch512.raw","Zelda512.raw"};
   	static String[] img_n = {"rick.raw"};
   	//
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		// TODO Auto-generated method stub
		new pixel();
	}
	public pixel() throws FileNotFoundException, IOException
	{
		for(String i : img_n)
    	{
			//紀錄開始時間
			date = new Date();
			timeMilli[0] = date.getTime();
			//讀圖片
        	String Filename= i;
        	System.out.println(i+" Start!!");
        	init_arr();
            if(Filename.indexOf(".raw") != -1)	
            {
            	raw(Filename);
            }
    		//PSNR
    		Find_PSNR();
    		image_output(Filename);
    		//紀錄結束時間
    		date = new Date();
    		timeMilli[1] = date.getTime();
    		System.out.println("執行時間 : "+(timeMilli[1]-timeMilli[0])*0.001+"s");
    		System.out.println("---------------------");
    		fc.text(i);
    	}
//		System.out.println("參數總和"+function.total_num);
		System.out.println("平均PSNR(db):"+ttt/img_n.length+"   平均藏入量(bit):"+total_st/img_n.length);
        System.out.println("END !\n");
    }
	private void init_arr() {
		for(int i = 0 ; i < image_h ; i++) {
			for(int j = 0 ; j < image_w ; j++) {
				st_arr[i][j]=255;
			}
		}
	}
	//讀圖片 (限定RAW)
    public void raw(String Filename) throws IOException,FileNotFoundException
    {
    	int max=0;
    	//
    	FileInputStream fi= new FileInputStream(Filename);
        for(int i=0;i<image_h;i++)				//讀入imagesize_x*imagesize_y灰階影像
        {
        	for(int j=0;j<image_w;j++)
    		{
        		original_image[i][j] = fi.read();
        		s1_image[i][j]=original_image[i][j] > 32 ? original_image[i][j]/6 : original_image[i][j]/6;
        		max=Math.max(max,s1_image[i][j]);
    		}
    	}
        System.out.println(max);
		
	}
	//PSNR
	public static void  Find_PSNR()
	{
		double SE=0,MSE,psnr; 
		//
		for(int i=0;i<image_h;i++)
    	{
    		for(int j=0;j<image_w;j++)
    		{
    			SE  +=Math.pow((original_image[i][j]-s1_image[i][j]),2);
				//System.out.println(SE);
    		}
    	}
		MSE=SE/(image_h*image_w);
		psnr=10*(Math.log((gray*gray)/MSE)/Math.log(10));

		System.out.printf("均方差 MSE = %8.6f, 重建影像品質 PSNR=%8.3f dB\n",MSE,psnr); //未改變的
	}
	//圖片輸出
	private void image_output(String Filename) 
	{
        try 
        {
        	if(Filename.indexOf(".raw")!=-1)
        	{
        		//s1
        		FileOutputStream fo_1 = new FileOutputStream("C8763_"+Filename);
            	for(int i=0;i<image_h;i++)				//影像輸出
         	   	{
            		for(int j=0;j<image_w;j++)
            		{
            			fo_1.write(s1_image[i][j]);
            		}
         	   	}
            	fo_1.close();
            	//s2
        	}
		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
        	System.out.println(e);
			e.printStackTrace();
		}
		
	}
}
