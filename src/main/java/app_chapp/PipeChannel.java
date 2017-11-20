package app_chapp;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor Konovalov on 19.11.2017.
 */
public class PipeChannel {

    private static final Logger LOGGER = Logger.getLogger(PipeChannel.class.getName());

    private Client client;
    private Server server;

    PipeChannel(Client client, Server server) {
        this.client = client;
        this.server = server;

        LOGGER.setUseParentHandlers(false);
        LogFormatter lf = new LogFormatter();
        LOGGER.setLevel(Level.CONFIG);
        FileHandler fh = null;
        try {
            fh = new FileHandler("logs/pipe.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fh.setFormatter(lf);
        LOGGER.addHandler(fh);

        LOGGER.log(Level.CONFIG,"Client and Server were initialized");
    }

    public void process() {
        String storage = "";

        LOGGER.log(Level.INFO,"Auth process started");

        /*
        Here is an implementation of full auth cycle, no init
         */

        // 1-st request
        storage = client.sendIdRequest(); LOGGER.log(Level.INFO,"Client sent id = {0}",storage);
        server.receiveIdRequest(storage); LOGGER.log(Level.INFO,"Server received client_id");
        storage = server.sendChallenge(); LOGGER.log(Level.INFO,"Server sent challenge = {0}",storage);
        client.receiveChallenge(storage); LOGGER.log(Level.INFO,"Client received server_challenge");

        // 2-nd request
        storage = client.sendAuthRequest(); LOGGER.log(Level.INFO,"Client sent auth hashed code = {0}",storage);
        server.receiveAuthRequest(storage); LOGGER.log(Level.INFO,"Server received client_code");
        storage = server.sendAuthResponse(); LOGGER.log(Level.INFO,"Server sent auth result = {0}",storage);
        client.receiveAuthResponse(storage); LOGGER.log(Level.INFO,"Client received server_response");

    }

    public void endSession() {
        client.endSession();
        server.endSession();

        LOGGER.log(Level.FINE,"End session, OK");
    }
}
