//
//  DirectCache.java
//  PresTafProject
//
//  Created by couvreur on Wed Nov 26 2003.
//  Copyright (c) 2003 __MyCompanyName__. All rights reserved.
//

package prestaf;

public final class DirectCache implements Cache
{
    static int N;
    int size = 0;

    MarkedAutomaton[] key = new MarkedAutomaton[N];
    MarkedSharedAutomaton[] value = new MarkedSharedAutomaton[N];

    DirectCache(int n)
    {
        N = n;
        key = new MarkedAutomaton[N];
        value = new MarkedSharedAutomaton[N];
    }

    public int size()
    {
        return size;
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


    public void put(MarkedAutomaton A, MarkedSharedAutomaton res)
    {
        if (!MarkedSharedAutomaton.isShared(A)) {
            int h = A.hashCode()%N;
            if (h<0)
                h+= N;
            if (key[h] == null)
                size ++;                            
            key[h] = A;
            value[h] = res;
        }
    }
    
    public MarkedSharedAutomaton get(MarkedAutomaton A) {
        MarkedSharedAutomaton res;
        nbAccess ++;
        if (MarkedSharedAutomaton.isShared(A))
            return (MarkedSharedAutomaton) A;
        else {
            int h = A.hashCode()%N;
            if (h<0)
                h+= N;
                
            if (key[h] != null && A.equals(key[h])) {
                nbWinAccess ++;
                return value[h];
            }
            else
                return null;
        }
    }
}
