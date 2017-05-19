//
//  Presburger.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package application;

import prestaf.*;
import java.util.Arrays;

public class Presburger 
{
    String expr;
    int[] var;
    public NPF value;

    Presburger(String s, int[] v, NPF val)
    {
        expr = s;
        var = v;
        value = val;
    }

    public String toString()
    {
        return expr;
    }

    public String statistics()
    {
        if (value == null)
            return "..... null";
        else if (value.isZero())
            return "zero";
        else if (value.isOne())
            return "one";
        else
            return ("Size = " + getNbStates()
                + ", #SCC = " + getNbSharedAutomata() 
                + ", #OutputAutomaton = " + getNbOutputAutomata() 
                + ", Deapth = " + (value.deapth()+1) );
    }

    Presburger bracket()
    {
        return new Presburger("(" + expr + ")" , var, value);
    }

    private NPF addVariable(int[] v)
    {
        if (v.length == var.length)
            return value;
        else {
            boolean[] b = new boolean[v.length];
            Arrays.fill(b,true);
            int k=0;
            for (int i=0; i< v.length && k<var.length; i++)
                if (v[i] == var[k]) {
                    b[i] = false;
                    k++;
                }
            
            return value.addVariable(b);
        }
    }

    public Presburger and(Presburger b)
    {
        int[] newVar = Term.merge(var,b.var);
        NPF u = addVariable(newVar);
        NPF v = b.addVariable(newVar);
        
        return new Presburger( expr + " && " + b.expr, newVar, u.and(v));
    }

    Presburger or(Presburger b)
    {
        int[] newVar = Term.merge(var,b.var);
        NPF u = addVariable(newVar);
        NPF v = b.addVariable(newVar);
        return new Presburger( expr + " || " + b.expr, newVar, u.or(v));
    }

    Presburger imply(Presburger b)
    {
        int[] newVar = Term.merge(var,b.var);
        NPF u = addVariable(newVar);
        NPF v = b.addVariable(newVar);
        return new Presburger( expr + " -> " + b.expr, newVar, u.imply(v));
    }

    Presburger equiv(Presburger b)
    {
        int[] newVar = Term.merge(var,b.var);
        NPF u = addVariable(newVar);
        NPF v = b.addVariable(newVar);
        return new Presburger( expr + " <-> " + b.expr, newVar, u.equiv(v));
    }

    Presburger not()
    {
        return new Presburger("!" + expr, var, value.not());
    }

    Presburger exists(int i, String v)
    {
        int pos = Arrays.binarySearch(var,i);
        if (pos<0 || pos >=var.length)
            return new Presburger("E." + v + " " + expr, var, value);
        else {
            int[] newVar = new int[var.length-1];
            System.arraycopy(var,0,newVar,0,pos);
            System.arraycopy(var,pos+1,newVar,pos,newVar.length-pos);
            return new Presburger("E." + v + " " + expr, newVar, value.exists(pos));
        }
    }

    Presburger forall(int i, String v)
    {
        int pos = Arrays.binarySearch(var,i);
        if (pos<0 || pos >=var.length)
            return new Presburger("A." + v + " " + expr, var, value);
        else {
            int[] newVar = new int[var.length-1];
            System.arraycopy(var,0,newVar,0,pos);
            System.arraycopy(var,pos+1,newVar,pos,newVar.length-pos);
            return new Presburger("A." + v + " " + expr, newVar, value.forall(pos));
        }
    }


    /* Linear constraint */
    static private int[] convert(int[] coef)
    {
        int[] res = new int[2*coef.length];
        for (int i=0;i<coef.length; i++) {
            res[2*i+1] = coef[i];
            res[2*i] = i;
        }
        return res;
    }
    
    static Presburger equals(Term t1,Term t2)
    {
        Term t = t2.minus(t1);
        return new Presburger(t1.toString() + " = " + t2.toString(),
            t.var, 
            NPF.equals(convert(t.coef),-t.constant,t.coef.length));
    }

    static Presburger notEquals(Term t1,Term t2)
    {
        Term t = t2.minus(t1);
        return new Presburger(t1.toString() + " != " + t2.toString(),
            t.var, 
            NPF.notEquals(convert(t.coef),-t.constant,t.coef.length));
    }

    static Presburger less(Term t1,Term t2)
    {
        Term t = t2.minus(t1);
        
        return new Presburger(t1.toString() + " < " + t2.toString(),
            t.var, 
            NPF.greater(convert(t.coef),-t.constant,t.coef.length));
    }

    static Presburger lessEquals(Term t1,Term t2)
    {
        Term t = t2.minus(t1);

        return new Presburger(t1.toString() + " <= " + t2.toString(),   
            t.var, 
            NPF.greaterEquals(convert(t.coef),-t.constant,t.coef.length));
    }

    static Presburger greater(Term t1,Term t2)
    {
        Term t = t1.minus(t2);
        return new Presburger(t1.toString() + " > " + t2.toString(),
            t.var, 
            NPF.greater(convert(t.coef),-t.constant,t.coef.length));
    }

    static Presburger greaterEquals(Term t1,Term t2)
    {
        Term t = t1.minus(t2);
        return new Presburger(t1.toString() + " >= " + t2.toString(), 
            t.var, 
            NPF.greaterEquals(convert(t.coef),-t.constant,t.coef.length));
    }
    
    /* statistics */
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

}
