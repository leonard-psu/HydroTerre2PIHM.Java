### HydroTerre2PIHM (HTCT) 
Convert HydroTerre ETV data bundles to be ready for PIHMgis v3.5 and PIHM v2.2.

### User guide
See HTCT_guide.pdf

### Windows Users
Edit ( 5 steps below ) and use HTCT.bat

Windows users need to edit the following lines (top portion of HTCT.bat file)
1. Specify the input folder location of the unzipped ETV data bundle&nbsp;&nbsp;&nbsp;&nbsp;    set indir=E:\Temp\HydroTerre_ETV_Data
2. Specify the output folder location&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                                   set outdir=%indir%\PIHM
3. Specify the java install location&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                                    set java_location="C:\Program Files\Java\jre1.8.0_201\bin\java"
4. Specify the HTCT.jar location (from this git site)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                   set HTCT_location="E:\PIHM_GIS\git_base\HydroTerre2PIHM.Java\HTCT.jar"
5. Specify the gdal bin install location(gdal_rasterize,gdal_translate)&nbsp; set gdal_location="G:\OSGeo4W64\bin"

### Windows Software Dependencies
1. JAVA JRE needs to be installed. The HTCT.bat has been tested with jre1.8.0_201
2. GDAL tools [gdal_rasterize, gdal_translate, and plugins] are required. Use one of these methods:
- [Option #1] Install and build by source code https://www.gdal.org/
- [Option #2] 2.2 OSGeo4W installer https://trac.osgeo.org/osgeo4w/ [Note: Tested with installing gdal components only]
- [Option #3] Windows Users Only: Unzip the gdal_min.zip into the same directory as the HTCT.bat location.

### Linux and Mac Users
Copy HTCT.sh and HTCT.jar into your unzipped ETV folder.
Use HTCT.sh at command line within your unzipped ETV folder.


### Linux and Mac Software Dependencies
1. JAVA JRE needs to be installed. [UBUNTU] sudo apt install default-jre
2. GDAL [UBUNTU] sudo apt-get install gdal-bin

Contact Lele Shu (lele 'dot' shu 'at' gmail.com) for more help.

Please cite this software as PIHM Analysis Suite developed by Lele Shu, contributions by PIHM and http://www.pihm.psu.edu
