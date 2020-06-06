package core;

import players.InteractivePlayer;
import players.NPCPlayer;
import players.Player;
import strategies.IGameStrategy;
import strategies.StrategyFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Handles the complex creation of core.WhistGame based on the configuration
 * available in whist.properties file
 */
public class GameFactory {
    private final int MAX_NUMBER_OF_PLAYERS = 4;
    private final String PROPERTIES_FILE_NAME= "whist.properties";
    // properties
    private int seed;
    private int nbHumanPlayers;
    private boolean enforceRules;
    private int nbRandomNPCPlayers;
    private int nbLegalNPCPlayers;
    private int nbStartCards;
    private int winningScore;
    private int nbSmartNPCPlayers;
    private boolean hideCards;
    // variables

    private static GameFactory instance;
    private WhistGame gameInstance;
    private StrategyFactory strategyFactory;
    private Properties config;
    private String originalStrategyString;
    private String legalStrategyString;
    private String smartStrategyString;
    private ArrayList<Player> players;
    private IGameStrategy originalStrategy;
    private IGameStrategy legalStrategy;
    private IGameStrategy smartStrategy;
    private Random random;

    /**
     * Loads game configurations from the provided properties file.
     * @throws IOException
     */
    private void loadConfig() throws IOException
    {
        //Checks the current working directory and changes properties file name accordingly
        String workingDir = System.getProperty("user.dir");
        String lastDirectory = workingDir.substring(workingDir.lastIndexOf(File.separator)+1);
        String configFileName=PROPERTIES_FILE_NAME;
        if (lastDirectory.equals("Whist"))
        {
            configFileName="swen30006"+File.separator+PROPERTIES_FILE_NAME;
        }
        else if(lastDirectory.equals("swen30006"))
        {
            configFileName=PROPERTIES_FILE_NAME;
        }
        //If the working directory is none of the expected values, informing the user and exiting.
        else
        {
            System.out.println("Error: configuration file "+PROPERTIES_FILE_NAME+
                    " is not available in the current working directory "+workingDir+
                    " Please add the file.");
            System.exit(10);
        }
        config = new Properties();
        FileReader inStream = null;
        try {
            inStream = new FileReader(configFileName);
            config.load(inStream);
            nbSmartNPCPlayers = Integer.parseInt(config.getProperty("nbSmartNPCPlayers"));
            nbRandomNPCPlayers = Integer.parseInt(config.getProperty("nbRandomNPCPlayers"));
            nbLegalNPCPlayers = Integer.parseInt(config.getProperty("nbLegalNPCPlayers"));
            seed = Integer.parseInt(config.getProperty("seed"));
            nbHumanPlayers = Integer.parseInt(config.getProperty("nbHumanPlayers"));
            enforceRules = Boolean.parseBoolean(config.getProperty("enforceRules"));
            nbStartCards = Integer.parseInt(config.getProperty("nbStartCards"));
            winningScore = Integer.parseInt(config.getProperty("winningScore"));
            originalStrategyString = config.getProperty("originalStrategy");
            legalStrategyString = config.getProperty("legalStrategy");
            smartStrategyString = config.getProperty("smartStrategy");
            hideCards = Boolean.parseBoolean(config.getProperty("hideCards"));
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

    /**
     * Loads game strategies using StrategyFactory and strategy names as
     * specified in the configuration file.
     */
    private void loadStrategies()
    {
        strategyFactory = StrategyFactory.getInstance();
        originalStrategy = strategyFactory.getStrategy(originalStrategyString);
        legalStrategy = strategyFactory.getStrategy(legalStrategyString);
        smartStrategy = strategyFactory.getStrategy(smartStrategyString);
    }

    /**
     * Creates game players as per the configuration file and as per the project specs.
     */
    private void createGamePlayers()
    {
        players = new ArrayList<Player>();
        int playerId=0;

        //Creation order follows the specs
        //1) Creating interactive (human) players
        for (int i=0;i<nbHumanPlayers;i++)
        {
            InteractivePlayer p = new InteractivePlayer(playerId);
            players.add(p);
            playerId++;
        }
        //2) Creating smart NPC players
        for (int i=0;i<nbSmartNPCPlayers;i++) {
            NPCPlayer p = new NPCPlayer(playerId);
            p.setStrategy(smartStrategy);
            players.add(p);
            playerId++;
        }
        //3) Creating random NPC players
        for (int i = 0; i< nbRandomNPCPlayers; i++) {
            NPCPlayer p = new NPCPlayer(playerId, random);
            p.setStrategy(originalStrategy);
            players.add(p);
            playerId++;
        }
        //4) Creating legal NPC players
        for (int i=0;i<nbLegalNPCPlayers;i++) {
            NPCPlayer p = new NPCPlayer(playerId, random);
            p.setStrategy(legalStrategy);
            players.add(p);
            playerId++;
        }
        for (Player p: players) System.out.println("\t"+p);

    }

    /**
     * Returns a Singleton of GameFactory.
     * @return
     */
    public static GameFactory getInstance()
    {
        if (instance ==null) instance = new GameFactory();
        return instance;
    }

    /**
     * Returns a Singleton of WhistGame.
     * @return
     * @throws IOException
     */
    public WhistGame getWhistGame() throws IOException
    {
        if (gameInstance==null) {
            loadConfig();
            random = new Random(seed);
            loadStrategies();
            createGamePlayers();
            if (players.size()!=MAX_NUMBER_OF_PLAYERS)
            {
                System.out.printf("Number of players (%d) is not the expected number of 4. Exiting..\n",
                        players.size());
                System.exit(10);
            }
            gameInstance = new WhistGame(players.size(), winningScore, nbStartCards, random, enforceRules, hideCards);
            gameInstance.addPlayers(players);
        }
        return gameInstance;
    }
}
