package data;

//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
//import java.io.FilenameFilter;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class ForcingData {
	private Vector<String> forcList = new Vector<String>();;
	String currentDir ; // name of temporary out
	// folder.
	String forcFile ; // output .forc file.
	String forcAvgFile ; // out .forc file with avg

	static String forcType[] = { "Prep", "Temp", "RH", "Wind", "RN", "G", "VP",
			"LAI", "MF", "SS" }; // Forcing data keywords from xml file.
	static String forcType0[] = { "Precip", "Temp", "RH", "Wind", "RN", "G",
			"VP", "LAI", "MF", "SS" };
	int nrows = 0;
	int ncols = 0;
	int startYear;
	int endYear;
	int[] NOForcData;
	boolean IsFirstRun = true;
	LandCover LC;
	LeafAreaIndex LAI;
	MeltFactor MF;

public int[] IsAvg;
	int[] forcIDs;
	double[][] time;
	double[][] data;
	double[][] average;
	Vector<String> siteList = new Vector<String>();;
	Vector<String> forcIDList = new Vector<String>();;
	Vector<String> timeList = new Vector<String>();;
	String inDir;
	String outDir;
public 	int flag=0;
	
	public ForcingData(String s1,String s2) {
		inDir=s1;
		String inFile=inDir.concat("HT_Forcing.xml") ;
 
		outDir=s2;
	 	File theDir = new File(outDir);

	  // if the directory does not exist, create it
	  if (!theDir.exists()) {
	    boolean result = theDir.mkdir();  
	     if(result) {    
	       System.out.println("Directory " + outDir+" created.");  
	     }
	  }
		// folder.
		 forcFile = outDir.concat("/HT.forc"); // output .forc file.
		 forcAvgFile = outDir.concat("/HT_avg.forc"); // out .forc file with avg

		System.out.println("\n\n Current directory= \t" + inDir);
		System.out.println("\n\n Output directory= \t" + outDir);	
		System.out.println("\n Forcing Data file= \t" + inDir+ "HT_Forcing.xml");
		

		readXML(inFile);

		writeOriginalData();
		LC = new LandCover(outDir);
		if (LC.unique.length>0){
			LAI = new LeafAreaIndex(LC.unique,startYear, endYear);
			MF = new MeltFactor(startYear, endYear);
	
			forcIDList.add(forcType[7]);
			forcIDList.add(forcType[8]);
			NOForcData[7] = LC.unique.length;
			NOForcData[8] = 1;
			if (writeForcFile()>0 )
			{		
				writeAvgForcFile();
				System.out.println("END of Forcing Data\n");
				flag=1;
			}
			else
			{
				System.out.println("Error occurs in writing forcing file.\n");
				flag=0;
			}
		}
		else
		{
			System.out.println("Landcover data is missing in folder"+outDir);
		}
		
		
	}

	/*
	 * private ForcingData(String inFileName) {
	 * 
	 * readXML(inFileName); writeForcFile(); }
	 */

	private int writeOriginalData() {
		try {
			FileWriter pFile = new FileWriter(outDir.concat("/OriginalForcingData.csv"));
			
			//Writing the head of matrix file
			pFile.write(""+ "," + ""+ ",");
			for(int i=0;i<forcList.size();i++)
			{
				for(int j=0; j<siteList.size();j++)
				{
					pFile.write(ids(forcList.get(i)) + ",");
					//pFile.write(forcList.get(i) + ",");
					//System.out.println(forcList.get(i) + ",");
				}
				
			}
			pFile.write("\n");
			
			//writing sites' ids for each forcing vairalble

			pFile.write(""+ "," + ""+ ",");
			for(int i=0;i<forcList.size();i++)
			{
				for(int j=0; j<siteList.size();j++)
				{
					pFile.write(forcType[i] + ",");
					//System.out.println(siteList.get(j) + ",");
				}
				
			}
			pFile.write("\n");
			pFile.write(""+ "," + ""+ ",");
			for(int i=0;i<forcList.size();i++)
			{
				for(int j=0; j<siteList.size();j++)
				{
					pFile.write(siteList.get(j) + ",");
					//System.out.println(siteList.get(j) + ",");
				}
				
			}
			pFile.write("\n");
			
			// writing data.
			for (int i = 0; i < data.length; i++) {
				//pFile.write(time[i][0] + "," + time[i][1] + ",");
				pFile.write(timeList.get(i) + ",");
				for (int j = 0; j < data[i].length; j++) {
					if (IsAvg[j]==0)
					{
						pFile.write(data[i][j] + ",");
					}
					
				}
				pFile.write("\n");
			}

			pFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	private void readXML(String inFileName) {

		try {


			File fXmlFile = new File(inFileName);// input xml file.

			System.out.println("\n***********************************");
			System.out.println("Starting to read the XML file: " + inFileName);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			
			String s1 = doc.getElementsByTagName("Start_Date").item(0).getTextContent()  ;// start day;
			String s2= doc.getElementsByTagName("End_Date").item(0).getTextContent()  ;// start day;
			startYear=Integer.parseInt(s1.substring(0, 4));
			endYear=Integer.parseInt(s2.substring(0, 4));
			
			// Start from forcing_record.
			
			NodeList nList = doc.getElementsByTagName("Forcing_Record");
			FileWriter fw = new FileWriter(outDir.concat("dataOriginal.csv"));	//Original csv data
			nrows = nList.getLength();
			time = new double[nrows][2];
			data = new double[nrows][];
			int k = 0;
			for (int i = 0; i < nList.getLength(); i++) {

				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) { // This line is
																// necessary.

					Element eElement = (Element) nNode;

					double startTime = Double.parseDouble(eElement
							.getElementsByTagName("Index_Start").item(0)
							.getTextContent()); // Start Time
					double endTime = Double.parseDouble(eElement
							.getElementsByTagName("Index_End").item(0)
							.getTextContent()); // End Time;
					String timestr = eElement
							.getElementsByTagName("DateTime").item(0)
							.getTextContent(); // Start Time
					timeList.addElement(timestr);;
					time[i][0] = startTime;
					time[i][1] = endTime;
					fw.write(time[i][0] + ",\t" + time[i][1] + ",\t");
					NodeList children = eElement.getChildNodes();

					for (int j = 0; j < children.getLength(); j++) {

						Node child = children.item(j);
						if (child.getNodeType() == Node.ELEMENT_NODE) {
							String huc_name = child.getNodeName();
							if (huc_name.startsWith("HUC")) // any nodes that
															// starts with HUC.
							{
								processHUCData((Element) child, k);
								IsFirstRun = false;
								for (int m = 0; m < data[k].length; m++) {
									fw.write(data[k][m] + ",\t");
								}
								fw.write("\n");
								k++;
							}
						}
					}

				}

			}
			fw.close();
			NOForcData = new int[forcType.length];
			for (int i = 0; i < forcList.size(); i++) {
				NOForcData[ids(forcList.get(i))] = siteList.size();
			}

			System.out.println("Done!\n");
		}		 catch (Exception e) {
			 System.err.println("Hey, Check if the file "+inFileName +" exist in current folder, OK? Otherwise it's gonna say:\n " + e.getMessage());
				e.printStackTrace();
				System.exit(-1) ; 
			}	

	}

	private int[] find(int[] a, int b) {
		int[] c = new int[10000000];
		int[] d;
		int j = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] == b) {
				c[j] = i;
				j++;
			}
		}
		d = new int[j];
		for (int i = 0; i < j; i++) {
			d[i] = c[i];
		}
		return d;
	}

	private void processHUCData(Element hucData, int k) {

		String siteID;
		NodeList children = hucData.getChildNodes();
		int NOTags = children.getLength(); // Number of Tags.
		int NOValue = (NOTags - 3) / 2; // Number of Values.
		double[] intvdata = new double[NOValue];

		if (IsFirstRun) {
			IsAvg = new int[NOValue];
			forcIDs = new int[NOValue];
			data = new double[nrows][intvdata.length];
			ncols = intvdata.length;
		}

		// System.exit(0);
		// System.out.println(mydata.Length);
		int i = 0;
		for (int j = 3; j < NOTags; j++)
		// first line is datetime, so we start from second line.
		{
			if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
				// System.out.println(i+" \t"+j);
				Element child = (Element) children.item(j);
				String data_name = child.getTagName();
				int i_start = data_name.indexOf("_") + 1;
				siteID = data_name.substring(i_start);
				if (IsFirstRun) {
					String forcName = data_name.substring(0, i_start - 1); // Name
																			// of
																			// forcing
																			// data.
					int forcid = ids(forcName);
					forcIDs[i] = forcid;

					if ((!siteList.contains(siteID))
							&& !(siteID.equalsIgnoreCase("Avg"))) {
						siteList.add(siteID);
						// System.out.println(siteID);
					}
					if (!forcList.contains(forcName) && forcid != -1) {
						forcList.add(forcName);
					}
					/*
					 * if(forcList.contains(forcType[1]) &&
					 * !forcList.contains(forcType[6])) //Add vapor pressure
					 * which can be calculated from Temperature. {
					 * forcList.add(forcType[6]); }
					 * if(!forcList.contains(forcType[7]))
					 * forcList.add(forcType[7]); //Add LAI which caluculated
					 * from LAND COVER CODE.
					 */
					if (forcid >= 0) {
						if (siteID.equals("Avg")) {
							IsAvg[i] = 1; // average value
						} else {
							IsAvg[i] = 0; // not average value. site value.
						}
					} else {
						IsAvg[i] = -1; // variable doesn't used in pihm
					}
				}
				if (IsAvg[i] != -1) {
					data[k][i] = Double.parseDouble(hucData
							.getElementsByTagName(data_name).item(0)
							.getTextContent());
				}
				i++;

			}
		}

		// return intvdata;
	}

	private int writeForcFile() {
		int i = 0;
		int x = 0;
		double tmp;
		int[] pos = find(IsAvg, 0);
		try {
			File f = new File(forcFile);
			f.delete();

			FileWriter pFile = new FileWriter(forcFile, true);

			for (i = 0; i < NOForcData.length; i++) { // Head of forc file.
				pFile.write(NOForcData[i] + " \t");// .forc
			}

			for (i = 0; i < forcType.length; i++) {
				switch (i) {
				case 7:
					for (int j = 0; j < LC.unique.length; j++) {
						pFile.append("\n" + forcType[i] + " \t" + (j + 1)
								+ "\t" + LAI.t.length + "\t 0.0002\n");
						// pFile.write("\n"+forcType[i]+" \t"+LC.unique[j]+" \t"+"0.0002\n");
						// //debug
						// System.out.print("\n"+forcType[i]+" \t"+LC.unique[j]+" \t"+"0.0002\n");
						for (int k = 0; k < LAI.t.length / 2; k++) {
							pFile.append(LAI.t[k * 2] + " \t" + LAI.lai[j][k]
									+ "\t" + LAI.t[k * 2 + 1] + "\t"
									+ LAI.lai[j][k] + "\n");
							// System.out.print(LAI.t[k]+" \t"+LAI.lai[j][k]+"\n");
						}
					}// output of LAI

					for (int j = 0; j < LC.unique.length; j++) {
						pFile.append("\nRL \t" + (j + 1) + "\t" + LAI.t.length
								+ "\n");
						for (int k = 0; k < LAI.t.length / 2; k++) {
							pFile.append(LAI.t[k * 2] + " \t" + LAI.rl[j][k]
									+ "\t" + LAI.t[k * 2 + 1] + "\t"
									+ LAI.rl[j][k] + "\n");
							// System.out.print(LAI.t[k]+" \t"+LAI.lai[j][k]+"\n");
						}
					}// output of RL
					break;
				case 8:
					pFile.append("\nMF \t 1 \t " + MF.length + "\n");
					for (int j = 0; j < MF.length; j++) {
						pFile.append(MF.ts[j] + "\t" + MF.mf[j] + "\n");
					}

				default:
					for (int j = 0; j < NOForcData[i]; j++) {
						if (i == 3) {
							pFile.append("\n" + forcType[i] + " \t" + (j + 1)
									+ " \t" + time.length * 2 + "\t" + 10
									+ "\n");
						} else {
							pFile.append("\n" + forcType[i] + " \t" + (j + 1)
									+ " \t" + time.length * 2 + "\n");
							// System.out.print("\n"+forcType[i]+" \t"+(j+1)+" \t"+time.length+"\n");
						}
						for (int k = 0; k < time.length; k++) {
							tmp = data[k][x + j];
							// System.out.print(Double.toString(time[k][0])+" \t"+tmp+" \t"+time[k][1]+" \t"+tmp+"\n");
							pFile.append(Double.toString(time[k][0]) + " \t"
									+ tmp + " \n" + time[k][1] + " \t" + tmp
									+ "\n");

						}
					}
					x = x + NOForcData[i] + 1;
					break;
				}
			}
			pFile.close();
		} catch (Exception e) {
			System.out.println("Error occur on write .forc files");
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	private void writeAvgForcFile() {
		int i = 0;
		double tmp;
		int[] pos = find(IsAvg, 1);
		int x = 0;
		int k = 0;
		try {
			File f = new File(forcAvgFile);
			f.delete();

			FileWriter pAvgFile = new FileWriter(forcAvgFile, true);

			for (i = 0; i < NOForcData.length; i++) { // Head of forc file.
				switch (i) { // .ForcAvg
				case 7:
					pAvgFile.write(NOForcData[i] + " \t");
					break;
				default:
					if (NOForcData[i] > 0) {
						pAvgFile.write("1 \t");
					} else {
						pAvgFile.write("0 \t");
					}
					break;
				}
			}

			for (i = 0; i < forcType.length; i++) {
				switch (i) {
				case 7:
					for (int j = 0; j < LC.unique.length; j++) {
						pAvgFile.append("\n" + forcType[i] + "\t" + (j + 1)
								+ "\t" + LAI.t.length + "\t 0.0002\n");
						// System.out.print("\n"+forcType[i]+" \t"+LC.unique[j]+" \t"+"0.0002\n");
						for (k = 0; k < LAI.t.length / 2; k++) {
							// System.out.print(LAI.t[k]+" \t"+LAI.lai[j][k]+"\n");
							pAvgFile.append(LAI.t[k * 2] + " \t"
									+ LAI.lai[j][k] + "\t" + LAI.t[k * 2 + 1]
									+ "\t" + LAI.lai[j][k] + "\n");
						}
					}// output of LAI

					for (int j = 0; j < LC.unique.length; j++) {
						pAvgFile.append("\nRL \t" + (j + 1) + "\t"
								+ LAI.t.length + "\n");

						for (k = 0; k < LAI.t.length / 2; k++) {
							// System.out.print(LAI.t[k]+" \t"+LAI.rl[j][k]+"\n");
							pAvgFile.append(LAI.t[k * 2] + " \t" + LAI.rl[j][k]
									+ "\t" + LAI.t[k * 2 + 1] + "\t"
									+ LAI.rl[j][k] + "\n");

						}
					}// output of RL
					break;
				case 8:
					pAvgFile.append("\nMF \t 1 \t " + MF.length + "\n");
					for (int j = 0; j < MF.length; j++) {
						pAvgFile.append(MF.ts[j] + "\t" + MF.mf[j] + "\n");
					}
					break;
				default:
					if (NOForcData[i] > 0) { // title of each variable.
						if (i == 3) {
							pAvgFile.append("\n" + forcType[i] + " \t" + (1)
									+ " \t" + time.length * 2 + "\t" + 10
									+ "\n");
						} else {
							pAvgFile.append("\n" + forcType[i] + " \t" + (1)
									+ " \t" + time.length * 2 + "\n");
							// System.out.print("\n"+forcType[i]+" \t"+(1)+" \t"+time.length+"\n");
						}
					}
					for (k = 0; k < time.length && NOForcData[i] > 0; k++) {// repeat
																			// value.
						tmp = data[k][pos[x]];
						pAvgFile.append(Double.toString(time[k][0]) + " \t"
								+ tmp + " \n" + time[k][1] + " \t" + tmp + "\n");
					}
					if (NOForcData[i] > 0) {
						x++;
					}
					break;
				}
			}
			pAvgFile.close();

		} catch (Exception e) {
			System.out.println("Error occur on write .AvgForc files.");
			e.printStackTrace();
		}
	}

	public static int ids(String str) {
		for (int i = 0; i < 10; i++) {
			if (str.equalsIgnoreCase(forcType[i])
					|| str.equalsIgnoreCase(forcType0[i])) {
				return i;
			}
		}
		return -1; // the data is not necessary for current PIHM model.
	}
	// end of Class ForcingData

}