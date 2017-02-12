//
//  SharedAutomaton.java
//  PresTaf Project
//
//  Created by couvreur on Tue Nov 25 2003.
//

package prestaf;

import java.util.WeakHashMap;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;

public final class SharedAutomaton 
{
    public OutputAutomaton outputAutomaton;
    MarkedSharedAutomaton[] bindFunction;
    int deapth;
    int nbStates = -1;
    int nbSharedAutomata = -1;
    int nbOutputAutomata = -1;

    MarkedSharedAutomaton[] uniqueMarkedSharedAutomaton;
    static WeakHashMap uniqueTable = new WeakHashMap();

    SharedAutomaton(OutputAutomaton outputAutomaton, MarkedSharedAutomaton bindFunction[])
    {
        this.outputAutomaton = outputAutomaton;
        this.bindFunction = bindFunction;
        
        if (bindFunction.length == 0)
            deapth = 0;
        else
            deapth = bindFunction[0].sharedAutomaton.deapth + 1;
    }
    
    static SharedAutomaton create(SharedAutomaton sharedAutomaton)
    {
        Object o = uniqueTable.get(sharedAutomaton);
        if (o == null) {
            uniqueTable.put(sharedAutomaton, new WeakReference(sharedAutomaton));
            
            int nbLocalStates = sharedAutomaton.outputAutomaton.nbLocalStates;
            sharedAutomaton.uniqueMarkedSharedAutomaton = new MarkedSharedAutomaton[nbLocalStates];
            for (int p=0; p<nbLocalStates; p++)
                sharedAutomaton.uniqueMarkedSharedAutomaton[p] = new MarkedSharedAutomaton(sharedAutomaton,p);

            return sharedAutomaton;
        }
        else
            return (SharedAutomaton) ((WeakReference) o).get();
    }

    /* equal and hash */
    
    public boolean equals(Object o)
    {
        if (o==null)
            return false;
        SharedAutomaton m = (SharedAutomaton) o;
        if (outputAutomaton != m.outputAutomaton || deapth != m.deapth)
            return false;
        
        return Arrays.equals(bindFunction,m.bindFunction);
    }

    int superHashCode()
    {
        return super.hashCode();
    }
    
    public int hashCode()
    {
        int res = outputAutomaton.superhashCode();

        for (int i=0; i<bindFunction.length; i++)
            res = 4483*res + bindFunction[i].hashCode() + 9173;
        return res;
    }

    /* close operations with 0 */
    
    void existsForallClose(boolean type) // type ? exists : forall
    {
        boolean[] done = new boolean[outputAutomaton.nbLocalStates];
        Arrays.fill(done,0,done.length,false);
            
        // simple stack structure
        int[] stack = new int[outputAutomaton.nbLocalStates];
        int in = 0; 
            
        for (int p=0;p<outputAutomaton.nbLocalStates;p++) 
        if (!done[p] && outputAutomaton.isFinal[p] != type) {
            int q = p;
                
            while (q>=0 && (outputAutomaton.isFinal[p] != type) && !done[q]) {
                stack[in] = q;
                in ++;
                done[q] = true;
                q = outputAutomaton.succ(q,0);
            }

            boolean tmp = (q <0) ? bindFunction[-1-q].isFinal() : outputAutomaton.isFinal[q];
            
            if (tmp == type)
                for (int i=0; i<in; i++) {
                    done[stack[i]] = true;
                    outputAutomaton.isFinal[stack[i]] = type;
                }

            in = 0;
        }
    }

    /* zero-one heuristic */
    
    MarkedSharedAutomaton zeroOneHeuristic()
    {
        if (bindFunction.length == 0 || (bindFunction.length == 1 && (bindFunction[0] == MarkedSharedAutomaton.zero || bindFunction[0] == MarkedSharedAutomaton.one)))
        {
            boolean isConstant = true;
            boolean isOne;

            if (bindFunction.length == 0)
                isOne = outputAutomaton.isFinal[0];
            else
                isOne = (bindFunction[0] == MarkedSharedAutomaton.one);
                
            for (int p=0; p<outputAutomaton.nbLocalStates && isConstant; p++) 
                isConstant = (isOne == outputAutomaton.isFinal[p]);

            if (isConstant) {
                return isOne?MarkedSharedAutomaton.one:MarkedSharedAutomaton.zero;
            }
        }
        return null;
    }

