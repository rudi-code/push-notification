package id.co.ogya.pushnotification.model;

public class UserToken {
	
	private String userName;
	private String registrationToken;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRegistrationToken() {
		return registrationToken;
	}
	public void setRegistrationToken(String registrationToken) {
		this.registrationToken = registrationToken;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserToken [userName=");
		builder.append(userName);
		builder.append(", registrationToken=");
		builder.append(registrationToken);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	

}
