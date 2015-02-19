package ftp.user;

/**
 * This class represents one user of the FTP service. It is determined by its username, its password and its home directory and its access on the directory
 * @author rakotoarivony
 *
 */
public class User {
	private String username;
	private String password;
	private String directory;
	private boolean writeAccess;
	private boolean readAccess;
	
	public User(String username,String password,String directory,boolean writeAccess,boolean readAccess) {
		this.username = username;
		this.password = password;
		this.directory = directory;
		this.writeAccess = false;
		this.readAccess = false;
	}
	
	public boolean getWriteAccess(){
		return this.writeAccess;
	}
	
	public boolean getReadAccess(){
		return this.readAccess;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setWriteAccess(boolean access) {
		this.writeAccess = access;
	}
	
	public void setReadAccess(boolean access) {
		this.readAccess = access;
	}
	
}
