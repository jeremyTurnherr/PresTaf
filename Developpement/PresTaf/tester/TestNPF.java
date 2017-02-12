//
//  TestNPF.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package tester;

import prestaf.*;
import java.util.Arrays;

public class TestNPF 
{    
    public static void testLinearConstraint(int max, int n)
    {
        SimpleLinearConstraint L = SimpleLinearConstraint.create(max,n);
        SimpleLinearConstraint L1 = L.addVariable();
                
        NPF equals = NPF.equals(L.axi,L.b,n); // linear = b
        NPF notEquals = NPF.notEquals(L.axi,L.b,n); // linear != b
        NPF less = NPF.less(L.axi,L.b,n); // linear < b
        NPF lessEquals = NPF.lessEquals(L.axi,L.b,n); // linear <= b
        NPF greater = NPF.greater(L.axi,L.b,n); // linear > b
        NPF greaterEquals = NPF.greaterEquals(L.axi,L.b,n); // linear >= b
        
        NPF addEquals = NPF.equals(L1.axi,L1.b,L1.nbVariable); //x0 + linear = b
        NPF lessEqualsBis =  addEquals.exists(0,n+1);
        
        if (!lessEqualsBis.equals(lessEquals))            
            throw new Error("E(xo) addEquals = lessEquals");

        if (!equals.or(less).equals(lessEquals) )
            throw new Error("less + equals = lessEquals");

        if (!equals.or(greater).equals(greaterEquals) )
            throw new Error("greater + equals = greaterEquals");

        if (!less.or(greater).equals(notEquals) )
            throw new Error("less + greater = notEquals");

        if (!less.and(equals).isZero())
            throw new Error("less . equals = 0");

        if (!greater.and(equals).isZero())
            throw new Error("greater . equals = 0");

        if (!less.and(greater).isZero())
            throw new Error("less . greater = 0");

        if (!less.or(greaterEquals).isOne() )
            throw new Error("less + greaterEquals = 1");

        if (!lessEquals.or(greater).isOne() )
            throw new Error("lessEquals + greater = 1");

    }

}
