package application.datamodel;

public class TeamMember {
	/*select login_tbl.user_no, 
	 * login_tbl.email, 
	 * member_tbl.name, 
	 * member_tbl.member_type, 
	 * team_tbl.team_no 
	*/
	private String email;
	private String name;
	private Integer memberType;
	private Integer teamNumber;
	private Integer userNumber;
	
	/**
	 * 
	 * @param userNumber
	 * @param email
	 * @param name
	 * @param memberType
	 * @param teamNumber
	 */
	public TeamMember(Integer userNumber, String email, String name, Integer memberType, Integer teamNumber) {
		this.userNumber = userNumber;
		this.email = email;
		this.name = name;
		this.memberType = memberType;
		this.teamNumber = teamNumber;
	}
	
	/**
	 * 
	 * @param teamMember
	 */
	public TeamMember(TeamMember teamMember) {
		this.email = teamMember.getEmail();
		this.name = teamMember.getName();
		this.memberType = teamMember.getMemberType();
		this.teamNumber = teamMember.getTeamNumber();
		this.userNumber = teamMember.getUserNumber();
	}
	
	/**
	 * 
	 */
	public TeamMember() {
		
	}

	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getMemberType() {
		return memberType;
	}

	/**
	 * 
	 * @param memberType
	 */
	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getTeamNumber() {
		return teamNumber;
	}

	/**
	 * 
	 * @param teamNumber
	 */
	public void setTeamNumber(Integer teamNumber) {
		this.teamNumber = teamNumber;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getUserNumber() {
		return userNumber;
	}

	/**
	 * 
	 * @param userNumber
	 */
	public void setUserNumber(Integer userNumber) {
		this.userNumber = userNumber;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
	
	
	

	
}
