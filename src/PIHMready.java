
/******************************************************************************
* $Id$
*
* Name:     parser.java
* Project:  XML parser.
* Purpose:  to parse the XML forcing data file from Hydroterre to *.forc file which is necessary for PIHM model.
* Author:   Lele shu <e mail : lzs157  at psu dot edu> 
*
******************************************************************************
* Copyright (c) 2013, Lele Shu
*
* Permission is hereby granted, free of charge, to any person obtaining a
* copy of this software and associated documentation files (the "Software"),
* to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense,
* and/or sell copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included
* in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*****************************************************************************/	
//import java.util.*;
import java.io.File;
import java.io.FilenameFilter;

import data.ForcingData;
import data.LeafAreaIndex;
import data.LandCover;
import data.Soil;

public class PIHMready {
	@SuppressWarnings("unused")
	public static void main(String[] args) 
	{
		String inDir,outDir;
        if (args.length>0) {
        	inDir=args[0];		//Input dir
        	outDir=args[1];			//Output dir;
        }
        else{
        	inDir = System.getProperty("user.dir").concat("/");
        	outDir=inDir.concat("PIHM/");
        	inDir = outDir;
        }
        System.out.println(inDir); 
        System.out.println(outDir); 

		ForcingData forcData=new ForcingData(inDir,outDir);
		Soil surgo=new Soil(inDir);
		TiffConvertor tiff=new TiffConvertor();
		UniformFile uniFile=new UniformFile();
		if (forcData.flag>0){
			System.out.println("\n\n\n End of converting processing\n\n");
		}
	}
}
