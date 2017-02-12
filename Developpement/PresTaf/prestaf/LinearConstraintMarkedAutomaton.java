//
//  LinearConstraintMarkedAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package prestaf;

import java.util.Arrays;

public final class LinearConstraintMarkedAutomaton implements MarkedAutomaton
{
    int type;
    int variable;
    int nbVariable;

    int index; // axi[index]>=variable
    int b;
    int[] axi; // list of (x,a) ordered in x 

    static final int EQUALS = 0;
    static final int POSITIVE = 1;
    static final int POSITIVENUL = 2;

    LinearConstraintMarkedAutomaton(int type, int[] axi, int b, int n)
    {
        assert (axi.length <= 2*n);
    
        this.type = type;
        index = 0; 
        variable = 0;
        nbVariable = n;
        this.b = b;
        this.axi = axi;
    }

    private LinearConstraintMarkedAutomaton(int type, int v, int i, int b, LinearConstraintMarkedAutomaton L)
    {
        assert (i == L.axi.length || L.axi[i]>= v);
    
        this.type = type;
        variable = v;
        index = i;
        nbVariable = L.nbVariable;
        this.b = b;
        axi = L.axi;
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
        if (type == EQUALS)
            return (b == 0);
        else if (type == POSITIVE)
            return (b > 0);
        else
            return (b >= 0);
    }

    public MarkedAutomaton succ(int a)
    {
        int newB = b;
        int newIndex = index;
        int newVariable = variable;

        /* compute b */
        if (a == 1 && index<axi.length && axi[index] == variable)
            newB += axi[index+1]; 
        
        /* compute index */
        if (index<axi.length && axi[index] == variable)
            newIndex +=2;

        /* compute variable */
        newVariable ++;

        /* compure result */
        if (newVariable < nbVariable)
            return new LinearConstraintMarkedAutomaton(type,newVariable, newIndex, newB,this);
        else {
            if (newB%2 == 0)
                return new LinearConstraintMarkedAutomaton(type,0, 0, newB/2,this);
            else {
                if (type == EQUALS)
                    return MarkedSharedAutomaton.zero;
                else
                    return new LinearConstraintMarkedAutomaton(POSITIVENUL,0, 0, (newB-1)/2,this);
            }
        }
    }
        
    /* hashCode, equals */
    final int BCONST = 971;
    final int VCONST = 9479;
    final int TCONST = 7723;

    public int hashCode()
    {
        int res = nbVariable;
        for (int i=0; i<axi.length; i++)
            res = 7723 * res + 971 + axi[i];
        res = 7723 * res + 971 + VCONST*variable + BCONST * b + TCONST * type;

        return res;
    }

    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            LinearConstraintMarkedAutomaton c = (LinearConstraintMarkedAutomaton) o;

            if (type != c.type)
                return false;
            else  if (variable != c.variable)
                return false;
            else  if (nbVariable != c.nbVariable)
                return false;
            else  if (b != c.b)
                return false;
            else  
                return Arrays.equals(axi,c.axi);
        }	
    }
}
