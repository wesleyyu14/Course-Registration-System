interface AdminMethods {
		//permissions for Admin is TRUE a.k.a ON. 
			// (permissions for Student and other Users are FALSE / OFF)
	public boolean permission();
	public void setUsername(String username);
	public void setPassword(String password);
	public void setFirstName(String firstName);
	public void setLastName(String lastName);
	public String getUsername();
	public String getPassword();
	public String getFirstName();
	public String getLastName();
	public String getFullName();
	
}

public class Admin extends User implements AdminMethods {
	
	Admin() {
		super();
	}
	
	Admin(String username, String password) {
		//super(String username, String password); 
		this.username = username;
		this.password = password;
	}
	
	Admin(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public boolean permission() {
		return true;
	}
	
	
}
