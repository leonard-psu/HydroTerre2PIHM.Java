### HydroTerre2PIHM (HTCT) 
Convert HydroTerre ETV data bundles to be ready for PIHMgis v3.5 and PIHM v2.2.

### User guide
See HTCT_guide.pdf

### Windows Users
Edit and use HTCT.bat

Windows users need to edit the following lines (top portion of HTCT.bat file)
1. Specify the input folder location of the unzipped ETV data bundle    set indir=E:\Temp\HydroTerre_ETV_Data
2. Specify the output folder location                                   set outdir=%indir%\PIHM
3. Specify the java install location                                    set java_location="C:\Program Files\Java\jre1.8.0_201\bin\java"
4. Specify the HTCT.jar location (from this git site)                   set HTCT_location="E:\PIHM_GIS\git_base\HydroTerre2PIHM.Java\HTCT.jar"

### Windows Software Dependencies
1. JAVA JRE needs to be installed. The HTCT.bat has been tested with jre1.8.0_201
2. GDAL tools [gdal_rasterize, gdal_translate, and plugins] are required. Use one of these methods:
- [Option #1] Install and build by source code https://www.gdal.org/
- [Option #2] 2.2 OSGeo4W installer https://trac.osgeo.org/osgeo4w/ [Note: Tested with installing gdal components only]

### Linux and Mac Users
Use HTCT.sh

### Linux and Mac Software Dependencies
1. JAVA JRE needs to be installed.
2. GDAL

Contact Lele Shu (lele 'dot' shu 'at' gmail.com) for more help.

Please cite this software as PIHM Analysis Suite developed by Lele Shu, contributions by PIHM and http://www.pihm.psu.edu
