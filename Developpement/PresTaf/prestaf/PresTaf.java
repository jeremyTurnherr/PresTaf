package prestaf;

public class PresTaf{
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
    {
		return npf.getNbStates();
    }
    
     public int getNbSharedAutomata(NPF npf)
    {
        return npf.getNbSharedAutomata();
    }

    public int getNbOutputAutomata(NPF npf)
    {
        return npf.getNbOutputAutomata();
    } 
    
    public String toDot(NPF npf)
    {
        return npf.toDot();
    }
	
	

}
