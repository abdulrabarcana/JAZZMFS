package com.arcanainfo.mediators.mfs.ussdservices;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class UssdLogRequest extends AbstractMediator {

	Log logger = LogFactory.getLog(UssdLogRequest.class);

	@Override
	public boolean mediate(MessageContext msgCtx) {
		// TODO Auto-generated method stub
		logger.info("[UssdLogRequest] Init");
		String transactionType=(String)msgCtx.getProperty("V_TRANSACTIONTYPE");
		String transactionDate=(String)msgCtx.getProperty("TRANSACTIONDATE");
		String sourceTransactionId=(String)msgCtx.getProperty("V_SOURCETRANSACTIONID");
		String operatorCnic=(String)msgCtx.getProperty("V_OPERATORCNIC");
		String operatorImei=(String)msgCtx.getProperty("V_OPERATORIMEI");
		String operatormsisdn=(String)msgCtx.getProperty("V_OPERATORMSISDN");
		String customerCnic=(String)msgCtx.getProperty("V_CUSTOMERCNIC");
		String customerMsisdn=(String)msgCtx.getProperty("V_CUSTOMERMSISDN");
		
		Date date=null;
		try {
			date=new SimpleDateFormat("yyyy-MM-dd").parse(transactionDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CallableStatement callableStatement = null;
		Connection conn = null;
		Hashtable<String, String> environment = new Hashtable<String, String>();
		 environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
		    Context initContext;
		    String isException=null;
		    String exceptionDetail=null;
		    try {
				isException="FALSE";
			    
			 initContext = new InitialContext(environment);
			 DataSource ds = (DataSource)initContext.lookup("odbc/ussdlogging");
			 if(ds!=null){
				 conn = ds.getConnection(); 
			 }
			 
			 String simpleProc = "{ call MRHA.SP_LogUSSDRequest(?,?,?,?,?,?,?,?,?,?) }";
			    // Step-3: prepare the callable statement
			 callableStatement = conn.prepareCall(simpleProc);
			 callableStatement.setString(1, transactionType);
			 callableStatement.setString(2, transactionDate);
			 callableStatement.setString(3, sourceTransactionId);
			 callableStatement.setString(4, operatorCnic);
			 callableStatement.setString(5, operatorImei);
			 callableStatement.setString(6, operatormsisdn);
			 callableStatement.setString(7, customerCnic);
			 callableStatement.setString(8, customerMsisdn);
			 callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);	
			 callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);	
					// execute savepassportdata store procedure
				callableStatement.executeUpdate();
				String status = callableStatement.getString(9);
				String reason = callableStatement.getString(10);
				log.debug("[Log USSD Request] SP Execution finished  we got "+ status);
				log.debug("[Log Ussd Request] SP Execution finished  we got "+ reason);
					msgCtx.setProperty("STATUS",status);
					msgCtx.setProperty("REASON",reason);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isException="TRUE";
		 	exceptionDetail=e.toString();
		} finally {
			 if (callableStatement != null) {
				   try {
					callableStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					isException="TRUE";
				 	exceptionDetail=e.toString();
									
				}
				   try {
					   if(conn!=null){
					conn.close();
					   }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					isException="TRUE";
				 	exceptionDetail=e.toString();
					
				}
			   }
			  
			   }
		 msgCtx.setProperty("IsException", isException);
		 	msgCtx.setProperty("ExceptionDetail_Req", exceptionDetail);
			
		return true;
	}

}
