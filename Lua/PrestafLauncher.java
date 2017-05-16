
import application.*;
import prestaf.*;
import org.keplerproject.luajava.*;



public class PrestafLauncher{
	
	
	
	
	public static void main(String[] argv){
		if (argv.length<1){
			System.out.println("Use: java -cp .;luajava-1.1.jar;../Developpement/PresTaf  PrestafLauncher [filename.lua]");
		}else{
		
			LuaState luaState = LuaStateFactory.newLuaState();
			luaState.openLibs();
			try {
				luaState.pushObjectValue(new PresTaf.PresTafMain());
				luaState.setGlobal("prestaf");
			} catch (LuaException e) {
				e.printStackTrace();
			}
			System.out.println("---------");
			luaState.LdoFile(argv[0]);
		}
	}
	
}
