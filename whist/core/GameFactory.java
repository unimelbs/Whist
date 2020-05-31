package core;

import players.InteractablePlayer;
import players.NPCPlayer;
import players.Player;
import strategies.IGameStrategy;
import strategies.StrategyFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Handles the complex creation of core.WhistGame based on the configuration
 * available in whist.properties file
 */
public class GameFactory {
    public static WhistGame instance;
    private static StrategyFactory strategyFactory;
    private static Properties config;
    private static int seed;
    //original.properties
    private static int nbHumanPlayers;
    private static boolean enforceRules;
    //legal.properties
    private static int nbNPCPlayers;
    private static int nbLegalNPCPlayers;
    private static int nbStartCards;
    private static int winningScore;
    //smart.properties
    private static int nbSmartNPCPlayers;
    private static String originalStrategyString;
    private static String legalStrategyString;
    private static String smartStrategyString;
    public enum Mode {ORIGINAL,LEGAL,SMART};
    //public static Mode mode;
    private static ArrayList<Player> players;
    private static IGameStrategy originalStrategy;
    private static IGameStrategy legalStrategy;
    private static IGameStrategy smartStrategy;


    private static void loadConfig() throws IOException
    {
        config = new Properties();
        FileReader inStream = null;
        try {
            inStream = new FileReader("whist.properties");
            config.load(inStream);
            nbSmartNPCPlayers = Integer.parseInt(config.getProperty("nbSmartNPCPlayers"));
            nbNPCPlayers = Integer.parseInt(config.getProperty("nbNPCPlayers"));
            nbLegalNPCPlayers = Integer.parseInt(config.getProperty("nbLegalNPCPlayers"));
            seed = Integer.parseInt(config.getProperty("seed"));
            nbHumanPlayers = Integer.parseInt(config.getProperty("nbHumanPlayers"));
            enforceRules = Boolean.parseBoolean(config.getProperty("enforceRules"));
            nbStartCards = Integer.parseInt(config.getProperty("nbStartCards"));
            winningScore = Integer.parseInt(config.getProperty("winningScore"));
            originalStrategyString = config.getProperty("originalStrategy");
            legalStrategyString = config.getProperty("legalStrategy");
            smartStrategyString = config.getProperty("smartStrategy");
            System.out.println("Configurations successfully loaded.");
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

    private static void loadStrategies()
    {
        strategyFactory = StrategyFactory.getInstance();
        originalStrategy = strategyFactory.getStrategy(originalStrategyString);
        legalStrategy = strategyFactory.getStrategy(legalStrategyString);
        smartStrategy = strategyFactory.getStrategy(smartStrategyString);
    }

    private static void createGamePlayers()
    {
        players = new ArrayList<Player>();
        int playerId=0;
        //Creating interactive (human) players
        for (int i=0;i<nbHumanPlayers;i++)
        {
            InteractablePlayer p = new InteractablePlayer(playerId);
            players.add(p);
            playerId++;
        }
        //Creating random NPC players
        for (int i=0;i<nbNPCPlayers;i++) {
            NPCPlayer p = new NPCPlayer(playerId);
            p.setStrategy(originalStrategy);
            players.add(p);
            playerId++;
        }
        //Creating legal NPC players
        for (int i=0;i<nbLegalNPCPlayers;i++) {
            NPCPlayer p = new NPCPlayer(playerId);
            p.setStrategy(legalStrategy);
            players.add(p);
            playerId++;
        }
        //Creating smart NPC players
        for (int i=0;i<nbSmartNPCPlayers;i++) {
            NPCPlayer p = new NPCPlayer(playerId);
            p.setStrategy(smartStrategy);
            players.add(p);
            playerId++;
        }
    }
    public static WhistGame getInstance() throws IOException
    {
        loadConfig();
        loadStrategies();
        createGamePlayers();
        if (players.size()!=4)
        {
            System.out.printf("Number of players (%d) is greater than the allowed maximum of 4. Exiting..\n",
                    players.size());
            System.exit(10);
        }

        assert(players.size()==4);
        instance = new WhistGame(players.size(), winningScore, nbStartCards, seed, enforceRules);
        System.out.printf("Original: %s, Legal: %s, Smart: %s",originalStrategy,legalStrategy,smartStrategy);
        instance.addPlayers(players);
        return instance;
    }
}
