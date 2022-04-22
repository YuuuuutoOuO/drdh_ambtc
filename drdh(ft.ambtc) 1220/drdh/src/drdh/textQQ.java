/*
 * textQQ : Test whether the embedded data is consistent with the retrieved data
*/
package drdh;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files

public class textQQ 
{
	static String data="";
	static String data1="";
   	static String[] img_n = {"Alan512.raw","Baboon512.raw","Barbara512.raw","Boat512.raw","body512_2.raw","F16_512.raw","flower512.raw","fruits512.raw","GoldHill512.raw","head512.raw",
			"ImageBook512.raw","ImgBook512.raw","Jet(F14)512.raw","Lena512.raw","Magazine512.raw","palyBoat512.raw","pens512.raw","Pepper512.raw","playboat512.raw",
			"Sailboat512.raw","splash512.raw","Tiffany512.raw","Toys512.raw","watch512.raw","Zelda512.raw"};
//   	static String[] img_n = {"Alan512.raw"};
	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		for(String n : img_n)
		{
			System.out.println(n+" Start!");
			try {
			      File myObj = new File(n+".txt");
			      File myObj1 = new File("out"+n+".txt");
			      Scanner myReader = new Scanner(myObj);
			      Scanner myReader1 = new Scanner(myObj1);
			      while (myReader.hasNextLine()) {
			        data = myReader.nextLine();
			      }
			      while (myReader1.hasNextLine()) {
				        data1 = myReader1.nextLine();
				      }
			      myReader.close();
			    } catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			System.out.printf("coding : %d, endocoding : %d\n",data.length(),data1.length());
			for(int i= 0 ; i < data.length() ; i++)
			{
				if(data.charAt(i) != data1.charAt(i)) System.out.println((data.charAt(i)-48)+" "+(data1.charAt(i)-48));
			}
		}
		System.out.println("END!");
	}
}
