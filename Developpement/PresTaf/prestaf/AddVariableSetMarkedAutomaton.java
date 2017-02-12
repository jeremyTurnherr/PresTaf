//
//  AddVariableSetMarkedAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Wed Dec 03 2003.
//

package prestaf;

import java.util.Arrays;

public final class AddVariableSetMarkedAutomaton implements MarkedAutomaton
{
    MarkedSharedAutomaton automaton;
    boolean[] variable;
    int index;
    int nbVariable;
    
    private AddVariableSetMarkedAutomaton(MarkedSharedAutomaton A, boolean[] v)
    {
        automaton = A;
        variable = v;
        index = 0;
        nbVariable = v.length;
    }
    
    static MarkedAutomaton create(MarkedSharedAutomaton A, boolean[] v)
    {
        if (A.equals(MarkedSharedAutomaton.one))
            return MarkedSharedAutomaton.one;
        else if (A.equals(MarkedSharedAutomaton.zero))
            return MarkedSharedAutomaton.zero;
        else
            return new AddVariableSetMarkedAutomaton(A,v);
    }

    private AddVariableSetMarkedAutomaton(MarkedSharedAutomaton A, boolean[] v, int i)
    {
        automaton = A;
        variable = v;
        index = i;
        nbVariable = v.length;
        this.index = index;
    }

    private static MarkedAutomaton create(MarkedSharedAutomaton A, boolean[] v, int i)
    {
        if (A.equals(MarkedSharedAutomaton.one))
            return MarkedSharedAutomaton.one;
        else if (A.equals(MarkedSharedAutomaton.zero))
            return MarkedSharedAutomaton.zero;
        else
            return new AddVariableSetMarkedAutomaton(A,v,i);
    }
    
    /* automata interface */
    public int getType()
    {
        return MarkedAutomaton.NORMAL;
    }

    public int getAlphabetSize()
    {
        return 2;
    }
    
    public boolean isFinal()
    {
        return automaton.isFinal();
    }
        
    public MarkedAutomaton succ(int a)
    {
        if (variable[index])
            return create(automaton,variable,(index+1)%nbVariable);
        else
            return create(automaton.sharedSucc(a),variable,(index+1)%nbVariable);
    }

    /* Equals and hashCode */
        
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            AddVariableSetMarkedAutomaton c = (AddVariableSetMarkedAutomaton) o;
            return automaton.equals(c.automaton) && Arrays.equals(variable,c.variable) && index == c.index;
        }	
    }

    public int hashCode()
    {
        int res = 3719 * (automaton.hashCode() + 5009*index + 6553) + 1949;
        for (int i=0; i<nbVariable; i++)
            if (variable[i])
                res = 9391*res + 5347;
            else
                res = 8377*res+ 947;
        return res;
    }
}
