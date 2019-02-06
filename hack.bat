@echo off 

set message=Hello World
echo %message%

set indir=E:\Temp\HydroTerre_ETV_Data
echo %indir%

set outdir=%indir%\PIHM
echo %outdir%

mkdir %outdir%

set nodatavalue=255
set forcmap=HT_ForcingIDs.shp
set forctif=HT_ForcingIDs.tif
set forcasc=HT_ForcingIDs.asc

echo Rasterizing Forcing map: %forcmap%

gdal_rasterize -a Force_ID  -a_nodata %nodatavalue% -ot Int32 -l HT_ForcingIDs -tr 30 30 %indir%\%forcmap% %indir%\%forctif%
gdal_translate -of AAIGrid %indir%\%forctif% %outdir%\%forcasc%

set gsgmap=HT_GGSURGO.shp
set gsgtif=HT_GGSURGO.tif
set gsgasc=HT_GGSURGO.asc

gdal_rasterize -a MUKEY -a_nodata $nodatavalue -ot Int32 -l HT_GGSURGO -tr 30 30 %indir%\%gsgmap% %indir%\%gsgtif%
gdal_translate -of AAIGrid %indir%\%gsgtif% %outdir%\%gsgasc%

set ext=asc

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
 gdal_translate -of AAIGrid %%i !outputfname!
 )

"C:\Program Files\Java\jre1.8.0_201\bin\java" -jar -Xmx8048m -Xms8012m "E:\PIHM_GIS\git_base\HydroTerre2PIHM.Java\HTCT.jar" %indir%\ %outdir%\
