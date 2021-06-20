package XamineRIS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * On login, portal is generated with options to view all scheduled appointments for current day, along with option to view all open orders  
 * 
 * 
 */
public class Technician extends User {
	private int  userId;
	private String firstName, lastName , password , email , userName; 
	private boolean isActive, isStaff, isSuperUser ;
	private Permission userPermission ;
		
	
	public Technician(String firstName, String lastName, String email, String userName) {
		
		super(firstName, lastName, email, userName);
		
		userPermission.setAccessLvl(2) ;
		userPermission.setCodeName("Tech");
		userPermission.setProgramName("Technician");
		userPermission.setName(userName, firstName, lastName);
				
	}//end Technician
	
	public static Connection getConnection(){
		 try{
			   String driver = "com.mysql.cj.jdbc.Driver";
			   String url = "jdbc:mysql://localhost:3306/xaminedatabase";
			   String username = "root";
			   String password = "Restoration2021!";
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
	
	public static Patient returnPatient(int ID) throws SQLException {
		
		 Patient currPatient[] = new Patient[1] ;
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from patient Where patientID  = ?  ;") ;
		 
		statement.setInt(1, ID );
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			 currPatient[0] = new Patient(result.getString("firstName"), result.getString("lastName")) ;
			 currPatient[0].setPatientId(result.getInt("patientID"));
			 currPatient[0].setEmail(result.getString("email"));
			 currPatient[0].setDateOfBirth(result.getString("dateOfBirth"));
			
	}
		 
		
		result.close();
		
		return currPatient[0] ;
	}
	
	
	public static ArrayList<Order> ViewAppointments() throws SQLException {
		/*Searches through orders for current day's appointments
		 * Puts Orders for selected date in arraylist for UIManager to access and display
		 * UIManager displays orderID, appt time, room number, and imaging ordered
		 * User can select appt and press select button to display patient information
		 */		
		
		//Adding parameters for testing
		ArrayList<Order> todaysOrders = new ArrayList<>();
		ArrayList<ImageFile> images = new ArrayList<>();
		Patient currPatient ;
		int index = 0;
		Connection conn = getConnection() ;
		
		PreparedStatement statement = conn.prepareStatement("Select * from imagingorder Where orderStatus = ? ; " ) ;
		statement.setString(1, "Checked-In");
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currPatient = returnPatient(result.getInt("patientId")) ;
			images = getOrderImages(result.getInt("orderId"));
			todaysOrders.add(index, new Order(result.getInt("OrderId"), currPatient ));
			todaysOrders.get(index).setApptTime(result.getString("appointment"));
			todaysOrders.get(index).setImagingOrder(result.getString("imagingNeeded"));
			todaysOrders.get(index).setVisitReason(result.getString("visitReason"));
			todaysOrders.get(index).setOrderStatus(result.getString("orderStatus"));
			todaysOrders.get(index).setApptTime(result.getString("appointment"));
			todaysOrders.get(index).setImages(images);
			index++ ;
		}
		
		System.out.println(" Today's appointments found successfully");
		return todaysOrders ;
	}
	
	
	
	public static ArrayList<Order> ViewOrders() throws SQLException {
		/*Displays all uncompleted orders
		 * Puts open Orders in arraylist for UIManager to access and display
		 * UIManager displays orderID, appt time, room number, imaging ordered, and if any images are attached
		 * User can select order and press select button to display patient information
		 */
		
		//Adding parameters for testing

		ArrayList<Order> testOrders = ViewAppointments() ;		
		
		//List for UIManager 
		ArrayList<Order> openOrders = new ArrayList<>();
		
		
		//Iterates through Arraylist looking for open Order, will iterate through database Order table in final version
		for(int i = 0; i < testOrders.size(); i++) {
			if(testOrders.get(i).getOrderStatus() == "Checked-In" || testOrders.get(i).getOrderStatus() == "Imaging Complete"  ) {
				openOrders.add(testOrders.get(i));
			}//end if
			
		}//end for loop
		
		testOrders.clear();
		
		return openOrders;
		
	}
	
