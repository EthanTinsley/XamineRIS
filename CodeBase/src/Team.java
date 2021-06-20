package XamineRIS;

public class Team {
	private int teamID;
	private String teamName;
	
	public Team(int id, String name) {
		this.teamID = id;
		this.teamName = name;
		
	}
	public int getTeamID() {
		return teamID;
	}
	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

}
