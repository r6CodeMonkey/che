package util.map;

import java.util.*;

/**
 * Created by timmytime on 03/03/16.
 */
public class GridCreator {

    private final UTMConvert utmConvert;
    /*
     this class creates the grids, around a give lat/long
     */
    public GridCreator(UTMConvert utmConvert){
        this.utmConvert = utmConvert;
    }

    //android variant...due to rendering etc.
    public Map<UTM, List<SubUTM>> getAndroidGrids(int gridsX, double latitude, double longitude){
        return null;
    }


    /*
      simply returns our string values.  could be direct for server.  not we can only really have 3by3 or 9by9...need to enforce in realitty.
     */
    public Map<model.UTM, List<model.UTM>> locateGrids(int gridsX, double latitude, double longitude){


        Map<model.UTM, List<model.UTM>> values = new HashMap<>();  //we are stuck with java7 so no lambda.  but thats ok its not heavy.


        int gridOffset = (gridsX-1)/2;

        //we know our offset (i hope)
        double latOffset = utmConvert.getLatOffset();
        double longOffset = utmConvert.getLongOffset();

        for(double lat = latitude-(latOffset*gridOffset);lat<=latitude+(latOffset*gridOffset);lat+=latOffset){

            for(double lng = longitude-(longOffset*gridOffset);lng<=longitude+(longOffset*gridOffset);lng+=longOffset) {

                model.UTM curentUTM = utmConvert.getUTMGrid(lat, lng);
                model.UTM currentSubUTM = utmConvert.getUTMSubGrid(curentUTM, lat, lng);

                updateMap(values, curentUTM, currentSubUTM);

            }

        }

       return values;

    }

    private void updateMap(Map<model.UTM, List<model.UTM>> map, model.UTM utm, model.UTM subUTM){
       boolean found = false;
        for(List<model.UTM> subUtmList : map.values()){

        }

        if(!found){  //cleary quicker way but no lambda...who cares.
            List<model.UTM> temp = new ArrayList<>();
            temp.add(subUTM);
            map.put(utm, temp);
        }
    }

    public static void main(String[] args){
        GridCreator gridCreator = new GridCreator(new UTMConvert());
        Map<model.UTM, List<model.UTM>> test = gridCreator.locateGrids(3, 0.0,0.0);//lol it looks good i chose a stupid lace marker,,perhaps. or not. yeah it works.  ha ha.

        for(model.UTM val : test.keySet()){
            System.out.println("we are utm "+val.getUtm());

            for(model.UTM sub : test.get(val)){
                System.out.println("we are sub "+sub.getUtm());

            }


        }

    }


}
