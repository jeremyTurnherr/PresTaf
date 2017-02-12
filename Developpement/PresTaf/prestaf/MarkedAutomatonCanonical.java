//
//  MarkedAutomatonCanonical.java
//  PresTaf Project
//
//  Created by couvreur on Tue Nov 25 2003.
//

package prestaf;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;

public final class MarkedAutomatonCanonical 
{
    static Cache cache = new CompleteCache();
    static HashMap localCache = new HashMap();
    static int level = 0;
    static public int RATE = 1;

    MarkedAutomaton markedAutomaton;
    int alphabetSize;

    MarkedAutomatonCanonical(MarkedAutomaton markedAutomaton)
    {
        this.markedAutomaton = markedAutomaton;
        alphabetSize = markedAutomaton.getAlphabetSize();
    }
    
    MarkedSharedAutomaton eval()
    {
        /* No thing to do */
        {
            MarkedSharedAutomaton o = cache.get(markedAutomaton);
            if (o!= null) {
                return o;
            }
        }
        
        // init
        HashMap done = new HashMap();;
        ArcStack todo = new ArcStack();
        SccStack scc = new SccStack();
        level ++;
        
        done.put(markedAutomaton, new Integer(0));
        todo.push(0);
        scc.addArc(-1,-1,markedAutomaton); // only markedAutomaton are added
        
        while (!todo.isEmpty()) {
            int sourceIndex = todo.getNodeIndex();
            int letter = todo.getLetter();
            
            if (letter < alphabetSize) {
                MarkedAutomaton source = scc.getNode(sourceIndex);
                MarkedAutomaton target = source.succ(letter);
                todo.incLetter();
                
                MarkedSharedAutomaton o = cache.get(target);
                if (o== null)
                    o = (MarkedSharedAutomaton) localCache.get(target);
                else
                    localCache.put(target,o);
                    
                if (o == null) { // target is not in the cache
                    Object doneTarget = done.get(target);
                    
                    if (doneTarget == null) {// target is a new state
                        todo.push(scc.currentSize);
                        done.put(target, new Integer(scc.currentSize));
                        scc.addArc(sourceIndex,letter,target);
                    }
                    else { // target is a current state
                        int targetIndex = ((Integer) doneTarget).intValue();
                        scc.addArc(sourceIndex,letter,targetIndex);
                    }
                }
                else { // target is in the cache
                    scc.setBorderArc(sourceIndex,letter,o);
                }
            }
            else { // all succ of source are computed
                todo.removeTop();
                if (sourceIndex == scc.getTopScc()) { // source is a scc top state
                    MarkedSharedAutomaton markedSharedSource = scc.resumeScc(localCache,done);
                    if (!todo.isEmpty()) {
                        scc.setBorderArc(todo.getNodeIndex(), todo.getLetter()-1,markedSharedSource);
                    }
                    else {
                        // update localCache
                        level --;
                        if (level == 0) {
                            Set set = localCache.entrySet();
                            Iterator iter = set.iterator();
                            int u=0;
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                if ( u % RATE == 0)
                                    cache.put((MarkedAutomaton) entry.getKey(),(MarkedSharedAutomaton) entry.getValue());
                                u++;
                                iter.remove();
                            }                            
                            assert localCache.isEmpty();
                        }
                        return markedSharedSource;
                    }
                }
            }
        }
        throw new Error("bug ! ");
//        return null;
    }
}



/* Structure for "todo" double iterator */

class ArcStack
{
    static int INIT_TABLE_SIZE = 100;
    int size= 0;
    
    int[] nodeLetter = new int[2*INIT_TABLE_SIZE];

    boolean isEmpty()
    {
        return size == 0;
    }

    int getNodeIndex()
    {
        return nodeLetter[size-2];
    }

    int getLetter()
    {
        return nodeLetter[size-1];
    }
    
    void incLetter()
    {
        nodeLetter[size-1]++;
    }
    
    void push(int n)
    {
        if (size == nodeLetter.length) {
            int[] newNodeLetter = new int[2*size];
            System.arraycopy(nodeLetter,0,newNodeLetter,0,size);
            nodeLetter = newNodeLetter; 
        }
        
        nodeLetter[size] = n;
        nodeLetter[size+1] = 0;
        size+=2;
    }

    void removeTop()
    {
        size-=2;
    }
}

class SccStack
{
    static int INIT_TABLE_SIZE = 100;

    int nbScc = 0;
    int currentSize= 0;
    int borderSize= 0;
    int alphabetSize = 1;

