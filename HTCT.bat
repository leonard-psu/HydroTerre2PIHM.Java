@echo off 
REM ====================================================================================================================================================
set indir=E:\Temp\HydroTerre_ETV_Data
set outdir=%indir%\PIHM
set java_location="C:\Program Files\Java\jre1.8.0_201\bin\java"
set HTCT_location="E:\PIHM_GIS\git_base\HydroTerre2PIHM.Java\HTCT.jar"
set gdal_location="G:\OSGeo4W64\bin"

REM ====================================================================================================================================================

set message=Starting HTCT tool
echo %message%
echo INDIR=%indir%
echo OUTDIR=%outdir%

set nodatavalue=255
set ext=asc
set forcmap=HT_ForcingIDs.shp
set forctif=HT_ForcingIDs.tif
set forcasc=HT_ForcingIDs.asc
set gsgmap=HT_GGSURGO.shp
set gsgtif=HT_GGSURGO.tif
set gsgasc=HT_GGSURGO.asc

REM ====================================================================================================================================================
mkdir %outdir%

echo Rasterizing Forcing map: %forcmap%

%gdal_location%\gdal_rasterize -a Force_ID  -a_nodata %nodatavalue% -ot Int32 -l HT_ForcingIDs -tr 30 30 %indir%\%forcmap% %indir%\%forctif%
%gdal_location%\gdal_translate -of AAIGrid %indir%\%forctif% %outdir%\%forcasc%
%gdal_location%\gdal_rasterize -a MUKEY -a_nodata $nodatavalue -ot Int32 -l HT_GGSURGO -tr 30 30 %indir%\%gsgmap% %indir%\%gsgtif%
%gdal_location%\gdal_translate -of AAIGrid %indir%\%gsgtif% %outdir%\%gsgasc%

REM ====================================================================================================================================================
REM Need to use ! for variables not % Classic problem
setlocal enableDelayedExpansion
FOR %%i IN (%indir%\*.tif) DO (
 set name=%%i
 echo !name!
 set str=!name:%indir%\=!
 set str=!str:.tif=.asc!
 set outputfname=!outdir!\!str!
 echo !str!
 echo !outputfname!
 %gdal_location%\gdal_translate -of AAIGrid %%i !outputfname!
 )

REM ====================================================================================================================================================

%java_location% -jar -Xmx8048m -Xms8012m %HTCT_location% %indir%\ %outdir%\
