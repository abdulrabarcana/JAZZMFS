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

public class SaveFMBilling
  extends AbstractMediator
{
  public SaveFMBilling() {}
  
  Log logger = LogFactory.getLog(SaveFMBilling.class);
  
  public boolean mediate(MessageContext msgCtx) {
    logger.debug("[SavePassportData] Init");
    
    String Consumer_Number = (String)msgCtx.getProperty("Consumer_Number");
    

    Connection conn = null;
    Hashtable<String, String> environment = new Hashtable();
    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
    
    CallableStatement callableStatement = null;
    

    try
    {
      Context initContext = new InitialContext(environment);
      DataSource ds = (DataSource)initContext.lookup("jdbc/ForiMazdori");
      if (ds != null) {
        conn = ds.getConnection();
      }
      
      String simpleProc = "{ call PRC_BILL_INQUIRY(?,?,?,?,?,?) }";
      
      callableStatement = conn.prepareCall(simpleProc);
      callableStatement.setString(1, Consumer_Number);
      callableStatement.registerOutParameter(2, 12);
      callableStatement.registerOutParameter(3, 12);
      callableStatement.registerOutParameter(4, 12);
      callableStatement.registerOutParameter(5, 12);
      callableStatement.registerOutParameter(6, 12);
      





      callableStatement.executeUpdate();
      String PARAM_RESPONSE_CODE = callableStatement.getString(2);
      String PARAM_CONSUMER_DET = callableStatement.getString(3);
      String PARAM_AMT_WITHIN_DUE_DT = callableStatement.getString(4);
      String PARAM_DUE_DATE = callableStatement.getString(5);
      String PARAM_BILLING_MONTH = callableStatement.getString(6);
      
      msgCtx.setProperty("PARAM_RESPONSE_CODE", PARAM_RESPONSE_CODE);
      msgCtx.setProperty("PARAM_CONSUMER_DET", PARAM_CONSUMER_DET);
      msgCtx.setProperty("PARAM_AMT_WITHIN_DUE_DT", PARAM_AMT_WITHIN_DUE_DT);
      msgCtx.setProperty("PARAM_DUE_DATE", PARAM_DUE_DATE);
      msgCtx.setProperty("PARAM_BILLING_MONTH", PARAM_BILLING_MONTH);
      log.info("[SavePassportData: " + PARAM_RESPONSE_CODE);
      log.debug("[SavePassportData] SP Execution finished");
    } catch (Exception e) {
      log.info("\nStacktrace: " + e.getStackTrace());
      log.info("\nException Message: " + e.getMessage());
      log.info("\nException Cause: " + e.getCause());
      

      e.printStackTrace();
      
      if (callableStatement != null) {
        try {
          callableStatement.close();
        }
        catch (SQLException f) {
          f.printStackTrace();
        }
        try {
          if (conn != null) {
            conn.close();
          }
        }
        catch (SQLException g) {
          g.printStackTrace();
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
          e.printStackTrace();
        }
        try {
          if (conn != null) {
            conn.close();
          }
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    
    return true;
  }
}