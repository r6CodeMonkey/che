package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by timmytime on 15/01/16.
 */
public class Alliance extends CoreModel {

    public String name, value, state;
    private List<Player> members = new ArrayList<>();

    public Alliance(String key) {
        super(key);
    }

    public Alliance(message.Alliance alliance) {
        super(alliance.getKey());
        name = alliance.getName();
        value = alliance.getValue();
        state = alliance.getState();
        members.addAll(alliance.getMembers().stream().map(Player::new).collect(Collectors.toList()));
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

        alliance.setMembers(members.stream().map(player -> new message.Player(player.getMessage())).collect(Collectors.toList()));

        return alliance.toString();
    }
}
