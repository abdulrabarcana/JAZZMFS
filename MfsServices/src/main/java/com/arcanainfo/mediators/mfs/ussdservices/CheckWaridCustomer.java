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

import com.jazzcash.SavePassportData;

public class CheckWaridCustomer extends AbstractMediator {

	Log logger = LogFactory.getLog(CheckWaridCustomer.class);

	@Override
	public boolean mediate(MessageContext msgCtx) {
		// TODO Auto-generated method stub
		logger.info("[CheckWaridCustomer] Init");
		String msisdn=(String)msgCtx.getProperty("NORMALIZED_MSISDN");
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
			 DataSource ds = (DataSource)initContext.lookup("odbc/checkwarid");
			 if(ds!=null){
				 conn = ds.getConnection(); 
			 }
			 
			 String simpleProc = "{ call TCS.CRM_API(?,?) }";
			    // Step-3: prepare the callable statement
			 callableStatement = conn.prepareCall(simpleProc);
			 callableStatement.setString(1, msisdn);
			 callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);	
				// execute savepassportdata store procedure
				callableStatement.executeUpdate();
				String result = callableStatement.getString(2);
				log.debug("[Check Warid Msisdn] SP Execution finished  we got "+ result);
				msgCtx.setProperty("RET",result);
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
