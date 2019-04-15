package com.arcanainfo.mediators.mfs.ussdservices;

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

public class LogKYCData extends AbstractMediator {

	Log logger = LogFactory.getLog(LogKYCData.class);

	@Override
	public boolean mediate(MessageContext msgCtx) {
		// TODO Auto-generated method stub
		logger.info("[LOGKYCDATA] Init");
	
		String customerCnic=(String)msgCtx.getProperty("V_CUSTOMERCNIC");
		String customerName=(String)msgCtx.getProperty("Name");
		String customerMotherName=(String)msgCtx.getProperty("MotherName");
		String customerGender=(String)msgCtx.getProperty("Gender");
		String customerAddress=(String)msgCtx.getProperty("Address");
		String customerDob=(String)msgCtx.getProperty("DOB");
		String customerCnicExp=(String)msgCtx.getProperty("CNICExp");
		
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
			 
			 String simpleProc = "{ call MRHA.SP_UPDATECUSTOMERKYCDATA(?,?,?,?,?,?,?,?,?) }";
			    // Step-3: prepare the callable statement
			 callableStatement = conn.prepareCall(simpleProc);
			 callableStatement.setString(1, customerCnic);
			 callableStatement.setString(2, customerName);
			 callableStatement.setString(3, customerMotherName);
			 callableStatement.setString(4, customerGender);
			 callableStatement.setString(5, customerAddress);
			 callableStatement.setString(6, customerDob);
			 callableStatement.setString(7, customerCnicExp);
			 callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);	
			 callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);	
					// execute savepassportdata store procedure
				callableStatement.executeUpdate();
				String status = callableStatement.getString(8);
				String reason = callableStatement.getString(9);
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
		 msgCtx.setProperty("ExceptionDetail", exceptionDetail);
			
	
		return true;
	}

}
