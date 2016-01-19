package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        this.missiles.addAll(gameObject.getMissiles().stream().map(Missile::new).collect(Collectors.toList()));
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

        gameObject.setMissiles(missiles.stream().map(missile -> new message.Missile(missile.getMessage())).collect(Collectors.toList()));
        gameObject.setUtmLocation(new message.UTMLocation(utmLocation.getMessage()));


        return gameObject.toString();
    }
}
