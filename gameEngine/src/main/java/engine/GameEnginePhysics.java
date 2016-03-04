package engine;

import factory.CheChannelFactory;
import message.HazelcastMessage;
import model.GameEngineModel;
import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;
import util.map.UTMConvert;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEnginePhysics {

    private static final int EARTH_RADIUS = 6371000;

    /*
     can be used for missile range / flying range.
     */
    public static double getHaversineDistance(double currentLat, double currentLong, double destLat, double destLong) {

        double phi, phi2, deltaPhi, deltaLambda, a, c;

        phi = Math.toRadians(currentLat);
        phi2 = Math.toRadians(destLat);
        deltaPhi = Math.toRadians(destLat - currentLat);
        deltaLambda = Math.toRadians(destLong - currentLong);

        a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) + Math.cos(phi) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);

        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;

    }

    public static double calculateBearing(double currentLat, double currentLong, double destLat, double destLong) {
        double phi, phi2, lambda, lambda2, y, x;

        phi = Math.toRadians(currentLat);
        phi2 = Math.toRadians(destLat);
        lambda = Math.toRadians(currentLong);
        lambda2 = Math.toRadians(destLong);


        y = Math.sin(lambda2 - lambda) * Math.cos(phi2);
        x = Math.cos(phi) * Math.sin(phi2) - Math.sin(phi) * Math.cos(phi2) * Math.cos(lambda2 - lambda);

        return Math.toDegrees(Math.atan2(y, x));
    }

    public static double getLatitude(double latitude, double distance, double bearing) {
        double phi = Math.toRadians(latitude);
        double theta = Math.toRadians(bearing);
        return Math.toDegrees(Math.asin(Math.sin(phi) * Math.cos(distance / EARTH_RADIUS) + Math.cos(phi) * Math.sin(distance / EARTH_RADIUS) * Math.cos(theta)));
    }

    public static double getLongitude(double latitude, double longitude, double newLatitude, double distance, double bearing) {
        double lambda, phi, phi2, theta;
        lambda = Math.toRadians(longitude);
        phi = Math.toRadians(latitude);
        phi2 = Math.toRadians(newLatitude);
        theta = Math.toRadians(bearing);

        return Math.toDegrees(lambda + Math.atan2(Math.sin(theta) * Math.sin(distance / EARTH_RADIUS) * Math.cos(phi),
                Math.cos(distance / EARTH_RADIUS) - Math.sin(phi) * Math.sin(phi2)));

    }

    /*
      process a model by its rules.  also pass in our timestep, as we may want to configure it faster or slower once we see how it runs.
     */
    public static void process(final GameEngineModel gameEngineModel, final UTMConvert utmConvert, final long milliseconds) {

        double displacement;
        if (gameEngineModel.getGameObject().acceleration == 0) {
            gameEngineModel.getGameObject().acceleration = (double) gameEngineModel.getGameObjectRules().getForce() / gameEngineModel.getGameObjectRules().getMass();
        }

        if (gameEngineModel.getGameObject().velocity < gameEngineModel.getGameObjectRules().getMaxSpeed()) {
            gameEngineModel.getGameObject().velocity = (double) gameEngineModel.getGameObject().velocity + (gameEngineModel.getGameObject().acceleration * (milliseconds / 1000));
        }

        if (gameEngineModel.getGameObject().velocity > gameEngineModel.getGameObjectRules().getMaxSpeed()) {
            gameEngineModel.getGameObject().velocity = (double) gameEngineModel.getGameObjectRules().getMaxSpeed();
        }


        //so now we just need displacement...which we know is velocity * time....
        displacement = gameEngineModel.getGameObject().velocity * (milliseconds / 1000);

        if (displacement > gameEngineModel.getGameObject().getDistanceBetweenPoints()) {
            displacement = gameEngineModel.getGameObject().getDistanceBetweenPoints();
        }


        gameEngineModel.getGameObject().setDistanceBetweenPoints((gameEngineModel.getGameObject().getDistanceBetweenPoints() - displacement));


        double bearing = calculateBearing(gameEngineModel.getGameObject().utmLocation.latitude,
                gameEngineModel.getGameObject().utmLocation.longitude,
                gameEngineModel.getGameObject().destinationUTMLocation.latitude,
                gameEngineModel.getGameObject().destinationUTMLocation.longitude);

        gameEngineModel.getGameUTMLocation().latitude = getLatitude(gameEngineModel.getGameObject().utmLocation.latitude, displacement, bearing);
        gameEngineModel.getGameUTMLocation().longitude = getLongitude(gameEngineModel.getGameObject().utmLocation.latitude,
                gameEngineModel.getGameObject().utmLocation.longitude, gameEngineModel.getGameUTMLocation().latitude, displacement, bearing);



        /*
          finally we also need to work out its new UTM / SubUTM...
         */
        gameEngineModel.getGameUTMLocation().utm = utmConvert.getUTMGrid(gameEngineModel.getGameUTMLocation().latitude, gameEngineModel.getGameUTMLocation().longitude);
        gameEngineModel.getGameUTMLocation().subUtm = utmConvert.getUTMSubGrid(gameEngineModel.getGameUTMLocation().utm,
                gameEngineModel.getGameUTMLocation().latitude, gameEngineModel.getGameUTMLocation().longitude);


        if (gameEngineModel.hasChangedGrid()) {
            gameEngineModel.getGameUTMLocation().state = Tags.MESSAGE;
            gameEngineModel.getGameUTMLocation().value = Tags.GAME_OBJECT_ENTERED;  //at present we wont send, object has left utm.  but we probably should...if so do it on old
            gameEngineModel.getGameObject().utmLocation.state = Tags.MESSAGE;
            gameEngineModel.getGameObject().utmLocation.value = Tags.GAME_OBJECT_LEFT;

            try {
                gameEngineModel.setMessage(new HazelcastMessage(CheChannelFactory.getCheChannel(gameEngineModel.getPlayerKey()) != null ?
                        CheChannelFactory.getCheChannel(gameEngineModel.getPlayerKey()).getChannel().remoteAddress().toString() : "fake", //for testing..proves callback works as it fails.
                        new JSONObject(gameEngineModel.getGameUTMLocation().getMessage())));
                gameEngineModel.setMessage2(new HazelcastMessage(CheChannelFactory.getCheChannel(gameEngineModel.getPlayerKey()) != null ?
                        CheChannelFactory.getCheChannel(gameEngineModel.getPlayerKey()).getChannel().remoteAddress().toString() : "fake",
                        new JSONObject(gameEngineModel.getGameObject().utmLocation.getMessage())));
            } catch (JSONException e) {
                //who cares..its not essential should log but its not issue.  just a message to grid.
            }

        }

    }


}