package message;

import factory.MessageFactory;
import model.CoreModel;
import org.json.JSONObject;
import util.Tags;

/**
 * Created by timmytime on 15/01/16.
 */
public class CheMessage extends CoreMessage {

    public CheMessage(){}

    public CheMessage(String message) {
        super(message);
    }

    public CheMessage(CoreModel model) {
        super(model);
    }

    @Override
    public void create() {
        JSONObject inner = new JSONObject();
        inner.put(Tags.TYPE, "");
        this.put(Tags.CHE, inner);

    }

    @Override
    public String getKey() {
        return null;
    }

    public String getType(){
        return this.getJSONObject(Tags.CHE).get(Tags.TYPE).toString();
    }

    @Override
    public void setKey(String key) {

    }

    public void setType(String type){
      this.getJSONObject(Tags.CHE).put(Tags.TYPE, type);
    }

    public void setMessage(CoreMessage message){
        this.getJSONObject(Tags.CHE).put(Tags.CORE, message);
    }


    public CoreMessage getMessage() {
        return MessageFactory.getCheMessage(this.getJSONObject(Tags.CHE).getJSONObject(Tags.CORE).toString(), this.getJSONObject(Tags.CHE).get(Tags.TYPE).toString());
    }

}
