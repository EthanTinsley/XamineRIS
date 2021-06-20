package XamineRIS;

import java.util.ArrayList;

public class SuperUser extends User {
	
	int userId;
	private String firstName, lastName , email , userName;
	char[] password; 
	private boolean isActive, isStaff, isSuperUser ;
	private Permission userPermission ;
	//array list to simulate user table
	private ArrayList<User> userList = new ArrayList<>();
	
	public SuperUser(String firstName, String lastName, String email, String userName) {
		
		super(firstName, lastName, email, userName);
		
		userPermission.setAccessLvl(5) ;
		userPermission.setCodeName("SupUser");
		userPermission.setProgramName("Super User");
		userPermission.setName(userName, firstName, lastName) ;
		
	}//end SuperUser
	
	public User CreateNewUser(String userType) {
		
		
		/*When Create New User is selected a list of user types will be generated
		 * Text boxes for each user parameter will be generated when type is selected along with a confirm button
		 * Once boxes are filled and confirm button is clicked information will be passed along to database
		 */
		switch(userType){
		case("Super User"):
			SuperUser newSuperUser = new SuperUser( firstName,  lastName,  email,  userName);
			return newSuperUser;
		case("Referring Dr"):
			Doctor newDr = new Doctor( firstName,  lastName,  email,  userName);
			return newDr;
		case("Receptionist"):
			Receptionist newRecep = new Receptionist( firstName,  lastName,  email,  userName);
			return newRecep;
		case("Technician"):
			Technician newTech = new Technician( firstName,  lastName,  email,  userName);
			return newTech;
		case("Radiologist"):
			Radiologist newRadio = new Radiologist( firstName,  lastName,  email,  userName);
			return newRadio;
				
		}//end switch
		
		return null;
			
	}//end CreateNewUser
	
	public void ViewUserInfo() {
		//activated by button
		
		//brings up list of users, type + name + ID
		
		//Search by type, Name, or ID
		
		//Selecting user and Select button brings up full info for user
		
		//Edit and Remove buttons generated
		
		//should be in UIManager
		
	}//end ViewUserInfo
	
	public void EditUser(User selectedUser) {
		
		//Activated by selecting user then pressing Edit user button
		
		//brings up text box for each user parameter
		
		//maybe UI focused
		
		//submit changes button generated
		
		
		
	}//end EditUser
	
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

	public void setPassword(char[] password) {
		this.password = password;
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

	public Permission getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(Permission userPermission) {
		this.userPermission = userPermission;
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	public void DeleteUser(User selectedUser) {
		
		//Activated by selecting user then pressing Remove User button
		
		//Ask for confirmation, generates Confirm and Cancel buttons
		
		//Remove user from table
		
		userList.remove(selectedUser);
		
	}//end DeleteUser
	

	

}
