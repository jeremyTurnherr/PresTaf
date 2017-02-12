//
//  ImplyMarkedAutomaton.java
//  PresTaf Project
//
//  Created by couvreur on Sun Nov 30 2003.
//

package prestaf;

public final class ImplyMarkedAutomaton implements MarkedAutomaton 
{
    MarkedSharedAutomaton parameter1;
    MarkedSharedAutomaton parameter2;
    
    private ImplyMarkedAutomaton(MarkedSharedAutomaton automaton1, MarkedSharedAutomaton automaton2)
    {
        parameter1 = automaton1;
        parameter2 = automaton2;
    }
    
    public static MarkedAutomaton create(MarkedSharedAutomaton automaton1, MarkedSharedAutomaton automaton2)
    {
        if (automaton1 == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.one;
        else if (automaton1 == MarkedSharedAutomaton.one)
            return automaton2;
        else if (automaton2 == MarkedSharedAutomaton.zero)
            return NotMarkedAutomaton.create(automaton1);
        else if (automaton2 == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else if (automaton1 == automaton2)
            return MarkedSharedAutomaton.one;
        else 
            return new ImplyMarkedAutomaton(automaton1,automaton2);
    }
    
    public int getAlphabetSize()
    {
        return parameter1.getAlphabetSize();
    }
    
    public boolean isFinal()
    {
        return !parameter1.isFinal() || parameter2.isFinal();
    }
    
    public MarkedAutomaton succ(int a)
    {
        return create(parameter1.sharedSucc(a),parameter2.sharedSucc(a));
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
            ImplyMarkedAutomaton a = (ImplyMarkedAutomaton) o;
            return parameter1 == a.parameter1 && parameter2 == a.parameter2; 
        }
    }

    public int hashCode()
    {
        return 1871 + 3911*parameter1.hashCode()+ 571*parameter2.hashCode();
    }

}
