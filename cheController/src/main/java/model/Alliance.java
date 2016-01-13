package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 13/01/16.
 */
public class Alliance {

    private final String name;
    private List<String> members = new ArrayList<>();

    public Alliance(String name) {
        this.name = name;
    }

    public void addMember(String member) {
        members.add(member);
    }

    public void removeMember(String member) {
        members.remove(member);
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        return members;
    }

}
