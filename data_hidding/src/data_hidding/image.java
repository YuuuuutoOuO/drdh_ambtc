/*
 * ENMI
 */

package data_hidding;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class image {
	static String Filename;
	static BufferedImage image;
    static BufferedImage tmpIma;
	static int imagesize_x; //image x
	static int imagesize_y; //image y
	static int imagesize_x_fix; //image x fixed
	static int imagesize_y_fix; //image y fixed
	static int[][] image_arr = new int[imagesize_x][imagesize_y]; //image array
	static int[][] image2; //image * (3*3) / (2*2)
   	static ArrayList<Integer> point_x =  new ArrayList<Integer>();//紀錄座標X
   	static ArrayList<Integer> point_y =  new ArrayList<Integer>();//紀錄座標Y
	//
//   	static String[] img_n = {"Alan512.raw","Baboon512.raw","Barbara512.raw","Boat512.raw","body512_2.raw","F16_512.raw","flower512.raw","fruits512.raw","GoldHill512.raw","head512.raw",
//   			"ImageBook512.raw","ImgBook512.raw","Jet(F14)512.raw","Jet(F16)511.raw","Lena512.raw","Magazine512.raw","palyBoat512.raw","pens512.raw","Pepper512.raw","playboat512.raw",
//   			"Sailboat512.raw","splash512.raw","Tiffany512.raw","Toys512.raw","watch512.raw","Zelda512.raw"};//測試黨*/
   	static String[] img_n = {"m1_512.bmp","m2_512.bmp","m3_512.bmp","m4_512.bmp","m5_512.bmp","m6_512.bmp"};//測試黨*/
   	
    public static void main(String args[])throws IOException,FileNotFoundException
    {
    	int num = 0;
    	while(num++ < img_n.length)
    	{
    		Filename = img_n[num-1];
    		System.out.println(Filename+" Start!!");
    		new image();
    	}
		System.out.println("Finish!!");
    }
    public image() throws FileNotFoundException, IOException
    {
    	if(Filename.indexOf(".raw") != -1)
    	{
    		image_raw();
    	}
    	else
    	{
    		LoadFile();
    		ChangPix();
    	}
    	image_x_y();
        image_fix();
        image_output();
    }
	//讀圖片 (僅RAW)
    public static void image_raw() throws IOException {
		// TODO Auto-generated method stub
    	imagesize_x = 512;
    	imagesize_y = 512;
    	FileInputStream fi= new FileInputStream(Filename);
    	image_arr = new int[imagesize_x][imagesize_y];

    	//
        for(int i=0;i<imagesize_x;i++)				//讀入imagesize_x*imagesize_y灰階影像
        {
        	for(int j=0;j<imagesize_y;j++)
    		{
        		image_arr[i][j] = fi.read();
    		}
    	}
	}
    //讀圖片 (RAW除外)
	public void LoadFile()
    {
            try
            {
                    image=ImageIO.read(new File(Filename));
                    tmpIma=new BufferedImage(image.getWidth()*3/2,image.getHeight()*3/2,BufferedImage.TYPE_BYTE_INDEXED);
                    image_arr = new int[image.getWidth()][image.getHeight()];
                    imagesize_x = image.getWidth();
                    imagesize_y = image.getHeight();
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
                    }
            }
    }
	//重組圖片
    public static void image_fix() {
    	int x1,x2,y1,y2;
		int[] xy =  new int[4];//fix: x1 x2 y1 y2;
		//
		for(int i=0 ; i < point_x.size(); i++)
		{
			x1 = point_x.get(i);//4個座標點
			x2 = x1+1;
			y1 = point_y.get(i);
			y2 = y1+1;
//			System.out.println(x1+" "+x2+" "+y1+" "+y2);
			xy[1] = (3*x2+1)/2;
			xy[3] = (3*y2+1)/2;
			xy[0] = xy[1]-2;
			xy[2] = xy[3]-2;
			//
			image2[xy[0]][xy[2]] = image_arr[x1][y1]; // 0 0
			//

			if(y1+1 < imagesize_y)
			{
				image2[xy[0]][xy[2]+1] = ((image_arr[x1][y1]+image_arr[x1][y1+1])/2); // 0 1
				image2[xy[0]][xy[3]] = image_arr[x1][y1+1]; // 0 2
			}
			if(x1+1 < imagesize_x)
			{
				image2[xy[0]+1][xy[2]] = (image_arr[x1][y1]+image_arr[x1+1][y1])/2; // 1 0
				image2[xy[1]][xy[2]] = image_arr[x1+1][y1]; // 2 0
			}
			if(y1+1 < imagesize_y && x1+1 < imagesize_x)
			{
				image2[xy[1]-1][xy[3]-1] = (image_arr[x1][y1]+image_arr[x1+1][y1]+image_arr[x1][y1+1]+image_arr[x1+1][y1+1])/4; // 1 1
				image2[xy[1]-1][xy[3]] = (image_arr[x1][y1+1]+image_arr[x1+1][y1+1])/2; // 1 2
				image2[xy[1]][xy[3]-1] = (image_arr[x1+1][y1]+image_arr[x1+1][y1+1])/2; // 2 1
				image2[xy[1]][xy[3]] = image_arr[x1+1][y1+1]; // 2 2
			}

		}
		
	}
	//紀錄X Y
    public static void image_x_y() {
        for(int i=0;i<imagesize_x;i+=2)				//讀入imagesize_x*imagesize_y灰階影像
        {
        	for(int j=0;j<imagesize_y;j+=2)
    		{
        		point_x.add(i);
        		point_y.add(j);
    		}
    	}
        //設定放大大小
        imagesize_x_fix = (imagesize_x*3/2);
        imagesize_y_fix = (imagesize_y*3/2);
        image2 = new int[imagesize_x_fix][imagesize_y_fix];
		System.out.printf("預測大小: % 4d * % 4d\n",imagesize_x_fix,imagesize_y_fix);
	}
    //輸出圖片
	private static void image_output() //輸出圖片
	{
		Color tmpcol;
        try 
        {
        	if(Filename.indexOf(".raw")!=-1)
        	{
        		FileOutputStream fo = new FileOutputStream("enmi_"+Filename);
            	for(int i=0;i<imagesize_x_fix;i++)				//影像輸出
         	   	{
            		for(int j=0;j<imagesize_y_fix;j++)
            		{
            			fo.write(image2[i][j]);
            		}
         	   	}
            	fo.close();
        	}
        	else
        	{
            	for(int i=0;i<imagesize_x_fix;i++)				//影像輸出
         	   	{
            		for(int j=0;j<imagesize_y_fix;j++)
            		{
            			tmpcol=new Color(image2[i][j],image2[i][j],image2[i][j]);
            			tmpIma.setRGB(i,j,tmpcol.getRGB());
            			
            		}
         	   	}
            	ImageIO.write(tmpIma, "BMP", new File("enmi_8bit_"+Filename));
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
