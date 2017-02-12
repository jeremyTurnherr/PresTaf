//
//  DirectCache.java
//  PresTafProject
//
//  Created by couvreur on Wed Nov 26 2003.
//  Copyright (c) 2003 __MyCompanyName__. All rights reserved.
//

package prestaf;

public final class DoubleDirectCache implements Cache
{
    static int N;
    int size = 0;
    static int nbTable = 2;

    int[][] count = new int[nbTable][N];
    MarkedAutomaton[][] key = new MarkedAutomaton[nbTable][N];
    MarkedSharedAutomaton[][] value = new MarkedSharedAutomaton[nbTable][N];

    DoubleDirectCache(int nbTable,int n)
    {
        this.nbTable = nbTable;
        N = n;
        count = new int[nbTable][N];
        key = new MarkedAutomaton[nbTable][N];
        value = new MarkedSharedAutomaton[nbTable][N];
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
                
            int t=0;
            while (t < nbTable && key[t][h] == null)
                t++;
                
            if (t>0)
                t--;
                
            if (key[t][h] == null)
                size ++;
                
            key[t][h] = A;
            value[t][h] = res;
        }
    }
    
    public MarkedSharedAutomaton get(MarkedAutomaton A) 
    {
        MarkedSharedAutomaton res;
        nbAccess ++;
        if (MarkedSharedAutomaton.isShared(A))
            return (MarkedSharedAutomaton) A;
        else {
            int h = A.hashCode()%N;
            if (h<0)
                h+= N;
                
            for (int t=nbTable-1; t>=0 && key[t][h] != null ;t--) {
                if (A.equals(key[t][h])) {
                    nbWinAccess ++;
                    count[t][h] ++;
                    
                    res = value[t][h];
                    
                    while (t<nbTable-1 && count[t][h]>=count[t+1][h]) {
                        moveUp(t,h);
                        t++;
                    }
                    
                    return res;
                }
            }
            return null;
        }
    }
    
    private void moveUp(int t, int h)
    {
        int tmp1 = count[t][h];
        count[t][h] = count[t+1][h];
        count[t+1][h] = tmp1;

        MarkedAutomaton tmp2 = key[t][h];
        key[t][h] = key[t+1][h]; 
        key[t+1][h] = tmp2;

        MarkedSharedAutomaton tmp3 = value[t][h];
        value[t][h] = value[t+1][h]; 
        value[t+1][h] = tmp3;
    }
}
