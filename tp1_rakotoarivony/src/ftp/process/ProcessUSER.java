package ftp.process;

public class ProcessUSER implements ProcessCommand {
	
	private String userName;
	public ProcessUSER instance = new ProcessUSER();
	
	private ProcessUSER(){
		this.userName = "myTest";
	}

	@Override
	public int process(String param) {
		if(param.equals(this.userName))
			return 331;
		else if(param.equals("anonymous")){
			return 230;
		}
		else
			return 332;
	}

}
