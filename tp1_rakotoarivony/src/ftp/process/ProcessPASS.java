package ftp.process;

public class ProcessPASS implements ProcessCommand {
	
	private String userName;

	public ProcessPASS(String userName){
		this.userName = userName;
	}

	@Override
	public int process(String password) {
		if(password.equals("password")){
			return 230;
		}
		else return 530;
	}

}
