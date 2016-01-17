import message.CheMessage;
import message.UTM;
import model.Acknowledge;
import model.Alliance;
import model.Player;
import model.UTMLocation;
import org.json.JSONException;
import org.json.JSONObject;
import util.Tags;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by timmytime on 17/01/16.
 */
public class TestSocketController {

    //basic stuff.  each test socket controller will have a user key.
    private  Player player;
    private Alliance alliance;
    private List<Alliance> alliances = new ArrayList<>();
    //need to confirm we get acks....
    private ActionListener allianceCallback;

    private final TestSocket testSocket;

    public TestSocketController() throws IOException {

        testSocket = new TestSocket(e -> {
            try {
                handleMessageReceived(e.getActionCommand());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        });


        //need to register and get a player back.
        Acknowledge acknowledge = new Acknowledge(UUID.randomUUID().toString());
        acknowledge.state = Tags.MESSAGE;
        acknowledge.value = "";
        player = new Player("");
        player.name =  UUID.randomUUID().toString();
        UTMLocation utmLocation = new UTMLocation();
        utmLocation.latitude = 1.0;
        utmLocation.longitude = 12.0;
        utmLocation.altitude = 11.0;
        utmLocation.speed = 5.0;
        utmLocation.state = "";
        utmLocation.value = "";

        player.utmLocation = utmLocation;

        CheMessage cheMessage = new CheMessage();
        cheMessage.create();

        cheMessage.setMessage(Tags.ACKNOWLEDGE, new message.Acknowledge(acknowledge.getMessage()));
        cheMessage.setMessage(Tags.PLAYER, new message.Player(player.getMessage()));

        System.out.println("che message" + cheMessage.toString());

        testSocket.write(cheMessage);





    }


    public void joinAlliance(String key){
        Acknowledge acknowledge = new Acknowledge(UUID.randomUUID().toString());
        acknowledge.state = Tags.MESSAGE;
        acknowledge.value = "";

        alliance = new Alliance(key);
        alliance.name = "testing";
        alliance.value = "";
        alliance.state = Tags.ALLIANCE_JOIN;

        CheMessage cheMessage = new CheMessage();
        cheMessage.create();

        cheMessage.setMessage(Tags.ACKNOWLEDGE, new message.Acknowledge(acknowledge.getMessage()));
        cheMessage.setMessage(Tags.PLAYER, new message.Player(player.getMessage()));
        cheMessage.setMessage(Tags.ALLIANCE, new message.Alliance(alliance.getMessage()));

        testSocket.write(cheMessage);
    }


    public void createAlliance(ActionListener allianceCallback){
        this.allianceCallback = allianceCallback;

        Acknowledge acknowledge = new Acknowledge(UUID.randomUUID().toString());
        acknowledge.state = Tags.MESSAGE;
        acknowledge.value = "";

        alliance = new Alliance("");
        alliance.name = "testing";
        alliance.value = "";
        alliance.state = Tags.ALLIANCE_CREATE;

        CheMessage cheMessage = new CheMessage();
        cheMessage.create();

        cheMessage.setMessage(Tags.ACKNOWLEDGE, new message.Acknowledge(acknowledge.getMessage()));
        cheMessage.setMessage(Tags.PLAYER, new message.Player(player.getMessage()));
        cheMessage.setMessage(Tags.ALLIANCE, new message.Alliance(alliance.getMessage()));

        testSocket.write(cheMessage);
    }


    private void handleMessageReceived(String message) throws JSONException {

        JSONObject jsonObject = new JSONObject(message);
        //we are either a che message, or an ack.
        //look for OUR UUID type.
        if(!jsonObject.isNull(Tags.CHE)){
            System.out.println("received che return message " + message);
        }

        if(!jsonObject.isNull(Tags.UTM_LOCATION)){
            System.out.println("received utm location return message " + message);
        }

        if(!jsonObject.isNull(Tags.ALLIANCE)) {
            System.out.println("received alliance return message " + message);
           //ok so case 1.  we created alliance
            switch(jsonObject.getJSONObject(Tags.ALLIANCE).get(Tags.STATE).toString()){
                case Tags.ALLIANCE_CREATE:
                    alliance.setKey(jsonObject.getJSONObject(Tags.ALLIANCE).get(Tags.ALLIANCE_KEY).toString());
                    alliances.add(alliance);
                    allianceCallback.actionPerformed(new ActionEvent(this, 1, jsonObject.getJSONObject(Tags.ALLIANCE).get(Tags.ALLIANCE_KEY).toString()));
                    break;
                case Tags.ALLIANCE_JOIN:
                    alliance = new Alliance(new message.Alliance(jsonObject.toString()));
                    alliances.add(alliance);
                    System.out.println("joined allinace " + alliance.getMessage());
                    break;
                case Tags.ALLIANCE_LEAVE:
                    break;
                case Tags.ALLIANCE_INVITE:
                    break;
                case Tags.ALLIANCE_POST:
                    break;
            }

        }

        if(!jsonObject.isNull(Tags.ACKNOWLEDGE)){
            //its an ack.
            if(jsonObject.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.STATE).equals(Tags.UUID)){
                player.setKey(jsonObject.getJSONObject(Tags.ACKNOWLEDGE).get(Tags.VALUE).toString());

                System.out.println("created player " + player.getKey());
            }
        }

    }

    public void stop(){
        testSocket.destroy();
    }
}
