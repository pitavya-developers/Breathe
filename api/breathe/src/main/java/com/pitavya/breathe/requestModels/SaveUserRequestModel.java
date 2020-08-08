package com.pitavya.breathe.requestModels;

import com.sun.istack.NotNull;

public class SaveUserRequestModel {

	@NotNull
	private String userName;
	@NotNull
	private String emailId;
	@NotNull
	private String providedId;
	@NotNull
	private String profilePicUri;

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

	public String getProfilePicUri() {
		return profilePicUri;
	}

	public void setProfilePicUri(String profilePicUri) {
		this.profilePicUri = profilePicUri;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
