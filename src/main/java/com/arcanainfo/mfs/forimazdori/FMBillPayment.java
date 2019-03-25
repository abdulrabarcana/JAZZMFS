package com.arcanainfo.mfs.forimazdori;


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


public class FMBillPayment extends AbstractMediator {
	
	Log logger = LogFactory.getLog(FMBillPayment.class);
	
	 public FMBillPayment() {}
	  
	  public boolean mediate(MessageContext msgCtx) {
	    logger.info("[FMBillPayment] Init");
	    
	    String PARAM_TID = (String)msgCtx.getProperty("Consumer_Number");
	    String PARAM_AMOUNT = (String)msgCtx.getProperty("Transaction_Amount");
	    String PARAM_TRANS_DATE = (String)msgCtx.getProperty("Tran_Date");
	    String PARAM_TRANS_TIME = (String)msgCtx.getProperty("Tran_Time");
	    String PARAM_CREATED_BY = (String)msgCtx.getProperty("created_by");
	    String PARAM_CREATED_IP = (String)msgCtx.getProperty("created_IP");
	    Float PARAM_AMOUNT_CONVERTED = Float.valueOf(Float.parseFloat(PARAM_AMOUNT));
	    CallableStatement callableStatement = null;
	    Connection conn = null;
	    Hashtable<String, String> environment = new Hashtable();
	    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
	    
	    try
	    {
	      Context initContext = new InitialContext(environment);
	      DataSource ds = (DataSource)initContext.lookup("jdbc/mfstest");
	      if (ds != null) {
	        conn = ds.getConnection();
	      }
	      
	      String simpleProc = "{ call PRC_BILL_PAYMENT(?,?,?,?,?,?,?,?) }";
	      
	      callableStatement = conn.prepareCall(simpleProc);
	      callableStatement.setString(1, PARAM_TID);
	      callableStatement.setFloat(2, PARAM_AMOUNT_CONVERTED.floatValue());
	      callableStatement.setString(3, PARAM_TRANS_DATE);
	      callableStatement.setString(4, PARAM_TRANS_TIME);
	      callableStatement.setString(5, "USSD");
	      callableStatement.setString(6, "localhost");
	      callableStatement.registerOutParameter(7, 12);
	      callableStatement.registerOutParameter(8, 12);
	      
	      callableStatement.executeUpdate();
	      
	      String param_ret_code = callableStatement.getString(7);
	      String param_ret_desc = callableStatement.getString(8);
	      

	      msgCtx.setProperty("param_ret_code", param_ret_code);
	      msgCtx.setProperty("param_ret_desc", param_ret_desc);
	    
	    }
	    catch (Exception e) {
	      msgCtx.setProperty("STACK_TRACE", e.getMessage());
	      e.printStackTrace();
	      
	      if (callableStatement != null) {
	        try {
	          callableStatement.close();
	        }
	        catch (SQLException g) {
	          msgCtx.setProperty("STACK_TRACE", g.getMessage());
	          g.printStackTrace();
	        }
	        try {
	          if (conn != null) {
	            conn.close();
	          }
	        }
	        catch (SQLException f) {
	          msgCtx.setProperty("STACK_TRACE", f.getMessage());
	          f.printStackTrace();
	        }
	      }
	    }
	    finally
	    {
	      if (callableStatement != null) {
	        try {
	          callableStatement.close();
	        }
	        catch (SQLException e) {
	          msgCtx.setProperty("STACK_TRACE", e.getMessage());
	          e.printStackTrace();
	        }
	        try {
	          if (conn != null) {
	            conn.close();
	          }
	        }
	        catch (SQLException e) {
	          msgCtx.setProperty("STACK_TRACE", e.getMessage());
	          e.printStackTrace();
	        }
	      }
	    }
	    
	    return true;
	  }
	

}
