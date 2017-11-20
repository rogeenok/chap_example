package app_chapp;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Igor Konovalov on 19.11.2017.
 */
public class Server {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Server() {
        LOGGER.setUseParentHandlers(false);
        LogFormatter lf = new LogFormatter();
        LOGGER.setLevel(Level.CONFIG);
        FileHandler fh = null;
        try {
            fh = new FileHandler("logs/server.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fh.setFormatter(lf);
        LOGGER.addHandler(fh);
    }

    private final String fileName = "database.txt";

    /*
    This field is used to hashMe on server
    */
    private String challenge;

    /*
    These fields get from client
     */
    private String c_id;
    private String c_hashed;

    private ChallengeGenerator challengeGenerator = new ChallengeGenerator();
    private PwdGenerator pwdGenerator = new PwdGenerator();

    /*
    key field is id, and second is pwd
    */
    private Map<String, String> users = new HashMap<String, String>();

    private void  readDataBase() throws FileNotFoundException, IOException {
        LOGGER.log(Level.INFO,"Opening db file");
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String s = "";
        LOGGER.log(Level.INFO,"Reading data from DB");
        while ( (s= bufferedReader.readLine()) != null) {
            int p = s.indexOf(" ");
            users.put(s.substring(0,p),s.substring(p+1));
        }
        LOGGER.log(Level.INFO,"Data fetched");
    }

    public void receiveIdRequest( String c_id) {
        this.c_id = c_id;
        LOGGER.log(Level.INFO,"Get user id = {0}",c_id);
    }

    public String sendChallenge() {
        challenge = challengeGenerator.generate();
        LOGGER.log(Level.INFO,"Generate challenge = {0}",challenge);
        return challenge;
    }

    public void receiveAuthRequest(String c_hashed) {
        this.c_hashed = c_hashed;
        LOGGER.log(Level.INFO,"Get user hashed password + challenge = {0}",c_hashed);
    }

    public String sendAuthResponse() {
        // find hashed password in database
        String hashed = users.get(c_id);
        LOGGER.log(Level.INFO,"User hashed code = {0}",hashed);
        if (hashed == null) {
            LOGGER.log(Level.INFO,"User wasn't found");
            return "User with that id not found";
        }

        // do insertions in the beginning of hashed pwd string
        hashed = hashed.replace(hashed.substring(0,2),challenge);
        LOGGER.log(Level.INFO,"Add challenge to user code = {0}",hashed);

        // hash result and create 'r-s' value
        String hashed2 = hashMe(hashed);
        LOGGER.log(Level.INFO,"Hashed new code r-s = {0}",hashed2);

        if (hashed2.equals(c_hashed)) {
            LOGGER.log(Level.INFO,"Authentificated");
            return "Authentificated successfully!";
        }
        else {
            LOGGER.log(Level.INFO,"Password is incorrect");
            return "Incorrect password";
        }
    }

    private String hashMe(String whatToHash) {
       try {
           return new Hasher().hash(whatToHash);
       } catch (NoSuchAlgorithmException e) {
           LOGGER.log(Level.SEVERE,"Unexpected exception durin hashing");
           return null;
       }
   }


    public void init() throws IOException, FileNotFoundException {
        LOGGER.log(Level.CONFIG, "Server init started");
        users.clear();
        readDataBase();
        LOGGER.log(Level.CONFIG, "Server init ended OK");
    }

    public String updateDB(String id) throws IOException {
        LOGGER.log(Level.INFO,"Open db = {0}",fileName);
        FileWriter fw = new FileWriter(fileName,true);

        String pwd = new PwdGenerator().generate();
        String hashed_pwd = hashMe(pwd);
        LOGGER.log(Level.INFO,"Password = {0} and its hashcode = {1} were generated",new Object[]{pwd,hashed_pwd});

        users.put(id, hashed_pwd);
        LOGGER.log(Level.INFO,"Added user to local db; size now = {0}", users.size());

        fw.write(id + " " + hashed_pwd);
        fw.append("\r\n");
        fw.flush();
        LOGGER.log(Level.INFO,"New user flushed into db");
        return pwd;
    }

    public void endSession() {
        LOGGER.log(Level.INFO,"Session ended OK");
    }
}
