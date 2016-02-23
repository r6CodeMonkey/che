package game;

import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import model.UTMLocation;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEnginePhysics {

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

        /*
         ok use haversine.  need to commit this so dont lose it.
         given the entire length, we can likely reverse logic.....

         so....instead of total distance (which we should capture to reduce processing from off see accel 1

         we then refactor formula to give me lat lng based on new displacement length...


         Haversine
formula: 	a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
c = 2 ⋅ atan2( √a, √(1−a) )
d = R ⋅ c
where 	φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);

JavaScript:

var R = 6371000; // metres
var φ1 = lat1.toRadians();
var φ2 = lat2.toRadians();
var Δφ = (lat2-lat1).toRadians();
var Δλ = (lon2-lon1).toRadians();

var a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
        Math.cos(φ1) * Math.cos(φ2) *
        Math.sin(Δλ/2) * Math.sin(Δλ/2);
var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

var d = R * c;

ok i can work with this...

         */


        //now we need to simply (i say simply) to the vector between current(lat,lng) and destination(lat, lng).  i dont think we can vector them so off to research again.



    }

}
