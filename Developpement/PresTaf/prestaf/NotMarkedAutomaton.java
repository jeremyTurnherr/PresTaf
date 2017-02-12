//
//  NotMarkedAutomaton.java
//  PresTaf Project
//
//  Created by couvreur on Sun Nov 30 2003.
//

package prestaf;

public final class NotMarkedAutomaton implements MarkedAutomaton 
{
    MarkedSharedAutomaton parameter;
    
    private NotMarkedAutomaton(MarkedSharedAutomaton automaton)
    {
        parameter = automaton;
    }
    
    public static MarkedAutomaton create(MarkedSharedAutomaton automaton)
    {
        if (automaton == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.one;
        else if (automaton == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.zero;
        else
        return new NotMarkedAutomaton(automaton);
    }
    
    public int getAlphabetSize()
    {
        return parameter.getAlphabetSize();
    }
    
    public boolean isFinal()
    {
        return !parameter.isFinal();
    }
    
    public MarkedAutomaton succ(int a)
    {
        return create(parameter.sharedSucc(a));
    }
    
    public int getType()
    {
        return MarkedAutomaton.NORMAL;
    }

    /* equal and compare */
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            NotMarkedAutomaton a = (NotMarkedAutomaton) o;
            return parameter == a.parameter; 
        }
    }

    public int hashCode()
    {
        return 719 + 1061*parameter.hashCode();
    }
}
