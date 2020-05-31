package strategies;

public class StrategyFactory {
    public static StrategyFactory instance;
    private String defaultStrategy="original";
    public static StrategyFactory getInstance()
    {
        if (instance==null) instance = new StrategyFactory();
        return instance;
    }

    public IGameStrategy getStrategy(String strategy)
    {
        try
        {
            if(strategy!=null) return (IGameStrategy) Class.forName(strategy).newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
