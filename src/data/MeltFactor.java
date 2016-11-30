package data;

public class MeltFactor {
	private double[] meltFactor;
	private double[] timeSeries;
	public int length;
	
	public double[] mf;
	public double[] ts;
	public  MeltFactor(int startyear, int endyear){
		System.out.println("Working in MeltFactor...");
		initial();
		if (MultiYear(startyear, endyear)>0)
		{
			System.out.println("MeltFactor is Done.");
		}
		
	}
	private int MultiYear(int startyear, int endyear){
		int n=endyear-startyear+1;
		if (n<=0){n=1;}
		ts=new double[n*timeSeries.length];
		mf=new double[timeSeries.length/2*n];
		int ep=0;	//end point;
		int tp=0;	//position in t, 
		int rlp=0;	//position in lai rl.
		int j;
		for (int i=startyear; i<=endyear;i++)
		{
			for ( j=0;j<timeSeries.length;j++)
			{
				ts[j+tp]=timeSeries[j]+ep;
			}
			tp=tp+timeSeries.length;
			if (isleapyear(i)==1) {	
				ep=ep+366;
				ts[tp-1]=ts[tp-1]+1;
				}
			else
			{	
				ep=ep+365;		
			}			
			
			for ( j=0;j<timeSeries.length/2;j++)
			{
					mf[j+rlp]=meltFactor[j];
			}
			rlp=rlp+timeSeries.length/2;
			
		}	
		tp=0;
		for (int i=startyear; i<=endyear;i++)
		{			
			tp=tp+timeSeries.length;
			if (i<endyear){
				ts[tp]=ts[tp-1]+1;
			}
		}
		return 1;
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

	
	private void initial(){
		
		timeSeries=new double[]{0, 31, 32, 61, 62, 93, 94, 124, 125, 156, 157, 187, 188, 219, 220, 251, 252, 282, 283, 314, 315, 345, 346, 365, 378, 409, 410, 439, 440, 471, 472, 502, 503, 534, 535, 565, 566, 597, 598, 629, 630, 660, 661, 692, 693, 723
};
		meltFactor=new double[]{ 0.001308019, 0.001308019, 0.001633298, 0.001633298, 0.002131198, 0.002131198, 0.002632776, 0.002632776, 
				0.003031171, 0.003031171, 0.003197325, 0.003197325, 0.003095839, 0.003095839, 0.00274524, 0.00274524, 0.002260213, 0.002260213, 0.001759481, 0.001759481, 0.001373646, 0.001373646, 0.001202083, 0.001202083, 0.001308019, 0.001308019, 0.001633298, 0.001633298, 0.002131198, 0.002131198, 0.002632776, 0.002632776, 0.003031171, 0.003031171, 0.003197325, 0.003197325, 0.003095839, 0.003095839, 0.00274524, 0.00274524, 0.002260213, 0.002260213, 0.001759481, 0.001759481, 0.001373646, 0.001373646
};
		if(timeSeries.length==meltFactor.length){
			length=timeSeries.length;
		}
		else{
			System.out.println("ERROR in MeltFactor: length of timeSeries and meltfactor mismatch.");
		}
		
	}
}
