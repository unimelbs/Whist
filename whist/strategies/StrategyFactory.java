package strategies;

/**
 * A Factory used to create game strategy object.
 */
public class StrategyFactory {

    private static StrategyFactory instance;

    /**
     * Uses Singleton pattern to create one instance of StrategyFactory.
     * @return
     */
    public static StrategyFactory getInstance()
    {
        if (instance==null) instance = new StrategyFactory();
        return instance;
    }

    /**
     * Returns Strategy object given its name. Implements a data-driven protected variation
     * to create strategies objects at run time.
     * @param strategy
     * @return
     */
    public IGameStrategy getStrategy(String strategy)
    {
        try
        {
            if(strategy!=null) return (IGameStrategy) Class.forName(strategy).getDeclaredConstructor().newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
