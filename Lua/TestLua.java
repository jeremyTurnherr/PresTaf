
/*
import org.keplerproject.luajava.*;


public class TestLua{
	
	
	public static class People{
		
		public static void Wowie(int a){
			System.out.println("wow");
		}
	}
	
	
	public static int add(int a, int b){
		return a+b;
	}
	
	public static void maintic(String[] args){
		LuaState L = LuaStateFactory.newLuaState();
		L.openLibs();
		
		L.LdoFile("TestLua.lua");
		
		System.out.println("Hello World from Java!");
		
	}
	
	
	public static void maintoc(String[] argv){//lua normal
		LuaState luaState = LuaStateFactory.newLuaState();
        luaState.openLibs();
        try {
            luaState.pushObjectValue(new javaaa());
            luaState.setGlobal("people");
        } catch (LuaException e) {
            e.printStackTrace();
        }
        luaState.LdoFile("TestLua.lua");
	}
	
	public static void maintac(String[] argv){//avec prestaf
		LuaState luaState = LuaStateFactory.newLuaState();
        luaState.openLibs();
        try {
            luaState.pushObjectValue(new javaaa());
            luaState.setGlobal("prestaf");
        } catch (LuaException e) {
            e.printStackTrace();
        }
        System.out.println("---------");
        luaState.LdoFile("TestLuaPrestaf.lua");
	}
	
	public static void maintuc(String[] argv){//avec prestaf et import
		System.out.println("import");
		LuaState luaState = LuaStateFactory.newLuaState();
        luaState.openLibs();
        try {
            luaState.pushObjectValue(new javaaa());
            luaState.setGlobal("prestaf");
        } catch (LuaException e) {
            e.printStackTrace();
        }
        System.out.println("---------");
        luaState.LdoFile("TestImport.lua");
	}
	
	public static void maintec(String[] argv){//avec ligne commande
		for (String s:argv){
			System.out.println(s);
		}
		
		LuaState luaState = LuaStateFactory.newLuaState();
        luaState.openLibs();
        try {
            luaState.pushObjectValue(new javaaa());
            luaState.setGlobal("prestaf");
        } catch (LuaException e) {
            e.printStackTrace();
        }
        System.out.println("---------");
        luaState.LdoFile(argv[0]);
	}
	
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