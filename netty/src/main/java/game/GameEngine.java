package game;

import util.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 23/02/16.
 */
public class GameEngine {

    private final Configuration configuration;

    private final List<GameEngineModel> gameEngineModelMap = new ArrayList<>();

    public GameEngine(Configuration configuration) {
        this.configuration = configuration;
    }

    public void addGameEngineModel(GameEngineModel gameEngineModel) {
        this.gameEngineModelMap.add(gameEngineModel);
    }

    public void removeGameEngineModel(GameEngineModel gameEngineModel) {
        this.gameEngineModelMap.remove(gameEngineModel);
    }

    /*
      next we need our loop....basically we move an object, ue update its current lat, long and then utm and sub utm based on rules.
     */
    private void engine() {

    }

    /*
     filter objects into utms..
     */
    private void filterUTM() {

    }

    /*
      filter our utm into sub utm and then process
     */
    private void filterSubUTM() {

    }

    /*
     check if we have hit anything, and if so update status to send to user
     */
    private void processMissiles() {

    }

    /*
      change our pub subs...add remove topics etc...
     */
    private void handleIteration() {

    }


}
