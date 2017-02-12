//
//  CompleteCache.java
//  PresTaf Project
//
//  Created by couvreur on Wed Nov 26 2003.
//

package prestaf;

import java.util.HashMap;

public final class CompleteCache implements Cache
{
    HashMap cache = new HashMap();

    public void put(MarkedAutomaton A, MarkedSharedAutomaton res)
    {
        if (!MarkedSharedAutomaton.isShared(A))
            cache.put(A,res);
    }

    public int size()
    {
        return cache.size();
    }
    
    long nbAccess = 0;
    long nbWinAccess = 0;
    
    public long getNbAccess()
    {
        return nbAccess;
    }
    public long getNbWinAccess()
    {
        return nbWinAccess;
    }

    
    public MarkedSharedAutomaton get(MarkedAutomaton A) {
        MarkedSharedAutomaton res;
        nbAccess++;
        if (MarkedSharedAutomaton.isShared(A)) {
            nbWinAccess++;
            return (MarkedSharedAutomaton) A;
        }
        else {
            res =  (MarkedSharedAutomaton) cache.get(A);
            if (res != null)
                nbWinAccess++;
            return res;
        }
    }

}
