package com.arcana.passport.inquiry.model;

public class PassportInquiryStoredProcedureResponse {
private String RESPONSE_CODE;
private String COSUMER_DETAIL;
private String BILL_STATUS;
private String AMOUNT_WITHIN_DUE_DATE;
private String AMOUNT_AFTER_DUEDATE;
private String BILLING_MONTH;
private String DATE_PAID;
private String AMOUNT_PAID;
private String TRAN_AUTH_ID;


public String getRESPONSE_CODE() {
	return RESPONSE_CODE;
}
public void setRESPONSE_CODE(String rESPONSE_CODE) {
	RESPONSE_CODE = rESPONSE_CODE;
}
public String getCOSUMER_DETAIL() {
	return COSUMER_DETAIL;
}
public void setCOSUMER_DETAIL(String cOSUMER_DETAIL) {
	COSUMER_DETAIL = cOSUMER_DETAIL;
}
public String getBILL_STATUS() {
	return BILL_STATUS;
}
public void setBILL_STATUS(String bILL_STATUS) {
	BILL_STATUS = bILL_STATUS;
}
public String getAMOUNT_WITHIN_DUE_DATE() {
	return AMOUNT_WITHIN_DUE_DATE;
}
public void setAMOUNT_WITHIN_DUE_DATE(String aMOUNT_WITHIN_DUE_DATE) {
	AMOUNT_WITHIN_DUE_DATE = aMOUNT_WITHIN_DUE_DATE;
}
public String getAMOUNT_AFTER_DUEDATE() {
	return AMOUNT_AFTER_DUEDATE;
}
public void setAMOUNT_AFTER_DUEDATE(String aMOUNT_AFTER_DUEDATE) {
	AMOUNT_AFTER_DUEDATE = aMOUNT_AFTER_DUEDATE;
}
public String getBILLING_MONTH() {
	return BILLING_MONTH;
}
public void setBILLING_MONTH(String bILLING_MONTH) {
	BILLING_MONTH = bILLING_MONTH;
}
public String getDATE_PAID() {
	return DATE_PAID;
}
public void setDATE_PAID(String dATE_PAID) {
	DATE_PAID = dATE_PAID;
}
public String getAMOUNT_PAID() {
	return AMOUNT_PAID;
}
public void setAMOUNT_PAID(String aMOUNT_PAID) {
	AMOUNT_PAID = aMOUNT_PAID;
}
public String getTRAN_AUTH_ID() {
	return TRAN_AUTH_ID;
}
public void setTRAN_AUTH_ID(String tRAN_AUTH_ID) {
	TRAN_AUTH_ID = tRAN_AUTH_ID;
}


	
	
}
