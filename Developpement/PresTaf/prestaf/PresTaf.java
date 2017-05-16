package prestaf;

public class PresTaf{
	
	public static class PresTafMain{
		
		int[] param;
		
		public PresTafMain(
			
		){}
		
		public void init_tab(int size){
			param=new int[size];
		}
		
		public void fill(int luapos,int val){
			param[luapos-1]=val;
		}
		
		public void what(){
			System.out.println("wat");
		}
		
		public PresTaf equals( int b, int n) {
			int[] axi=param;
			System.out.println("tentative");
			
			return new PresTaf(NPF.equals(axi,b,n));
			//~ return null;
		}
		
		public PresTaf notEquals(int[] axi, int b, int n){
			return new PresTaf(NPF.notEquals(axi,b,n));
		}
		
		public PresTaf greater( int b, int n){
			int[] axi=param;
			return new PresTaf(NPF.greater(axi,b,n));	
		}
		
		public PresTaf greaterEquals(int[] axi, int b, int n) {
			return new PresTaf(NPF.greaterEquals(axi,b,n));	
		}
		
		public PresTaf less(int[] axi, int b, int n){
			return new PresTaf(NPF.less(axi,b,n));	
		}
		
		public PresTaf lessEquals(int[] axi, int b, int n){
			return new PresTaf(NPF.lessEquals(axi,b,n));	
		}
		
	}
	
	
	NPF npf;
	
	public PresTaf(NPF n){
		npf=n;
	}
	
	public String tostring(){
		return "wow"+npf;
	}
	
	
    public PresTaf addVariable(int v, int modulo){
		return new PresTaf(npf.addVariable(v,modulo));
	}
	
    public PresTaf addVariable(int v)
    {
        return new PresTaf(npf.addVariable(v));
    }
    
     
    public PresTaf addVariable(boolean[] tab){
		return new PresTaf(npf.addVariable(tab));
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

    public PresTaf or(PresTaf p)
    {
        assert (npf.nbVariable == p.npf.nbVariable);
        return new PresTaf(new NPF(npf.nbVariable, npf.value.or(p.npf.value)));
    }

    public PresTaf and(PresTaf p)
    {
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

    

    public PresTaf exists(boolean[] tab)
    {
        assert (npf.nbVariable % tab.length == 0 );

        int nbRemove = 0;
        for (int i=0; i<tab.length; i++)
            if (tab[i])
                nbRemove ++;
        
        return new PresTaf(new NPF( (npf.nbVariable/tab.length) * (tab.length-nbRemove),npf.value.exists(tab)));
    }



    public PresTaf forall(int v)
    {
        if (NPF.simpleExists)
            return new PresTaf(npf.forall1(v));
        else
            return new PresTaf(npf.forall2(v));
    }
	
	
	public int deapth(){
		return npf.deapth();
	}
	
	public int getNbStates()
    {
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
