package game;

import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import model.UTMLocation;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEnginePhysics {

    private static final int EARTH_RADIUS = 6371000;

    /*
     may not need but usefule
     */
    public static double getHaversineDistance(double currentLat, double currentLong, double destLat, double destLong){
        double phi, phi2, deltaPhi,deltaLambda, a, c;

        phi = Math.toRadians(currentLat);
        phi2 = Math.toRadians(destLat);
        deltaPhi = Math.toRadians(destLat - currentLat);
        deltaLambda = Math.toRadians(destLong - currentLong);

        a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) + Math.cos(phi) * Math.cos(phi2) * Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);

        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return EARTH_RADIUS * c;

    }

    public static double calculateBearing(double currentLat, double currentLong, double destLat, double destLong){
        double phi, phi2, lambda, lambda2, y,x;

        phi = Math.toRadians(currentLat);
        phi2 = Math.toRadians(destLat);
        lambda = Math.toRadians(currentLong);
        lambda2 = Math.toRadians(destLong);


        y = Math.sin(lambda2 - lambda) * Math.cos(phi2);
        x = Math.cos(phi) * Math.sin(phi2) - Math.sin(phi) * Math.cos(phi2) * Math.cos(lambda2 - lambda);

        return /*((*/Math.toDegrees(Math.atan2(y,x))/*+180) % 360)*/;
    }

    public static double getLatitude(double latitude, double distance, double bearing){
        double phi = Math.toRadians(latitude);
        return Math.toDegrees(Math.asin(Math.sin(phi) * Math.cos(distance / EARTH_RADIUS) + Math.cos(phi) * Math.sin(distance / EARTH_RADIUS) * Math.cos(bearing)));
    }

    public static double getLongitude(double latitude, double longitude, double newLatitude, double distance, double bearing){
        double lambda, phi, phi2;
        lambda = Math.toRadians(longitude);
        phi = Math.toRadians(latitude);
        phi2 = Math.toRadians(newLatitude);

        return ((Math.toDegrees(lambda - Math.atan2(Math.sin(bearing) * Math.sin(distance/EARTH_RADIUS) * Math.cos(phi),
                                  Math.cos(distance/EARTH_RADIUS) - Math.sin(phi) * Math.sin(phi2)))+540)%360-180);

    }

    /*
      process a model by its rules.  also pass in our timestep, as we may want to configure it faster or slower once we see how it runs.
     */
    public static void process(final GameEngineModel gameEngineModel, final long milliseconds){

         double displacement;
        if(gameEngineModel.getGameObject().acceleration != 0){
            gameEngineModel.getGameObject().acceleration = gameEngineModel.getGameObjectRules().getForce() / gameEngineModel.getGameObjectRules().getMass();
        }

        if(gameEngineModel.getGameObject().velocity < gameEngineModel.getGameObjectRules().getMaxSpeed()) {
            gameEngineModel.getGameObject().velocity = gameEngineModel.getGameObject().velocity + (gameEngineModel.getGameObject().acceleration * (milliseconds / 1000));
        }

        if(gameEngineModel.getGameObject().velocity > gameEngineModel.getGameObjectRules().getMaxSpeed()){
            gameEngineModel.getGameObject().velocity = gameEngineModel.getGameObjectRules().getMaxSpeed();
        }

        //so now we just need displacement...which we know is velocity * time....
        displacement = gameEngineModel.getGameObject().velocity * (milliseconds/1000);

        double d = getHaversineDistance(gameEngineModel.getGameObject().utmLocation.latitude,
                gameEngineModel.getGameObject().utmLocation.longitude,
                gameEngineModel.getGameObject().destinationUTMLocation.latitude,
                gameEngineModel.getGameObject().destinationUTMLocation.longitude);

        /*
          so basically we have the total distance to destination.  now what we need to do is simply is take the displacement from the distance, and see if we can
          work out the degrees from there.  basically...

          so

          1: c = new distance / earth radius.
          now need to find a....
          c/2 = Math.atan(1,2).

         */



    }

    public static void main(String[] args){

        double d = GameEnginePhysics.getHaversineDistance(50.0686, 5.7161, 58.6400, 3.0700);

        double bearing = GameEnginePhysics.calculateBearing(50.0686, 5.7161, 58.6400, 3.0700);

        double lat = GameEnginePhysics.getLatitude(50.0686, 2000, -9.131774011425927);

        double lng = GameEnginePhysics.getLongitude(50.0686,5.7161,50.05137985817801,2000, -9.131774011425927);

        System.out.println("lng "+lng);

    }


}
