//
//  MarkedSharedAutomaton.java
//  PresTaf Project
//
//  Created by couvreur on Tue Nov 25 2003.
//

package prestaf;

import java.util.HashMap;
import java.util.ArrayList;

public final class MarkedSharedAutomaton implements MarkedAutomaton, Comparable
{
    public SharedAutomaton sharedAutomaton;
    public int initial;
    
    final static public MarkedSharedAutomaton zero = buildZeroOne(false);
    final static public MarkedSharedAutomaton one = buildZeroOne(true);
    
    MarkedSharedAutomaton(SharedAutomaton sharedAutomaton, int initial)
    {
        this.sharedAutomaton = sharedAutomaton;
        this.initial = initial;
    }
    
    /* MarkedAutomaton interface */
    
    public int getAlphabetSize()
    {
        return sharedAutomaton.outputAutomaton.alphabetSize;
    }
    
    public boolean isFinal()
    {
        return sharedAutomaton.outputAutomaton.isFinal[initial];
    }


    public MarkedAutomaton succ(int a)
    {
        int q = sharedAutomaton.outputAutomaton.succ(initial,a);
        if (q >=0)
            return sharedAutomaton.uniqueMarkedSharedAutomaton[q];
        else
            return sharedAutomaton.bindFunction[-1-q];
    }

    public MarkedSharedAutomaton sharedSucc(int a)
    {
        int q = sharedAutomaton.outputAutomaton.succ(initial,a);
        if (q >=0)
            return sharedAutomaton.uniqueMarkedSharedAutomaton[q];
        else
            return sharedAutomaton.bindFunction[-1-q];
    }

    
    public int getType()
    {
        return MarkedAutomaton.NORMAL;
    }

    /* zero, one */

    static MarkedSharedAutomaton buildZeroOne(boolean b)
    {
        OutputAutomaton outputAutomaton = OutputAutomaton.create(new OutputAutomaton(new int[]{}, new boolean[]{b},0));
        SharedAutomaton sharedAutomaton = SharedAutomaton.create(new SharedAutomaton(outputAutomaton, new MarkedSharedAutomaton[]{}));
        return sharedAutomaton.uniqueMarkedSharedAutomaton[0];
    }
    
    /* operation */
    
    public MarkedSharedAutomaton not()
    {
        return canonical(NotMarkedAutomaton.create(this));
    }

    public MarkedSharedAutomaton and(MarkedSharedAutomaton A)
    {
        return canonical(AndMarkedAutomaton.create(this,A));
    }

    public MarkedSharedAutomaton or(MarkedSharedAutomaton A)
    {
        return canonical(OrMarkedAutomaton.create(this,A));
    }

    public MarkedSharedAutomaton imply(MarkedSharedAutomaton A)
    {
        return canonical(ImplyMarkedAutomaton.create(this,A));
    }

    public MarkedSharedAutomaton equiv(MarkedSharedAutomaton A)
    {
        return canonical(EquivMarkedAutomaton.create(this,A));
    }

    public MarkedSharedAutomaton exists2(int index, int modulo)
    {
        return canonical(ForallExists2MarkedAutomaton.create(this,index,modulo,true));
    }
    
    public MarkedSharedAutomaton exists(int index, int modulo)
    {
        return canonical(ForallExistsMarkedAutomaton.create(this,index,modulo,true));
    }

    public MarkedSharedAutomaton forall2(int index, int modulo)
    {
        return canonical(ForallExists2MarkedAutomaton.create(this,index,modulo,false));
    }

    public MarkedSharedAutomaton forall(int index, int modulo)
    {
        return canonical(ForallExistsMarkedAutomaton.create(this,index,modulo,false));
    }

    public MarkedSharedAutomaton exists(boolean[] table)
    {
        return canonical(ForallExistsSetMarkedAutomaton.create(this,table,true));
    }

    public MarkedSharedAutomaton forall(boolean[] table)
    {
        return canonical(ForallExistsSetMarkedAutomaton.create(this,table,false));
    }

    
    public MarkedSharedAutomaton addVariable(int index, int modulo)
    {
        return canonical(AddVariableMarkedAutomaton.create(this,index,modulo));
    }
    
