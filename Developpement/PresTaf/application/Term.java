//
//  Term.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package application;

import java.util.HashMap;
import java.util.Arrays;

public class Term 
{
    static HashMap table = new HashMap();
    static int size = 0;
    
    String expr;
    int[] var;
    int[] coef;
    int constant;

    Term( String expr, int[] var, int[] coef, int constant)
    {
        this.expr = expr;
        this.var = var;
        this.coef = coef;
        this.constant = constant;
    }

    public String toString()
    {
        return expr;
    }

    public String debug()
    {
        StringBuffer res = new StringBuffer();
        
        for (int i= 0; i< nbVar(); i++) 
            if (coef[i] != 0)
                res.append(coef[i] + "* V" + var[i] + " + ");
        
        res.append(""+ constant);
        
        return res.toString();
    }

    static int getIndex(String v)
    {
        Object o = table.get(v);
        if (o != null) {
            return ((Integer) o).intValue();
        }
        else {
            table.put(v,new Integer(size));
            size ++;
            return size-1;
        }
    }

    int nbVar() {
        return var.length;
    }

    Term neg()
    {
        int[] negCoef = new int[nbVar()];
        
        for (int i=0; i<nbVar(); i++)
            negCoef[i] = - coef[i];
        return new Term("-" + expr, var, negCoef, - constant);
    }

    static int[] merge(int [] a, int[] b) {
        int n = 0;
        {
            int ai = 0;
            int bi = 0;
            while (ai < a.length && bi < b.length) {
                if (a[ai] == b[bi]) {
                    ai ++;
                    bi ++;
                    n ++;
                }
                else if (a[ai] < b[bi]) {
                    ai ++;
                    n ++;
                }
                else {
                    bi ++;
                    n ++;
                }
            }
            n = n + (a.length - ai) + (b.length - bi);
        }
        
        int[] res = new int[n];
        n = 0;
        {
            int ai = 0;
            int bi = 0;
            while (ai < a.length && bi < b.length) {
                if (a[ai] == b[bi]) {
                    res[n] = a[ai];
                    ai ++;
                    bi ++;
                    n ++;
                }
                else if (a[ai] < b[bi]) {
                    res[n] = a[ai];
                    ai ++;
                    n ++;
                }
                else {
                    res[n] = b[bi];
                    bi ++;
                    n ++;
                }
            }
            
            while (ai < a.length) {
                    res[n] = a[ai];
                    ai ++;
                    n ++;
            }

            while (bi < b.length) {
                    res[n] = b[bi];
                    bi ++;
                    n ++;
            }
        }
        return res;
    }

    Term plus(Term t)
    {
        int[] plusVar = merge(var,t.var);
        int[] plusCoef = new int[plusVar.length];
        Arrays.fill(plusCoef,0);
        
        {
            int k = 0;
            for (int i = 0; i<plusVar.length && k< nbVar(); i ++) {
                if (var[k] == plusVar[i]) {
                    plusCoef[i] = coef[k];
                    k++; 
                }
            }
        }

        {
            int k = 0;
            for (int i = 0; i<plusVar.length && k<t.nbVar(); i ++) {
                if (t.var[k] == plusVar[i]) {
                    plusCoef[i] += t.coef[k];
                    k++; 
                }
            }
        }
        
        return new Term(expr + " + " + t.expr, plusVar, plusCoef, constant + t.constant);
    }

    Term minus(Term t)
    {
        int[] plusVar = merge(var,t.var);
        int[] plusCoef = new int[plusVar.length];
        Arrays.fill(plusCoef,0);
        
        {
            int k = 0;
            for (int i = 0; i<plusVar.length && k<nbVar(); i ++) {
                if (var[k] == plusVar[i]) {
                    plusCoef[i] = coef[k];
                    k++; 
                }
            }
        }

        {
            int k = 0;
            for (int i = 0; i<plusVar.length && k<t.nbVar(); i ++) {
                if (t.var[k] == plusVar[i]) {
                    plusCoef[i] -= t.coef[k];
                    k++; 
                }
            }
        }
        
        return new Term(expr + " - " + t.expr, plusVar, plusCoef, constant - t.constant);
    }

    static Term factor(int a, String v)
    {
        int index = getIndex(v);
        
        return new Term(a + "*" + v, new int[] {index}, new int[] {a}, 0);
    }

    static Term variable(String v)
    {
        int index = getIndex(v);
        
        return new Term( ""+ v, new int[] {index}, new int[] {1}, 0);
    }

    static Term integer(int a)
    {
        return new Term("" + a, new int[0], new int[0], a);
    }
}