	public static void UpdateImages(Order order, int index) throws SQLException {
		//Updates selected order with checked in status, modality, and appt team *******
		
		//ADD SOME KIND OG FUNCTION TO DETERMINE imageID
				
				int orderID = order.getOrderID();
				String lbl = order.getImages().get(index).getLabel();
				int imageID = 0;
				String pathName = order.getImages().get(index).getPath();
				
				Connection conn = getConnection();
				
				//Will pull all images, check id, and set new id value to highest previous id plus 1
				PreparedStatement firstStatement = conn.prepareStatement("Select * from image");
				
				ResultSet result = firstStatement.executeQuery() ;
				
				while(result.next()) {
					imageID = result.getInt("imageID");
				};
				
				imageID++;
				order.getImages().get(index).setID(imageID);

				PreparedStatement secondStatement = conn.prepareStatement("Insert into image(orderID, imageID, imageLabel, imageDate, imageFile, pathName) Values(?, ?, ?, null, null, ?) ;") ;
				 
				secondStatement.setInt(1, orderID );
				secondStatement.setInt(2, imageID);
				secondStatement.setString(3, lbl);
				secondStatement.setString(4, pathName);

				secondStatement.executeUpdate() ;
				
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
	
	
	//---------------------------------------------End SQL Calls-----------------------------------------------------------
	
	//pretty sure this should be in UIManager handled by button, may move once more of the UI is developed
	public String SelectOrder(Order selectedOrder) {
	  
	  /*Displays Patient info, if patient has been checked in, and all previous info displayed
	   *Generates buttons to submit order, view images on order and to add images to order.
	  */
		
		//Display orderID, patient name, patient gender, patient age, appt time, appt day, room number, imaging ordered, number of images if images exist
	 	
	 	return "Patient Name: " + selectedOrder.getPatient().getFirstName() + " " + selectedOrder.getPatient().getLastName();
	 }//end SelectOrder
	 
	
	
	public void RemoveImage(Order selectedOrder, ImageFile img) {
		
		/*Removes image from order
		 * 
		 */
		
		selectedOrder.getImages().remove(img);
		
		
	}//end DeleteImage
	
	public ImageFile ViewImage(Order selectedOrder) {
		
		/*opens image viewer allowing user to view all images attached to order
		 * generates buttons to close viewer and to remove image from order 
		 * 
		 */
		
		//Will select first image in list, next and previous button can be used to display more images in list by iterating index number
		ImageFile selectedImg = selectedOrder.getImages().get(0);
		
		return selectedImg;
		
	}
	
	public ImageFile ViewNextImage(Order selectedOrder, ImageFile img) {
		
		/*opens image viewer allowing user to view all images attached to order
		 * generates buttons to close viewer and to remove image from order 
		 * 
		 */
		
		//finds index of currently selected image
		int currentIndex = selectedOrder.getImages().indexOf(img);
		
		//Will select next image in list
		ImageFile nextImg = selectedOrder.getImages().get(currentIndex+1);
		
		return nextImg;
		
	}
	
public ImageFile ViewPreviousImage(Order selectedOrder, ImageFile img) {
		
		/*opens image viewer allowing user to view all images attached to order
		 * generates buttons to close viewer and to remove image from order 
		 * 
		 */
		
		//finds index of currently selected image
		int currentIndex = selectedOrder.getImages().indexOf(img);
		
		//Will select next image in list
		ImageFile prevImg = selectedOrder.getImages().get(currentIndex-1);
		
		return prevImg;
		
	}
	
	
	public Boolean CheckIfEmpty(Order selectedOrder) {
		
		//Checks if image field is empty before allowing order submittal 
		
		Boolean isEmpty = true;
		
		if (selectedOrder.getImages() != null)
			 isEmpty = false;
		
		return isEmpty;
		
	}//end CheckIfEmpty
	
	
	public static void SubmitOrder(Order selectedOrder) throws SQLException {
		
		// Upload all the images currently held in the order 
		// Change order status to "Imaging Complete"
		Connection conn = getConnection() ;

		PreparedStatement statement = conn.prepareStatement("Update imagingorder Set orderStatus = ?   Where orderID  = ?  ;") ;
		 
		statement.setString(1, "Imaging Complete");
		statement.setInt(2, selectedOrder.getOrderID());
		
		statement.executeUpdate();
	}//end SubmitOrder
	
	//------------------------------Setters/Getters------------------------------------------------

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

	//public String getPassword() {
	//	return password;
	//}

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