package prestaf;

public class PresTaf{

	
	public static class PresTafMain{
		
		int[] param;
		
		int[][] succ;
		boolean [] isFinal;
		
		public PresTafMain(
			
		){}

			/**
			 * Function used to initialize the java 2 dimensional array in order to pass the array of successors
		* @param alphabetSize the alphabet size
		* @param netats the number of states
		*/

		public void init_succ(int alphabetSize,int netats){
			succ=new int[netats][];
			for (int i=0;i<netats;i++){
				succ[i]=new int[alphabetSize];
			}
		}

		/**
			 * Function used to initialize the java boolean array in order to pass the array of final states
		* @param netats the number of states
		*/
		
		public void init_final(int netats){
			isFinal=new boolean[netats];
		}

		/**
			 * Function used fill one index of the successor array (use this after initializing the array)
		* @param state the index of state
		* @param letter the letter of the transition
		* @param t the state successor of the transition
		*/
		
		public void fillsucc(int state,int letter,int t){
			succ[state-1][letter-1]=t;
		}

		/**
			 * Function used fill one index of the final states array (use this after initializing the array)
		* @param state the index of state
		* @param fin if the state is final or not
		*/
		
		public void fillfinal(int state,boolean fin){
			isFinal[state-1]=fin;
		}

		/**
			 * Function used to get the automaton associated with the arrays of successors and final states previously filled
		* @param initial the index of the initial state
		* @return an automaton
		*/
		
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
		/**
			 * Function used initialize the java array corresponding to the variables and the coefficients
		* @param size the size of the array, corresponding to two times the number of variables
		*/
		
		public void init_tab(int size){
			param=new int[size];
		}

		/**
			 * Function used fill one index of the variable/coefficient array (use this after initializing the array)
		* @param luapos the position of the value in the lua table
		* @param val the index of the variable or the coefficient
		*/
		
		public void fill(int luapos,int val){
			param[luapos-1]=val;
		}
		
		/**
		* @param b the constant
		* @param n the number of states
		* @return PresTaf which contains the new NPF for the equals operation using the array of variables previously filled
		*/
		
		public PresTaf equals( int b, int n) {
			int[] axi=param;
			
			
			return new PresTaf(NPF.equals(axi,b,n));
		}

		/**
		* @param b the constant
		* @param n the number of states
		* @return PresTaf which contains the new NPF for the not equals operation using the array of variables previously filled
		*/
		
		public PresTaf notEquals(int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.notEquals(axi,b,n));
		}

		/**
		* @param b the constant
		* @param n the number of states
		* @return PresTaf which contains the new NPF for the greater operation using the array of variables previously filled
		*/
		
		public PresTaf greater( int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.greater(axi,b,n));	
		}

		/**
		* @param b the constant
		* @param n the number of states
		* @return PresTaf which contains the new NPF for the greater equals operation using the array of variables previously filled
		*/
		
		public PresTaf greaterEquals( int b, int n) {
			int[] axi=param;
			return new PresTaf(NPF.greaterEquals(axi,b,n));	
		}

		/**
		* @param b the constant
		* @param n the number of states
		* @return PresTaf which contains the new NPF for the less operation using the array of variables previously filled
		*/
		
		public PresTaf less(int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.less(axi,b,n));	
		}

		/**
		* @param b the constant
		* @param n the number of states
		* @return PresTaf which contains the new NPF for the less equals operation using the array of variables previously filled
		*/
		
		public PresTaf lessEquals( int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.lessEquals(axi,b,n));	
		}
		
	}
	
	
	NPF npf;
	
	/*passage de paramètres*/
	
	boolean[] param;
	
	/**
			 * Function used to initialize the java array in order to pass the array of variables
		* @param size the number of variables
		*/
		
	public void init_tab(int size){
		param=new boolean[size];
	}

	/**
			 * Function used fill one index of the variables array (use this after initializing the array)
		* @param luapos the position of the variable in the lua table
		* @param val if the variable already exists
		*/
	
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
	* @return new PresTaf containing the npf value with the variables of another npf added. the variables correspond to the param array previously filled
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
