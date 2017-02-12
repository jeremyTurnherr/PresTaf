//
//  ForallExistsMarkedAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Mon Dec 01 2003.
//

package prestaf;

import java.util.Arrays;

public final class ForallExists2MarkedAutomaton implements MarkedAutomaton
{
    MarkedSharedAutomaton[] parameter;
    boolean type;
    int index;
    int modulo;
    
    private ForallExists2MarkedAutomaton(MarkedSharedAutomaton[] automaton,int i, int n, boolean type)
    {
        parameter = automaton;
        this.type = type;
        index = i;
        modulo =n;
    }

    static MarkedAutomaton create(MarkedSharedAutomaton automaton,int index, int modulo, boolean type)
    {
        if (automaton == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (automaton == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else if (modulo<2) {
            return type ? MarkedSharedAutomaton.one : MarkedSharedAutomaton.zero; 
        }
        else
            return create(new MarkedSharedAutomaton[]{automaton}, index, modulo, type, automaton.getAlphabetSize());
    }
    
    private static MarkedAutomaton create(MarkedSharedAutomaton[] automaton,int index, int modulo, boolean type, int alphabetSize)
    {
        if (automaton.length == 0)
            return type ? MarkedSharedAutomaton.one : MarkedSharedAutomaton.zero;
        else if (automaton[0] == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else if (automaton[0] == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (index == 0)
            return create(zeroIndexCase(automaton,type,alphabetSize),modulo-1,modulo,type,alphabetSize);
        else 
            return new ForallExists2MarkedAutomaton(automaton,index,modulo,type);
    }
    
    public int getAlphabetSize()
    {
        return parameter[0].getAlphabetSize();
    }
    
    public boolean isFinal()
    {
        for (int i=0; i< parameter.length; i++)
            if (type == parameter[i].isFinal())
                return type;

        return !type;
    }
    
    public MarkedAutomaton succ(int a)
    {
        // index >0
        MarkedSharedAutomaton[] succ_a = new MarkedSharedAutomaton[parameter.length];
        for (int i=0; i< parameter.length; i++)
            succ_a[i] = (MarkedSharedAutomaton) parameter[i].succ(a);
        
        return create(simplify(succ_a,type), index-1,modulo,type,parameter[0].getAlphabetSize());
    }

    public int getType()
    {
        return type ? MarkedAutomaton.EXISTS : MarkedAutomaton.FORALL;
    }

    private static MarkedSharedAutomaton[] zeroIndexCase(MarkedSharedAutomaton[] p, boolean type, int alphabetSize) 
    {
        MarkedSharedAutomaton[] succ = new MarkedSharedAutomaton[p.length*alphabetSize];

        for (int i=0; i<p.length; i++)
        for (int a=0; a<alphabetSize; a++)
            succ[alphabetSize*i+a] = (MarkedSharedAutomaton) p[i].succ(a);
        return simplify(succ, type);
    }

    /* equal */
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            ForallExists2MarkedAutomaton a = (ForallExists2MarkedAutomaton) o;
            return type == a.type && index == a.index && modulo == a.modulo && Arrays.equals(parameter,a.parameter); 
        }
    }

    public int hashCode()
    {
        int res = 0;
        for (int i=0; i<parameter.length; i++)
            res = res * 8677 + 6619 + parameter[i].hashCode();
        
        return modulo+ 6619 + 8677 *(index + 6619 + 8677 *(6619 +(type?17:7) + 8677 *(6619 + 8677*(res ))));
    }
    
 
    private static MarkedSharedAutomaton[] simplify(MarkedSharedAutomaton[] p, boolean type)
    {
        assert p.length >= 1;
        Arrays.sort(p);
        
        if (type) {
            if (Arrays.binarySearch(p, MarkedSharedAutomaton.one) >=0)
                return new MarkedSharedAutomaton[0];
        }
        else {
            if (Arrays.binarySearch(p, MarkedSharedAutomaton.zero) >=0)
                return new MarkedSharedAutomaton[0];
        }
        
        int k = 0;
        for (int i=0;i<p.length;i++) {
            if (p[i] != MarkedSharedAutomaton.one && p[i] != MarkedSharedAutomaton.zero && (i == 0 || p[i] != p[i-1] )) {
                p[k] = p[i];
                k++;
            }
        }
        if (k==0)
            k = 1;
        
        MarkedSharedAutomaton[] res = new MarkedSharedAutomaton[k];
        System.arraycopy(p,0,res,0,k);
        return res;
    }
}
