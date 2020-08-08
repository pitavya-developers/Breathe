package com.pitavya.breathe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID")
	private long userId;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "PROVIDED_ID")
	private String providedId;

	@Column(name = "profilePic")
	private String profilePic;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getProvidedId() {
		return providedId;
	}

	public void setProvidedId(String providedId) {
		this.providedId = providedId;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
