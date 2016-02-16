package util;

/**
 * Created by timmytime on 11/02/16.
 */
public class GameObjectTypes {


    //land  .. need sub types of types really....but anyway.
    public static final int TANK = 0;
    public static final int MISSILE_LAUNCHER = 1;
    public static final int RV = 2;

    //air
    public static final int MINI_DRONE = 3;
    public static final int LONGRANGE_DRONE = 20;
    public static final int ARMED_DRONE = 19;
    public static final int BOMBER = 4;
    public static final int FIGHTER = 5;
    public static final int HELICOPTER = 6;

    //sea
    public static final int SUB = 7;
    public static final int CARRIER = 8;
    public static final int FAC = 9;
    public static final int DESTROYER = 23;

    //infrastuture
    public static final int SATELLITE = 10;
    public static final int GARRISON = 11;
    public static final int OUTPOST = 12;
    public static final int PORT = 17;
    public static final int AIRPORT = 18;


    //missile again need range variants but its a start....
    public static final int G2A = 13;
    public static final int G2G = 14;
    public static final int A2G = 15;
    public static final int A2A = 16;
    public static final int CLUSTER = 21;
    public static final int CARPET = 22;
    public static final int W2W = 24;
    public static final int W2A = 25;
    public static final int W2G = 26;
    public static final int WATER_MINE = 27;
    public static final int GROUND_MINE = 28;


    public static String getTypeName(int type) {

        switch (type) {
            case TANK:
                return "Tank";
            case MISSILE_LAUNCHER:
                return "Missile\nLauncher";
            case RV:
                return "ATV";
            case MINI_DRONE:
                return "Mini Drone";
            case LONGRANGE_DRONE:
                return "Longrange\nDrone";
            case ARMED_DRONE:
                return "Armed Drone";
            case BOMBER:
                return "Bomber";
            case FIGHTER:
                return "Fighter";
            case HELICOPTER:
                return "Helicopter";
            case SUB:
                return "Submarine";
            case CARRIER:
                return "Aircraft\nCarrier";
            case FAC:
                return "Fast\nAttack\nCraft";
            case DESTROYER:
                return "Destroyer";
            case SATELLITE:
                return "Satellite";
            case GARRISON:
                return "Garrison";
            case OUTPOST:
                return "Outpost";
            case PORT:
                return "Port";
            case AIRPORT:
                return "Airport";
            case G2A:
                return "Ground\n2\nAir";
            case G2G:
                return "Ground\n2\nGround";
            case A2G:
                return "Air\n2\nGround";
            case CLUSTER:
                return "Cluster\nBomb";
            case A2A:
                return "Air\n2\nAir";
            case CARPET:
                return "Carpet\nBomb";
            case W2A:
                return "Water\n2\nAir";
            case W2G:
                return "Water\n2\nGround";
            case W2W:
                return "Water\n2\nWater";
            case WATER_MINE:
                return "Watermine";
            case GROUND_MINE:
                return "Landmine";

        }
        return "";
    }

}
