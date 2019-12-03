//requirements 1-4 are looser 
//requirements 5-12 are "hard". Make sure to follow these requirements.\

public abstract class User implements java.io.Serializable {
	String username;
	String password;
	String firstName = null;
	String lastName = null; 
	String fullName = null;
	
	User() {
	}
	
	User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public boolean permission() {
		return false;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFullName() {
		this.fullName = this.firstName + " " + this.lastName;
		return this.fullName;
	}
	
}

