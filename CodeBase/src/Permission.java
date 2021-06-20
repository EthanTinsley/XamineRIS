
package XamineRIS;

public class Permission {
	
	// This object will serve as our way of ensuring that each job performed within the system corresponds
	// with the appropriate user IE Access Control. 
	// it uses an integer value that determines which GUI is called by the system so, 0 = no privileges, 1 = Referring Dr, 2 = Reception 
	// 3 = Technician , 4 = Radiologist 5 = SuperUser 
	// the Permission object also keeps track of the Users name, program name ex(receptionist), and codename ex(Referring Doctor = RefDr)
	// Contact me for any questions or concerns! - Ethan 
	
	private int accessLvl ;
	private String name, programName , codeName ;
	
	public Permission(String userType) {
		this.programName = userType;
		
		switch(programName) {
		case "ReferringDr":{
			this.accessLvl = 1;
			this.codeName = "RefDr";
			break;
		}
		case "Receptionist":{
			this.accessLvl = 2;
			this.codeName = "Recep";
			break;
		}
		case "Technician":{
			this.accessLvl = 3;
			this.codeName = "Tech";
			break;
		}
		case "Radiologist":{
			this.accessLvl = 4;
			this.codeName = "Radio";
			break;
		}
		case "SuperUser":{
			this.accessLvl = 0;
			this.codeName = "Sup";
			break;
		}

		
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAccessLvl() {
		return accessLvl;
	}

	public String getProgramName() {
		return programName;
	}

	public String getCodeName() {
		return codeName;
	}

	void setAccessLvl(int level) {
		accessLvl = level ;
	}
	
	void setName(String firstName, String lastName, String userName) {
		
		name = userName + ": " + firstName + " " + lastName ;
	}
	
	void setCodeName(String codeName) {
		codeName = this.codeName ;
	}
	
	void setProgramName(String program) {
		programName = program ;
	}


}
