//
//  MinimalAutomaton.java
//  MinAutomaton
//
//  Created by couvreur on Mon Jun 16 2003.
//  Copyright (c) 2003 __MyCompanyName__. All rights reserved.
//
package prestaf;

import java.util.TreeSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.BitSet;

public final class MinimalAutomatonHopcroft 
{

    OutputAutomaton input;
    OutputAutomaton output;
    int[] relation;

    // Simple informations
    static int nbLocalStates = 0;
    static int nbOutputStates = 0;
    static int alphabetSize = 0;

    /* Description automate original */
    final int INITSIZE = 10;
    static int[][] pred = new int[0][];

    /* Description automate minimal */
    static int nbClass = 0;
    int[] classMap; // node -> class
    int[] classSucc;
    boolean[] classAccepted;
    
    /* Elements pour algo d minimisation*/
    static  int[] nodeMap;
    static Node[] nodeTable = new Node[0];
    static NodeClassSet toDo;

    /* affine structures */    
    static MyNodeClassSet predNodeClassSet; // pour la fonction affine
    static Node[] predNode;
    static int[] predNodeClassSize;

    public MinimalAutomatonHopcroft(OutputAutomaton input) {
    
        this.input = input;
    }

    public void eval()
    {
        init();
        affine();

        // Calcul de classMap
        classMap = new int[nbLocalStates];
        
        for (int i=0; i< nbLocalStates; i++)
            classMap[nodeTable[i].node] = nodeTable[i].nodeClass.index;
                        
        // Calcul de la fonction succ et accepted sur les classes
        classSucc = new int[nbClass*alphabetSize];
        classAccepted = new boolean[nbClass];
        
        for (int i=0; i< nbLocalStates; i++) {
            classAccepted[classMap[i]] = input.isFinal[i];
            for (int a=0; a< alphabetSize; a++) {
                int res = input.succ(i,a);
                if (res >= 0)
                    classSucc[alphabetSize*classMap[i]+a] = classMap[res];
                else
                    classSucc[alphabetSize*classMap[i]+a] = res;
            }
        }
        
        relation = classMap;
        output = new OutputAutomaton(classSucc,classAccepted,nbOutputStates);
        
        // clean
        for (int i=0; i< nbLocalStates; i++) {
            nodeTable[i].nodeClass = null;
        }
    }
        
    public void init() 
    {
        int oldNbLocalStates = nbLocalStates;
        int oldNbOutputStates = nbOutputStates;
        
        nbLocalStates = input.nbLocalStates;
        nbOutputStates = input.nbOutputStates;
        alphabetSize = input.alphabetSize;
    
        nbClass = 0;
    
        predEval();
                
        // construction de la classe initiale, nodeMap et nodeTable
        NodeClass nodeClass = new NodeClass(0,nbLocalStates, nbClass);
        nbClass ++;

        if (oldNbLocalStates < nbLocalStates) {
            nodeMap = new int[nbLocalStates];
            Node[] newNodeTable = new  Node[nbLocalStates];
            System.arraycopy(nodeTable,0,newNodeTable,0,oldNbLocalStates);
            nodeTable = newNodeTable;

            for (int i=0; i<oldNbLocalStates; i++) {
                nodeTable[i].set(i,nodeClass);
                nodeMap[i] = i;
            }
            for (int i=oldNbLocalStates; i<nbLocalStates; i++) {
                nodeTable[i] = new Node(i,nodeClass);
                nodeMap[i] = i;
            }
        }
        else {
            for (int i=0; i<nbLocalStates; i++) {
                nodeTable[i].set(i,nodeClass);
                nodeMap[i] = i;
            }
        }

        if (oldNbLocalStates+oldNbOutputStates < nbLocalStates+nbOutputStates)
            toDo = new NodeClassSet(nbLocalStates+nbOutputStates);
        
        // ajout des parametres dans todo
        for (int p= nbLocalStates; p<nbLocalStates+nbOutputStates; p++)
            toDo.put(new NodeClass(p,p+1,p));
        
        // init pour la fonction affine
        if (oldNbLocalStates < nbLocalStates) {
            predNodeClassSet = new NodeClassSet(nbLocalStates);
            predNodeClassSize = new int[nbLocalStates];
            predNode = new Node[nbLocalStates];
        }
    }

