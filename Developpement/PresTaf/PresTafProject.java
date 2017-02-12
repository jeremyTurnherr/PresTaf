//
//  PresTafProject.java
//  PresTafProject
//
//  Created by couvreur on Tue Nov 25 2003.
//

import tester.*;
import prestaf.*;
import application.*;

public class PresTafProject {


    public static void maintic (String args[]) 
    {
    
            NPF res = TestFormule.sumxyz(2);
        
            System.out.println ("nbVar = " + 1 + ", Size = " + res.getNbStates()
                + ", #SCC = " + res.getNbSharedAutomata() 
                + ", #OutputAutomaton = " + res.getNbOutputAutomata() 
                + ", Deapth = " + (res.value.deapth()+1) );
        
            System.out.println(res.toDot());


        System.out.println();
        System.out.println("Shared-Automaton-Unique-Table-Size = " + PresTafSystem.sharedAutomatonUniqueSize());
        System.out.println("Output-Automaton-Unique-Table-Size = " + PresTafSystem.outputAutomatonUniqueSize());
        System.out.println("Cache-Size = " + PresTafSystem.cacheSize());
        System.out.println("Cache-Statistics (win / get) = " + PresTafSystem.getNbWinAccess() + "/"+ PresTafSystem.getNbAccess());

    }


    public static void main (String args[]) 
    {
        PresburgerMain.main(args);
    }
}
