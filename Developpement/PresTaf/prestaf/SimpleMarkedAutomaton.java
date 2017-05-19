//
//  SimpleMarkedAutomaton.java
//  PresTafProject
//
//  Created by couvreur on Mon Dec 01 2003.

package prestaf;
import java.util.Random;
import java.util.Arrays;

class SimpleMarkedAutomaton implements MarkedAutomaton
{
    int initial;
    int[][] succ;
    boolean[] isFinal;

    SimpleMarkedAutomaton(int initial, int[][] succ, boolean[] isFinal)
    {
        this.initial = initial;
        this.succ = succ;
        this.isFinal = isFinal;
        existsForallClose(true);
        existsForallClose(false);
    }
    
    public int getAlphabetSize()
    {
        return succ[0].length;
    }
    
    public boolean isFinal()
    {
        return isFinal[initial];
    }
    
    public MarkedAutomaton succ(int a)
    {
        return new SimpleMarkedAutomaton(succ[initial][a],succ,isFinal);
    }
    
    public int getType()
    {
        return MarkedAutomaton.NORMAL;
    }

    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        else if (o.getClass() != getClass())
            return false;
        else
        {
            SimpleMarkedAutomaton A = (SimpleMarkedAutomaton) o;
            return A.initial == initial && A.succ == succ && A.isFinal == isFinal;
        }
    }
    
     public int hashCode()
    {
        return initial + 5*isFinal.hashCode() + 13 * succ.hashCode();
    }

    static SimpleMarkedAutomaton create(int nbStates, int alphabetSize)
    {
        Random random = new Random();
        int initial =0;

        boolean[] isFinal = new boolean[nbStates];
        for (int p=0; p<nbStates; p++)
            isFinal[p] = random.nextBoolean();

        int[][] succ = new int[nbStates][alphabetSize];
        for (int p=0; p<nbStates; p++)
        for (int a=0; a<alphabetSize; a++)
            succ[p][a] = random.nextInt(nbStates);
        
        return new SimpleMarkedAutomaton(initial,succ,isFinal);
    }

    SimpleMarkedAutomaton duplicate(int time)
    {
        final int A = 2;
        final int B = 0;
        
        int nbStates = succ.length;
        int alphabetSize = succ[0].length;

        Random random = new Random();
        int[] index = new int[nbStates+1];
        index[0] = 0;
        for (int i=1; i <nbStates+1; i++)
            index[i] = index[i-1] + 1 + random.nextInt(time);
        
        if (index[nbStates] % A == 0)
            index[nbStates] ++;    

        int newNbStates = index[nbStates];
        
        int newInitial = (A*initial + B) % newNbStates;
        int[][] newSucc = new int[newNbStates][alphabetSize];
        boolean[] newIsFinal = new boolean[newNbStates]; 
        
        for (int i = 0; i<nbStates; i++) 
        for (int j = index[i]; j <index[i+1]; j++) {
            newIsFinal[(A*j+B)%newNbStates] = isFinal[i];
            for (int a=0; a<alphabetSize; a++) {
                int tgt = succ[i][a]; 
                newSucc[(A*j+B)%newNbStates][a] = (A * ( index[tgt] + random.nextInt(index[tgt+1] - index[tgt]) ) + B)%newNbStates;
            }
        }
        
        return new SimpleMarkedAutomaton(newInitial,newSucc,newIsFinal);
    }
    
    public String toString()
    {
        int nbStates = succ.length;
        int alphabetSize = succ[0].length;
        StringBuffer res = new StringBuffer();
        
        res.append("static int initial = " + initial + ";\n");
        res.append("static boolean[] isFinal = {" + isFinal[0]);

        for (int i=1; i< nbStates; i++)
            res.append(", " + isFinal[i]);
        res.append("};\n");


        res.append("static int[][] succ = { \n");
        for (int i=0; i< nbStates; i++) {
            res.append("\t { ");

            res.append(succ[i][0]);
            for (int a=1; a< alphabetSize; a++) 
                res.append("," + succ[i][a] + " ");
            if (i< nbStates-1)
                res.append("},\n");
            else
                res.append("}\n");
        }
        res.append("};\n");
      
        return res.toString();    
    }
    
    
    private void existsForallClose(boolean type) // type ? exists : forall
    {
        int nbLocalStates = succ.length;
        
        boolean[] done = new boolean[nbLocalStates];
        Arrays.fill(done,0,done.length,false);
            
        // simple stack structure
        int[] stack = new int[nbLocalStates];
        int in = 0; 
            
        for (int p=0;p<nbLocalStates;p++) 
        if (!done[p] && isFinal[p] != type) {
            int q = p;
                
            while ( (isFinal[q] != type) && !done[q]) {
                stack[in] = q;
                in ++;
                done[q] = true;
                q = succ[q][0];
            }
            
            if (isFinal[q] == type)
                for (int i=0; i<in; i++) {
                    done[stack[i]] = true;
                    isFinal[stack[i]] = type;
                }

            in = 0;
        }
    }

}