    /* Calcul de Pred */
    private void predEval()
    {
        if (pred.length <(nbLocalStates+nbOutputStates) *alphabetSize) {
            int[][] newPred = new int[(nbLocalStates+nbOutputStates)*alphabetSize][];
            System.arraycopy(pred, 0, newPred,0,pred.length);
            
            for (int i=pred.length;i<newPred.length; i++)
                newPred[i] = new int[INITSIZE];
            
            pred = newPred;
        }
        
        for (int i=0; i<(nbLocalStates+nbOutputStates)*alphabetSize;i++)
            pred[i][0] = 0;

        for (int a=0; a<alphabetSize; a++)
        for (int p = 0; p<nbLocalStates; p++) {
            int q = getSucc(p,a);
            {
                int index = q*alphabetSize + a;
                int[] list = pred[index];
                if (list[0] == list.length -1) {
                    int[] newList = new int[list.length *2 +1];
                    System.arraycopy(list,0,newList,0,list.length);
                    list = newList;
                    pred[index] = newList;
                }
                list[0]++;
                list[list[0]] = p;
            }
        }
    }

    private int[] getPred(int i, int a)
    {
            return pred[alphabetSize*i+a];
    }

    private int getSucc(int i, int a)
    {
        int res = input.succ(i,a);
        if (res<0)
            return nbLocalStates -res -1;
        else
            return res;
    }

    private boolean isAccepted(int i)
    {
        if (i>=nbLocalStates)
            return false;
        else
            return input.isFinal[i];
    }


    /************** l'algo *****************************************/
    
    private void affineAccept()
    {
        int u = 0;
        int v = nbLocalStates-1;
        
        while (u<=v)
        {
            
            if (isAccepted(nodeTable[u].node))
                u ++;
            else if (!isAccepted(nodeTable[v].node))
                v--;
            else {
                permute(u,v);
                u++;
                v--;
            }
        }
        
        NodeClass initialNodeClass = nodeTable[0].nodeClass;
        
        if (u != 0 && v != nbLocalStates-1) {
            initialNodeClass.last = u;
            NodeClass newNodeClass = new NodeClass(u,nbLocalStates,nbClass);
            nbClass ++;

            for (int i=u; i<nbLocalStates; i++)
                nodeTable[i].nodeClass = newNodeClass;
                
            if (newNodeClass.size() < initialNodeClass.size())
                toDo.put(newNodeClass);
            else
                toDo.put(initialNodeClass);
        }
    }
    
    private void computePivots(NodeClass nodeClass)
    {   
        int letter = nodeClass.letter;
        
        // Parcours des predecesseurs des noeuds de nodeClass
        for (int i = nodeClass.first; i < nodeClass.last; i++) {
            int u;
            if (i < nbLocalStates)
                u = nodeTable[i].node;
            else
                u = i;

            int[] pred_u = getPred(u,letter);

            for (int v = 1; v<=pred_u[0]; v++) {
    
                int index_v = nodeMap[pred_u[v]];
                Node node_v = nodeTable[index_v];
                NodeClass nodeClass_v = node_v.nodeClass;
                int newIndex_v = nodeClass_v.first + predNodeClassSize[nodeClass_v.index];
                
                predNodeClassSet.put(nodeClass_v);
                
                
                predNode[newIndex_v] = node_v;
                predNodeClassSize[nodeClass_v.index] ++;
            }
        }
    }
    

    void predClassAffine()
    {
        
        while (!predNodeClassSet.isEmpty())
        {
            NodeClass nodeClass = predNodeClassSet.get();

            if (nodeClass.size() != predNodeClassSize[nodeClass.index]) {
                // ajout au debut
                NodeClass newNodeClass = new NodeClass(nodeClass.first, 
                                        nodeClass.first + predNodeClassSize[nodeClass.index],nbClass);
                nbClass ++;
                
                for (int i=newNodeClass.first; i<newNodeClass.last; i++) {
                    permute(i,nodeMap[predNode[i].node]);
                    nodeTable[i].nodeClass = newNodeClass;
                }
                
                nodeClass.first = newNodeClass.last;
                nodeClass.letter = 0;
                
                if (toDo.contains(nodeClass) || nodeClass.size() > newNodeClass.size())
                    toDo.put(newNodeClass);
                else 
                    toDo.put(nodeClass);
            }
            predNodeClassSize[nodeClass.index] = 0;
        }
    }
    
    void affine()
    {
        affineAccept();

        while (!toDo.isEmpty())
        {
            NodeClass nodeClass = toDo.get();

            computePivots(nodeClass);
            nodeClass.letter ++;

            if (nodeClass.letter < alphabetSize)
                toDo.put(nodeClass);
            
            predClassAffine();
        }
    }
    
