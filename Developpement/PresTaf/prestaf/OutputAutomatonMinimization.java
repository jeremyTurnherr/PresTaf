//
//  OutputAutomatonMinimization.java
//  PresTaf Project
//
//  Created by couvreur on Tue Nov 25 2003.
//

package prestaf;

public final class OutputAutomatonMinimization 
{
    OutputAutomaton input;
    OutputAutomaton output;
    int[] relation;
    
    OutputAutomatonMinimization(OutputAutomaton input)
    {
        this.input = input;
    }

    void eval() 
    {  
        if (input.nbLocalStates == 1) {
            output = input;
            relation = new int[]{0};
            return;
        }            
        else {
//            MinAutomatonBlum algo = new MinAutomatonBlum(input);
            MinimalAutomatonHopcroft algo = new MinimalAutomatonHopcroft(input);
            algo.eval();
        
            if (algo.output.nbLocalStates == input.nbLocalStates) {
                output = algo.output;
                relation = algo.relation;
                return;
            }
            else {
                relation = algo.relation;
                algo.input = algo.output;
                algo.eval();
                
                for (int p=0; p<input.nbLocalStates; p++)
                    relation[p] = algo.relation[relation[p]];
              
                output = algo.output;
                return;
            }
        }
    }
}
