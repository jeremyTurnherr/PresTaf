//
//  MarkedAutomaton.java
//  PresTaf Project
//
//  Created by couvreur on Tue Nov 25 2003.
//

package prestaf;

public interface MarkedAutomaton 
{
    public int getAlphabetSize();
    public boolean isFinal();
    public MarkedAutomaton succ(int a);
    
    public static int NORMAL = 0; 
    public static int EXISTS = 1; 
    public static int FORALL = 2; 

    public int getType();
}
