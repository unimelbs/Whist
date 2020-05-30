package core;

import players.InteractablePlayer;
import players.NPCPlayer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Handles the complex creation of core.WhistGame based on the configuration
 * available in whist.properties file
 */
public class GameFactory {
    public static WhistGame instance;
    private static Properties config;
    private static int seed;
    //original.properties
    private static int nbHumanPlayers;
    private static boolean enforceRules;
    //legal.properties
    private static int nbNPCPlayers;
    private static int nbStartCards;
    private static int winningScore;
    //smart.properties
    private static int nbSmartNPCPlayers;
    private static String strategy;
    public static enum Mode {ORIGINAL,LEGAL,SMART};
    public static Mode mode;

    private static void loadConfig() throws IOException
    {
        config = new Properties();
        FileReader inStream = null;
        try {
            inStream = new FileReader("whist.properties");
            config.load(inStream);
            System.out.println("TE10: configurations successfully loaded.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }

    public static WhistGame getInstance() throws IOException
    {
        loadConfig();
        nbSmartNPCPlayers = Integer.parseInt(config.getProperty("nbSmartNPCPlayers"));
        nbNPCPlayers = Integer.parseInt(config.getProperty("nbNPCPlayers"));
        seed = Integer.parseInt(config.getProperty("seed"));
        nbHumanPlayers = Integer.parseInt(config.getProperty("nbHumanPlayers"));
        enforceRules = Boolean.parseBoolean(config.getProperty("enforceRules"));
        nbStartCards = Integer.parseInt(config.getProperty("nbStartCards"));
        winningScore = Integer.parseInt(config.getProperty("winningScore"));
        strategy = config.getProperty("strategy");

        int nbPlayers = nbHumanPlayers+nbNPCPlayers+nbSmartNPCPlayers;

        instance = new WhistGame(nbPlayers, winningScore, nbStartCards, seed, enforceRules);
        return instance;
    }
}
