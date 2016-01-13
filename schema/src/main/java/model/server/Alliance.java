package model.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 13/01/16.
 */
public class Alliance {

    public final String key;
    public final String name;
    private List<String> members = new ArrayList<>();

    public Alliance(String key, String name) {

        this.key = key;
        this.name = name;
    }

    public void addMember(String member) {
        members.add(member);
    }

    public void removeMember(String member) {
        members.remove(member);
    }


    public List<String> getMembers() {
        return members;
    }

}
