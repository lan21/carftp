package ftp.process;

public class ProcessSYST implements ProcessCommand {

	public ProcessSYST instance = new ProcessSYST();
	
	private ProcessSYST(){};
	
	@Override
	public int process(String param) {
		return 215;
	}

}
