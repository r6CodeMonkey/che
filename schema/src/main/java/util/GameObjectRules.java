package util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by timmytime on 21/02/16.
 */
@XmlRootElement
public class GameObjectRules {

    int mass, impactRadius, maxSpeed, maxRange, force;


    public int getMass(){
        return mass;
    }

    public int getImpactRadius(){
        return impactRadius;
    }

    public int getMaxSpeed(){
        return maxSpeed;
    }

    public int getMaxRange(){
        return maxRange;
    }

    public int getForce(){ return force;}

    @XmlElement
    public void setMass(int mass){
        this.mass = mass;
    }
    @XmlElement
    public void setImpactRadius(int impactRadius){
        this.impactRadius = impactRadius;
    }
    @XmlElement
    public void setMaxSpeed(int maxSpeed){
        this.maxSpeed = maxSpeed;
    }
    @XmlElement
    public void setMaxRange(int maxRange){
        this.maxRange = maxRange;
    }
    @XmlElement
    public void setForce(int force){
        this.force = force;
    }


}