    MarkedAutomaton[] currentNodes = new MarkedAutomaton[INIT_TABLE_SIZE];
    int[] succ = new int[INIT_TABLE_SIZE];
    BorderArc[] borderArc = new BorderArc[INIT_TABLE_SIZE];

    int[] currentNodesLimit = new int[INIT_TABLE_SIZE];
    int[] borderNodesLimit = new int[INIT_TABLE_SIZE];

    MarkedAutomaton getNode(int index)
    {
        return currentNodes[index];
    }
    
    int getTopScc()
    {
        return currentNodesLimit[nbScc-1];
    }

    void addArc(int source, int a, int target)
    {
        succ[alphabetSize*source+a] = target;
        while (currentNodesLimit[nbScc-1] > target) 
            nbScc --;
    }

    void addArc(int source, int a, MarkedAutomaton target)
    {
        if (source == -1) {
            alphabetSize = target.getAlphabetSize();
            if (succ.length < alphabetSize)
                succ = new int[INIT_TABLE_SIZE*alphabetSize];
        }
        assert (alphabetSize>0);
    
        if (currentSize == currentNodes.length) 
            currentNodes = resize(currentNodes);
            
        if ((currentSize+1)*alphabetSize > succ.length)
            succ = resize(succ);
        
        if (nbScc == currentNodesLimit.length) {
            currentNodesLimit = resize(currentNodesLimit);
            borderNodesLimit = resize(borderNodesLimit);
        }
        
        currentNodes[currentSize] = target;
        
        currentNodesLimit[nbScc] = currentSize;
        borderNodesLimit[nbScc] = borderSize;

        if (source != -1)
            succ[alphabetSize*source+a] = currentSize;
            
        currentSize ++;
        nbScc ++;
    }

    void setBorderArc(int source, int a, MarkedSharedAutomaton target)
    {
        if (borderSize == borderArc.length)
            borderArc = resize(borderArc);
            
        succ[alphabetSize*source+a] = -1-borderSize;
        borderArc[borderSize] = new BorderArc(source,a,target);
        borderSize ++;
    }

    MarkedSharedAutomaton resumeScc(HashMap localCache, HashMap done)
    {
        MarkedSharedAutomaton[] bindFunction;
                
        // compute bindFunction and update succ
        if (borderNodesLimit[nbScc-1] < borderSize) {
            Arrays.sort(borderArc,borderNodesLimit[nbScc-1],borderSize);
        
            // update succ
            final int borderFirst = borderNodesLimit[nbScc-1];
            
            int bindFunctionSize = 1;
            succ[alphabetSize*borderArc[borderFirst].source + borderArc[borderFirst].letter] = -1;
            
            for (int b=borderFirst+1; b <borderSize; b++) {
                if (borderArc[b].target != borderArc[b-1].target)
                    bindFunctionSize ++;
                succ[alphabetSize*borderArc[b].source + borderArc[b].letter] = -bindFunctionSize;
            }
            
            bindFunction = new MarkedSharedAutomaton[bindFunctionSize];
            bindFunction[0] = borderArc[borderFirst].target;

            int bindFunctionIndex = 1;
            for (int b=borderFirst+1; b <borderSize; b++)
                if (borderArc[b].target != borderArc[b-1].target) {
                    bindFunction[bindFunctionIndex] = borderArc[b].target;
                    bindFunctionIndex ++;
                }
        }
        else
            bindFunction = new MarkedSharedAutomaton[0];
            
        // create succ
        int localFirst = currentNodesLimit[nbScc-1];
        int nbLocalNodes = currentSize - localFirst;
        
        int[] newSucc = new int[nbLocalNodes*alphabetSize]; 
        System.arraycopy(succ, localFirst*alphabetSize, newSucc, 0, nbLocalNodes*alphabetSize);
        
        // a revoir
        for (int pa=0;pa<alphabetSize*nbLocalNodes;pa++)
            if (newSucc[pa] >= localFirst)
                newSucc[pa] -= localFirst;
            
        // create isFinal
        boolean isFinal[] = new boolean[nbLocalNodes];
        for (int p=0; p<nbLocalNodes; p++) 
            isFinal[p] = currentNodes[p+localFirst].isFinal();
                
        // create OutputAutomaton and SharedAutomaton
        OutputAutomaton outputAutomaton = new OutputAutomaton(newSucc,isFinal, bindFunction.length);
        SharedAutomaton sharedAutomaton = new SharedAutomaton(outputAutomaton, bindFunction);
        
        // close operation
        int closeType = currentNodes[localFirst].getType();

        if (closeType == MarkedAutomaton.EXISTS)
            sharedAutomaton.existsForallClose(true);
        else if (closeType == MarkedAutomaton.FORALL)
            sharedAutomaton.existsForallClose(false);
                
        // one-zero heuristic
        MarkedSharedAutomaton zeroOne = sharedAutomaton.zeroOneHeuristic();
        if (zeroOne != null) {
                for (int p=0; p<nbLocalNodes; p++) {
                    localCache.put(currentNodes[localFirst+p],zeroOne);
                    done.remove(currentNodes[localFirst+p]);
                }
                cleanScc();
                return zeroOne;
        }

        // homomorphic check
        if (bindFunction.length !=0) {
            BorderArc border = borderArc[borderNodesLimit[nbScc-1]];
            MarkedSharedAutomaton[] relation = sharedAutomaton.homomorphicCheck(border.source - localFirst,border.letter,border.target);

            if (relation != null) { 
                for (int p=0; p<nbLocalNodes; p++) {
                    localCache.put(currentNodes[localFirst+p],relation[p]);
                    done.remove(currentNodes[localFirst+p]);
                }
                cleanScc();
                return relation[0];
            }
        }
        
        // minimizing outputAutomaton
        OutputAutomatonMinimization algo = new OutputAutomatonMinimization(outputAutomaton);
        algo.eval();
        OutputAutomaton minOutputAutomaton = OutputAutomaton.create(algo.output);
        SharedAutomaton minSharedAutomaton = SharedAutomaton.create(new SharedAutomaton(minOutputAutomaton,bindFunction));
        MarkedSharedAutomaton[] unique = minSharedAutomaton.uniqueMarkedSharedAutomaton;
            
        for (int p=0; p<nbLocalNodes; p++) {
            localCache.put(currentNodes[localFirst+p],unique[algo.relation[p]]);
            done.remove(currentNodes[localFirst+p]);
        }
        cleanScc();
        return unique[algo.relation[0]];
    }
    
