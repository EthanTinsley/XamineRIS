package XamineRIS;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Doctor extends User {
	
	private int userID ;
	private String firstName, lastName , email , userName, password; 
	private boolean isActive, isStaff, isSuperUser ;
	private Permission userPermission ;
	
	public Doctor( String firstName, String lastName, String email, String userName) {
		super(firstName, lastName, email, userName);
		
		this.firstName = firstName ;
		this.lastName = lastName ;
		this.email = email ;
		this.userName = userName ;
		
	}
	
	public static Connection getConnection(){
		 try{
			   String driver = "com.mysql.cj.jdbc.Driver";
			   String url = "jdbc:mysql://localhost:3306/xaminedatabase";
			   String username = "root";
			   String password = "";
			   Class.forName(driver);
			   
			   Connection conn = DriverManager.getConnection(url,username,password);
			   System.out.println("Connected");
			   System.out.println("");
			   return conn;
			  } 
		 catch(Exception e){
			 System.out.println(e);
		}	  
		return null ;
	}
	
	public Patient[] returnPatient(String firstName, String lastName, String dateOfBirth, String email) throws SQLException {
		
		 Patient[] Patients = new Patient[5] ;
		 String  DoctorName = this.userName ;
		 int index = 0 ;
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select *"
		 							+ "From patient " + 
				 					  "Where firstName = ? " + 
				 					  "And lastName = ? " +
				 					  "AND email = ? " +
				 					  "AND dateOfBirth = ? ") ;
		 
		statement.setString(1, firstName);
		statement.setString(2, lastName);
		statement.setString(3, email);
		statement.setString(4, dateOfBirth);
		ResultSet result = statement.executeQuery() ;
		
		while(result.next() && index <= 4) {
			 Patients[index] = new Patient(result.getString("firstName"), result.getString("lastName")) ;
			 Patients[index].setPatientId(result.getInt("patientID"));
			 Patients[index].setEmail(result.getString("email"));
			 Patients[index].setDateOfBirth(result.getString("dateOfBirth"));
			 index++ ;
		 }
		
		result.close();
		
		return Patients ;
	}
	
	public Order[] returnOrders(String firstName, String lastName, String dateOfBirth, String email) throws SQLException {
		
		 Order[] Orders = new Order[5] ;
		 Patient[] Patients = returnPatient(firstName, lastName, dateOfBirth, email) ;
		 String  DoctorName = getUserName() ;
		 int index = 0 ;
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select OrderId, patientID, orderStatus, appointment, visitReason, imagingNeeded \r\n\t" + 
			 		"From imagingOrder" + "\r\n\t" +
				 	"Where patientID in ( Select patientID " + "\r\n\t" +
				 	"From patient" + "\r\n\t" + 
			 		"Where firstName = ? \r\n\t" + 
			 		"And lastName = ? \r\n\t" + 
			 		"AND email = ? \r\n\t" + 
			 		"AND dateOfBirth = ? " +
			 		"AND referringDoctorUserName = ? );" ) ;
		 
		 	statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, email);
			statement.setString(4, dateOfBirth);
			statement.setString(5, DoctorName);
			ResultSet result = statement.executeQuery() ;
			
			
			while(result.next() && index <= 4) {
				System.out.print(index);
				Orders[index] = new Order(result.getInt("OrderId"), Patients[0]);
				Orders[index].setImagingOrder(result.getString("imagingNeeded"));
				Orders[index].setVisitReason(result.getString("visitReason"));
				Orders[index].setOrderStatus(result.getString("orderStatus"));
				Orders[index].setApptTime(result.getString("appointment"));
				index++ ;
			}
			result.close();
			
		return Orders ;
		
	}
	
	public void newPatient(String firstName, String lastName, String dateOfbirth, String email, String gender, String Phone, 
			boolean latex, boolean xraydye, boolean mridye, String Notes  ) throws SQLException {
		
		int IDNum = 0 ;
		String  DoctorName = getUserName() ;
		
		Connection conn = getConnection() ;
		
		PreparedStatement newIdstatement = conn.prepareStatement("Select MAX(patientID) from patient ;") ;
	    ResultSet newIdNum = newIdstatement.executeQuery() ;
		
	    while(newIdNum.next()) {
	    IDNum = newIdNum.getInt("MAX(patientID)") ;
	    }
		newIdNum.close();
	    
	    IDNum++ ;
	    
	    PreparedStatement statement = conn.prepareStatement("Insert into patient(patientID, referringDoctorUserName, firstName, lastName, "
	    		+ "email, dateOfBirth, gender, allergyLatex, allergyXrayDye, allergyMridye , notes , phoneNumber) "
	    		+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? ) ;") ;
	    
	    statement.setInt(1 , IDNum );
	    statement.setString(2, DoctorName);
	    statement.setString(3, firstName);
	    statement.setString(4, lastName);
	    statement.setString(5, email);
	    statement.setString(6, dateOfbirth);
	    statement.setString(7, gender );
	    statement.setBoolean(8, latex);
	    statement.setBoolean(9,  xraydye);
	    statement.setBoolean(10, mridye);
	    statement.setString(11, Notes);
	    statement.setString(12, Phone);
	    
	    statement.execute();
	    
	    System.out.println("Data Successfully Uploaded");
	}
	
	public void newOrder( Patient patient , String imagesNeeded , String notes  ) throws SQLException {
		
		int OrderIDNum = 0 ;
		
		Connection conn = getConnection() ;
		
		PreparedStatement newIdstatement = conn.prepareStatement("Select MAX(OrderId) from imagingorder ;") ;
	    ResultSet newIdNum = newIdstatement.executeQuery() ;
		
	    while(newIdNum.next()) {
	    OrderIDNum = newIdNum.getInt("MAX(OrderId)") ;
	    }
		newIdNum.close();
	    
	    OrderIDNum++ ;
	    
	    PreparedStatement statement = conn.prepareStatement("Insert into imagingorder(orderID, patientID, orderStatus, appointment, "
	    		+ "visitReason, imagingNeeded, teamID, modalityID, imagefolderID, technicalReport, apptTime) "
	    		+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? ) ;") ;
	    
	    statement.setInt(1 , OrderIDNum );
	    statement.setInt(2, patient.getPatientId());
	    statement.setString(3 , "Unscheduled");
	    statement.setString(4, null);
	    statement.setString(5,  notes);
	    statement.setString(6,  imagesNeeded);
	    statement.setString(7,  null);
	    statement.setString(8,  null);
	    statement.setString(9,  null);
	    statement.setString(10, null);
	    statement.setString(11, null);
	   
	    
	    statement.execute() ;
	    
	    System.out.println("Data Successfully Uploaded");
	} 
	
	public static ArrayList<Order> ViewResults() throws SQLException {
		/*Searches through orders for current day's appointments
		 * Puts Orders for selected date in arraylist for UIManager to access and display
		 * UIManager displays orderID, appt time, room number, and imaging ordered
		 * User can select appt and press select button to display patient information
		 */		
		
		//Adding parameters for testing
		ArrayList<Order> completeOrders = new ArrayList<>();
		Patient currPatient ;
		int index = 0;
		Connection conn = getConnection();
		System.out.println(LocalDate.now()) ;
		PreparedStatement statement = conn.prepareStatement("Select * from imagingorder Where orderStatus = ? ; " ) ;
		statement.setString(1, "Complete");
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currPatient = ReturnPatient(result.getInt("patientID") ) ;
			completeOrders.add(index, new Order(result.getInt("orderID")));
			completeOrders.get(index).setImagingOrder(result.getString("imagingNeeded"));
			completeOrders.get(index).setVisitReason(result.getString("visitReason"));
			completeOrders.get(index).setOrderStatus(result.getString("orderStatus"));
			completeOrders.get(index).setRadioAnalysis(result.getString("technicalReport"));
			
			completeOrders.get(index).setPatient(currPatient);
			
			index++ ;
		}
		
		System.out.println(" Results found successfully");
		return completeOrders ;
	}
	
	public static Patient ReturnPatient(int ID) throws SQLException {
		
		 Patient currPatient = new Patient("", "");
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from patient Where patientID  = ?  ;") ;
		 
		statement.setInt(1, ID );
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			 currPatient = new Patient(result.getString("firstName"), result.getString("lastName")) ;
			 currPatient.setPatientId(result.getInt("patientID"));
			 currPatient.setEmail(result.getString("email"));
			 currPatient.setPhoneNumber(result.getString("phoneNumber"));
			 currPatient.setAllergyLatex(result.getBoolean("allergyLatex"));
			 currPatient.setAllergyMridye(result.getBoolean("allergyMridye"));
			 currPatient.setAllergyXraydye(result.getBoolean("allergyXrayDye"));
			 currPatient.setNotes(result.getString("notes"));
			 currPatient.setDateOfBirth(result.getString("dateOfBirth"));
	}
		 
		
		result.close();
		
		return currPatient;
	}
	
public static ArrayList<ImageFile> getOrderImages(int OrderID) throws SQLException {
		
		ArrayList<ImageFile> images = new ArrayList<ImageFile>() ;
		
		Connection conn = getConnection() ;
		PreparedStatement statement = conn.prepareStatement("Select * from image Where orderID  = ?  ;") ;
		 
		statement.setInt(1, OrderID );
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			ImageFile image = new ImageFile(result.getInt("imageID"), null , result.getString("pathName"), result.getString("imagelabel"), null);
			images.add(image) ;
			System.out.println(image.getLabel());
		}
		
		return images ;
	}

	public int getUserId() {
		return userID;
	}

	public void setUserId(int userId) {
		this.userID = userId;
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


	public void setPassword(String password) {
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
	
}