    /* HomomorphormicCheck */

    MarkedSharedAutomaton[] homomorphicCheck(int source, int letter, MarkedSharedAutomaton target)
    {        
        SharedAutomaton bindShared = target.sharedAutomaton;
        
        // check target zero-one
        if (target == MarkedSharedAutomaton.zero || target == MarkedSharedAutomaton.one)
            return null;
        
        // looking for an homomorphism
        
        // simple stack and done map
        int[] state = new int[outputAutomaton.nbLocalStates];
        int in = 0;
        int[] done = new int[outputAutomaton.nbLocalStates]; // homomorphism
        Arrays.fill(done,0,outputAutomaton.nbLocalStates,-1);
        
        boolean isHomomorphic = false;
    
        for (int q=0; q<bindShared.outputAutomaton.nbLocalStates && !isHomomorphic; q++)
        if (bindShared.outputAutomaton.succ(q,letter) == target.initial 
            && bindShared.outputAutomaton.isFinal[q] == outputAutomaton.isFinal[source] ) {
            done[source] = q;
            state[in] = source;
            in ++;
            isHomomorphic = true;
            
            while (in>0 && isHomomorphic) {
                in --;
                int p = state[in]; // done is defined
                for (int a=0; a<outputAutomaton.alphabetSize; a++) {
                    int u = outputAutomaton.succ(p,a);
                    int v = bindShared.outputAutomaton.succ(done[p],a);
                    
                    if (u>=0 && v>=0) {// two local states
                        if (done[u] == -1 && outputAutomaton.isFinal[u] == bindShared.outputAutomaton.isFinal[v]) {
                            done[u] = v;
                            state[in] = u;
                            in ++;
                        }
                        else if (done[u] != v)
                            isHomomorphic = false;
                    }
                    
                    else if (u>=0 && v<0) {// u is local and v is an output state
                        isHomomorphic = false;
                    }
                    
                    else if (u<0 && v>=0) { // u must be equivalent to v
                        if (bindFunction[-1-u].initial != v || bindFunction[-1-u].sharedAutomaton != bindShared)
                            isHomomorphic = false;
                    }
                    else {// if (u<0 && v <0)  bindFunction[u] must be bindShared.bindFunction[v]
                        if (bindFunction[-1-u] != bindShared.bindFunction[-1-v])
                            isHomomorphic = false;
                    }
                }
            }
            
            // reset stack and done
            if (!isHomomorphic) {
                in = 0;
                Arrays.fill(done,0,outputAutomaton.nbLocalStates,-1);
            }
        }

        if (isHomomorphic) {
            MarkedSharedAutomaton[] res = new MarkedSharedAutomaton[outputAutomaton.nbLocalStates];
            for (int p=0; p<outputAutomaton.nbLocalStates; p++)
                res[p] = bindShared.uniqueMarkedSharedAutomaton[done[p]];
            return res;
        }
        
        else
            return null;
    }
    
    int getNbStates()
    {
        if (nbStates <0)
            computeSizes();
        return nbStates;
    }

    int getNbSharedAutomata()
    {
        if (nbStates <0)
            computeSizes();
        return nbSharedAutomata;
    }

    int getNbOutputAutomata()
    {
        if (nbStates <0)
            computeSizes();
        return nbOutputAutomata;
    }
    
    private void computeSizes()
    {
        HashSet done = new HashSet();
        HashSet outputDone = new HashSet();
        // simple stack
        ArrayList todo = new ArrayList();
        
        nbStates = 0;
        nbSharedAutomata = 0;
        nbOutputAutomata = 0;
        
        done.add(this);
        todo.add(this);
        
        while (!todo.isEmpty()) {
            SharedAutomaton A = (SharedAutomaton) todo.get(todo.size()-1);
            todo.remove(todo.size()-1);
            
            if (!outputDone.contains(A.outputAutomaton)) {
                outputDone.add(A.outputAutomaton);
                nbOutputAutomata ++;
            }
            
            nbStates += A.outputAutomaton.nbLocalStates;
            nbSharedAutomata ++;
            
            for (int i=0; i<A.bindFunction.length; i++) {
                SharedAutomaton S = A.bindFunction[i].sharedAutomaton; 
                if (!done.contains(S)) {
                    done.add(S);
                    todo.add(S);
                }
            }
        }
    }
    
}

