package com.jazzcash;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext; 
import org.apache.synapse.mediators.AbstractMediator;

public class SavePassportData extends AbstractMediator { 

	Log logger = LogFactory.getLog(SavePassportData.class);
	public boolean mediate(MessageContext msgCtx) { 
		// TODO Implement your mediation logic here 
		logger.debug("[SavePassportData] Init");
		String transactionID=(String)msgCtx.getProperty("TRANSACTIONID");
		String cnic=(String)msgCtx.getProperty("CNIC");
		String customerMSISDN=(String)msgCtx.getProperty("Customer_MSISDN");
		String passport_Pages=(String)msgCtx.getProperty("Passport_Pages");
		Integer passportPages=Integer.parseInt(passport_Pages);
		String passportFee=(String)msgCtx.getProperty("Passport_Fee");
		Integer passport_fee=Integer.parseInt(passportFee);
		String transactionCharges=(String)msgCtx.getProperty("Transaction_Charges");
		Integer transaction_charges=Integer.parseInt(transactionCharges);
		String homeDeliveryAmount=(String)msgCtx.getProperty("Home_Delivery_Amount");
		Integer home_delivery_amount=Integer.parseInt(homeDeliveryAmount);
		String fed=(String)msgCtx.getProperty("FED");
		Integer FED=Integer.parseInt(fed);
		String totalAmount=(String)msgCtx.getProperty("Total_Amount");
		Integer total_amount=Integer.parseInt(totalAmount);
		String passportType=(String)msgCtx.getProperty("Passport_Type");
		String passportCategory=(String)msgCtx.getProperty("Passport_Category");
		String paymentType=(String)msgCtx.getProperty("Payment_Type");
		String homeDelivery=(String)msgCtx.getProperty("Home_Delivery");
		String validity=(String)msgCtx.getProperty("Validity");
		CallableStatement callableStatement = null;
		Connection conn = null;
		Hashtable<String, String> environment = new Hashtable<String, String>();
		 environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
		    Context initContext;
		
		 try {
			 initContext = new InitialContext(environment);
			 DataSource ds = (DataSource)initContext.lookup("jdbc/mfstest");
			 if(ds!=null){
				 conn = ds.getConnection(); 
			 }
			 
			 String simpleProc = "{ call sp_savepassportdata(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
			    // Step-3: prepare the callable statement
			 callableStatement = conn.prepareCall(simpleProc);
			 callableStatement.setString(1, transactionID);
				callableStatement.setString(2, cnic);
				callableStatement.setString(3, customerMSISDN);
				callableStatement.setInt(4, passportPages);
				callableStatement.setInt(5, passport_fee);
				callableStatement.setInt(6, transaction_charges);
				callableStatement.setInt(7, home_delivery_amount);
				callableStatement.setInt(8, FED);
				callableStatement.setInt(9, total_amount);
				callableStatement.setString(10, passportType);
				callableStatement.setString(11, passportCategory);
				callableStatement.setString(12, paymentType);
				callableStatement.setString(13, homeDelivery);
				callableStatement.setString(14, validity);
				
				// execute savepassportdata store procedure
				callableStatement.executeUpdate();
				
				log.debug("[SavePassportData] SP Execution finished");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			 if (callableStatement != null) {
				   try {
					callableStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   try {
					   if(conn!=null){
					conn.close();
					   }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
			  
			   }
		return true;
	}
	
	}


