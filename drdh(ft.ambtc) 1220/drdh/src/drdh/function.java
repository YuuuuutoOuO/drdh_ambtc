/*
 * function : This includes functions for data embedding and data decoding
 */
package drdh;
import java.util.Arrays;
import java.util.Comparator;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

public class function {
	static int total_st = 0;
	static int num=0;
	static String txt="";
	public static int total_num=0;
	public static int ed = -1;
    public static int get_st(int t)
    {
    	int s = (int) (Math.random()*Math.pow(2, t));
//    	int s = (int)Math.pow(2, t)-1;
    	total_num+=s;
    	txt +=String.valueOf(s);
    	return s;
    }
    //
	//2D�}�C�Ƨ�
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
    
    //�O�J�ƾ�
    public static int secret (int st, int pixel,int x,int y, int min, int max)
    {
    	int pixel_out;
    	//�ˬd�O�_�i�O�J
//    	if(st <= 0  | (max + Math.pow(2,st) > 255) | (min + Math.pow(2,st) > 255)) return pixel; 
    	if(st <= 0) return pixel; 
    	if(pixel >  max & (max+(int)Math.pow(2, st)-1) > 255) return pixel;
    	if(pixel <= max & (min+(int)Math.pow(2, st)-1) > 255) return pixel;
//    	if((min+(int)Math.pow(2, st)-1) >= max) return pixel;//�קK�L�k�P�_
    	
    	//�O�J�o:">
    	pixel_out = pixel > max ? max+get_st(st) : min+get_st(st);
    	rdrh_ambtc.st_arr[x][y] = pixel > max ?pixel_out-max:pixel_out-min;
//    	if(pixel > max)
//    		txt+=String.valueOf(pixel_out-max);
//    	if(pixel <= max)
//    		txt+=String.valueOf(pixel_out-min);
    	if(rdrh_ambtc.num==ed & pixel > max) 
    		System.out.printf("st:%d p:%d min:%d max:%d out:%d en:%d\n",st,pixel,min,max,pixel_out,(pixel_out-max)); 
    	if(rdrh_ambtc.num==ed & pixel <= max) 
    		System.out.printf("st:%d p:%d min:%d max:%d out:%d en:%d\n",st,pixel,min,max,pixel_out,(pixel_out-min)); 

		if(pixel_out <= 255 & pixel_out >= 0)
		{
			//
			String a = Integer.toBinaryString(pixel_out-pixel);
			total_st+=st;
	        return pixel_out;
		}
		else
		{
			System.out.println("???????????????????");
			return pixel;
		}
    }
    
    //���o�`�äJ�q
    public static int total_st ()
    {
    	int a = total_st;
    	System.out.printf("ST = %d , bpp = %8.3f dB\n",total_st,(double)total_st/(512*512));
    	total_st=0;
    	return a;
    }
    
    //�ѽX
    public static int encoding (int st, int pixel, int number)
    {
    	int en = 0;
    	//
    	en = pixel - number;
    	
    	//�P�_�O�_���O�J
    	if(en < 0 | (number+(int)Math.pow(2, st)-1) > 255 | st==0) return 0;
    	if(rdrh_ambtc_decoding.num==ed) System.out.printf("st:%d P:%d m:%d en:%d \n",st,pixel,number,en); 
    	txt+=String.valueOf(en);
    	//
    	total_num+=en;
//    	System.out.println(en+" "+Integer.toBinaryString(en));
    	return Integer.toBinaryString(en).length();
    }
    //
    public void  text(String name)
    {
    	try 
    	{ // �����ɮ׫إߩ�Ū�����ѡA��catch�������~�æC�L�A�]�i�Hthrow
	    	File writename = new File(name+".txt"); // �۹���|�A�p�G�S���h�n�إߤ@�ӷs��output�Ctxt�ɮ�
	    	writename.createNewFile(); // �إ߷s�ɮ�
	    	BufferedWriter out = new BufferedWriter(new FileWriter(writename));
	    	out.write(txt); // \r\n�Y������
	    	out.flush(); // ��֨��Ϥ��e���J�ɮ�
	    	out.close(); // �̫�O�o�����ɮ�
	    	txt="";
    	} 
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    	}
    }

}
