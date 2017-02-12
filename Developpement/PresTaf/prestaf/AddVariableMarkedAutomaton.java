//
//  AddVariableMarkedAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Mon Dec 01 2003.
//

package prestaf;

public final class AddVariableMarkedAutomaton implements MarkedAutomaton
{
    MarkedSharedAutomaton parameter;
    int index;
    int modulo;
    
    private AddVariableMarkedAutomaton(MarkedSharedAutomaton automaton,int i, int n)
    {
        parameter = automaton;
        index = i;
        modulo =n;
    }
    
    public static MarkedAutomaton create(MarkedSharedAutomaton automaton,int i, int n)
    {
        if (automaton == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (automaton == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else
            return new AddVariableMarkedAutomaton(automaton,i,n);
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
        if (index == 0)
            return create(parameter, modulo,modulo);
        else {
            return create((MarkedSharedAutomaton) parameter.succ(a),index - 1,modulo);
        }
    }

    public int getType()
    {
        return MarkedAutomaton.NORMAL;
    }

    /* equals and hash */
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            AddVariableMarkedAutomaton a = (AddVariableMarkedAutomaton) o;
            return parameter == a.parameter && index == a.index && modulo == a.modulo; 
        }
    }

    public int hashCode()
    {
        return modulo+ 983 + 3011 *(index + 983 + 3011 *(6619  + 3011 *(983 + 3011*(parameter.hashCode() ))));
    }
}
