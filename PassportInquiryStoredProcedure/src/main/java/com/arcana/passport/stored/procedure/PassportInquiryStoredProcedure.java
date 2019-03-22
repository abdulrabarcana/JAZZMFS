package com.arcana.passport.stored.procedure;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import com.arcana.passport.inquiry.model.PassportInquiryRequest;
import com.arcana.passport.inquiry.model.PassportInquiryStoredProcedureResponse;
import com.arcana.passport.payment.model.PassportPaymentRequest;


public class PassportInquiryStoredProcedure extends AbstractMediator { 
    private static Logger log = LogManager.getLogger(PassportInquiryStoredProcedure.class);
private static int paddingLength=299;

//	private static final String DB_CONNECTION = "jdbc:oracle:thin:@10.50.13.116:1522:TABSPROD";
//	private static final String DB_USER = "root";
//	private static final String DB_PASSWORD = "Veon@123";
//	private static final String DB_DRIVER = "oracle.jdbc.OracleDriver";
	
	public boolean mediate(MessageContext context) {
		
		try {
		     log.info("************ Class Mediator Starts ****************");

			String Username = (String) context.getProperty("Username");
			String Password = (String) context.getProperty("Password");
			String Consumer_Number = (String) context.getProperty("Consumer_Number");
			String Bank_Mnemonic = (String) context.getProperty("Bank_Mnemonic");
			String Reserved = (String) context.getProperty("Reserved");
			String Tran_Auth_Id = (String) context.getProperty("Tran_Auth_Id");
			String Transaction_Amount = (String) context.getProperty("Transaction_Amount");
			String Tran_Date = (String) context.getProperty("Tran_Date");
			String Tran_Time = (String) context.getProperty("Tran_Time"); 


			//differentiating  the downstream stored prodecure
            if(Tran_Auth_Id.length()==0){
            	//calling inquiry stored procedure
   		     log.info("************ Inquiry sp ****************" +Tran_Auth_Id);
		    PassportInquiryRequest passportInqueryRequest=new PassportInquiryRequest(Username, Password, Consumer_Number, Bank_Mnemonic, Reserved);
			callOracleStoredProcOUTParameter(passportInqueryRequest,context);
            }
            else{
            	// calling payment stored procedure
      		     log.info("************payment  sp ****************" +Tran_Auth_Id);

	        PassportPaymentRequest passportPaymentRequest=new PassportPaymentRequest(Username, Password,Consumer_Number, Tran_Auth_Id,Transaction_Amount, Tran_Date, Tran_Time, Bank_Mnemonic, Reserved);
		    callOracleStoredProcOUTParameter(passportPaymentRequest,context);
 }
		} catch (SQLException e) {
			context.setProperty("response_code", paddingRight("03", paddingLength));
			log.info("************ Exception 1 ****************" + e.getStackTrace());
		     log.info("************ Exception 2 ****************" + e.getMessage());
		     log.info("************ Exception 3 ****************" + e.getCause());
		}
		
		
		
		return true; 
		
	}
	
