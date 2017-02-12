//
//  ForallExistsMarkedAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Mon Dec 01 2003.
//

package prestaf;

import java.util.Arrays;

public final class ForallExistsSetMarkedAutomaton implements MarkedAutomaton
{
    MarkedSharedAutomaton parameter;
    boolean type; // type ? MarkedAutomaton.EXISTS : MarkedAutomaton.FORALL;
    boolean[] table;
    int index;
    
    private ForallExistsSetMarkedAutomaton(MarkedSharedAutomaton automaton, boolean[] table, int index, boolean type)
    {
        parameter = automaton;
        this.type = type;
        this.index = index;
        this.table = table;
    }
    
    public static MarkedAutomaton create(MarkedSharedAutomaton automaton, boolean[] table, boolean type)
    {
        if (automaton == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (automaton == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else if (table.length == 0)
            return automaton;
            
        {
            boolean test = table[0];
            int i = 0;
            for (;i<table.length && table[i] == test; i++);
            if (i == table.length) {
                if (test)
                    return type ? MarkedSharedAutomaton.one : MarkedSharedAutomaton.zero; 
                else
                    return automaton;
            }
        }
        
        boolean[] t = new boolean[table.length];
        System.arraycopy(table, 0, t, 0, table.length);
        ForallExistsSetMarkedAutomaton A = new ForallExistsSetMarkedAutomaton(automaton, t, 0, type);
        A.advance();

        if (A.parameter == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (A.parameter == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else
            return A;
    }
    
    private void advance()
    {
        int alphabetSize = parameter.getAlphabetSize();
        
        while (table[index]) {
            MarkedSharedAutomaton res;
            if (type) {
                res = MarkedSharedAutomaton.zero;
                for (int a=0; a <alphabetSize; a++)
                    res = res.or((MarkedSharedAutomaton) parameter.succ(a));
            }
            else  {
                res = MarkedSharedAutomaton.one;
                for (int a=0; a <alphabetSize; a++)
                    res = res.and((MarkedSharedAutomaton) parameter.succ(a));
            }
            parameter = res;
            index = (index+1)%(table.length);
        }
    }

    public static MarkedAutomaton create(MarkedSharedAutomaton automaton, boolean[] table, int index, boolean type)
    {
                    
        ForallExistsSetMarkedAutomaton A = new ForallExistsSetMarkedAutomaton(automaton, table, index, type);
        A.advance();

        if (A.parameter == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (A.parameter == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else
            return A;
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
        return create((MarkedSharedAutomaton) parameter.succ(a), table, (index+1) % table.length,type);
    }

    public int getType()
    {
        return type ? MarkedAutomaton.EXISTS : MarkedAutomaton.FORALL;
    }

    /* equal */
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            ForallExistsSetMarkedAutomaton a = (ForallExistsSetMarkedAutomaton) o;
            return parameter == a.parameter && type == a.type && index == a.index && Arrays.equals(table,a.table); 
        }
    }

    public int hashCode()
    {
        int res = 0;
        for (int i=0;i<table.length; i++)
            res = 8677*res + 6619 + (table[i]?3:7);
            
        return res + 6619 + 8677 *(index + 6619 + 8677 *(6619 +(type?17:7) + 8677 *(6619 + 8677*(parameter.hashCode() ))));
    }
}
