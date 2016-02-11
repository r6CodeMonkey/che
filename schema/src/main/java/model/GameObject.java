package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 15/01/16.
 */
public class GameObject extends CoreModel {

    public String state, value;
    public int type, subType;
    //where are we located now...rest is calculated see physics shit. ie we can only be in 1 place.
    public UTMLocation utmLocation;

    //general physics variables.
    public double mass, acceleration, velocity;
    //is it fixed / hit / destroyed / located
    public boolean fixed, hit, destroyed, located;
    //contains a list of missiles.  perhaps supports holding all sorts. (no all missiles are same, just different settings).
    private List<Missile> missiles = new ArrayList<>();


    public GameObject(message.GameObject gameObject) {

        this.key = gameObject.getKey();
        this.state = gameObject.getState();
        this.value = gameObject.getValue();
        this.mass = gameObject.getMass();
        this.velocity = gameObject.getVelocity();
        this.acceleration = gameObject.getAcceleration();
        this.fixed = gameObject.isFixed();
        this.hit = gameObject.isHit();
        this.located = gameObject.isLocated();
        this.destroyed = gameObject.isDestroyed();
        this.type = gameObject.getType();
        this.subType = gameObject.getSubType();
        for(message.Missile missile : gameObject.getMissiles()){
           missiles.add(new Missile(missile));
        }
         this.utmLocation = new UTMLocation(gameObject.getUtmLocation());

    }

    public GameObject(String key) {
        super(key);
        utmLocation = new UTMLocation();
        this.state = "";
        this.value = "";
        this.mass = 0;
        this.velocity = 0;
        this.acceleration = 0;
        this.fixed = false;
        this.hit = false;
        this.located = false;
        this.destroyed = false;
        this.type = -1;
        this.subType = -1;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    @Override
    public String getMessage() {
        message.GameObject gameObject = new message.GameObject();
        gameObject.create();

        //need to create message then all done....thank fuck.
        gameObject.setKey(this.key);
        gameObject.setState(state);
        gameObject.setValue(value);
        gameObject.setAcceleration(acceleration);
        gameObject.setVelocity(velocity);
        gameObject.setMass(mass);
        gameObject.setHit(hit);
        gameObject.setDestroyed(destroyed);
        gameObject.setLocated(located);
        gameObject.setFixed(fixed);
        gameObject.setType(type);
        gameObject.setSubType(subType);

        List<message.Missile> temp = new ArrayList<>();
        for(Missile missile : missiles){
            temp.add(new message.Missile(missile.getMessage()));
        }

        gameObject.setMissiles(temp);

        gameObject.setUtmLocation(new message.UTMLocation(utmLocation.getMessage()));


        return gameObject.toString();
    }
}
