
/*
import org.keplerproject.luajava.*;


public class TestLua{
	
	
	
	public static void main(String[] argv){//avec ligne commande
		
		
		LuaState luaState = LuaStateFactory.newLuaState();
        luaState.openLibs();
        try {
            luaState.pushObjectValue(new PresburgerBridge(luaState));
            luaState.setGlobal("prestaf");
        } catch (LuaException e) {
            e.printStackTrace();
        }
        System.out.println("---------");
        luaState.LdoFile("testInterface.lua");
	}
	
}
*/