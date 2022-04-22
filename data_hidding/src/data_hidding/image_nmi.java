/*
 * NMI
 */
package data_hidding;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class image_nmi {
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
   	static String[] img_n = {"m1_512.bmp","m2_512.bmp","m3_512.bmp","m4_512.bmp","m5_512.bmp","m6_512.bmp"};//測試黨*/
    public static void main(String args[])throws IOException,FileNotFoundException
    {
    	int num = 0;
    	while(num++ < img_n.length)
    	{
    		Filename = img_n[num-1];
    		System.out.println(Filename+" Start!!");
    		new image_nmi();
    	}
		System.out.println("Finish!!");
    }
    public image_nmi() throws FileNotFoundException, IOException
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
        int x,y,x_f,y_f;
        int[] xy =  new int[4];//fix: x1 x2 y1 y2;
        //
        for(int i=0 ; i < point_x.size(); i++)
        {
            x = point_x.get(i);
            y = point_y.get(i);
            x_f = (int) (3*x+1)/2;
            y_f = (int) (3*y+1)/2;
            image2[x_f][y_f] = image_arr[x][y];
            image2[x_f][y_f+1] = (image_arr[x][y]+image_arr[x][y+1])/2;
            image2[x_f][y_f+2] = image_arr[x][y+1];

            image2[x_f+1][y_f] = (image_arr[x][y]+image_arr[x+1][y])/2;
            image2[x_f+1][y_f+1] = (image_arr[x][y]+image_arr[x][y+1]+image_arr[1+x][y])/3;
            image2[x_f+1][y_f+2] = (image_arr[x][y+1]+image_arr[x+1][y+1])/2;

            image2[x_f+2][y_f] = image_arr[x+1][y];
            image2[x_f+2][y_f+1] = (image_arr[x+1][y]+image_arr[x+1][y+1])/2;
            image2[x_f+2][y_f+2] = image_arr[x+1][y+1];

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
        		FileOutputStream fo = new FileOutputStream("nmi_"+Filename);
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
            	ImageIO.write(tmpIma, "BMP", new File("nmi_8bit_"+Filename));
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
