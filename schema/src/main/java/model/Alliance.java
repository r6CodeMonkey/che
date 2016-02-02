package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 15/01/16.
 */
public class Alliance extends CoreModel {

    public String name, value, state;//tt
    private List<Player> members = new ArrayList<>();

    public Alliance(String key) {
        super(key);
    }

    public Alliance(message.Alliance alliance) {
        super(alliance.getKey());
        name = alliance.getName();
        value = alliance.getValue();
        state = alliance.getState();
        for(message.Player player : alliance.getMembers()){
            members.add(new Player(player));
        }
    }

    public List<Player> getMembers() {
        return members;
    }

    @Override
    public String getMessage() {

        message.Alliance alliance = new message.Alliance();
        alliance.create();

        alliance.setKey(key);
        alliance.setName(name);
        alliance.setValue(value);
        alliance.setState(state);

        List<message.Player> temp = new ArrayList<>();

        for(Player player : members){
            temp.add(new message.Player(player.getMessage()));
        }

        alliance.setMembers(temp);

        return alliance.toString();
    }
}
