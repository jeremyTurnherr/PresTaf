//
//  SimpleLinearConstraint.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package tester;

import java.util.Random;
import java.util.Arrays;

public class SimpleLinearConstraint 
{
    int nbVariable;
    int[] axi;
    int b;
    
    static Random random = new Random();
    
    SimpleLinearConstraint(int[] axi, int b, int n)
    {
        nbVariable = n;
        this.axi = axi;
        this.b = b;
    }
        
    static SimpleLinearConstraint create(int max, int n) 
    {
        int nbVariable = n;
        int[] variable = new int[n];
        int nbCoef = 0;
        
        for (int i=0; i<n; i++)
            if (random.nextBoolean()) {
                variable[nbCoef] = i;
                nbCoef ++;
            }
        
        int[] axi = new int[2*nbCoef];
        for (int i=0; i<nbCoef; i++) {
            axi[2*i] = variable[i];
            axi[2*i+1] = random.nextBoolean() ? random.nextInt(max)+1 : -random.nextInt(max)-1;
        }
        
        int b = random.nextInt(max);
        
        return new SimpleLinearConstraint(axi,b,n);
    }
    
    SimpleLinearConstraint addVariable () 
    {
        int[] newAxi = new int[axi.length+2];

        newAxi[1] = 1;
        newAxi[0] = 0;
        
        System.arraycopy(axi,0,newAxi,2,axi.length);
        for (int i=0;i<axi.length; i+=2)
            newAxi[i+2]++;
            
        return new SimpleLinearConstraint(newAxi,b,nbVariable+1);
    }
}
