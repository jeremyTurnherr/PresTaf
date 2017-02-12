//
//  AndMarkedAutomaton.java
//  PresTaf Project
//
//  Created by couvreur on Sun Nov 30 2003.
//

package prestaf;

public final class AndMarkedAutomaton implements MarkedAutomaton 
{
    MarkedSharedAutomaton parameter1;
    MarkedSharedAutomaton parameter2;
    
    private AndMarkedAutomaton(MarkedSharedAutomaton automaton1, MarkedSharedAutomaton automaton2)
    {
        parameter1 = automaton1;
        parameter2 = automaton2;
    }
    
    public static MarkedAutomaton create(MarkedSharedAutomaton automaton1, MarkedSharedAutomaton automaton2)
    {
        if (automaton1 == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (automaton1 == MarkedSharedAutomaton.one)
            return automaton2;
        else if (automaton2 == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (automaton2 == MarkedSharedAutomaton.one)
            return automaton1;
        else if (automaton1 == automaton2)
            return automaton1;
//        else if (automaton1.compareTo(automaton2)<0)
        else if (automaton1.hashCode()<automaton2.hashCode())
            return new AndMarkedAutomaton(automaton1,automaton2);
        else
            return new AndMarkedAutomaton(automaton2,automaton1);
    }
    
    public int getAlphabetSize()
    {
        return parameter1.getAlphabetSize();
    }
    
    public boolean isFinal()
    {
        return parameter1.isFinal() && parameter2.isFinal();
    }
    
    public MarkedAutomaton succ(int a)
    {
        return create(parameter1.sharedSucc(a), parameter2.sharedSucc(a));
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
            AndMarkedAutomaton a = (AndMarkedAutomaton) o;
            return parameter1 == a.parameter1 && parameter2 == a.parameter2; 
        }
    }

    public int hashCode()
    {
        return 3719 + 4073*(parameter1.hashCode()+ 3719 + 4073*parameter2.hashCode());
    }

}