    void permute(int u, int v)
    {
        if (u!=v) {
            Node node = nodeTable[u];
            nodeTable[u] = nodeTable[v];
            nodeTable[v] = node;
        
            nodeMap[nodeTable[u].node] = u;
            nodeMap[nodeTable[v].node] = v;
        }
    }
    
    
    public String toString()
    {
        StringBuffer res = new StringBuffer();

        for (int i=0; i<nbLocalStates; i++)
            res.append("(n" + nodeTable[i].node +", C" + nodeTable[i].nodeClass.index + ") ");
        res.append("\n");

        
        for (int i=0; i<nbLocalStates; i=nodeTable[i].nodeClass.last) {
            int classIndex = nodeTable[i].nodeClass.index;
            res.append("C" + classIndex + " :");
            for (int j = 0; j<predNodeClassSize[classIndex]; j++) {
                res.append(" n" +predNode[nodeTable[i].nodeClass.first+j].node);
            }
            res.append(", ");
        }
        res.append("\n");
        
        return res.toString();
    }

    // some static classes

    final static class Node
    {
        int node;
    
        NodeClass nodeClass;
    
        Node(int node, NodeClass nodeClass)
        {
            this.node = node;
            this.nodeClass = nodeClass;
        }

        void set(int node, NodeClass nodeClass)
        {
            this.node = node;
            this.nodeClass = nodeClass;
        }
    }


    static class NodeClass implements Comparable
    {
        int index;
        int letter;
        int first;
        int last;
    
        NodeClass(int first, int last, int index)
        {
            this.first = first;
            this.last = last;
            this.index = index;
            letter = 0;
        }

        public boolean equals(Object o)
        {
            NodeClass nc = (NodeClass) o;
            return index == nc.index;
        }
    
        public int compareTo(Object o)
        {
            NodeClass nc = (NodeClass) o;
            return index - nc.index;
        }
    
    
        int size()
        {
            return last-first;
        }
    }

/**************************************************************************/
    interface MyNodeClassSet
    {
        public void put(NodeClass nodeClass);
        public NodeClass get();
        public boolean isEmpty();     
    }

    final static class SimpleNodeClassSet implements MyNodeClassSet
    {
        BitSet belong;
        NodeClass[] table;
        
        int size = 0; 
        boolean putting = true;
        
        SimpleNodeClassSet(int n)
        {
            belong = new BitSet(n);
            table = new NodeClass[n];
        }

        public void put(NodeClass nodeClass)
        {
            if (!belong.get(nodeClass.index)) {
                putting = true;
                table[size] = nodeClass;
                belong.set(nodeClass.index);
                size++;
            }
        }
        
        public NodeClass get()
        {
            if (putting) {
                Arrays.sort(table,0,size);
                putting = false;
            }
            
            size --;
            NodeClass res = table[size];
            belong.set(res.index, false);
            table[size] = null;
            return res;
        }
                
        public boolean isEmpty() 
        {
            return size == 0;
        }
    }


/**************************************************************************/
    final static class NodeClassSet implements MyNodeClassSet
    {
        BitSet belong;
        NodeClass[] table;
        
        int size = 0; 
    
        NodeClassSet(int n)
        {
            belong = new BitSet(n);
            table = new NodeClass[n];
        }

        public boolean isEmpty()
        {
            return size == 0;
        }
    
        public boolean contains(NodeClass nodeClass)
        {
            return belong.get(nodeClass.index);
        }
    
        public void put(NodeClass nodeClass)
        {
            if (!contains(nodeClass)) {
                table[size] = nodeClass;
        
                for (int u=size; u >0 && table[(u+1)/2-1].compareTo(table[u]) < 0 ; u = (u+1)/2-1) {
                    swap(u,(u+1)/2-1);
                }
                belong.set(nodeClass.index);
                size ++;
            }
        }
    
        public NodeClass get()
        { 
            assert size>0;
            
            NodeClass res = table[0];
            belong.set(res.index,false);
            size --;

            table[0] = table[size];
            table[size] = null;
            
            int u = 0;
            boolean todo = true;
            while (todo && 2*u+1 < size) {
                todo = false;
                int v;
                if (2*u+2 < size) {
                    if (table[2*u+1].compareTo(table[2*u+2]) >0)
                        v = 2*u+1;
                    else
                        v = 2*u+2;
                }
                else
                    v = 2*u+1;
                    
                if (table[u].compareTo(table[v]) < 0) {
                    todo = true;
                    swap(u, v);
                    u = v;
                }
            }


            return res;
        }
        
        private void swap(int i, int j)
        {
            NodeClass tmp = table[i]; 
            table[i] = table[j];
            table[j] = tmp;
        }

    }
}
