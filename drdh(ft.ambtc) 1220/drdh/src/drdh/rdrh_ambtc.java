/*
 * rdrh_ambtc : Embed the data into raw, the data is randomly generated, if you need to embed your own data, you need to modify function.get_st()
 */
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


public class rdrh_ambtc 
{
	//參數
	static int gray = 255; //灰階大小
	static int image_h = 1344; //圖片長度
	static int image_w = 750; //圖片寬度
	static int range = 4; //判斷陣列大小(2-16)
	static int[] add = {1, 1}; //是否要額外增加容量 0:case1 1:case2
   	static String key = "0";//密鑰吧:">   	0:2 1:1 
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
		new rdrh_ambtc();
	}
	public rdrh_ambtc() throws FileNotFoundException, IOException
	{
		//取得座標
		get_coordinate();
		//產生方塊內座標
		generate_block();
		//產生KEY
		key = generate_key(key);
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
    		//取得像素
    		get_pixel();
    		//交換
    		asusna_switch();
    		//PSNR
    		Find_PSNR();
    		//檢查像素
//    		check();
    		//檢查交換證率
//    		check_switch();
    		//圖片輸出
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
	//檢查交換證率
	private void check_switch() {
		int is1 = 0;
		int is2 = 0;
		for(int j = 0 ; j < point_x.size() ; j++)
		{
			int x = point_x.get(j);
			int y = point_y.get(j);
			for(int a = 0 ; a < block_point_x.size(); a++)
			{
				int x1 = x+block_point_x.get(a);
				int y1 = y+block_point_y.get(a);
				if(original_image[x1][y1] == s1_image_switch[x1][y1])	is1++;
				if(original_image[x1][y1] != s2_image_switch[x1][y1])	is2++;
			}
		}
		System.out.println(is1+" "+is2);
		
	}
	//檢查像素
	private void check() {
		for(int j = 0 ; j < point_x.size() ; j++)
		{
			int x = point_x.get(j);
			int y = point_y.get(j);
			for(int a = 0 ; a < block_point_x.size(); a++)
			{
				int x1 = x+block_point_x.get(a);
				int y1 = y+block_point_y.get(a);
				System.out.printf("X:%d Y:%d key:%d Original:%d s1:%d ss1:%d ss2:%d   \n",x1,y1,key.charAt(j)-48,original_image[x1][y1],s2_image[x1][y1],s1_image_switch[x1][y1],s2_image_switch[x1][y1]);
			}
		}
		
	}
	//交換
	private void asusna_switch() 
	{
		for(int i = 0 ; i < point_x.size() ; i++ )
		{
			int k = key.charAt(i)-48;
			int x = point_x.get(i);
			int y = point_y.get(i);
			for(int a = 0 ; a < block_point_x.size(); a++)
			{
//				System.out.println(block_point_y);
				int x1 = x+block_point_x.get(a);
				int y1 = y+block_point_y.get(a);
				s1_image_switch[x1][y1] = k==0 ? s1_image[x1][y1] : s2_image[x1][y1];
				s2_image_switch[x1][y1] = k==0 ? s2_image[x1][y1] : s1_image[x1][y1];
			}
		}
		
	}
	//產生key
	private String generate_key(String key2) {
		String a = key2;
		while (key2.length() < (128*128))
		{
			key2+=a;
		}
		return key2;
	}
	//讀圖片 (限定RAW)
    public void raw(String Filename) throws IOException,FileNotFoundException
    {
    	//
    	FileInputStream fi= new FileInputStream(Filename);
        for(int i=0;i<image_h;i++)				//讀入imagesize_x*imagesize_y灰階影像
        {
        	for(int j=0;j<image_w;j++)
    		{
        		original_image[i][j] = fi.read();
        		s1_image[i][j]=original_image[i][j];
    		}
    	}
		
	}
	//取得座標
	private void get_coordinate() 
	{
		for(int i = 0 ; i < image_h && i+4<image_h ; i+=4)
		{
			for(int j = 0 ; j < image_w && j+4<image_w ; j+=4)
			{
				point_x.add(i);
				point_y.add(j);
			}
		}
		
	}
	//產生方塊內座標
	private void generate_block() 
	{
		for(int i = 0 ; i < 4 ; i+=1)
		{
			for(int j = 0 ; j < 4 ; j+=1)
			{
				block_point_x.add(i);
				block_point_y.add(j);
			}
		}
//		System.out.println(block_point_x);
//		System.out.println(block_point_y);
	}
	//取得像素
	private void get_pixel()
	{
		int total = 0;
		for(int i = 0 ; i < point_x.size() ; i++)
		{
			ArrayList<Integer> pixel_array_case1 =  new ArrayList<Integer>();
			ArrayList<Integer> pixel_array_case2 =  new ArrayList<Integer>();
			int[] arr_min  = {256,256};//1 2
			int[] arr_max  = {256,256};//1 2
			int[][] block_pixel = new int[16][4];//可嵌入數量 像素值 座標X 座標Y
			int avg = 0;
			int x = point_x.get(i);
			int y = point_y.get(i);
			int D = 0; //差值
			int st = 0;//嵌入長度
			int not0 = 0;//計算可嵌入區塊數量
			//取得像素值
			for(int a = 0 ; a < block_point_x.size(); a++)
			{
				int x1 = x+block_point_x.get(a);
				int y1 = y+block_point_y.get(a);
//				System.out.print("X:"+x+" "+block_point_x.get(a)+" ");
//				System.out.println("Y:"+y+" "+block_point_y.get(a));
				block_pixel[a][0] = 0;
				block_pixel[a][1] = original_image[x1][y1];
				block_pixel[a][2] = x1;
				block_pixel[a][3] = y1;
				//找平均值
				avg+=original_image[x1][y1];
			}
			//找平均值
			avg/=block_point_x.size();
			//分類
			for(int j=0 ; j<block_point_x.size() ; j++)
			{
				if(block_pixel[j][1]>avg)	pixel_array_case1.add(block_pixel[j][1]);
				else						pixel_array_case2.add(block_pixel[j][1]);
			}
			Collections.sort(pixel_array_case1);
			Collections.sort(pixel_array_case2);
//			if(num==ed)System.out.println(pixel_array_case1);
//			if(num==ed)System.out.println(pixel_array_case2);
			//找1/4 & 3/4
			if(pixel_array_case1.size()>=range)
			{
				int size = pixel_array_case1.size();
				arr_min[0] = (pixel_array_case1.get((int) Math.round(size*0.25)-1) + pixel_array_case1.get((int) Math.round(size*0.25)))/2;
				arr_max[0] = (pixel_array_case1.get((int) Math.round(size*0.75)-1) + pixel_array_case1.get((int) Math.round(size*0.75)))/2;
			}
			if(pixel_array_case2.size()>=range)
			{
				int size = pixel_array_case2.size();
				arr_min[1] = (pixel_array_case2.get((int) Math.round(size*0.25)-1) + pixel_array_case2.get((int) Math.round(size*0.25)))/2;
				arr_max[1] = (pixel_array_case2.get((int) Math.round(size*0.75)-1) + pixel_array_case2.get((int) Math.round(size*0.75)))/2;
			}
//			if(num==ed)System.out.println(arr_min[0]+" "+arr_max[0]);
//			if(num==ed)System.out.println(arr_min[1]+" "+arr_max[1]);
//			if(num==-1)	System.out.printf("avg:%d min1:%d max1:%d min2:%d max2:%d\n", avg, arr_min[0], arr_max[0], arr_min[1], arr_max[1]);
			//計算可嵌入量
			for(int j = 0 ; j < 16; j++)
			{
				if(block_pixel[j][1]>avg & block_pixel[j][1]>arr_max[0]) 		D = block_pixel[j][1]-arr_max[0];
				else if(block_pixel[j][1]>avg) 									D = block_pixel[j][1]-arr_min[0];
				if(block_pixel[j][1]<=avg & block_pixel[j][1]>arr_max[1]) 		D = block_pixel[j][1]-arr_max[1];
				else if(block_pixel[j][1]<=avg) 								D = block_pixel[j][1]-arr_min[1];
				//
//				st = D > 0 ? (int)(Math.log(D)/Math.log(2))+1 : 0;
				if(D<=0) continue;
				if(block_pixel[j][1]>avg & block_pixel[j][1]<=arr_max[0])		st=(int)(Math.log(D)/Math.log(2))+add[1];
				else if(block_pixel[j][1]<avg & block_pixel[j][1]<=arr_max[1])	st=(int)(Math.log(D)/Math.log(2))+add[1];
				else 															st=(int)(Math.log(D)/Math.log(2))+add[0];
				//
				block_pixel[j][0] = st;
			}
//			fc.Sort2DArrayBasedOnColumnNumber(block_pixel, 1);//整理陣列
			//資料嵌入
			for(int j = 0 ; j < 16 ; j++)
			{
				int x1 = block_pixel[j][2];
				int y1 = block_pixel[j][3];
				if(block_pixel[j][1]> avg) s2_image[x1][y1] = fc.secret(block_pixel[j][0],block_pixel[j][1],block_pixel[j][2],block_pixel[j][3],arr_min[0],arr_max[0]);
				if(block_pixel[j][1]<=avg) s2_image[x1][y1] = fc.secret(block_pixel[j][0],block_pixel[j][1],block_pixel[j][2],block_pixel[j][3],arr_min[1],arr_max[1]);
				if(s2_image[x1][y1]>255 || s2_image[x1][y1] < 0) System.out.println(block_pixel[j][1]+" "+s2_image[x1][y1]);
			}
			num++;

		}
//		System.out.println("ST : "+total);
		total_st+=fc.total_st();

	}
	//PSNR
	public static void  Find_PSNR()
	{
		double SE=0,MSE,psnr; 
		double SE_1=0,MSE_1,psnr_1; 
		double SE_2=0,MSE_2,psnr_2; 
		//
		for(int i=0;i<image_h;i++)
    	{
    		for(int j=0;j<image_w;j++)
    		{
    			SE  +=Math.pow((original_image[i][j]-s2_image[i][j]),2);
    			SE_1+=Math.pow((original_image[i][j]-s1_image_switch[i][j]),2);
    			SE_2+=Math.pow((original_image[i][j]-s2_image_switch[i][j]),2);
				//System.out.println(SE);
    		}
    	}
		MSE=SE/(image_h*image_w);
		psnr=10*(Math.log((gray*gray)/MSE)/Math.log(10));
		MSE_1=SE_1/(image_h*image_w);
		psnr_1=10*(Math.log((gray*gray)/MSE_1)/Math.log(10));
		MSE_2=SE_2/(image_h*image_w);
		psnr_2=10*(Math.log((gray*gray)/MSE_2)/Math.log(10));
		ttt+=((psnr_1+psnr_2)/2);

//		System.out.printf("均方差 MSE = %8.6f, 重建影像品質 PSNR=%8.3f dB\n",MSE,psnr); //未改變的
		System.out.printf("均方差 MSE = %8.6f, 重建影像品質 PSNR=%8.3f dB\n",MSE_1,psnr_1);
		System.out.printf("均方差 MSE = %8.6f, 重建影像品質 PSNR=%8.3f dB\n",MSE_2,psnr_2);
		System.out.printf("平均均方差 MSE = %8.6f, 平均重建影像品質 PSNR=%8.3f dB\n",(MSE_1+MSE_2)/2,(psnr_1+psnr_2)/2);
	}
	//圖片輸出
	private void image_output(String Filename) 
	{
        try 
        {
        	if(Filename.indexOf(".raw")!=-1)
        	{
        		//s1
        		FileOutputStream fo_1 = new FileOutputStream("didh_ambtc_s1_"+Filename);
        		FileOutputStream fo_2 = new FileOutputStream("didh_ambtc_s2_"+Filename);
        		FileOutputStream fo_3 = new FileOutputStream("st.raw");
            	for(int i=0;i<image_h;i++)				//影像輸出
         	   	{
            		for(int j=0;j<image_w;j++)
            		{
            			fo_1.write(s1_image_switch[i][j]);
            			fo_2.write(s2_image_switch[i][j]);
            			fo_3.write(st_arr[i][j]);
            		}
         	   	}
            	fo_1.close();
            	fo_2.close();
            	fo_3.close();
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