    public MarkedSharedAutomaton addVariable(boolean[] tab)
    {
        return canonical(AddVariableSetMarkedAutomaton.create(this,tab));
    }

    /* Comparable interface */
    
    public int compareTo(Object o)
    {        
        if (o.getClass() != getClass())
            return getClass().hashCode() - o.getClass().hashCode() ;
        else
        {
            MarkedSharedAutomaton A = (MarkedSharedAutomaton) o;
            if (A.sharedAutomaton.deapth != sharedAutomaton.deapth)
                return A.sharedAutomaton.deapth - sharedAutomaton.deapth;
            else                 
                return A.hashCode() - hashCode();
        }
    }

    /* useful function */
    
    static public boolean isShared(MarkedAutomaton A)
    {
        return one.getClass() == A.getClass();
    }

    /* canonical */
    
    public static MarkedSharedAutomaton canonical(MarkedAutomaton A)
    {
        if (isShared(A))
            return (MarkedSharedAutomaton) A;
        else {
            MarkedAutomatonCanonical algo = new MarkedAutomatonCanonical(A);
            return algo.eval();
        }
    }

    /* statistics */
    public int deapth()
    {
        return sharedAutomaton.deapth;
    }

    public int getNbStates()
    {
        return sharedAutomaton.getNbStates();
    }

    public int getNbSharedAutomata()
    {
        return sharedAutomaton.getNbSharedAutomata();
    }

    public int getNbOutputAutomata()
    {
        return sharedAutomaton.getNbOutputAutomata();
    } 
    
    public String toDot()
    {
        if (this == zero)
            return "digraph system { \n 0 [label=zero]\n}\n";

        if (this == one)
            return "digraph system { \n 0 [label=one]\n}\n";
    
        StringBuffer res = new StringBuffer("digraph system { \n");
        HashMap done = new HashMap();
        int countSharedAutomaton = 1;
        done.put(sharedAutomaton, new Integer(0));

        int countStates = 0;

        // simple stack
        ArrayList todo = new ArrayList();
        todo.add(sharedAutomaton);
    
        res.append("I -> C0I" + initial + "\n");
        
        while (!todo.isEmpty()) {
            SharedAutomaton A = (SharedAutomaton) todo.get(todo.size()-1);
            todo.remove(todo.size()-1);
            int indexSharedAutomaton = ((Integer) done.get(A)).intValue();
            
            for (int i=0; i<A.outputAutomaton.nbLocalStates; i++) {
                if (A.outputAutomaton.isFinal[i])
                    res.append("C"+ indexSharedAutomaton + "I"+ i + " [label=A" + countStates +"]\n" );
                else
                    res.append("C"+ indexSharedAutomaton + "I"+ i + " [label=" + countStates +"]\n" );
                countStates ++;
                
                for (int a=0; a<A.outputAutomaton.alphabetSize; a++) {
                    int j = A.outputAutomaton.succ(i,a);
                    res.append("C"+ indexSharedAutomaton + "I"+ i + "->");
                    if (j>=0)
                        res.append("C"+ indexSharedAutomaton + "I"+ j);
                    else {
                        MarkedSharedAutomaton M = A.bindFunction[-1-j];
                        if (M == one)
                            res.append("one");
                        else if (M == zero)
                            res.append("zero");
                        else {
                            int indexTgt;
                            Object o = done.get(M.sharedAutomaton);
                            if (o== null) {
                                indexTgt = countSharedAutomaton;
                                countSharedAutomaton ++;
                                done.put(M.sharedAutomaton, new Integer(indexTgt));
                                todo.add(M.sharedAutomaton);
                            }
                            else {
                                indexTgt = ((Integer) o).intValue();
                            }
                            res.append("C"+ indexTgt + "I"+ M.initial);
                        }
                    }
                    res.append(" [label=" + a + "]\n");
                }
            }
        }
        
        res.append("}\n");
        return  res.toString();
    }
}
