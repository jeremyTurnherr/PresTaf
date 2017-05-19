package prestaf;

public class PresTaf{
<<<<<<< HEAD
	
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
			succ[state][letter]=t;
		}
		
		public void fillfinal(int state,boolean fin){
			isFinal[state]=fin;
		}
		
		public PresTaf createAutomaton(int initial){
			//~ return new PresTaf(new NPF(0,(MarkedSharedAutomaton)(new SimpleMarkedAutomaton(initial,succ,isFinal))));
			return null;
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
    
     
    public PresTaf addVariable(){
		return new PresTaf(npf.addVariable(param));
	}
	
	public boolean isZero()
    {
        return npf.value == MarkedSharedAutomaton.zero;
    }

    public boolean isOne()
    {
        return npf.value == MarkedSharedAutomaton.one;
    }
    
    public PresTaf not()
    {
        return new PresTaf(new NPF(npf.nbVariable, npf.value.not()));
    }

    public PresTaf Or(PresTaf p)
    {
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.or(p.npf.value)));
    }

    public PresTaf And(PresTaf p)
    {
		System.out.println("keskese");
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.and(p.npf.value)));
    }
    

    public PresTaf imply(PresTaf p)
    {
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.imply(p.npf.value)));
    }
    public PresTaf equiv(PresTaf p)
    {
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.equiv(p.npf.value)));
    }

    

    public PresTaf exists(int v)
    {
        return new PresTaf(npf.exists(v));
    }



    public PresTaf forall(int v)
    {
        return new PresTaf(npf.forall(v));
    }
	
	
	public int deapth(){
		return npf.deapth();
	}
	
	public int getNbStates()
=======
	private static PresTaf instance;
	
	private PresTaf(){
		}
	
	
	public static PresTaf init(){
		if(instance==null){
			instance=new PresTaf();
		}
		return instance;
	}
	
	
    public NPF addVariable(NPF npf,int v, int modulo){
		return npf.addVariable(v,modulo);
	}
	
    public NPF addVariable(NPF npf,int v)
    {
        return npf.addVariable(v);
    }
    
     
    public NPF addVariable(NPF npf,boolean[] tab){
		return npf.addVariable(tab);
	}
	
	
	public NPF equals(int[] axi, int b, int n) {
		return NPF.equals(axi,b,n);
	}
	
	public NPF notEquals(int[] axi, int b, int n){
		return NPF.notEquals(axi,b,n);
	}
	
	public NPF greater(int[] axi, int b, int n){
		return NPF.greater(axi,b,n);	
	}
	
	public NPF greaterEquals(int[] axi, int b, int n) {
		return NPF.greaterEquals(axi,b,n);	
	}
	
	public NPF less(int[] axi, int b, int n){
		return NPF.less(axi,b,n);	
	}
	
	public NPF lessEquals(int[] axi, int b, int n){
		return NPF.lessEquals(axi,b,n);	
	}
	
	public int deapth(NPF npf){
		return npf.deapth();
	}
	
	public int getNbStates(NPF npf)
>>>>>>> 68108cfd04ff7b1c3f738838c48a6c03b03e22ca
    {
		return npf.getNbStates();
    }
    
<<<<<<< HEAD
     public int getNbSharedAutomata()
=======
     public int getNbSharedAutomata(NPF npf)
>>>>>>> 68108cfd04ff7b1c3f738838c48a6c03b03e22ca
    {
        return npf.getNbSharedAutomata();
    }

<<<<<<< HEAD
    public int getNbOutputAutomata()
=======
    public int getNbOutputAutomata(NPF npf)
>>>>>>> 68108cfd04ff7b1c3f738838c48a6c03b03e22ca
    {
        return npf.getNbOutputAutomata();
    } 
    
<<<<<<< HEAD
    public String toDot()
=======
    public String toDot(NPF npf)
>>>>>>> 68108cfd04ff7b1c3f738838c48a6c03b03e22ca
    {
        return npf.toDot();
    }
	
	

}
