import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timmytime on 16/01/16.
 */
public class Setup {

    private static final int MAX_SOCKETS = 10;
    private List<TestSocketController> socketControllers = new ArrayList<>();

    private List<String> allianceKeys = new ArrayList<>();





    public Setup() throws Exception {




    }

    public static void main(String[] args) throws Exception {

        Setup setup = new Setup();

        try {
            setup.start();
        } catch (Exception e) {
            e.printStackTrace();
            setup.stop();

        }
    }

    public void start() throws Exception {


        for(int i=0;i<MAX_SOCKETS; i++){
            socketControllers.add(new TestSocketController());
        }


        socketControllers.get(0).createAlliance(e -> {
            allianceKeys.add(e.getActionCommand());
            System.out.println("created alliance "+e.getActionCommand());
            //now we have it...lets try and join
            socketControllers.get(1).joinAlliance(e.getActionCommand());

        });





    }

    public void stop() {
        socketControllers.forEach(TestSocketController::stop);
    }
}
