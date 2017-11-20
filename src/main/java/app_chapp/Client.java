package app_chapp;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor Konovalov on 19.11.2017.
 */
public class Client {

    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public Client() {
        LOGGER.setUseParentHandlers(false);
        LogFormatter lf = new LogFormatter();
        LOGGER.setLevel(Level.CONFIG);
        FileHandler fh = null;
        try {
            fh = new FileHandler("logs/client.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fh.setFormatter(lf);
        LOGGER.addHandler(fh);
    }

    /*
    Set these fields in App.java dialog
     */
    private String id;
    private String pwd;

    /*
    This field get rom server
     */
    private String s_challenge;

    public String sendIdRequest() {
        LOGGER.log(Level.INFO,"Send id = {0} to Server",id);
        return id;
    }

    public void receiveChallenge(String s_challenge) {
        this.s_challenge = s_challenge;
        LOGGER.log(Level.INFO,"Get challenge from Server = {0}",s_challenge);
    }

    public String sendAuthRequest() {
        // hash password and create 'q' value
        String hashed = hashMe(pwd);
        LOGGER.log(Level.INFO,"Hashed password = {0}",hashed);

        // do insertions in the beginning of hashed pwd string
        hashed = hashed.replace(hashed.substring(0,2),s_challenge);
        LOGGER.log(Level.INFO,"Add challenge to hashed code = {0}",hashed);

        // hash result and create 'r' value
        String hashed2 = hashMe(hashed);
        LOGGER.log(Level.INFO,"Created hashed r value = {0}",hashed2);

        // and return it [to pipe]
        return hashed2;
    }

    public void receiveAuthResponse(String auth_response) {
        System.out.println("[Server]:" + auth_response);
        LOGGER.log(Level.INFO,"Get response from server = {0}",auth_response);
    }

    protected String hashMe(String whatToHash) {
        try {
            return new Hasher().hash(whatToHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void endSession() {
        LOGGER.log(Level.INFO, "Session ended OK");
    }

    public void setId(String id) {
        this.id = id;
        LOGGER.log(Level.CONFIG,"Id = {0} was setted",id);
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        LOGGER.log(Level.CONFIG,"Pwd = {0} was setted",pwd);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}

