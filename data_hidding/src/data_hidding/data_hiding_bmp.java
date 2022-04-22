package data_hidding;
/*
 * data hiding (768*768) ,support raw bmp png...
 * */
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class data_hiding_bmp
{
        static String Filename;
        BufferedImage image;
        BufferedImage tmpIma;
    	static int n = 3;                           // 影像區塊大小為 n*n
    	static int image_arr[][]; 
    	static int image2[][];
       	static ArrayList<Integer> point_x =  new ArrayList<Integer>();//紀錄座標X
       	static ArrayList<Integer> point_y =  new ArrayList<Integer>();//紀錄座標Y
       	static int imagesize_x;
       	static int imagesize_y;
       	//
       	static function fc = new function();
    	//公式參數
       	static int k = 8; //2^3
       	static int[] block_x = {0,1,1,2}; //3*3-X
       	static int[] block_y = {1,0,2,1}; //3*3-Y
       	static int[] range = {0,0,0,0,1,1,1,1};
       	static boolean[] p = {true,false,false,true,true,false,false,true}; //true:modified false:un modified
       	static int len_b; //需隱藏資料的長度
       	static int total_b;//總共藏入數量
       	
//       	static String[] img_n = {"m1_512.bmp","m2_512.bmp","m3_512.bmp","m4_512.bmp","m5_512.bmp","m6_512.bmp"};//測試黨*/
//       	       	static String[] img_n = {"Alan512.raw","Baboon512.raw","Barbara512.raw","Boat512.raw","body512_2.raw","F16_512.raw","flower512.raw","fruits512.raw","GoldHill512.raw","head512.raw",
//			"ImageBook512.raw","ImgBook512.raw","Jet(F14)512.raw","Jet(F16)511.raw","Lena512.raw","Magazine512.raw","palyBoat512.raw","pens512.raw","Pepper512.raw","playboat512.raw",
//			"Sailboat512.raw","splash512.raw","Tiffany512.raw","Toys512.raw","watch512.raw","Zelda512.raw"};//測試黨*/
       	static String[] img_n = {"Zelda512.raw"};
       	//
        public static void main(String argv[]) throws FileNotFoundException, IOException
        {
                new data_hiding_bmp();
        }
        //
        public data_hiding_bmp() throws FileNotFoundException, IOException
        {
        	for(String i : img_n)
        	{
            	Filename= "nmi_"+i;
            	System.out.println(i+" Start!!");
            	total_b=0;
                if(Filename.indexOf(".raw") != -1)	
                {
                	raw();
                }
                else	
                {
                    LoadFile();
                	ChangPix();
                }
//                image_output();
//                Find_PSNR();
                //-------第二次編嗎

                second();
                image_output();
                Find_PSNR();
                System.out.println("END !\n");
        	}
        }
		//讀圖片 (限定RAW)
        public void raw() throws IOException,FileNotFoundException
        {
        	imagesize_x = 768;
        	imagesize_y = 768;
        	FileInputStream fi= new FileInputStream(Filename);
        	image_arr = new int[imagesize_x][imagesize_y];
        	image2 = new int[imagesize_x][imagesize_y];
        	//
            for(int i=0;i<imagesize_x;i++)				//讀入imagesize_x*imagesize_y灰階影像
            {
            	for(int j=0;j<imagesize_y;j++)
        		{
            		image_arr[i][j] = fi.read();
            		image2[i][j]=image_arr[i][j];
        		}
        	}
        	block_3x3(imagesize_x, imagesize_y);
			
		}
        //讀圖片 (RAW除外)
		public void LoadFile()
        {
                try
                {
                        image=ImageIO.read(new File(Filename));
//                		tmpIma=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);   //24 bit 
                        tmpIma=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_INDEXED);     //8 bit
                        imagesize_x = image.getWidth();
                        imagesize_y = image.getHeight();
                        image_arr = new int[imagesize_x][imagesize_y];
                        image2 = new int[imagesize_x][imagesize_y];
                }
                catch(Exception e)
                {
                        javax.swing.JOptionPane.showMessageDialog(null, "載入圖檔錯誤: "+Filename);
                        image=null;
                }
        }
        //轉灰階
        public void ChangPix()
        {          
                System.out.println(image.getWidth()+" "+image.getHeight());
                for(int i=0;i<image.getWidth();i++)
                {
                        for(int j=0;j<image.getHeight();j++)
                        {
                            Color color=new Color(image.getRGB(i,j));
                            int tmp=(color.getRed()+color.getGreen()+color.getBlue())/3;
                            image_arr[i][j] = tmp;
                            image2[i][j] = tmp;

                        }
                }
                block_3x3(imagesize_x,imagesize_y);
        }
        //求座標
        public void block_3x3(int imagesize_x, int imagesize_y) // 將圖片分成3*3，並儲存座標
    	{  
    		point_x.clear();
    		point_y.clear();
    		for(int i = 0 ; i < imagesize_x-n+1; i+=n)
    		{
    			for(int j = 0 ; j < imagesize_y-n+1; j+=n)
    			{
    				//System.out.println(i+" --- "+j);
    				point_x.add(i);
    				point_y.add(j);
    			}
    		}
    		System.out.println("可用像素:" + point_x.size()*4);
    		len_b = point_x.size()*4;
    		if(len_b <= point_x.size()*4)	bk();
    		else System.out.printf("空間不夠:\"< (%d<%d)\n",point_x.size()*4,len_b);
    		
    	}
        //
    	public void bk() //將需要的座標取出來
    	{ 
    		int[][] hd = new int[4][4];//index 0:可填入位元數 1:最小值 2:需修改像素-x 3:需修改像素-y
    		int bk_x;
    		int bk_y;
    		for(int i = 0 ; i < point_x.size(); i++)
    		{
    			for(int j = 0 ; j < block_x.length; j++)
    			{
    				bk_x = point_x.get(i)+block_x[j]; //point_x.get(i)
    				bk_y = point_y.get(i)+block_y[j]; //point_y.get(i)
    				//hiding(point_x.get(i)+block_x[j], point_y.get(i)+block_y[j]);
        			hd[j][0] = block_x[j]%2==0 ? Math.abs(image_arr[bk_x][bk_y-1]-image_arr[bk_x][bk_y+1]) : Math.abs(image_arr[bk_x-1][bk_y]-image_arr[bk_x+1][bk_y]); 
                    hd[j][0] = Math.log(hd[j][0])<= 0 ? 0 : (int)(Math.log(hd[j][0]) / Math.log(2));//s
                    hd[j][1] = block_x[j]%2==0 ? Math.min(image_arr[bk_x][bk_y-1],image_arr[bk_x][bk_y+1]) : Math.min(image_arr[bk_x-1][bk_y],image_arr[bk_x+1][bk_y]); //min
                    hd[j][2] = bk_x; //x
                    hd[j][3] = bk_y;//y
    			}
				fc.Sort2DArrayBasedOnColumnNumber(hd,1); //排列順序 
				for(int j=0 ; j<4 ; j++)
				{
					if(j < 2 && hd[j][0]>0) //最大的兩個特別處理
					{
						image2[hd[j][2]][hd[j][3]] = fc.get_b(hd[j][0],hd[j][1]); 
					}
					else
					{
						hiding(hd[j][2],hd[j][3]);
					}
				}	
    		}
    		
    	}
    	
    	//
		public void hiding(int x, int y) 
		{ 
			int pixel = image_arr[x][y];
			//System.out.println(pixel+" " + image2[x][y]+" "+x+" "+y);
			int[] s = {Integer.MAX_VALUE,Integer.MAX_VALUE}; //index 0:P值 1:i-p(目前最小值,abs)
			ArrayList<Integer> len = new ArrayList<Integer>(); //儲存可能的解
//			System.out.println(x+" "+y+" >>>" + pixel);
			int b = Math.random() >=0.5 ? 1 : 0;
			for(int i = pixel-8 ; i < pixel+8 ; i++)
			{
				if(i >255 || i < 0) continue;;//避免超出範圍
				if(range[i%k]==b && p[i%k]!=true && Math.abs(i-pixel) < s[1]) //range必須=b ,  p[i%k]=true代表為M 需更改為UM的值 , i-p求最小值
				{
					len.add(i);
					s[0]=i;
					s[1]=Math.abs(i-pixel);
				}
			}
			//System.out.printf("Pixel X:% 3d,  Pixel Y:% 3d,  Pixel:% 3d>% 3d,  D:% 3d, distance:% 3d,  B:% 3d\n",x,y,image_arr[x][y],s[0],s[0]%k,s[1],b);
			image2[x][y] = s[0]; //隱藏像素
			//System.out.println(image[x][y] + " " + image2[x][y]);
            total_b++;
		}
		//第二次編碼
		private void second()
		{
			int[] pixel_xy = new int[4];
			int center;
			for(int i = 0 ; i < point_x.size(); i++)
    		{
				for(int j=0 ; j<pixel_xy.length ; j++)
				{
					pixel_xy[j] = image2[point_x.get(i)+block_x[j]] [point_y.get(i)+block_y[j]];
//					if(i < 10000)	System.out.println((point_x.get(i)+block_x[j]) + "."+(point_y.get(i)+block_y[j]));
				}
				center = image2[point_x.get(i)+1] [point_y.get(i)+1];
				if(i == point_x.size()-1) //最後一筆
					image2[point_x.get(i)+1][point_y.get(i)+1] = fc.second_hiding(pixel_xy,center,1);
				else
					image2[point_x.get(i)+1][point_y.get(i)+1] = fc.second_hiding(pixel_xy,center,0);


    		}
			
		}
		//求mse & psnr
		public static void  Find_PSNR()
		{
			int i,j;
			int aaa = 0;
			double SE=0,MSE,psnr; 
			for(i=0;i<imagesize_x;i++)
	    	{
	    		for(j=0;j<imagesize_y;j++)
	    		{
					SE+=Math.pow((image_arr[i][j]-image2[i][j]),2);
					if(Math.abs(image_arr[i][j]-image2[i][j]) > 0)
						aaa++;
					//System.out.println(SE);
	    		}
	    	}
			MSE=SE/(imagesize_x*imagesize_y);
	    	psnr=10*(Math.log((255*255)/MSE)/Math.log(10));

			System.out.printf("均方差 MSE = %8.6f, 重建影像品質 PSNR=%8.3f dB\n",MSE,psnr);
			fc.get_total(total_b);
		}
		//輸出圖片
		private void image_output() 
		{
            try 
            {
            	if(Filename.indexOf(".raw")!=-1)
            	{
            		FileOutputStream fo = new FileOutputStream("dh_"+Filename);
                	for(int i=0;i<imagesize_x;i++)				//影像輸出
             	   	{
                		for(int j=0;j<imagesize_y;j++)
                		{
                			fo.write(image2[i][j]);
                		}
             	   	}
                	fo.close();
            	}
            	else
            	{
            		for(int i=0;i<imagesize_x;i++)				//影像輸出
             	   	{
                		for(int j=0;j<imagesize_y;j++)
                		{
                            Color tmpcol=new Color(image2[i][j],image2[i][j],image2[i][j]);
                            try
                            {
                                tmpIma.setRGB(i,j,tmpcol.getRGB());
                            }
                            catch(Exception e)
                            {
                            }
                		}
             	   	}
            	    ImageIO.write(tmpIma,"bmp",new java.io.File("dh_"+Filename));
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