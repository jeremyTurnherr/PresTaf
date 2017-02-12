//
//  Cache.java
//  PresTaf Project
//
//  Created by couvreur on Wed Nov 26 2003.
//

package prestaf;

public interface Cache 
{
    void put(MarkedAutomaton A, MarkedSharedAutomaton res);
    MarkedSharedAutomaton get(MarkedAutomaton A);
    int size();
    long getNbAccess();
    long getNbWinAccess();
}
