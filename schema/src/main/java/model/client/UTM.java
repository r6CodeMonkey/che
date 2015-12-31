package model.client;

import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

import java.io.Serializable;

/**
 * Created by timmytime on 31/12/15.
 */
public class UTM extends JSONObject implements Serializable {

    private String utm, subUtm;

    public UTM(String utm) throws JSONException{
        super(utm);
    }

    public UTM(JSONObject utm) throws JSONException {
        setUtm(utm.getString(Tags.UTM));
        setSubUtm(utm.getString(Tags.SUB_UTM));
    }

    public UTM(String utm, String subUtm) throws JSONException{
        this.put(Tags.UTM, utm);
        this.put(Tags.SUB_UTM, subUtm);
        setUtm(utm);
        setSubUtm(subUtm);
    }

    public void setUtm(String utm){
        this.utm = utm;
    }

    public void setSubUtm(String subUtm){
        this.subUtm = subUtm;
    }

    public String getUtm(){
        return utm;
    }

    public String getSubUtm(){
        return subUtm;
    }
}
