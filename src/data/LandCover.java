package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;
import java.io.File;


public class LandCover {
	public String inFile=new String();
	public String outFile=new String();
	public String ascFile=new String();
	public String uF1=new String();
	public String uF0=new String();
	public String inDir=new String();
	private Vector<String> uniqV=new Vector<String>();
	public int[] unique;

	private Vector<String> head=new Vector<String>();
	
	public  LandCover(String s)
	{
		inDir=s;	// In DIR for lanccover; that is same as the output dir gdal results.
		  inFile=inDir.concat("HT_LandCover.asc");	//asc readin;
		  outFile=inDir.concat("HT_LandCover.txt");	//NLCD CODE file.
		  ascFile=inDir.concat("HT_LandCover0.asc");	//ASC FILE WITH 1~N maping the NLCD codes.
		  uF1= inDir.concat("Uniform1.asc");		//Uniform 1;
		  uF0=inDir.concat("Uniform0.asc");			//Uniform 0;
		System.out.println(inFile);
		if (readLC() >0 && exportLC() >0 ){
				System.out.println("LandCover is done.");			
		}
		else
		{ System.out.println("Warning: LandCover is not ready.");	
			System.exit(1);
		}	
	}
	private int readLC()
	{
		System.out.println("Start find the unique value of landcover data.");
		try{
			//FileReader fr=new FileReader(inFile);		
			//File f=new File(outFile);
			//f.delete();		
			FileWriter fw=new FileWriter(outFile);		
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			
			String line;
			int i=1;

			
			while ((line = br.readLine()) != null && i<7) 
			{
				
				String[] arr=line.split("\\s+",-1);
				head.add(arr[0]);
				head.add(arr[1]);
				
				//System.out.println(line);			
				i++;
			}
			while ((line = br.readLine()) != null) 
			{			
				String[] arr=line.split("\\s+",-1);
				for( i=0;i<arr.length;i++)
				{
					if(! uniqV.contains(arr[i]))				
						uniqV.add(arr[i]);				
				}
			}		

			uniqV.remove(0);
			unique=new int[uniqV.size()];
			//System.out.println(uniqV.size()+"\t"+univalue.length);
			for(i=0;i<uniqV.size();i++)
			{
				unique[i]=Integer.parseInt(uniqV.get(i))  ;
			}
			for(i=0;i<unique.length;i++)
			{
				int tmp=unique[i];
				int pos=i;
				for(int j=i+1;j<unique.length;j++)
				{
					if (tmp>unique[j])
					{
						tmp=unique[j];
						pos=j;
					}
				}
				tmp=unique[i];
				unique[i]=unique[pos];
				unique[pos]=tmp;
			}
			for(i=0;i<unique.length;i++)
				fw.write(unique[i]+"\n");
			br.close();
			fw.close();			
		}	
		catch (FileNotFoundException e) {
			
			System.err.println("Hey, Check if the file "+inFile +" exist in current folder, OK? Otherwise it's gonna say:\n " + e.getMessage());	
			return -1;
		}
		catch (Exception e) 	{
			System.out.println("Error occur when handling "+outFile+" files");e.printStackTrace(); 
			return -1;
			}
		
		System.out.println("\n The "+unique.length+" unique LC code in LandCover are:");
		for(int i=0;i<unique.length;i++)
			{
				System.out.println(unique[i]);
			}
		return 1;
	}
	private int exportLC()
	{
		try{	
			FileWriter fw=new FileWriter(ascFile);		
			FileWriter fw1=new FileWriter(uF1);		
			FileWriter fw0=new FileWriter(uF0);
			BufferedReader br = new BufferedReader(new FileReader(inFile));			
			String line;
			int i=0;
			while ((line = br.readLine()) != null && i<6) 
			{								
				fw.write(line+"\n");	
				fw1.write(line+"\n");	
				fw0.write(line+"\n");					
				i++;
			}
			while ((line = br.readLine()) != null) 
			{			
				String[] arr=line.split("\\s+",-1);
				for( i=1;i<arr.length;i++)
				{					
					fw.write(getindex(unique,arr[i])+"\t");	
					fw0.write("0\t");
					fw1.write("1\t");
				}
				fw.write("\n");
				fw0.write("\n");fw1.write("\n");
			}		
			br.close();
			fw.close();	
			fw1.close();
			fw0.close();
		}	
		catch (FileNotFoundException e) {	 
			System.err.println("Hey, Check if the file "+inFile +" exist in current folder, OK? Otherwise it's gonna say:\n " + e.getMessage());	 
			return -1;
			}
		catch (Exception e) 	{ 
			System.out.println("Error occur when handling "+outFile+" files");e.printStackTrace(); 
			return -1;
		}
		return 1;
	}
	private int getindex(int[] a, int x)
	{
		int p=0;
		for (int i=0;i<a.length;i++)
		{
			if(x==a[i]){
				p=i+1;
				break;
			}			
		}
		return p;
	}
	private int getindex(int[] a, String x)
	{
		return getindex(a,Integer.parseInt(x));
	}
}
