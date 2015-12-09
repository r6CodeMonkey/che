package message.in;

import util.UTM;
import util.UTMConvert;

import java.io.Serializable;

/**
 * Created by timmytime on 17/02/15.
 */
public class InGridMessage implements Serializable {


    //grids are simply do we remove an item from a grid given its co-ordinates...at this point
    //we just need co-ordinates and ids
    UTM utm;
    UTM subUtm;

    private CoreMessage coreMessage;


    public InGridMessage(CoreMessage message, UTMConvert utmConvert) {

        coreMessage = message;
        utm = utmConvert.getUTMGrid(message.getLatitude(), message.getLongitude());
        subUtm = utmConvert.getUTMSubGrid(utm, message.getLatitude(), message.getLongitude());

    }

/*
    public InGridMessage(InPackageMessage message, UTMConvert utmConvert) {

        utm = utmConvert.getUTMGrid(message.getLatitude(), message.getLongitude());
        subUtm = utmConvert.getUTMSubGrid(utm, message.getLatitude(), message.getLongitude());
    }

    public InGridMessage(InAllianceMessage message, UTMConvert utmConvert) {

        utm = utmConvert.getUTMGrid(message.getLatitude(), message.getLongitude());
        subUtm = utmConvert.getUTMSubGrid(utm, message.getLatitude(), message.getLongitude());
    }
*/

    public UTM getUtm() {
        return utm;
    }

    public UTM getSubUtm() {
        return subUtm;
    }

    public CoreMessage getCoreMessage() {
        return coreMessage;
    }

    public void setUtm(UTM utm) {
        this.utm = utm;
    }

    public void setSubUtm(UTM subUtm) {
        this.subUtm = subUtm;
    }

}
