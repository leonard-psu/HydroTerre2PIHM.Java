package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;
import java.io.File;


public class Soil {
	private String inFile=new String();
	public String outFile=new String();
	public int[] unique;
	private String cDir;
	public  Soil(String s)
	{
		cDir=s;
		  inFile=cDir.concat("/HT_Soil_Surgo.txt");
		  outFile=cDir.concat("/Soil.txt");
		readLC();
		//exportTxt();
		
		System.out.println("Soil Table is done.");
	}
	private void readLC()
	{
		try{
			//FileReader fr=new FileReader(inFile);		
			//File f=new File(outFile);
			//f.delete();		
			FileWriter fw=new FileWriter(outFile);		
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			
			String line;
			int i=1;
			

			while ((line = br.readLine()) != null) 
			{			
				if( i==1)
				{
					fw.write(line);
					fw.write("\n");
				}
				
				if(i>2){
					String[] arr=line.split("\\s+",-1);
					fw.write(i-2+"\t");
					for(int j=1;j<arr.length;j++)
						fw.write(arr[j]+"\t");
					fw.write("\n");
				}
				;
				i++;
			}		
			br.close();
			fw.close();			
		}	
		catch (FileNotFoundException e) {	    System.err.println("Hey, Check if the file "+inFile +" exist in current folder, OK? Otherwise it's gonna say:\n " + e.getMessage());	return;    }
		catch (Exception e) 	{ System.out.println("Error occur when handling "+outFile+" files");e.printStackTrace(); return ;}
	
	}
}
