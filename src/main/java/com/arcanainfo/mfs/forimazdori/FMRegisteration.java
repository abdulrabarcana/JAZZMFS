package com.arcanainfo.mfs.forimazdori;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class FMRegisteration extends AbstractMediator
{
	public FMRegisteration() {}
    
  Log logger = LogFactory.getLog(FMRegisteration.class);
  
  
  public boolean mediate(MessageContext msgCtx) {
    logger.debug("[SavePassportData] Init");
    
    String initiator_Msisdn = (String)msgCtx.getProperty("I_MSISDN");
    String initiator_cnic = (String)msgCtx.getProperty("I_CNINC");
    String beneficiary_Msisdn = (String)msgCtx.getProperty("B_MSISDN");
    String beneficiary_cnic = (String)msgCtx.getProperty("B_CNINC");
    String name_ = (String)msgCtx.getProperty("Name");
    String gender = (String)msgCtx.getProperty("Gender");
    String Birthday = (String)msgCtx.getProperty("Birthday");
    String Education = (String)msgCtx.getProperty("Education");
    String Occupation = (String)msgCtx.getProperty("Occupation");
    String Experience = (String)msgCtx.getProperty("Experience");
    String Summary = (String)msgCtx.getProperty("Summary");
    String s1 = (String)msgCtx.getProperty("Fee");
    Double Fee = Double.valueOf(Double.parseDouble(s1));
    String EffectiveDate = (String)msgCtx.getProperty("EffectiveDate");
    String RefereeMSISDN = (String)msgCtx.getProperty("RefereeMSISDN");
    String s2 = (String)msgCtx.getProperty("Rate");
    Double Rate = Double.valueOf(Double.parseDouble(s2));
    String Channel = (String)msgCtx.getProperty("Channel");
    String Created_IP = (String)msgCtx.getProperty("Created_IP");
    String created_by = (String)msgCtx.getProperty("created_by");
    
    Connection conn = null;
    Hashtable<String, String> environment = new Hashtable();
    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
    
    CallableStatement callableStatement = null;
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-mm-yyyy");
    
    try
    {
      Context initContext = new InitialContext(environment);
      DataSource ds = (DataSource)initContext.lookup("jdbc/ForiMazdori");
      java.util.Date date1 = sdf1.parse(Birthday);
      java.util.Date date2 = sdf1.parse(EffectiveDate);
      java.sql.Date sqlStartDate1 = new java.sql.Date(date1.getTime());
      java.sql.Date sqlStartDate2 = new java.sql.Date(date2.getTime());
      if (ds != null) {
        conn = ds.getConnection();
      }
      
      String simpleProc = "{ call PRC_INITIATE_SUBSCRIBTION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
      
      callableStatement = conn.prepareCall(simpleProc);
      callableStatement.setString(1, beneficiary_cnic);
      callableStatement.setString(2, initiator_Msisdn);
      callableStatement.setString(3, beneficiary_Msisdn);
      callableStatement.setString(4, initiator_cnic);
      callableStatement.registerOutParameter(5, 12);
      callableStatement.setDouble(6, Fee.doubleValue());
      callableStatement.setString(7, name_);
      callableStatement.setString(8, Occupation);
      callableStatement.setString(9, Experience);
      callableStatement.setString(10, RefereeMSISDN);
      callableStatement.setDate(11, sqlStartDate2);
      callableStatement.setString(12, Channel);
      callableStatement.setString(13, "USSD");
      callableStatement.setString(14, "10.50.13.117");
      callableStatement.setString(15, gender);
      callableStatement.setString(16, Education);
      callableStatement.setDate(17, sqlStartDate1);
      callableStatement.setDouble(18, Rate.doubleValue());
      callableStatement.setString(19, Summary);
      callableStatement.registerOutParameter(20, 12);
      callableStatement.registerOutParameter(21, 12);
      



      callableStatement.executeUpdate();
      String param_tid = callableStatement.getString(5);
      String param_ret_code = callableStatement.getString(20);
      String param_ret_desc = callableStatement.getString(21);
      
      msgCtx.setProperty("param_tid", param_tid);
      msgCtx.setProperty("param_ret_code", param_ret_code);
      msgCtx.setProperty("param_ret_desc", param_ret_desc);
      
      log.debug("[SavePassportData] SP Execution finished");
    }
    catch (Exception e) {
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
 
 
