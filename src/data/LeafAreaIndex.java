package data;

import java.io.FileNotFoundException;
import java.io.FileWriter;

public class LeafAreaIndex {
	private double time[];	//time series
	private double[][] c;	//LAI value.
	private double[][] leafAreaIndex;	
	private double[][] roughLen;
	private int 	noc;	//Number of classes
	public double		t[];
	public double[][]	lai;
	public double[][]	rl;
	
public LeafAreaIndex(int[] s,int startyear, int endyear)
	{		
		System.out.println("Start process the LAI data.");
		noc=s.length;
		 LAIandRL(s);
		 System.out.println("Startyear="+startyear+"\tEndYear="+endyear);
		 MultiYear(startyear,endyear);
		System.out.println("END of Leaf Area Index and Roughness Lengthpart.\n");		
	}
	private void MultiYear(int startyear, int endyear){
			int n=endyear-startyear+1;
			if (n<=0){n=1;}
			t=new double[n*time.length];
			lai=new double[noc][time.length/2*n];
			rl=new double[noc][time.length/2*n];
			int ep=0;	//end point;
			int tp=0;	//position in t, 
			int rlp=0;	//position in lai rl.
			int j;
			for (int i=startyear; i<=endyear;i++)
			{				
				for ( j=0;j<time.length;j++)
				{
					t[j+tp]=time[j]+ep;
				}
				tp=tp+time.length;
				if (isleapyear(i)==1) {	
					ep=ep+366;
					t[tp-1]=t[tp-1]+1;
					}
				else
				{	
					ep=ep+365;		
				}

				
				for ( j=0;j<time.length/2;j++)
				{
					for (int k=0;k<noc;k++){
						lai[k][j+rlp]=leafAreaIndex[k][j];
						rl[k][j+rlp]=roughLen[k][j];
						}	
				}
				rlp=rlp+time.length/2;
				
			}	
			tp=0;
			for (int i=startyear; i<=endyear;i++)
			{			
				tp=tp+time.length;
				if (i<endyear){
					t[tp]=t[tp-1]+1;
				}
			}
	}
	private int isleapyear(int n){
		if (n % 4>0){
			return 0;
		}
		else{
			if (n%100 >0 ){
				if (n%400 >0) {
					return 0;
				}
				else{
					return 1;
				}
			}
			else
			{return 1;}
		}
			
	}
	private void LAIandRL(int[] s){
		time=new double[]{0,31,32,59,60,90,91,120,121,151,152,181,181,212,212,243,244,273,274,304,305,334,335,365};
		
		defLAI();
		leafAreaIndex=new double[noc][time.length/2];	//initial lai;
		roughLen=new double[noc][time.length/2];	//initial rl;
		for(int i=0;i<noc;i++){
			leafAreaIndex[i]=eqns(s[i]);
		}
		defRL();
		for(int i=0;i<noc;i++){
			roughLen[i]=eqns(s[i]);
		}
		
		/*for(int i=0;i<t.length;i++)
		{
			//System.out.println(s[i]);
			lai[i]=eqns(s[i]);
			String fn="out.tmp/LAI_"+s[i]+".txt";
			try{
				FileWriter fw=new FileWriter(fn);
				for(int j=0;j<lai.length;j++)
				{
					fw.write(t[j]+"\t"+lai[i][j]);
				}
				fw.close();
			}
			catch (FileNotFoundException e) {	    System.err.println("Hey, Check if the file "+fn +" exist in current folder, OK? Otherwise it's gonna say:\n " + e.getMessage());	    }
			catch (Exception e) 	{ System.out.println("Error occur when handling "+fn+" files");e.printStackTrace(); }
		}*/
	}
	
