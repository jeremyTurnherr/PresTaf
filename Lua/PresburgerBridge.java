


import application.*;
import application.*;
import java.io.*;
import org.keplerproject.luajava.*;


class PresburgerBridge{
	
	Presburger p;
	LuaState state;
	
	public PresburgerBridge(LuaState s){
		state=s;
		System.out.println("bridge initialized");
	}
	
	//~ PresburgerBridge(String s, int[] v, NPF val)
    //~ {
        //~ expr = s;
        //~ var = v;
        //~ value = val;
    //~ }
	
	public void set_formula(String file){
		
		PresburgerMonteurYacc yyparser;
        try {
            yyparser = new PresburgerMonteurYacc(new FileReader(new File(file)),true);
            p = yyparser.parse();
        }
        catch(Throwable e) {
            System.out.println(e.toString());
            p = null;
        }
        System.out.println("-->"+p);
	}
	
	
	
	
    public String toString()
    {
		if (p!=null){
			return p.toString();
		}else{
			return "null";
		}
    }
    
    public int getNbStates()
    {
        return p.getNbStates();
    }
    
    public int getNbSharedAutomata()
    {
        return p.getNbSharedAutomata();
    }

    public int getNbOutputAutomata()
    {
        return p.getNbOutputAutomata();
    }  

    public String statistics()
    {
        return p.statistics();
        
    }
    
    public PresburgerBridge New(String newobject){
		PresburgerBridge res=null;
        try {
			res=new PresburgerBridge(state);
            state.pushObjectValue(res);
            state.setGlobal(newobject);
        } catch (LuaException e) {
            e.printStackTrace();
        }
        return res;
	}
	
	public PresburgerBridge newtest(){
		return new PresburgerBridge(state);
	}
	
	public void opand(PresburgerBridge pb,String newobject){
		System.out.println("opand");
		PresburgerBridge res=New(newobject);
		System.out.println("opand");
		System.out.println(">>> "+res);
		
		res.p=p.and(pb.p);
	}

    
	
	
	
	
	
}

