//
//  ForallExistsMarkedAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Mon Dec 01 2003.
//

package prestaf;

public final class ForallExistsMarkedAutomaton implements MarkedAutomaton
{
    MarkedSharedAutomaton parameter;
    boolean type;
    int index;
    int modulo;
    
    private ForallExistsMarkedAutomaton(MarkedSharedAutomaton automaton,int i, int n, boolean type)
    {
        parameter = automaton;
        this.type = type;
        index = i;
        modulo =n;
    }
    
    public static MarkedAutomaton create(MarkedSharedAutomaton automaton,int index, int modulo, boolean type)
    {
        if (automaton == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (automaton == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else if (modulo<2) {
            return type ? MarkedSharedAutomaton.one : MarkedSharedAutomaton.zero; 
        }
        else if (index == 0)
            return create(zeroIndexCase(automaton,type),modulo-1,modulo,type);
        else {
            return new ForallExistsMarkedAutomaton(automaton,index,modulo,type);
        }
    }
    
    public int getAlphabetSize()
    {
        return parameter.getAlphabetSize();
    }
    
    public boolean isFinal()
    {
        return parameter.isFinal();
    }
    
    public MarkedAutomaton succ(int a)
    {
        // index >0
        return create(parameter.sharedSucc(a), index-1,modulo,type);
    }

    public int getType()
    {
        return type ? MarkedAutomaton.EXISTS : MarkedAutomaton.FORALL;
    }

    private static MarkedSharedAutomaton zeroIndexCase(MarkedSharedAutomaton A,boolean type) 
    {
        int alphabetSize = A.getAlphabetSize();
        MarkedSharedAutomaton res;
        if (type) {
            res = MarkedSharedAutomaton.zero;
            for (int a=0; a <alphabetSize; a++)
                res = res.or(A.sharedSucc(a));
        }
        else  {
            res = MarkedSharedAutomaton.one;
            for (int a=0; a <alphabetSize; a++)
                res = res.and(A.sharedSucc(a));
        }
        return res;
    }

    /* equal */
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            ForallExistsMarkedAutomaton a = (ForallExistsMarkedAutomaton) o;
            return parameter == a.parameter && type == a.type && index == a.index && modulo == a.modulo; 
        }
    }

    public int hashCode()
    {
        return modulo+ 6619 + 8677 *(index + 6619 + 8677 *(6619 +(type?17:7) + 8677 *(6619 + 8677*(parameter.hashCode() ))));
    }
    
    
}