    // useful functions
    private void cleanScc()
    {
        Arrays.fill(currentNodes,currentNodesLimit[nbScc-1],currentSize,null);
        Arrays.fill(borderArc,borderNodesLimit[nbScc-1],borderSize,null);
                
        currentSize = currentNodesLimit[nbScc-1];
        borderSize = borderNodesLimit[nbScc-1];
        nbScc--;
    }
    
    private BorderArc[] resize(BorderArc[] tab)
    {
        BorderArc[] res = new BorderArc[2*tab.length];
        System.arraycopy(tab,0,res,0,tab.length);
        return res;
    }

    private MarkedAutomaton[] resize(MarkedAutomaton[] tab)
    {
        MarkedAutomaton[] res = new MarkedAutomaton[2*tab.length];
        System.arraycopy(tab,0,res,0,tab.length);
        return res;
    }

    private int[] resize(int[] tab)
    {
        int[] res = new int[2*tab.length];
        System.arraycopy(tab,0,res,0,tab.length);
        return res;
    }

    public String toString()
    {
        StringBuffer res = new StringBuffer();
        res.append( "nbScc = " + nbScc + "\n");
        res.append( "currentSize = " + currentSize + "\n");
        res.append( "borderSize = " + borderSize + "\n");
    
        res.append("Current nodes limit: ");
        for (int i=0;i<nbScc; i++) {
            res.append(currentNodesLimit[i] +" ");
        }
        res.append("\n");

        res.append("Border nodes limit: ");
        for (int i=0;i<nbScc; i++) {
            res.append(borderNodesLimit[i] +" ");
        }
        res.append("\n");

        res.append("Border: ");
        for (int i=0;i<borderSize; i++) {
            res.append(borderArc[i] +" ");
        }
        res.append("\n");

        res.append("succ: \n");
        for (int i=0; i<currentSize; i++) {
            res.append("\t succ[" + i + "] = ");
            for (int a=0; a<alphabetSize; a++) 
                res.append( succ[i*alphabetSize+a] +" ");
            res.append("\n");
        }
        res.append("\n");
        
        return res.toString();
    }
}

class BorderArc implements Comparable
{
    int source;
    int letter;
    MarkedSharedAutomaton target;

    BorderArc(int source, int a, MarkedSharedAutomaton target)
    {
        this.source = source;
        this.letter = a;
        this.target = target;
    }

    public boolean equals(Object o)
    {
        BorderArc arc = (BorderArc) o;
        return target == arc.target;
    }
    
    public int compareTo(Object o)
    {
        BorderArc arc = (BorderArc) o;
        return target.compareTo(arc.target);
    }
    
    public String toString()
    {
        return "(" + source + "--" + letter + "->" + target.hashCode() + ") ";
    }
}
