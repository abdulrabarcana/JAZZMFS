package com.arcana.passport.inquiry.model;

public class PassportInquiryRequest {
	
	
	public PassportInquiryRequest(String username, String password, String consumer_Number, String bank_Mnemonic,String reserved) {
		super();
		Username = username;
		Password = password;
		Consumer_Number = consumer_Number;
		Bank_Mnemonic = bank_Mnemonic;
		Reserved = reserved;
	}
	private String Username;
	private String Password;
	private String Consumer_Number;
	private String Bank_Mnemonic;
	private String Reserved;
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getConsumer_Number() {
		return Consumer_Number;
	}
	public void setConsumer_Number(String consumer_Number) {
		Consumer_Number = consumer_Number;
	}
	public String getBank_Mnemonic() {
		return Bank_Mnemonic;
	}
	public void setBank_Mnemonic(String bank_Mnemonic) {
		Bank_Mnemonic = bank_Mnemonic;
	}
	public String getReserved() {
		return Reserved;
	}
	public void setReserved(String reserved) {
		Reserved = reserved;
	}

	
	
}
