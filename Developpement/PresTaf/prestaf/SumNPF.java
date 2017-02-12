//
//  ForallExistsMarkedAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Mon Dec 01 2003.
//

import java.util.Arrays;
import java.util.ArrayList;
import prestaf.*;

public final class SumNPF implements MarkedAutomaton
{
    int nbVar;
    SumPair[] parameter;
    
/*
    static NPF eval(NPF npf0, NPF npf1)
    {
        assert npf0.nbVariable == npf1.nbVariable;
        MarkedSharedAutomaton res = MarkedSharedAutomaton.canonical(create(npf0,npf1));
        return new NPF(npf0.nbVariable,res);
    }
*/
    private ActionMarkedAutomaton(Pair[] p0, Pair[] p1)
    {
        parameter = p;
    }

    static MarkedAutomaton create(MarkedSharedAutomaton relation, MarkedSharedAutomaton set)
    {
        if (set == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (set == MarkedSharedAutomaton.one)
            return relation.exists(1,1);
        else if (relation == MarkedSharedAutomaton.zero)
            return MarkedSharedAutomaton.zero;
        else if (relation == MarkedSharedAutomaton.one)
            return MarkedSharedAutomaton.one;
        else
            return new ActionMarkedAutomaton(new Pair[]{new Pair(relation,set)});
    }
        
    public int getAlphabetSize()
    {
        return 2;
    }
    
    public boolean isFinal()
    {
        for (int i=0; i< parameter.length; i++) {
            if (parameter[i].relation.isFinal() && parameter[i].set.isFinal()) {
                return true;
            }
        }
        return false;
    }
    
    public MarkedAutomaton succ(int a)
    {
        Pair[] res = new Pair[2*parameter.length];
        int resSize = 0;
        
        for (int i=0; i< parameter.length; i++) {
            Pair[] p = parameter[i].succ(a);
            if (p != null) {
                if (p[0] == Pair.one)
                    return MarkedSharedAutomaton.one;
                else
                    for (int j=0; j<p.length; j++) {
                        res[resSize] = p[j];
                        resSize++;
                    }
            }
        }
        
        if (resSize == 0)
            return MarkedSharedAutomaton.zero;
            
        return new ActionMarkedAutomaton(simplify(res,resSize));
    }

    public int getType()
    {
        return MarkedAutomaton.EXISTS;
    }

    /* equal */
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else {
            ActionMarkedAutomaton a = (ActionMarkedAutomaton) o;
            return Arrays.equals(parameter,a.parameter); 
        }
    }

    public int hashCode()
    {
        int res = 0;
        for (int i=0; i<parameter.length; i++)
            res = res * 941 + 4007 + parameter[i].hashCode();
        
        return res;
    }
 
    private static Pair[] simplify(Pair[] p, int n)
    {
        assert p.length >= 1;
        Arrays.sort(p,0,n);
                
        int k = 1;
        for (int i=1;i<n;i++) {
            if (!p[i].equals(p[i-1])) {
                p[k] = p[i];
                k++;
            }
        }
        
        Pair[] res = new Pair[k];
        System.arraycopy(p,0,res,0,k);
        return res;
    }
   
    public String toString()
    {
        StringBuffer res = new StringBuffer("------------------------\n");
        for (int i=0; i<parameter.length; i++) {
            res.append("p" + i + " : \n");
            res.append("Relation\n" + parameter[i].relation.toDot());
            res.append("Set\n" + parameter[i].set.toDot());
        }
        return res.toString();
    }
    
   
    static final class SumPair implements Comparable
    {
        MarkedSharedAutomaton parameter1;
        MarkedSharedAutomaton parameter2;
        boolean[] reste;
                
        SumPair(MarkedSharedAutomaton p1, MarkedSharedAutomaton p2, int nbVar)
        {
            if (p1.hashCode()<p2.hashCode) {
                parameter1 = p1;
                parameter2 = p2;
            else {
                parameter1 = p2;
                parameter2 = p1;
            }
            reste = new boolean[nbVar];
            Arrays.fill(reste,false);
        }

        SumPair(MarkedSharedAutomaton p1, MarkedSharedAutomaton p2, boolean reste)
        {
            if (p1.hashCode()<p2.hasCode) {
                parameter1 = p1;
                parameter2 = p2;
            else {
                parameter1 = p2;
                parameter2 = p1;
            }
            this.reste = reste;
        }
        
        static SumPair create(MarkedSharedAutomaton p1, MarkedSharedAutomaton p2, int nbVar)
        {
            if (p1 == MarkedSharedAutomaton.zero || p2 ==MarkedSharedAutomaton.zero)
                return zero;
            else if ( (p1 == MarkedSharedAutomaton.one && p2.isFinal()) || (p2 == MarkedSharedAutomaton.one && p1.isFinal()))
                return one;
            else {
                return new SumPair(p1,p2,nbVar);
            }
        }

        
        public int compareTo(Object o)
        {
            SumPair s = (SumPair) o;
            if (parameter1 != s.parameter1)
                return s.parameter1.hashCode() - parameter1.hashCode();
            else if (parameter2 != s.parameter2)
                return s.parameter2.hashCode() - parameter2.hashCode();
            else
                for (int i=0; i< reste.length; i++)
                    if (reste[i] != s.reste[i])
                        return s.reste[i]?1:-1;
            return 0;
        }
        
        public boolean equals(Object o)
        {
            SumPair s = (SumPair) o;
            return parameter1 == s.parameter1 && parameter1 == s.parameter1 && Arrays.equals(reste,s.reste);
        }
        
        public int hashCode()
        {
            int res = 2609 + parameter1.hashCode() + 1583* parameter2.hashCode();
            for (int i=0;i<reste.length; i++)
                res = 2609 + 1583*res + reste[i]?1:0;
            return res;
        }

/***/

        Pair[] succ(int a)
        {
            assert relation != MarkedSharedAutomaton.one && relation != MarkedSharedAutomaton.zero;
            assert set != MarkedSharedAutomaton.zero;
            
            MarkedSharedAutomaton Ra = relation.sharedSucc(a);
            
            if (Ra == MarkedSharedAutomaton.zero)
                return null;
            else if (Ra == MarkedSharedAutomaton.one)
                return new Pair[] {one};
            
            MarkedSharedAutomaton Ra0 = Ra.sharedSucc(0);
            MarkedSharedAutomaton Ra1 = Ra.sharedSucc(1);
            
            if (set == MarkedSharedAutomaton.one)
                return new Pair[]{create(Ra0.or(Ra1),set)};
                
            MarkedSharedAutomaton set0 = set.sharedSucc(0);
            MarkedSharedAutomaton set1 = set.sharedSucc(1);
            
            Pair p0 = create(Ra0,set0);
            Pair p1 = create(Ra1,set1);
            
            if (p0 == one || p1 == one)
                return new Pair[]{one};
            else if (p0 == zero) {
                if (p1 == zero)
                    return null;
                else
                    return new Pair[]{p1};
            } 
            else if (p1 == zero)
                return new Pair[]{p0};
            else
                return new Pair[]{p0,p1};
        }
    }
}
