package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 15/01/16.
 */
public class GameObject extends CoreModel {

    public String state, value;
    //where are we located now...rest is calculated see physics shit. ie we can only be in 1 place.
    public UTMLocation utmLocation;

    //general physics variables.
    public double mass, acceleration, velocity;
    //is it fixed / hit / destroyed / located
    public boolean fixed, hit, destroyed, located;
    //contains a list of missiles.  perhaps supports holding all sorts.
    private List<Missile> missiles = new ArrayList<>();


    public GameObject(message.GameObject gameObject) {

    }

    public GameObject(String key) {
        super(key);
        utmLocation = new UTMLocation();
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    @Override
    public String getMessage() {
        message.GameObject gameObject = new message.GameObject();
        gameObject.create();

        return gameObject.toString();
    }
}
