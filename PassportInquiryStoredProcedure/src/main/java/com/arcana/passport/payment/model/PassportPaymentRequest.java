package com.arcana.passport.payment.model;

public class PassportPaymentRequest {
	
	private String USERNAME;
	private String PASSWORD;
	private String CONSUMER_NUMBER;
	private String TRAN_AUTH_ID;
	private String TRANSACTION_AMOUNT;
	private String TRAN_DATE;
	private String TRAN_TIME;
	private String BANK_MNEMONIC;
	private String RESERVED;
	
	
	public PassportPaymentRequest(String uSERNAME, String pASSWORD, String cONSUMER_NUMBER, String tRAN_AUTH_ID,
			String tRANSACTION_AMOUNT, String tRAN_DATE, String tRAN_TIME, String bANK_MNEMONIC, String rESERVED) {
		super();
		USERNAME = uSERNAME;
		PASSWORD = pASSWORD;
		CONSUMER_NUMBER = cONSUMER_NUMBER;
		TRAN_AUTH_ID = tRAN_AUTH_ID;
		TRANSACTION_AMOUNT = tRANSACTION_AMOUNT;
		TRAN_DATE = tRAN_DATE;
		TRAN_TIME = tRAN_TIME;
		BANK_MNEMONIC = bANK_MNEMONIC;
		RESERVED = rESERVED;
	}
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	public String getCONSUMER_NUMBER() {
		return CONSUMER_NUMBER;
	}
	public void setCONSUMER_NUMBER(String cONSUMER_NUMBER) {
		CONSUMER_NUMBER = cONSUMER_NUMBER;
	}
	public String getTRAN_AUTH_ID() {
		return TRAN_AUTH_ID;
	}
	public void setTRAN_AUTH_ID(String tRAN_AUTH_ID) {
		TRAN_AUTH_ID = tRAN_AUTH_ID;
	}
	public String getTRANSACTION_AMOUNT() {
		return TRANSACTION_AMOUNT;
	}
	public void setTRANSACTION_AMOUNT(String tRANSACTION_AMOUNT) {
		TRANSACTION_AMOUNT = tRANSACTION_AMOUNT;
	}
	public String getTRAN_DATE() {
		return TRAN_DATE;
	}
	public void setTRAN_DATE(String tRAN_DATE) {
		TRAN_DATE = tRAN_DATE;
	}
	public String getTRAN_TIME() {
		return TRAN_TIME;
	}
	public void setTRAN_TIME(String tRAN_TIME) {
		TRAN_TIME = tRAN_TIME;
	}
	public String getBANK_MNEMONIC() {
		return BANK_MNEMONIC;
	}
	public void setBANK_MNEMONIC(String bANK_MNEMONIC) {
		BANK_MNEMONIC = bANK_MNEMONIC;
	}
	public String getRESERVED() {
		return RESERVED;
	}
	public void setRESERVED(String rESERVED) {
		RESERVED = rESERVED;
	}
	
	

}
