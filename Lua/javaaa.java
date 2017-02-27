import application.*;


class javaaa{

	private int x;

	public javaaa(){
		x=0;
	}

	public void Wowie(){
		System.out.println("wow ");
	}

	public void Wowie_parameter(int a){
		System.out.println("woww "+a);
	}

	public void Wowie_inc(int a){
		x+=a;
		System.out.println("wowwwwwww "+x);
	}
	
	public void execPrestaf(String args){
		String[] params=new String[2];
		params[0]="";
		params[1]=args;
		PresburgerMain.main(params);
	} 
}
