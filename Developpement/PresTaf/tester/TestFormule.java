//
//  TestNPF.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package tester;

import prestaf.*;
import java.util.Arrays;

public class TestFormule 
{

    public static NPF sumxyz(int nbVar)
    {
        NPF sum1 = NPF.equals(new int[]{0,1,1,1,2,-1},0,3);
        NPF res = sum1;
        NPF plus = sum1;
        
        for (int i=1; i<nbVar; i++) {
            for (int k=0; k<3; k++) {
                res = res.addVariable(0);
                plus = plus.addVariable(3);
            }
            res = res.and(plus);
        }
        return res;
    }

    public static void badLinear(int n)
    {
        NPF f = NPF.one(n);
        for (int v=0;v<n;v++) {
            f = f.and(NPF.equals(new int[]{v,1},0,n));
        }
        
        assert f.nbVariable == n;
        
        for (int v1=0; v1<n; v1++)
        for (int v2=v1+1; v2<n; v2++) {
            System.out.print(".");
            
            assert f.nbVariable == n;

            f = f.addVariable(1,1);
            assert f.nbVariable == 2*n;

            f = f.addVariable(2*n);
            assert f.nbVariable == 2*n+1;

            NPF g = NPF.one(2*n+1);
            
            for (int v=0;v<n;v++) {
                NPF h;
                if (v==v1 || v==v2)
                    h = NPF.equals(new int[]{2*v,1,2*v+1,-1,2*n,1}, 0,2*n+1);
                else
                    h = NPF.equals(new int[]{2*v,1,2*v+1,-1}, 0,2*n+1);
                    
                g = g.and(h);
            }
            f = f.and(g);
            assert f.nbVariable == 2*n+1;
            
            System.out.println("\n\tG" + n + ": #state = " + f.getNbStates() 
            + ", #SCC = " + f.getNbSharedAutomata() 
            + ", #Output = " + f.getNbOutputAutomata() 
            + ", deapth = " + f.deapth());

            
            f = f.exists(2*n);
            assert f.nbVariable == 2*n;
            f = f.exists(0,2);
            assert f.nbVariable == n;
        
            System.out.println("\nF" + n + ": #state = " + f.getNbStates() 
            + ", #SCC = " + f.getNbSharedAutomata() 
            + ", #Output = " + f.getNbOutputAutomata() 
            + ", deapth = " + f.deapth());

        }

        System.out.println("\nF" + n + ": #state = " + f.getNbStates() 
        + ", #SCC = " + f.getNbSharedAutomata() 
        + ", #Output = " + f.getNbOutputAutomata() 
        + ", deapth = " + f.deapth());

        System.out.println();
        System.out.println("Shared-Automaton-Unique-Table-Size = " + PresTafSystem.sharedAutomatonUniqueSize());
        System.out.println("Output-Automaton-Unique-Table-Size = " + PresTafSystem.outputAutomatonUniqueSize());
        System.out.println("Cache-Size = " + PresTafSystem.cacheSize());
    }

    public static void badbis(int n)
    {
        int[][] C = new int[n][2*n];

        for (int v=0; v<n; v++) {
            C[v][0] = v;
            C[v][1] = 1;
            Arrays.fill(C[v],2,2*n,-1);
        }

        int[] index = new int[n];
        Arrays.fill(index,0,n,2);
        int count = n;
        
        for (int v1=0; v1<n; v1++)
        for (int v2=v1+1; v2<n; v2++) {
            C[v1][index[v1]] = count;
            C[v2][index[v2]] = count;
            index[v1] +=2;
            index[v2] +=2;
            count++;
        }
                
        NPF f = NPF.one(count);
        for (int v=0; v<n; v++) {
            NPF c = NPF.equals(C[v],0,count);
            f = f.and(c);
        }

        boolean[] table = new boolean[count];
        Arrays.fill(table,0,n,false);
        Arrays.fill(table,n,count,true);
  
        f = f.exists(table);
        
        System.out.println("F" + n + ": #state = " + f.getNbStates() 
        + ", #SCC = " + f.getNbSharedAutomata() 
        + ", #Output = " + f.getNbOutputAutomata() 
        + ", deapth = " + f.deapth());
        
    }
}
