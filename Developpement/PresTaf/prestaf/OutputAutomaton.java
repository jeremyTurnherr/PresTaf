//
//  OutputAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Tue Nov 25 2003.
//

package prestaf;

import java.util.WeakHashMap;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public final class OutputAutomaton 
{
    final int alphabetSize;
    final int nbLocalStates;
    final int nbOutputStates;
    private int[] succ;
    boolean[] isFinal;
    
    static WeakHashMap uniqueTable = new WeakHashMap();
    
    OutputAutomaton(int[] succ, boolean[] isFinal, int nbOutputStates)
    {
        this.succ = succ;
        this.isFinal = isFinal;
        nbLocalStates = isFinal.length;
        alphabetSize = succ.length/nbLocalStates;
        this.nbOutputStates = nbOutputStates;
    }
    
    static OutputAutomaton create(OutputAutomaton outputAutomaton)
    {
        Object o = uniqueTable.get(outputAutomaton);
        if (o == null) {
            uniqueTable.put(outputAutomaton, new WeakReference(outputAutomaton));
            return outputAutomaton;
        }
        else
            return (OutputAutomaton) ((WeakReference) o).get();
    }

    /* accessor */
    int succ(int p, int a) 
    {
        return succ[a+alphabetSize*p];
    }
    
    /* hash, equals*/
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        OutputAutomaton t = (OutputAutomaton) o;
        if (nbOutputStates != t.nbOutputStates || nbLocalStates != t.nbLocalStates || alphabetSize != t.alphabetSize)
            return false;
        else {
            return Arrays.equals(isFinal,t.isFinal) && Arrays.equals(succ,t.succ);
        }
    }

    int superhashCode()
    {
        return super.hashCode();
    }
    
    public int hashCode()
    {
        int res = alphabetSize + 7477*nbLocalStates + nbOutputStates + 5623;
                
        for (int p = 0; p< nbLocalStates; p++)
            res = 7477*res+ (isFinal[p]?1:0);
            
        for (int i = 0; i< succ.length; i++)
            res = 7477*res + succ[i] + 5623;
            
        return res;
    }

    public String toString()
    {
        StringBuffer res = new StringBuffer();
        res.append("alphabetSize = " + alphabetSize + "\n");
        res.append("nbLocalStates = " + nbLocalStates + "\n");
        res.append("nbOutputStates = " + nbOutputStates + "\n");

        res.append("isFinal = ");
        for (int i=0; i< nbLocalStates; i++)
            res.append( (isFinal[i]?1:0)+ " ");
        res.append("\n");
        
        res.append("succ = \n");
        for (int i=0; i< nbLocalStates; i++) {
            res.append("\t succ[" + i + "] = ");
            for (int a=0; a< alphabetSize; a++) 
                res.append(succ(i,a) + " ");
        }
        res.append("\n");
      
        return res.toString();
    }
}
