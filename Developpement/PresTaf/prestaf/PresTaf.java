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
			succ[state][letter]=t;
		}
		
		public void fillfinal(int state,boolean fin){
			isFinal[state]=fin;
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
    
    public PresTaf Not()
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
	
	public int getNbStates(){
		return npf.getNbStates();
    }
    

     public int getNbSharedAutomata()

    {
        return npf.getNbSharedAutomata();
    }

    public int getNbOutputAutomata()

    {
        return npf.getNbOutputAutomata();
    } 

    public String toDot()

    {
        return npf.toDot();
    }
	
	

}
