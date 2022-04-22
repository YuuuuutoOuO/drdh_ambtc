/*
 *drdh : The original data hiding program proposed by the professor 
 */
package drdh;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class drdh 
{
	static int image_h = 512;
	static int image_w = 512;
	static int test = 0;
	//
	static int[][] original_image = new int[image_h][image_w];
	static int[][] s1_image = new int[image_h][image_w];
	static int[][] s2_image = new int[image_h][image_w];
	static int[][] s1_image_switch = new int[image_h][image_w];
	static int[][] s2_image_switch = new int[image_h][image_w];
   	static ArrayList<Integer> point_x =  new ArrayList<Integer>();//�����y��X
   	static ArrayList<Integer> point_y =  new ArrayList<Integer>();//�����y��Y
   	static ArrayList<Integer> block_point_x =  new ArrayList<Integer>();//�����y��X
   	static ArrayList<Integer> block_point_y =  new ArrayList<Integer>();//�����y��Y
   	static String key = "11001110";
   	//
   	function fc = new function();
   	//
   	static String[] img_n = {"Alan512.raw","Baboon512.raw","Barbara512.raw","Boat512.raw","body512_2.raw","F16_512.raw","flower512.raw","fruits512.raw","GoldHill512.raw","head512.raw",
			"ImageBook512.raw","ImgBook512.raw","Jet(F14)512.raw","Jet(F16)511.raw","Lena512.raw","Magazine512.raw","palyBoat512.raw","pens512.raw","Pepper512.raw","playboat512.raw",
			"Sailboat512.raw","splash512.raw","Tiffany512.raw","Toys512.raw","watch512.raw","Zelda512.raw"};
//   	static String[] img_n = {"Zelda512.raw"};
   	//
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		// TODO Auto-generated method stub
		new drdh();
	}
	public drdh() throws FileNotFoundException, IOException
	{
		//���o�y��
		get_coordinate();
		//���ͤ�����y��
		generate_block();
		//����KEY
		key = generate_key(key);
		for(String i : img_n)
    	{
			//Ū�Ϥ�
        	String Filename= i;
        	System.out.println(i+" Start!!");
            if(Filename.indexOf(".raw") != -1)	
            {
            	raw(Filename);
            }
    		//���o����
    		get_pixel();
    		//�洫
    		asusna_switch();
    		//PSNR
    		Find_PSNR();
    		//�ˬd����
//    		check();
    		//�ˬd�洫�Ҳv
//    		check_switch();
    		//�Ϥ���X
    		image_output(Filename);
    	}
        System.out.println("END !\n");
    }
	//�ˬd�洫�Ҳv
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
	//�ˬd����
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
	//���ͤ�����y��
	private void generate_block() {
		for(int i = 0 ; i < 4 ; i+=1)
		{
			for(int j = 0 ; j < 4 ; j+=1)
			{
				block_point_x.add(i);
				block_point_y.add(j);
			}
		}
//		System.out.println(block_point_x.size());
	}
	//�洫
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
	//����key
	private String generate_key(String key2) {
		String a = key2;
		while (key2.length() < (128*128))
		{
			key2+=a;
		}
		return key2;
	}
	//Ū�Ϥ� (���wRAW)
    public void raw(String Filename) throws IOException,FileNotFoundException
    {
    	//
    	FileInputStream fi= new FileInputStream(Filename);
        for(int i=0;i<image_h;i++)				//Ū�Jimagesize_x*imagesize_y�Ƕ��v��
        {
        	for(int j=0;j<image_w;j++)
    		{
        		original_image[i][j] = fi.read();
        		s1_image[i][j]=original_image[i][j];
    		}
    	}
		
	}
	//���o�y��
	private void get_coordinate() 
	{
		for(int i = 0 ; i < image_h ; i+=4)
		{
			for(int j = 0 ; j < image_w ; j+=4)
			{
				point_x.add(i);
				point_y.add(j);
			}
		}
		
	}
	//���o����
	private void get_pixel()
	{
		int total = 0;
		for(int i = 0 ; i < point_x.size() ; i++)
		{
			ArrayList<Integer> pixel_array =  new ArrayList<Integer>();
			int[][] block_pixel = new int[16][4];//�i�O�J�ƶq ������ �y��X �y��Y
			int min = 256;
			int avg = 0;
			int x = point_x.get(i);
			int y = point_y.get(i);
			int D = 0; //�t��
			int st = 0;//�O�J����
			int not0 = 0;//���h�֥i�O�J������
			//���o������
			for(int a = 0 ; a < block_point_x.size(); a++)
			{
				int x1 = x+block_point_x.get(a);
				int y1 = y+block_point_y.get(a);
				pixel_array.add(original_image[x1][y1]);
				//
				block_pixel[a][0] = 0;
				block_pixel[a][1] = original_image[x1][y1];
				block_pixel[a][2] = x1;
				block_pixel[a][3] = y1;
			}
			//��̤p��&������
			for(int j = 0 ; j < 16 ; j++)
			{
				avg+=block_pixel[j][1];
				min = Math.min(min, block_pixel[j][1]);
			}
//			if(avg == min) System.out.println("SAME!!");
			avg/=pixel_array.size();
			//�p�⦳�h�ֹ����i�O�J
			for(int j = 0 ; j < 16 ; j++)
			{
				if(block_pixel[j][1] > avg) //CASE 1
				{
					D = block_pixel[j][1] - avg;
					st = D > 0 ?(int) (Math.log(D)/Math.log(2)) : 0;
//					System.out.printf("P:%d avg:%d D:%d st:%d\n",pixel_array.get(j),avg,D,st);
					
				}
				else//CASE 2
				{
					D = block_pixel[j][1] - min;
					st = D > 0 ?(int)(Math.log(D)/Math.log(2))+1 : 0;
//					System.out.printf("P:%d min:%d D:%d st:%d\n",pixel_array.get(j),min,D,st);	
			
				}
				if(st > 0) not0++;
				block_pixel[j][0] = st;
			}
			fc.Sort2DArrayBasedOnColumnNumber(block_pixel, 1);//��z�}�C
			for(int j =0 ; j < 16 & test == -1 ; j++)
			{
				System.out.print(block_pixel[j][0]+" ");
				System.out.print(block_pixel[j][1]+" ");
				System.out.print(block_pixel[j][2]+" ");
				System.out.println(block_pixel[j][3]+" ");
			}
					
			//��ƴO�J
			for(int j = 0 ; j < 16 ; j++)
			{
				int x1 = block_pixel[j][2];
				int y1 = block_pixel[j][3];
//				
//				if(j < 16 & block_pixel[j][0] > 0)
				if(j < (int)((not0+1)*0.666))
				{
					total+=block_pixel[j][0];
					s2_image[x1][y1] = fc.secret(block_pixel[j][0],block_pixel[j][1],min,avg);
				}
				else
				{
					s2_image[x1][y1] = block_pixel[j][1];
				}
				if(s2_image[x1][y1] > 255) System.out.println("C8763");
//				if(j >= (int)((not0+1)*0.3334) && block_pixel[j][0] > 0)
//				{
//					total+=block_pixel[j][0];
//					s2_image[x1][y1] = fc.secret(block_pixel[j][0],block_pixel[j][1],min,avg);
//				}
//				else
//				{
//					s2_image[x1][y1] = block_pixel[j][1];
//				}
			}


			test++;

			if (test == -1)
			{
				System.out.println(pixel_array+" "+min+" "+avg);
				test++;
			}
		}
//		System.out.println("ST : "+total);
		fc.total_st();

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
		psnr=10*(Math.log((255*255)/MSE)/Math.log(10));
		MSE_1=SE_1/(image_h*image_w);
		psnr_1=10*(Math.log((255*255)/MSE_1)/Math.log(10));
		MSE_2=SE_2/(image_h*image_w);
		psnr_2=10*(Math.log((255*255)/MSE_2)/Math.log(10));

//		System.out.printf("����t MSE = %8.6f, ���ؼv���~�� PSNR=%8.3f dB\n",MSE,psnr); //�����ܪ�
		System.out.printf("����t MSE = %8.6f, ���ؼv���~�� PSNR=%8.3f dB\n",MSE_1,psnr_1);
		System.out.printf("����t MSE = %8.6f, ���ؼv���~�� PSNR=%8.3f dB\n",MSE_2,psnr_2);
		System.out.printf("��������t MSE = %8.6f, �������ؼv���~�� PSNR=%8.3f dB\n",(MSE_1+MSE_2)/2,(psnr_1+psnr_2)/2);
	}
	//�Ϥ���X
	private void image_output(String Filename) 
	{
        try 
        {
        	if(Filename.indexOf(".raw")!=-1)
        	{
        		//s1
        		FileOutputStream fo_1 = new FileOutputStream("s1_"+Filename);
        		FileOutputStream fo_2 = new FileOutputStream("s2_"+Filename);
            	for(int i=0;i<image_h;i++)				//�v����X
         	   	{
            		for(int j=0;j<image_w;j++)
            		{
            			fo_1.write(s1_image_switch[i][j]);
            			fo_2.write(s2_image_switch[i][j]);
            		}
         	   	}
            	fo_1.close();
            	fo_2.close();
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
