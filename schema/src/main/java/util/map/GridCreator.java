package util.map;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by timmytime on 03/03/16.
 */
public class GridCreator {

    private final UTMConvert utmConvert;

    /*
     this class creates the grids, around a give lat/long
     */
    public GridCreator(UTMConvert utmConvert) {
        this.utmConvert = utmConvert;
    }

    public GridCreator() {
        this.utmConvert = new UTMConvert();
    }

    public static void main(String[] args) {

        //test data 51.89222264490244 0.603324696421598 works
        //test data 2 51.89119652 0.92197262 doesnt work

        GridCreator gridCreator = new GridCreator(new UTMConvert());
        Map<model.UTM, List<model.UTM>> test = gridCreator.getGrids(3, 51.89120537, 0.92166068);//lol it looks good i chose a stupid lace marker,,perhaps. or not. yeah it works.  ha ha.

     /*   for (model.UTM val : test.keySet()) {
            System.out.println("we are utm " + val.getUtm());

            for (model.UTM sub : test.get(val)) {
                System.out.println("we are sub " + sub.getUtm());

            }


        }
       */ gridCreator = new GridCreator();

        Map<UTM, List<SubUTM>> androidGrids = gridCreator.getAndroidGrids(5, 51.89119652, 0.92197262 );
       //    Map<UTM, List<SubUTM>> androidGrids = gridCreator.getAndroidGrids(5, 51.89222264490244, 0.603324696421598 );

        for (UTM val : androidGrids.keySet()) {
            System.out.println("we are utm " + val.getUtmLat() + val.getUtmLong());

            for (SubUTM subUTM : androidGrids.get(val)) {
                System.out.println("we are sub " + subUTM.getSubUtmLat() + subUTM.getSubUtmLong());
            }
        }


    }

    //android variant...due to rendering etc.
    public Map<UTM, List<SubUTM>> getAndroidGrids(int gridsX, double latitude, double longitude) {

        //performance wise we should fix but,,,
        Map<model.UTM, List<model.UTM>> grids = getGrids(gridsX, latitude, longitude);

        //now create the fucked up android variant.  or go work out why i did that (because no other).  ok i see.  it needs ints and strings..probably can fix.
        //ok its because if we only have a subutm, we need to work out what it is.  we cant lose that functionality, and its not stopping a release given
        //where im at and what else is required.  it will go in backlog as P1.
        Map<UTM, List<SubUTM>> values = new HashMap<>();

        for (model.UTM utm : grids.keySet()) {

            UTM temp = new UTM(utm.getUtm());
            for (model.UTM subUTM : grids.get(utm)) {
                updateMap(values, temp, new SubUTM(subUTM.getUtmLat(), subUTM.getUtmLong()));
            }


        }

        return values;
    }

    /*
      simply returns our string values.  could be direct for server.  not we can only really have 3by3 or 9by9...need to enforce in realitty.
     */
    public Map<model.UTM, List<model.UTM>> getGrids(int gridsX, double latitude, double longitude) {


        Map<model.UTM, List<model.UTM>> values = new HashMap<>();  //we are stuck with java7 so no lambda.  but thats ok its not heavy.


        double gridOffset = (gridsX - 1) / 2;

        //we know our offset (i hope)
        double latOffset = utmConvert.getLatOffset();
        double longOffset = utmConvert.getLongOffset();

        DecimalFormat df = new DecimalFormat("#.########");

        System.out.println("latoffset "+latOffset+", long offset "+longOffset+", grid offset "+gridOffset);

        //for fuck sake.  need to go BigDecmial.  java is gay and cant use java8.
        BigDecimal latLimit = new BigDecimal(latitude + (latOffset * gridOffset));
        BigDecimal longLimit = new BigDecimal(longitude + (longOffset * gridOffset));

        BigDecimal lat = new BigDecimal(latitude - ( latOffset * gridOffset));
        BigDecimal lng = new BigDecimal(longitude - (longOffset * gridOffset));


        //fuck this.  will have to do it manually based on the fucking grids.  had enough tonight.  stupid stupid bug in java.

        /*
         so dumbass code is
         1: get our start point....we know that.  then simply loop + grids across and down.  yes/
         */

        for(int stepX = 0; stepX < gridsX; stepX++) {
            for (int stepY = 0; stepY < gridsX; stepY++) {
                model.UTM curentUTM = utmConvert.getUTMGrid(lat.doubleValue(), lng.doubleValue());
                model.UTM currentSubUTM = utmConvert.getUTMSubGrid(curentUTM, lat.doubleValue(), lng.doubleValue());

                updateMap(values, curentUTM, currentSubUTM);

               lat = lat.add(new BigDecimal(latOffset));
            }

            lat = new BigDecimal(latitude - ( latOffset * gridOffset));

            lng = lng.add(new BigDecimal(longOffset));
        }



        return values;

    }

    private void updateMap(Map<UTM, List<SubUTM>> map, UTM utm, SubUTM subUTM) {
        boolean found = false;
        for (List<SubUTM> subUtmList : map.values()) {
            if (subUtmList != null) {
                subUtmList.add(subUTM);
                found = true;
            }

        }

        if (!found) {  //cleary quicker way but no lambda...who cares.
            List<SubUTM> temp = new ArrayList<>();
            temp.add(subUTM);
            map.put(utm, temp);
        }
    }

    private void updateMap(Map<model.UTM, List<model.UTM>> map, model.UTM utm, model.UTM subUTM) {
        boolean found = false;
        for (List<model.UTM> subUtmList : map.values()) {
            if (subUtmList != null) {
                subUtmList.add(subUTM);
                found = true;
            }

        }

        if (!found) {  //cleary quicker way but no lambda...who cares.
            List<model.UTM> temp = new ArrayList<>();
            temp.add(subUTM);
            map.put(utm, temp);
        }
    }


}
