package XamineRIS;
public class Receptionist extends User {
	
	private int userId ;
	private String  firstName, lastName , password , email , userName; 
	private boolean isActive, isStaff, isSuperUser ;
	private Permission userPermission ;
	
public Receptionist(String firstName, String lastName, String email, String userName) {
		
		super(firstName, lastName, email, userName);
		
		userPermission.setAccessLvl(2) ;
		userPermission.setCodeName("SupUser");
		userPermission.setProgramName("Super User");
		userPermission.setName(userName, firstName, lastName) ;
		
	}//end SuperUser

public Boolean AssignedAppointment(String orderId) {//Notifies team when appointment is made, should be in receptionist class
	
	Boolean apptScheduled = false;
	
	return apptScheduled;
	
}//end AssignedAppointment

}
