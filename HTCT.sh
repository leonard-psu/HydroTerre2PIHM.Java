#! /bin/bash

printf "\n\n\n";
#========================================================================
if ! type "gdal_translate" > /dev/null; then
  echo "============================================="
  echo	"    ERROR: GDAL is missing. "
  echo	"   Please install the GDAL package first"
  echo	"Download the GDAL via http://www.gdal.org "
  echo "============================================="
  exit 1; 
fi
if ! type "java" > /dev/null; then
  echo "============================================="
  echo	"    ERROR: Java is missing. "
  echo	"   Please install the Java package first"
  echo	"Download the Java Runtime Environment(JRE) or Java Development Kit(JDK) via https://www.oracle.com/java/index.html "
  echo "============================================="
  exit 1; 
fi

nodatavalue=255;
#========================================================================

indir="$(pwd)/";
printf "\nCurrent working path:"
echo $indir;

lin=${#indir}

outdir="$indir/PIHM/"
sodir="PIHM/";	#short dir version
printf "\n\nOutput Folder:"
echo $outdir;

if [ ! -d "$outdir" ]; then
    mkdir $outdir;
else
    rm -r $outdir;
    mkdir $outdir;
fi


#========================================================================
forcmap="HT_ForcingIDs.shp";
forctif="HT_ForcingIDs.tif";
forcasc="HT_ForcingIDs.asc";
if [ ! -f "$forcmap" ]; then
    printf "\n\n!!WARNING:Forcing map, $forcmap does not exist.\n"
else
    printf "\n\nRasterizing Forcing map: $forcmap\n"
    gdal_rasterize -a Force_ID  -a_nodata $nodatavalue -ot Int32 -l HT_ForcingIDs -tr 30 30 "$indir$forcmap" "$indir$forctif"
    gdal_translate -of AAIGrid "$indir$forctif" "$outdir$forcasc"
fi
#========================================================================

gsgmap="HT_GGSURGO.shp";
gsgtif="HT_GGSURGO.tif";
gsgasc="HT_GGSURGO.asc";
if [ ! -f "$gsgmap" ]; then
    printf "\n\n!!WARNING:GGSURGO map, $gsgmap does not exist.\n";
else
    printf "\n\nRasterizing gsging map: $gsgmap\n"
    gdal_rasterize -a MUKEY -a_nodata $nodatavalue -ot Int32 -l HT_GGSURGO -tr 30 30 "$indir$gsgmap" "$indir$gsgtif"
    printf "\nTranslating the map to raster.\n"
    gdal_translate -of AAIGrid "$indir$gsgtif" "$outdir$gsgasc"
fi 
#========================================================================

printf "\n\nCoverting GeoTiff to ASCII Grid"
ext="asc";
i=0
while read line
do
    printf "\n\n";
    array[ $i ]="$line" 
    file=${line:lin}
    echo "in = $line"

    l=${#file}
    fcore=${file:0:$l-3}
    fout="$outdir$fcore$ext"
    echo "out = $fout"
    gdal_translate -of AAIGrid $line $fout
    (( i++ ))

done < <(ls $indir*.tif)

#========================================================================
echo ${array[1]}

printf "\n\n\n";
echo "Parsing forcing data"
java -jar -Xmx8048m -Xms8012m HTCT.jar $indir $outdir