	private static void callOracleStoredProcOUTParameter(PassportInquiryRequest passportInqueryRequest,MessageContext context) throws SQLException {
		 DateTimeFormatter dtfDueDate = DateTimeFormatter.ofPattern("yyyyMMdd"); 
		 DateTimeFormatter dtfBiilingDate = DateTimeFormatter.ofPattern("yyMM");  

		   LocalDateTime now = LocalDateTime.now();
		   String DueDate=dtfDueDate.format(now);
		   String BillingDate=dtfBiilingDate.format(now);
		   String amount_whole="";
		   String amount_whole_after="";
		   String amount_whole_within="";
		   String amount_decimal="";
			String amount_decimal_after="";
			String amount_decimal_within="";
			String amount_after_sign="+";
			String amount_with_in_sign="+";
			
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		Hashtable<String, String> environment = new Hashtable<String, String>();
		 environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
		    Context initContext;

		 try {
			 initContext = new InitialContext(environment);
			 DataSource ds = (DataSource)initContext.lookup("jdbc/mfsstg");
			 if(ds!=null){
				 dbConnection = ds.getConnection(); 
			 }
			String getDBUSERByUserIdSql = "{call MPFC.BILL_INQUIRY(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			callableStatement = dbConnection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.setString(1, passportInqueryRequest.getUsername());
			callableStatement.setString(2, passportInqueryRequest.getPassword());
			callableStatement.setString(3, passportInqueryRequest.getConsumer_Number());
			callableStatement.setString(4, passportInqueryRequest.getBank_Mnemonic());
			callableStatement.setString(5, passportInqueryRequest.getReserved());
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
					

			// execute getDBUSERByUserId store procedure
			callableStatement.executeUpdate();
			PassportInquiryStoredProcedureResponse passportInquiryStoredProcedureResponse=new PassportInquiryStoredProcedureResponse();
			passportInquiryStoredProcedureResponse.setRESPONSE_CODE( callableStatement.getString(6));
			passportInquiryStoredProcedureResponse.setCOSUMER_DETAIL( callableStatement.getString(7));
			passportInquiryStoredProcedureResponse.setBILL_STATUS(callableStatement.getString(8));
			passportInquiryStoredProcedureResponse.setAMOUNT_WITHIN_DUE_DATE( callableStatement.getString(9));
			passportInquiryStoredProcedureResponse.setAMOUNT_AFTER_DUEDATE( callableStatement.getString(10));
			passportInquiryStoredProcedureResponse.setBILLING_MONTH(callableStatement.getString(11));
			passportInquiryStoredProcedureResponse.setDATE_PAID(callableStatement.getString(12));
			passportInquiryStoredProcedureResponse.setAMOUNT_PAID(callableStatement.getString(13));
			passportInquiryStoredProcedureResponse.setTRAN_AUTH_ID(callableStatement.getString(14));
			
			
			context.setProperty("status_code", passportInquiryStoredProcedureResponse.getRESPONSE_CODE());

			
			log.info("************ Response Code From Inquiry SP *************** Response_Code" + passportInquiryStoredProcedureResponse.getRESPONSE_CODE()+"\nConsumer Detail: "+passportInquiryStoredProcedureResponse.getCOSUMER_DETAIL()+ "\nBill Status: "+ passportInquiryStoredProcedureResponse.getBILL_STATUS()+"\nAmount with in due date: "+passportInquiryStoredProcedureResponse.getAMOUNT_WITHIN_DUE_DATE()+ "\nAmount After Due Date: "+ passportInquiryStoredProcedureResponse.getAMOUNT_AFTER_DUEDATE() + " Billing Month: "+passportInquiryStoredProcedureResponse.getBILLING_MONTH() +  "\nDate_Paid " +passportInquiryStoredProcedureResponse.getDATE_PAID() + "\nAmount_Paid "+passportInquiryStoredProcedureResponse.getAMOUNT_PAID()+"\nTran_id "+ passportInquiryStoredProcedureResponse.getTRAN_AUTH_ID() );


			if(passportInquiryStoredProcedureResponse.getRESPONSE_CODE().equals("00")){
			//if(passportInquiryStoredProcedureResponse.getBILL_STATUS().startsWith("P")){
			
				if(passportInquiryStoredProcedureResponse.getAMOUNT_PAID()!=null)
				amount_decimal=passportInquiryStoredProcedureResponse.getAMOUNT_PAID().split("\\.")[1];
				amount_decimal_after=passportInquiryStoredProcedureResponse.getAMOUNT_AFTER_DUEDATE().split("\\.")[1];
				amount_decimal_within=passportInquiryStoredProcedureResponse.getAMOUNT_WITHIN_DUE_DATE().split("\\.")[1];
				
				if(passportInquiryStoredProcedureResponse.getAMOUNT_PAID()!=null)
				amount_whole=passportInquiryStoredProcedureResponse.getAMOUNT_PAID().split("\\.")[0];
				amount_whole_after=passportInquiryStoredProcedureResponse.getAMOUNT_AFTER_DUEDATE().split("\\.")[0];
				amount_whole_within=passportInquiryStoredProcedureResponse.getAMOUNT_WITHIN_DUE_DATE().split("\\.")[0];
				
				log.info("************ Altertered Response *************** Amount Decimal: " + amount_decimal+"\nAmount Decimal After: "+amount_decimal_after+ "\nAmount Deceimal within: "+ amount_decimal_within+"\nAmount whole number: "+amount_whole+ "\nAmount Whole After: "+ amount_whole_after + "\nAmount Whole within: "+amount_whole_within );

				if(passportInquiryStoredProcedureResponse.getAMOUNT_PAID().startsWith("+")){
					amount_whole=amount_whole.split("\\+")[1];					
				}
				if(passportInquiryStoredProcedureResponse.getAMOUNT_WITHIN_DUE_DATE().startsWith("+")){
					amount_whole_within=amount_whole_within.split("\\+")[1];
				}
				if(passportInquiryStoredProcedureResponse.getAMOUNT_AFTER_DUEDATE().startsWith("+")){
					amount_whole_after=amount_whole_after.split("\\+")[1];
				}
				if(passportInquiryStoredProcedureResponse.getAMOUNT_PAID().startsWith("-")){
					amount_whole=amount_whole.split("\\-")[1];
					
				}
				if(passportInquiryStoredProcedureResponse.getAMOUNT_WITHIN_DUE_DATE().startsWith("-")){
					amount_whole_within=amount_whole_within.split("\\-")[1];
					amount_with_in_sign="-";
				}
				if(passportInquiryStoredProcedureResponse.getAMOUNT_AFTER_DUEDATE().startsWith("-")){}
				amount_whole_after=amount_whole_after.split("\\-")[1];
				amount_after_sign="-";
			    
			if(passportInquiryStoredProcedureResponse.getAMOUNT_PAID()!=null)
			passportInquiryStoredProcedureResponse.setAMOUNT_PAID(String.format("%010d", amount_whole)+amount_decimal);
			passportInquiryStoredProcedureResponse.setAMOUNT_WITHIN_DUE_DATE(amount_with_in_sign+String.format("%011d", amount_whole_within)+amount_decimal_within);
			passportInquiryStoredProcedureResponse.setAMOUNT_AFTER_DUEDATE(amount_after_sign+String.format("%011d", amount_whole_after)+amount_decimal_after);
			
				if(passportInquiryStoredProcedureResponse.getDATE_PAID()==null)
				passportInquiryStoredProcedureResponse.setDATE_PAID(paddingLeft("", 8));
				if(passportInquiryStoredProcedureResponse.getAMOUNT_PAID()==null)
				passportInquiryStoredProcedureResponse.setAMOUNT_PAID(paddingLeft("",12));
				if(passportInquiryStoredProcedureResponse.getTRAN_AUTH_ID()==null)
				passportInquiryStoredProcedureResponse.setTRAN_AUTH_ID(paddingLeft("", 6));
				log.info("Response in Case Of status !=P:" + passportInquiryStoredProcedureResponse.getDATE_PAID()+"\nAmount Paid: "+passportInquiryStoredProcedureResponse.getAMOUNT_PAID()+"\nTran_auth_id"+passportInquiryStoredProcedureResponse.getTRAN_AUTH_ID());
			
			//context.setProperty("response_code", passportInquiryStoredProcedureResponse.getRESPONSE_CODE());
				//context.setProperty("response_code", paddingRight(+paddingRight(passportInquiryStoredProcedureResponse.getCOSUMER_DETAIL(),30)+passportInquiryStoredProcedureResponse.getBILL_STATUS()+dtf.format(now)+"+0000000"+passportInquiryStoredProcedureResponse.getAMOUNT_AFTER_DUEDATE()+"00"+"+0000000"+passportInquiryStoredProcedureResponse.getAMOUNT_AFTER_DUEDATE()+"00"+passportInquiryStoredProcedureResponse.getBILLING_MONTH(),paddingLength));
			context.setProperty("response_code", paddingRight(passportInquiryStoredProcedureResponse.getRESPONSE_CODE()+paddingRight(passportInquiryStoredProcedureResponse.getCOSUMER_DETAIL(),30)+passportInquiryStoredProcedureResponse.getBILL_STATUS()+DueDate+passportInquiryStoredProcedureResponse.getAMOUNT_WITHIN_DUE_DATE()+passportInquiryStoredProcedureResponse.getAMOUNT_AFTER_DUEDATE()+BillingDate+passportInquiryStoredProcedureResponse.getDATE_PAID()+passportInquiryStoredProcedureResponse.getAMOUNT_PAID()+passportInquiryStoredProcedureResponse.getTRAN_AUTH_ID(),paddingLength)+"");
			context.setProperty("status", "OK");

			log.info("Response Length:"+ context.getProperty("response_code").toString().length());
			}
			
			else if(passportInquiryStoredProcedureResponse.getRESPONSE_CODE().startsWith("01")||passportInquiryStoredProcedureResponse.getRESPONSE_CODE().startsWith("03")||passportInquiryStoredProcedureResponse.getRESPONSE_CODE().startsWith("04")){
				context.setProperty("response_code", paddingRight(passportInquiryStoredProcedureResponse.getRESPONSE_CODE(),paddingLength));
				context.setProperty("status", "KO");
				log.info("Response Length:"+ context.getProperty("response_code").toString().length());

			}
			
			else{
				context.setProperty("response_code", paddingRight("03",paddingLength));
				context.setProperty("status", "KO");
				context.setProperty("status_code", "03");
				log.info("Response Length:"+ context.getProperty("response_code").toString().length());


			}
			
			log.info("************ Response Code From Inquiry SP ***************" + paddingRight(passportInquiryStoredProcedureResponse.getRESPONSE_CODE(), paddingLength));

		} catch (SQLException e) {
			context.setProperty("response_code", paddingRight("03", paddingLength));
			context.setProperty("status", "KO");
			context.setProperty("status_code", "03");


			log.info("Response Length:"+ context.getProperty("response_code").toString().length());

		     log.info("************ Exception 1 ****************" + e.getStackTrace());
		     log.info("************ Exception 2 ****************" + e.getMessage());
		     log.info("************ Exception 3 ****************" + e.getCause());


		}
		catch (Exception e) {
			context.setProperty("response_code", paddingRight("03", paddingLength));
			context.setProperty("status", "KO");
			context.setProperty("status_code", "03");
			log.info("Response Length:"+ context.getProperty("response_code").toString().length());

				log.info("************ Exception 1 ****************" + e.getStackTrace());
			     log.info("************ Exception 2 ****************" + e.getMessage());
			     log.info("************ Exception 3 ****************" + e.getCause());


		} finally {
			context.setProperty("resub", "");

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}
	private static void callOracleStoredProcOUTParameter(PassportPaymentRequest passportPaymentRequest,MessageContext context) throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		Hashtable<String, String> environment = new Hashtable<String, String>();
		 environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
		    Context initContext;

		 try {
			 initContext = new InitialContext(environment);
			 DataSource ds = (DataSource)initContext.lookup("jdbc/mfsstg");
			 if(ds!=null){
				 dbConnection = ds.getConnection(); 
			 }
			
			String getDBUSERByUserIdSql = "{call MPFC.BILL_PAYMENT(?,?,?,?,?,?,?,?,?,?)}";

			callableStatement = dbConnection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.setString(1, passportPaymentRequest.getUSERNAME());
			callableStatement.setString(2, passportPaymentRequest.getPASSWORD());
			callableStatement.setString(3, passportPaymentRequest.getCONSUMER_NUMBER());
			callableStatement.setString(4, passportPaymentRequest.getTRAN_AUTH_ID());
			callableStatement.setString(5, passportPaymentRequest.getTRANSACTION_AMOUNT());
			callableStatement.setString(6, passportPaymentRequest.getTRAN_DATE());
			callableStatement.setString(7, passportPaymentRequest.getTRAN_TIME());
			callableStatement.setString(8, passportPaymentRequest.getBANK_MNEMONIC());
			callableStatement.setString(9, passportPaymentRequest.getRESERVED());
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);		

			// execute getDBUSERByUserId store procedure
			callableStatement.executeUpdate();
			String response_code = callableStatement.getString(10);
			context.setProperty("response_code", paddingRight(response_code, 222));
			log.info("Response Length:"+ context.getProperty("response_code").toString().length());

			
		     log.info("************ Response Code From Payment SP***************" + paddingRight(response_code, 222));

		} catch (SQLException e) {
			context.setProperty("response_code", paddingRight("05", 222));

		     log.info("************ Exception 1 ****************" + e.getStackTrace());
		     log.info("************ Exception 2 ****************" + e.getMessage());
		     log.info("************ Exception 3 ****************" + e.getCause());



			System.out.println(e.getMessage());
		}
			catch (Exception e) {
				context.setProperty("response_code", paddingRight("05", 222));
				log.info("************ Exception 1 ****************" + e.getStackTrace());
			    log.info("************ Exception 2 ****************" + e.getMessage());
			    log.info("************ Exception 3 ****************" + e.getCause());
		} finally {

			if (callableStatement != null) {
				callableStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		 
		 

	}
	public static String paddingRight(String string, int length) {
	    return String.format("%1$-" + length + "s", string);
	}
	
	public static String paddingLeft(String string, int length) {
	    return String.format("%1$" + length + "s", string);
	}
//	private static Connection getDBConnection() {
//
//		Connection dbConnection = null;
//
//		try {
//
//			Class.forName(DB_DRIVER);
//
//		} catch (ClassNotFoundException e) {
//
//			System.out.println(e.getMessage());
//
//		}
//
//		try {
//
//			dbConnection = DriverManager.getConnection(
//				DB_CONNECTION, DB_USER,DB_PASSWORD);
//			return dbConnection;
//
//		} catch (SQLException e) {
//
//		     log.info("************ Exception ****************" + e.getStackTrace());
//
//		}
//
//		return dbConnection;
//
//	}
}