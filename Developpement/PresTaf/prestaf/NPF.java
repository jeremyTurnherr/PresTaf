//
//  NPF.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package prestaf;

public final class NPF 
{
    static boolean simpleExists = true;
    public final int nbVariable;
    public final MarkedSharedAutomaton value;
    
    public NPF(int n, MarkedSharedAutomaton value)
    {
        assert (value.getAlphabetSize() == 2 || value.getAlphabetSize() == 0);
        assert (n>=0);
        assert (n>0 || value == MarkedSharedAutomaton.zero || value ==MarkedSharedAutomaton.one);
        nbVariable = n;
        this.value = value;
    }
    
    /* equals */
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            NPF a = (NPF) o;
            return (nbVariable == a.nbVariable && value == a.value); 
        }	
    }
    
    /* zero-one */

    static public NPF zero(int n)
    {
        return new NPF(n,MarkedSharedAutomaton.zero);
    }

    static public NPF one(int n)
    {
        return new NPF(n,MarkedSharedAutomaton.one);
    }

    /* Boolean operations */
    
    public boolean isZero()
    {
        return value == MarkedSharedAutomaton.zero;
    }

    public boolean isOne()
    {
        return value == MarkedSharedAutomaton.one;
    }
    
    public NPF not()
    {
        return new NPF(nbVariable, value.not());
    }

    public NPF or(NPF npf)
    {
        assert (nbVariable == npf.nbVariable);
        return new NPF(nbVariable, value.or(npf.value));
    }

    public NPF and(NPF npf)
    {
        assert (nbVariable == npf.nbVariable);
        return new NPF(nbVariable, value.and(npf.value));
    }
    

    public NPF imply(NPF npf)
    {
        assert (nbVariable == npf.nbVariable);
        return new NPF(nbVariable, value.imply(npf.value));
    }
    public NPF equiv(NPF npf)
    {
        assert (nbVariable == npf.nbVariable);
        return new NPF(nbVariable, value.equiv(npf.value));
    }

    /* exists, forall, addVariable operations */

    public NPF exists2(int v, int modulo)
    {
        assert (v<modulo && v>=0 && nbVariable % modulo == 0 );
        
        int k = nbVariable/modulo;
        return new NPF(nbVariable-k,value.exists2(v,modulo));
    }

    public NPF exists2(int v)
    {
        assert (v<nbVariable && v>=0);
        
        return new NPF(nbVariable-1,value.exists2(v,nbVariable));
    }

    public NPF exists(int v, int modulo)
    {
        if (simpleExists)
            return exists1(v,modulo);
        else
            return exists2(v,modulo);
    }

    public NPF exists(int v)
    {
        if (simpleExists)
            return exists1(v);
        else
            return exists2(v);
    }

    
    public NPF exists1(int v, int modulo)
    {
        assert (v<modulo && v>=0 && nbVariable % modulo == 0 );
        
        int k = nbVariable/modulo;
        return new NPF(nbVariable-k,value.exists(v,modulo));
    }

    public NPF exists1(int v)
    {
        assert (v<nbVariable && v>=0);
        
        return new NPF(nbVariable-1,value.exists(v,nbVariable));
    }

    public NPF exists(boolean[] tab)
    {
        assert (nbVariable % tab.length == 0 );

        int nbRemove = 0;
        for (int i=0; i<tab.length; i++)
            if (tab[i])
                nbRemove ++;
        
        return new NPF( (nbVariable/tab.length) * (tab.length-nbRemove),value.exists(tab));
    }

    public NPF forall2(int v, int modulo)
    {
        assert (v<modulo && v>=0 && nbVariable % modulo == 0 );
        int k = nbVariable/modulo;
        return new NPF(nbVariable-k,value.forall2(v,modulo));
    }

    public NPF forall2(int v)
    {
        assert (v<nbVariable && v>=0);
        
        return new NPF(nbVariable-1,value.forall2(v,nbVariable));
    }


    public NPF forall(int v)
    {
        if (simpleExists)
            return forall1(v);
        else
            return forall2(v);
    }


    public NPF forall(int v, int modulo)
    {
        if (simpleExists)
            return forall1(v,modulo);
        else
            return forall2(v,modulo);
    }

    public NPF forall1(int v, int modulo)
    {
        assert (v<modulo && v>=0 && nbVariable % modulo == 0 );
        int k = nbVariable/modulo;
        return new NPF(nbVariable-k,value.forall(v,modulo));
    }

    public NPF forall1(int v)
    {
        assert (v<nbVariable && v>=0);
        
        return new NPF(nbVariable-1,value.forall(v,nbVariable));
    }

    public NPF forall(boolean[] tab)
    {
        assert (nbVariable % tab.length == 0 );

        int nbRemove = 0;
        for (int i=0; i<tab.length; i++)
            if (tab[i])
                nbRemove ++;
        
        return new NPF( (nbVariable/tab.length) * (tab.length-nbRemove),value.forall(tab));
    }

    public NPF addVariable(int v, int modulo)
    {
        assert (v<=modulo && v>=0 && modulo>0 && nbVariable % modulo == 0 );
        int k = nbVariable/modulo;
        return new NPF(nbVariable+k,value.addVariable(v,modulo));
    }

    public NPF addVariable(int v)
    {
        assert (v<=nbVariable && v>=0 );
        return new NPF(nbVariable+1,value.addVariable(v,nbVariable));
    }
    
    public NPF addVariable(boolean[] tab)
    {
        int nbAdd = 0;
        for (int i=0; i<tab.length; i++)
            if (tab[i])
                nbAdd ++;
        
        assert (nbVariable % (tab.length - nbAdd) == 0 );
        
        int k = nbVariable/(tab.length - nbAdd);
        return new NPF(nbVariable+k*nbAdd,value.addVariable(tab));
    }

    /* linear constraints */

    private static boolean checkConstraint(int[] axi, int n)
    {
        if (axi.length> 2*n)
            return false;
        else if (axi.length == 0)
            return true;
        else {
            for (int i=2; i<axi.length; i+=2)
                if (axi[i-2]>=axi[i])
                    return false;
                    
            return axi[axi.length-2]<n;
        }
    }

    static public NPF equals(int[] axi, int b, int n) 
    {
        assert checkConstraint(axi,n);
        
        if (axi.length == 0) {
            if (b == 0)
                return new NPF(n,MarkedSharedAutomaton.one);
            else
                return new NPF(n,MarkedSharedAutomaton.zero);
        }
        else
            return new NPF(n, MarkedSharedAutomaton.canonical(new LinearConstraintMarkedAutomaton(LinearConstraintMarkedAutomaton.EQUALS,axi,-b,n)));
    }

    static public NPF notEquals(int[] axi, int b, int n) 
    {
        return equals(axi,b,n).not();
    }

    static public NPF greater(int[] axi, int b, int n) 
    {
        assert checkConstraint(axi,n);
        
        if (axi.length == 0) {
            if (0 > b)
                return new NPF(n,MarkedSharedAutomaton.one);
            else
                return new NPF(n,MarkedSharedAutomaton.zero);
        }
        else
            return new NPF(n, MarkedSharedAutomaton.canonical(new LinearConstraintMarkedAutomaton(LinearConstraintMarkedAutomaton.POSITIVE,axi,-b,n)));
    }

    static public NPF greaterEquals(int[] axi, int b, int n) 
    {
        assert checkConstraint(axi,n);
        
        if (axi.length == 0) {
            if (0 >= b)
                return new NPF(n,MarkedSharedAutomaton.one);
            else
                return new NPF(n,MarkedSharedAutomaton.zero);
        }
        else
            return new NPF(n, MarkedSharedAutomaton.canonical(new LinearConstraintMarkedAutomaton(LinearConstraintMarkedAutomaton.POSITIVENUL,axi,-b,n)));
    }

    static public NPF less(int[] axi, int b, int n)
    {
        assert checkConstraint(axi,n);
        
        int[] c = new int[axi.length];
        for (int i=0; i< axi.length/2; i++) {
            c[2*i+1] = -axi[2*i+1];
            c[2*i] = axi[2*i];
        }
            
        return greater(c,-b,n);
    }

    static public NPF lessEquals(int[] axi, int b, int n)
    {
        assert checkConstraint(axi,n);
        
        int[] c = new int[axi.length];
        for (int i=0; i< axi.length/2; i++) {
            c[2*i+1] = -axi[2*i+1];
            c[2*i] = axi[2*i];
        }
            
        return greaterEquals(c,-b,n);
    }
    
    /* statistics */
    public int deapth()
    {
        return value.deapth();
    }
    
    public int getNbStates()
    {
        return value.getNbStates();
    }

    public int getNbSharedAutomata()
    {
        return value.getNbSharedAutomata();
    }

    public int getNbOutputAutomata()
    {
        return value.getNbOutputAutomata();
    } 
    
    public String toDot()
    {
        return value.toDot();
    }

}
