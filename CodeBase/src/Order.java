package XamineRIS;
import java.time.LocalDate;
import java.util.ArrayList;

public class Order
{
    private int orderID;
    private String orderStatus;
    private Modality Modality;
    private String imagingOrder;
    private String imagingOrderStatus = "open";
    private String apptRoom;
    private String apptTeam;
	private String lastModified;
    private String radioAnalysis;
    private String visitReason ;
    private String apptTime;
    
    private LocalDate apptDay;
    private LocalDate dateCreated;
    
    private Boolean apptScheduled = false;
    private Boolean patientCheckedIn = false;
    
    private Patient patient;
    
    private ArrayList<ImageFile> images = new ArrayList<>();
	private String scheduled;
	private int teamID;
	private String teamName ;
    
	private Team team ;
	
    // TO-DO 
    //AllergyCheck implement
    // Implement a text box for analysis by the radiologist 


    
    public Order( int orderID, Patient patient){
     
    this.orderID = orderID;
    this.patient = patient;

    }
    
	public Order(int int1) {
		// TODO Auto-generated constructor stub
		this.orderID = int1 ;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Modality getModality() {
		return this.Modality ;
	}

	public void setModality(Modality mod) {
		this.Modality = mod ;
	}

	public String getImagingOrder() {
		return imagingOrder;
	}

	public void setImagingOrder(String imagingOrder) {
		this.imagingOrder = imagingOrder;
	}

	public String getImagingOrderStatus() {
		return imagingOrderStatus;
	}

	public void setImagingOrderStatus(String imagingOrderStatus) {
		this.imagingOrderStatus = imagingOrderStatus;
	}

	public String getApptRoom() {
		return apptRoom;
	}

	public void setApptRoom(String apptRoom) {
		this.apptRoom = apptRoom;
	}

	public String getApptTime() {
		return apptTime;
	}

	//Format and verify string is an accepted time
	public void setApptTime(String apptTime ) {
		this.apptTime = apptTime;
	}

	public String getApptDay() {
		return this.scheduled;
	}

	public void setApptDay(LocalDate apptDay) {
		this.apptDay = apptDay;
	}

	public ArrayList<ImageFile> getImages() {
		return images;
	}

	public void setImages(ArrayList<ImageFile> images) {
		this.images = images;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getRadioAnalysis() {
		return radioAnalysis;
	}

	public void setRadioAnalysis(String radioAnalysis) {
		this.radioAnalysis = radioAnalysis;
	}

	public Boolean getApptScheduled() {
		return apptScheduled;
	}

	public void setApptScheduled(Boolean apptScheduled) {
		this.apptScheduled = apptScheduled;
	}

	public Boolean getPatientCheckedIn() {
		return patientCheckedIn;
	}

	public void setPatientCheckedIn(Boolean patientCheckedIn) {
		this.patientCheckedIn = patientCheckedIn;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getVisitReason() {
		return visitReason;
	}

	public void setVisitReason(String visitReason) {
		this.visitReason = visitReason;
	}
	
	//public String getApptTeam() {
		//return apptTeam.getTeamName();
	//}

	public void setApptTeam(String string) {
		this.apptTeam = string;
	}
	
	public void AllergyConflictCheck() {
		//Checks if allergies conflict with imaging ordered
		
	}//end AllergyConflictCheck
	
	public String toString() {
		String result ;
		
		result = "OrderID: " + orderID + "\n" +
				 "PatientID " + patient.getPatientId() + "\n" +
				 "PatientName: " + patient.getFirstName() + " " + patient.getLastName() + "\n" +
				 "Patient E-mail: " + patient.getEmail() + "\n" +
				 "Patient DoB: " + patient.getDateOfBirth() + "\n" +
				 "Order Status " + orderStatus + "\n" + 
				 "Appointment: " + apptTime + "\n" + 
				 "Visit Reason: " + visitReason + "\n" +
				 "Imagaing Required: " + imagingOrder ;
		
		return result ;
		
	}
	

	public void setApptDay(String scheduled) {
		// TODO Auto-generated method stub
		this.scheduled = scheduled ;
		
	}

	public void setTeamID(int iD) {
		// TODO Auto-generated method stub
		this.teamID =  iD ;
	}

	

	public int getTeamID() {
		// TODO Auto-generated method stub
		return this.teamID ;
	}

	public String getApptTeam() {
		// TODO Auto-generated method stub
		return this.teamName ;
	}

	public void setPatientID(int int1) {
		// TODO Auto-generated method stub
		this.patient.setPatientId(int1);
		
	}

	public int getPatientID() {
		// TODO Auto-generated method stub
		return this.patient.getPatientId() ;
	}

	public void setTeam(Team team) {
		// TODO Auto-generated method stub
		this.team = team ;
	}

	public Team getTeam() {
		return this.team ;
	}
	

	
    
}
