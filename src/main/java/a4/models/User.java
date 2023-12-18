package a4.models;

public class User {
	
	private String username; 
	private String password; 
	private String email; 
	private float balance; 
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(float balance) {
		this.balance = balance;
	}
}
