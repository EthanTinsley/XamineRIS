package XamineRIS;

public class Patient {

	int patientId;
	private String email, firstName, lastName, gender, middleName, notes, phoneNumber , DateOfBirth;
	private int age;
	private boolean allergyLatex, allergyAsthma, allergyMridye, allergyXraydye;
	private String allergy = "None";
	// private Doctor patientDr
	// implement date
	

	public Patient(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isAllergyLatex() {
		return allergyLatex;
	}

	public void setAllergyLatex(boolean allergyLatex) {
		this.allergyLatex = allergyLatex;
	}

	public boolean isAllergyAsthma() {
		return allergyAsthma;
	}

	public void setAllergyAsthma(boolean allergyAsthma) {
		this.allergyAsthma = allergyAsthma;
	}

	public boolean isAllergyMridye() {
		return allergyMridye;
	}

	public void setAllergyMridye(boolean allergyMridye) {
		this.allergyMridye = allergyMridye;
	}

	public boolean isAllergyXraydye() {
		return allergyXraydye;
	}

	public void setAllergyXraydye(boolean allergyXraydye) {
		this.allergyXraydye = allergyXraydye;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public String getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}


	
	
}
