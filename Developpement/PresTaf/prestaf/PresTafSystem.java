//
//  PresTafSystem.java
//  PresTaf Project
//
//  Created by couvreur on Wed Nov 26 2003.
//

package prestaf;

public final class PresTafSystem 
{

    /* Set cache type */
    public static void setDirectCache(int N)
    {
        setCache(new DirectCache(N));
    }

    public static void setCompleteCache()
    {
        setCache(new CompleteCache());
    }

    public static void setDoubleCache(int nbTable, int n)
    {
        setCache(new DoubleDirectCache(nbTable,n));
    }


    private static void setCache(Cache cache)
    {
        MarkedAutomatonCanonical.cache = cache;
    }

    public static void setStoreRate(int rate)
    {
        MarkedAutomatonCanonical.RATE = rate;
    }

    /* table statistics */

    public static long getNbAccess()
    {
        return MarkedAutomatonCanonical.cache.getNbAccess();
    }
    public static long getNbWinAccess()
    {
        return MarkedAutomatonCanonical.cache.getNbWinAccess();
    }

    public static int sharedAutomatonUniqueSize() 
    {
        return SharedAutomaton.uniqueTable.size();
    }

    public static int outputAutomatonUniqueSize() 
    {
        return OutputAutomaton.uniqueTable.size();
    }
    
    public static int cacheSize() 
    {
        return MarkedAutomatonCanonical.cache.size();
    }

    public static int localCacheSize() 
    {
        return MarkedAutomatonCanonical.localCache.size();
    }

    public static void setSimpleExists(boolean b)
    {
        NPF.simpleExists = b;
    }

}
