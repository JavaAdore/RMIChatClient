package com.chat.common;

public class UserDTO extends User{

	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	
	
	private ClientInt clientInt;
	
	
	private int minAge;
	
	private int maxAge;
	

	public ClientInt getClientInt() {
		return clientInt;
	}

	public void setClientInt(ClientInt clientInt) {
		this.clientInt = clientInt;
	}

	@Override
	public String toString() {
		return super.getEmail();
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	
	
	
	
	
}
