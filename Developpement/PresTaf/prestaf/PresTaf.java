package prestaf;

public class PresTaf{

	
	public static class PresTafMain{
		
		int[] param;
		
		int[][] succ;
		boolean [] isFinal;
		
		public PresTafMain(
			
		){}

		public void init_succ(int alphabetSize,int netats){
			succ=new int[netats][];
			for (int i=0;i<netats;i++){
				succ[i]=new int[alphabetSize];
			}
		}
		
		public void init_final(int netats){
			isFinal=new boolean[netats];
		}
		
		public void fillsucc(int state,int letter,int t){
			succ[state-1][letter-1]=t;
		}
		
		public void fillfinal(int state,boolean fin){
			isFinal[state-1]=fin;
		}
		
		public SimpleMarkedAutomaton createAutomaton(int initial){
			return new SimpleMarkedAutomaton(initial,succ,isFinal);
			//~ return null;
		}
		
		public String printab(){
			String res="[";
			for (int i:param){
				res+=i+" ,";
			}
			return res+"]";
		}
		
		public void init_tab(int size){
			param=new int[size];
		}
		
		public void fill(int luapos,int val){
			param[luapos-1]=val;
		}
		
		
		
		public PresTaf equals( int b, int n) {
			int[] axi=param;
			
			
			return new PresTaf(NPF.equals(axi,b,n));
			//~ return null;
		}
		
		public PresTaf notEquals(int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.notEquals(axi,b,n));
		}
		
		public PresTaf greater( int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.greater(axi,b,n));	
		}
		
		public PresTaf greaterEquals( int b, int n) {
			int[] axi=param;
			return new PresTaf(NPF.greaterEquals(axi,b,n));	
		}
		
		public PresTaf less(int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.less(axi,b,n));	
		}
		
		public PresTaf lessEquals( int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.lessEquals(axi,b,n));	
		}
		
	}
	
	
	NPF npf;
	
	/*passage de paramètres*/
	
	boolean[] param;
	
	
	public void init_tab(int size){
		param=new boolean[size];
	}
	
	public void fill(int luapos,boolean val){
		param[luapos-1]=val;
	}
	//--
		
	
	
	public PresTaf(NPF n){
		npf=n;
	}
	
	public String tostring(){
		return npf+"";
	}
	
	
    public PresTaf addVariable(int v, int modulo){
		return new PresTaf(npf.addVariable(v,modulo));
	}
	
    public PresTaf addVariable(int v)
    {
        return new PresTaf(npf.addVariable(v));
    }
    
    		         /**
	* @return Sate numbers of the automata
	*/
     
    public PresTaf addVariable(){
		return new PresTaf(npf.addVariable(param));
	}
	
	/**
	*
	* @return true if automata recognize nothing
	*/
	
	public boolean isZero()
    {
        return npf.value == MarkedSharedAutomaton.zero;
    }
    
    	/**
	
	* @return true if automata recognize all
	*/

    public boolean isOne()
    {
        return npf.value == MarkedSharedAutomaton.one;
    }
    
    /**
	*
	* @return PresTaf which contains the new NPF for the not operation on the automata
	*/
    
    public PresTaf Not()
    {
        return new PresTaf(new NPF(npf.nbVariable, npf.value.not()));
    }
    
       /**
	* @param p p contains the automata for the Or operation
	* @return PresTaf which contains the new NPF for the Or operation on the automatas
	*/

    public PresTaf Or(PresTaf p)
    {
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.or(p.npf.value)));
    }
    
         /**
	* @param p p contains the automata for the And operation
	* @return PresTaf which contains the new NPF for the And operation on the automatas
	*/


    public PresTaf And(PresTaf p)
    {
		System.out.println("keskese");
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.and(p.npf.value)));
    }
        
         /**
	* @param p p contains the automata for the imply operation
	* @return PresTaf which contains the new NPF for the imply operation on the automatas
	*/

    

    public PresTaf imply(PresTaf p)
    {
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.imply(p.npf.value)));
    }
    
         /**
	* @param p p contains the automata for the equivalent operation
	* @return PresTaf which contains the new NPF for the equivalent operation on the automatas
	*/

    public PresTaf equiv(PresTaf p)
    {
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.equiv(p.npf.value)));
    }

         /**
	* @param v the index of the variable to valuate
	* @return PresTaf which contains the new NPF which has 1 state, one if can be true, zero else
	*/


    public PresTaf exists(int v)
    {
        return new PresTaf(npf.exists(v));
    }

         /**
	* @param v the index of the variable to valuate
	* @return PresTaf which contains the new NPF which has 1 state, one if always true, zero else
	*/

    public PresTaf forall(int v)
    {
        return new PresTaf(npf.forall(v));
    }
	
	         /**
	* @return Deapth of the automata
	*/
	
	public int deapth(){
		return npf.deapth();
	}
	
		         /**
	* @return Sate numbers of the automata
	*/
	public int getNbStates(){
		return npf.getNbStates();
    }
    
    		         /**
	* @return  Numbers of the shared automata
	*/

     public int getNbSharedAutomata()

    {
        return npf.getNbSharedAutomata();
    }

    public int getNbOutputAutomata()

    {
        return npf.getNbOutputAutomata();
    } 
    
    		         /**
	* @return to print the automata in graphviz
	*/

    public String toDot()

    {
        return npf.toDot();
    }
	
	

}
