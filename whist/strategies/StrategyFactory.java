package strategies;

public class StrategyFactory {
    public static StrategyFactory instance;
    public static StrategyFactory getInstance()
    {
        if (instance==null) instance = new StrategyFactory();
        return instance;
    }

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
