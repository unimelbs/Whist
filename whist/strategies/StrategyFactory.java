package strategies;

public class StrategyFactory {
    public static StrategyFactory instance;

    private String defaultStrategy="original";
    public StrategyFactory getInstance(String strategy)
    {
        if (instance==null) instance = new StrategyFactory();

        return instance;
    }

    private IGameStrategy getStrategy(String strategy) throws ClassNotFoundException
    {

        try
        {
            if(strategy!=null) return (IGameStrategy) Class.forName(strategy).newInstance();
        }
        catch (Exception e){

        }
        return null;
    }

}
