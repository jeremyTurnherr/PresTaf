//
//  PresburgerMain.java
//  PresTafProject
//
//  Created by couvreur on Tue Dec 02 2003.
//

package application;
import prestaf.*;
import java.io.*;
import java.util.*;

public class PresburgerMain 
{

    public static void main (String[] args) 
    {
        if (args.length == 1) { 
            File file = new File(args[0]);
            try {
                PresburgerAnalyser analyser = new PresburgerAnalyser(new FileReader(file),false);
                System.out.println(analyser.formula.statistics());
            }
            catch(Exception e ) {
                System.out.println(e.toString());
            }
            System.out.println();
            System.out.println("Shared-Automaton-Unique-Table-Size = " + PresTafSystem.sharedAutomatonUniqueSize());
            System.out.println("Output-Automaton-Unique-Table-Size = " + PresTafSystem.outputAutomatonUniqueSize());
            System.out.println("Cache-Size = " + PresTafSystem.cacheSize());
        }
        else if (args.length == 2) {
            if (args[0].equals("-dot")) {
                File file = new File(args[1]);
                try {
                    PresburgerAnalyser analyser = new PresburgerAnalyser(new FileReader(file),false);
                    System.out.println(analyser.formula.value.toDot());
                }
                catch(Exception e ) {
                    System.out.println(e.toString());
                }
            }
            else {
                File file = new File(args[1]);
                try {
                    PresburgerAnalyser analyser = new PresburgerAnalyser(new FileReader(file),true);
                }
                catch(Exception e ) {
                    System.out.println(e.toString());
                }
            }
        }
        else if (args.length == 3) {
            if (args[0].equals("-direct")) {
                int N = Integer.parseInt(args[1]);
                PresTafSystem.setDirectCache(N);
                
                File file = new File(args[2]);
                try {
                    PresburgerAnalyser analyser = new PresburgerAnalyser(new FileReader(file),false);
                    System.out.println(analyser.formula.statistics());
                }
                catch(Exception e ) {
                    System.out.println(e.toString());
                }
                System.out.println();
                System.out.println("Shared-Automaton-Unique-Table-Size = " + PresTafSystem.sharedAutomatonUniqueSize());
                System.out.println("Output-Automaton-Unique-Table-Size = " + PresTafSystem.outputAutomatonUniqueSize());
                System.out.println("Cache-Size = " + PresTafSystem.cacheSize());
            }
            else
                System.out.println("java -jar PresTaf.jar [-dot|-verbose|-direct <size>] <file>");
        }
        else
            System.out.println("java -jar PresTaf.jar [-dot|-verbose|-direct <size>] <file>");
    } 


    public static void test(String s)
    {
        PresburgerAnalyser analyser = new PresburgerAnalyser(s,false);

        Presburger formula = analyser.formula;
        System.out.println(formula.value.toDot());

        System.out.println("Shared-Automaton-Unique-Table-Size = " + PresTafSystem.sharedAutomatonUniqueSize());
        System.out.println("Output-Automaton-Unique-Table-Size = " + PresTafSystem.outputAutomatonUniqueSize());
        System.out.println("Cache-Size = " + PresTafSystem.cacheSize());
    }
  
}
