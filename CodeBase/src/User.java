package XamineRIS;


public class User {

	private int userId;
	private String firstName;
	private String lastName;
	private char[] password;
	private String email;
	private String userName; 
	private boolean isActive, isStaff, isSuperUser ;
	private Permission userPermission ;
	
	
	// TO-DO 
	// Fix password char Array 
	// 
	// set password 
	
	

	public User( String firstName, String lastName, String email, String userName){
		
		this.userName = userName ;
		this.firstName = firstName ;
		this.lastName = lastName ;
		this.email = email;
		
	
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(String password , int count) {
		char[] securePassword = new char[count];
		
		for(int i = 0; i < password.length(); i++) {
			securePassword[i] = password.charAt(i);
			
		}
		this.password = securePassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isStaff() {
		return isStaff;
	}

	public void setStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}

	public boolean isSuperUser() {
		return isSuperUser;
	}

	public void setSuperUser(boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
	}
	
	public String toString(){
		String result ;
		
		result = "Username: " + userName + "\n" + "Name: " + firstName + " " +
				 lastName + "\n" + "Email: " + email ;
		
		return result ;
		
	}
	
	public Permission getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(Permission userPermission) {
		this.userPermission = userPermission;
	}
	
	
}