	public double[] eqns(int x)
	{
		double[] tmp=new double[c.length];
		switch(x) {
		case 11:	//Openwater; 
			tmp=sum(product(0.9,c[0]),product(0.1,c[6]));		break;
		case 12:
			tmp=sum(product(0.8,c[0]),product(0.2,c[6]));		break;
		case 42:
			tmp=sum(product(0.6,c[2]),product(0.4,c[10]));		break;
		case 41:
			tmp=sum(product(0.6,c[4]),product(0.4,c[10]));		break;
		case 43:
			tmp=sum(product(0.6,c[5]),product(0.4,c[10]));		break;
		case 40:
			tmp=product(0.65,c[5]);		break;
		case 91:
		case 93:
			tmp=product(0.6,c[5]);			break;
		case 92:
		case 94:
			tmp=product(0.6,c[7]);			break;
		case 50:
			tmp=c[8];			break;
		case 52:
			tmp=product(0.6,c[8]);			break;
		case 90:
			tmp=sum(product(0.6,c[8]),product(0.4,c[12]));			break;
		case 51:
			tmp=product(0.6,c[9]);			break;
		case 95:
			tmp=sum(product(0.6,c[9]),product(0.4,c[12]));			break;
		case 70:
			tmp=product(0.85,c[10]);			break;
		case 71:
		case 72:
		case 73:
		case 74:
		case 80:
			tmp=product(0.9,c[10]);			break;
		case 96:
			tmp=sum(product(0.8,c[10]),product(0.2,c[2]));			break;
		case 97:
			tmp=sum(product(0.8,c[10]),product(0.2,c[4]));			break;
		case 98:
		case 99:
			tmp=product(0.2,c[10]);			break;
		case 81:
		case 82:
			tmp=product(0.6,c[11]);			break;
		case 30:
		case 32:
			tmp=c[12];			break;
		case 31:
			tmp=sum(product(0.92,c[12]),product(0.08,c[9]));			break;
		case 20:
			tmp=product(0.65,c[13]);			break;
		case 21:
			tmp=sum(product(0.9,c[13]),product(0.1,c[10]));		break;
		case 22:
			tmp=sum(product(0.35,c[13]),product(0.65,c[10]));		break;
		case 23:
			tmp=sum(product(0.65,c[13]),product(0.35,c[10]));		break;
		case 24:
			tmp=sum(product(0.9,c[13]),product(0.1,c[10]));		break;
		default:
			tmp=c[0];		break;
			
		}
		return tmp;
	}
	private void defLAI()
	{
		c=new double[][]{ {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				  {8.76,  9.16,  9.827,  10.093,  10.36,  10.76,  10.493,  10.227,  10.093,  9.827,  9.16,  8.76},
				  {5.117,  5.117,  5.117,  5.117,  5.117,  5.117,  5.117,  5.117,  5.117,  5.117,  5.117,  5.117},
				  { 8.76,  9.16,  9.827,  10.093,  10.36,  10.76,  10.493,  10.227,  10.093,  9.827,  9.16,  8.76},
				  { 0.52,  0.52,  0.867,  2.107,  4.507,  6.773,  7.173,  6.507,  5.04,  2.173,  0.867,  0.52},
				  { 4.64,  4.84,  5.347,  6.1,  7.4335,  8.7665,  8.833,  8.367,  7.5665,  6,  5.0135,  4.64},
				  { 5.276088,  5.528588,  6.006132,  6.4425972,  7.2448806,  8.3639474,  8.540044,  8.126544,  7.2533006,  6.3291908,  5.6258086,  5.300508},
				  {2.3331824,  2.4821116,  2.7266101,  3.0330155,  3.8849492,  5.5212224,  6.2395131,  5.7733017,  4.1556703,  3.1274641,  2.6180116,  2.4039116 },
				  {0.580555,  0.6290065,  0.628558,  0.628546,  0.919255,  1.7685454,  2.5506969,  2.5535975,  1.7286418,  0.9703975,  0.726358,  0.6290065 },
				  { 0.3999679,  0.4043968,  0.3138257,  0.2232945,  0.2498679,  0.3300675,  0.4323964,  0.7999234,  1.1668827,  0.7977234,  0.5038257,  0.4043968},
				  { 0.782,  0.893,  1.004,  1.116,  1.782,  3.671,  4.782,  4.227,  2.004,  1.227,  1.004,  0.893},
				  { 0.782,  0.893,  1.004,  1.116,  1.782,  3.671,  4.782,  4.227,  2.004,  1.227,  1.004,  0.893},
				  {0.001,  0.001,  0.001,  0.001,  0.001,  0.001,  0.001,  0.001,  0.001,  0.001,  0.001,  0.001 },
				  { 1.2867143,  1.3945997,  1.5506977,  1.7727263,  2.5190228,  4.1367678,  5.0212291,  4.5795799,  2.8484358,  1.8856229,  1.5178736,  1.3656797},
				 };
		
	}
	private void defRL()
	{
		c=new double[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{1.112, 1.103, 1.088, 1.082, 1.076, 1.068, 1.073, 1.079, 1.082, 1.088, 1.103, 1.112},
				{2.653, 2.653, 2.653, 2.653, 2.653, 2.653, 2.653, 2.653, 2.653, 2.653, 2.653, 2.653},
				{1.112, 1.103, 1.088, 1.082, 1.076, 1.068, 1.073, 1.079, 1.082, 1.088, 1.103, 1.112},
				{0.52, 0.52, 0.666, 0.91, 1.031, 1.044, 1.042, 1.037, 1.036, 0.917, 0.666, 0.52},
				{0.816, 0.8115, 0.877, 0.996, 1.0535, 1.056, 1.0575, 1.058, 1.059, 1.0025, 0.8845, 0.816},
				{0.7602524, 0.7551426, 0.7772204, 0.8250124, 0.846955, 0.8449668, 0.8471342, 0.8496604, 0.8514252, 0.8299022, 0.7857734, 0.7602744},
				{0.35090494, 0.34920916, 0.36891486, 0.40567288, 0.42336056, 0.42338372, 0.42328378, 0.42485112, 0.42631836, 0.40881268, 0.37218526, 0.35096866},
				{0.05641527, 0.05645892, 0.05557872, 0.05430207, 0.05425842, 0.05399002, 0.05361482, 0.0572041, 0.05892068, 0.05821407, 0.05709462, 0.05645892},
				{0.03699235, 0.03699634, 0.03528634, 0.03272533, 0.03272134, 0.03270066, 0.03268178, 0.03907616, 0.04149324, 0.04032533, 0.03823134, 0.03699634},
				{0.0777, 0.0778, 0.0778, 0.0779, 0.0778, 0.0771, 0.0759, 0.0766, 0.0778, 0.0779, 0.0778, 0.0778},
				{0.0777, 0.0778, 0.0778, 0.0779, 0.0778, 0.0771, 0.0759, 0.0766, 0.0778, 0.0779, 0.0778, 0.0778},
				{0.0112, 0.0112, 0.0112, 0.0112, 0.0112, 0.0112, 0.0112, 0.0112, 0.0112, 0.0112, 0.0112, 0.0112},
				{0.1947138, 0.19413424, 0.20831414, 0.23348558, 0.24574614, 0.24605016, 0.24538258, 0.24630454, 0.247455, 0.23527388, 0.20963734, 0.19478494}
				 };
		
	}
	private double[] product(double c, double[] a)
	{
		double[] temp=new double[a.length];
		for(int i=0;i<a.length;i++)
		{
			temp[i]=c*a[i];
		}
		return temp;
	}
	private double[] sum(double[] a, double[] b)
	{
		double[] temp=new double[a.length];
		if(a.length!=b.length)
		{
			System.out.println("Error: Dimensions of the vectors don't match.");
		}
		for(int i=0;i<a.length;i++)
		{
			temp[i]=b[i]+a[i];
		}
		return temp;
	}
}
