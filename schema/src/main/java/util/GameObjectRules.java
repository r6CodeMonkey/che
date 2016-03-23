package util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by timmytime on 21/02/16.
 */
@XmlRootElement
public class GameObjectRules implements Serializable {

    int mass, impactRadius, maxSpeed, maxRange, force, strength;


    public int getMass() {
        return mass;
    }

    @XmlElement
    public void setMass(int mass) {
        this.mass = mass;
    }

    public int getImpactRadius() {
        return impactRadius;
    }

    @XmlElement
    public void setImpactRadius(int impactRadius) {
        this.impactRadius = impactRadius;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    @XmlElement
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getMaxRange() {
        return maxRange;
    }

    @XmlElement
    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public int getForce() {
        return force;
    }

    @XmlElement
    public void setForce(int force) {
        this.force = force;
    }

    public int getStrength() {
        return strength;
    }

    @XmlElement
    public void setStrength(int strength) {
        this.strength = strength;
    }


}
