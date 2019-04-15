package com.arcanainfo.mediators.mfs.ussdservices;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import com.wso2.esb.mfs.jaxb.Result;
import com.wso2.esb.mfs.jaxb.ResultParameter;

public class DbPoller
  extends AbstractMediator
{
  Log logger = LogFactory.getLog(DbPoller.class);
  
  public boolean mediate(MessageContext msgCtx)
  {
	  logger.info("[DBPoller] Init");
	String result = null;
    String OriginatorConversationID = (String)msgCtx.getProperty("TRANSACTION_ID");
    String Conversationid = (String)msgCtx.getProperty("CONVERSATION_ID");
    String findResponseInCacheSQL = null;
    String FailureReason =null;
    String status = "INIT";
    String isException=null;
    String exceptionDetail=null;
    Hashtable environment = new Hashtable();
    environment.put("java.naming.factory.initial", "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
    try
    {
    	isException="FALSE";
      Context initContext = new InitialContext(environment);
      DataSource ds = (DataSource)initContext.lookup("jdbc/MfsMigrationDS");
      if (ds != null)
      {
        Connection conn = ds.getConnection();
        if (Conversationid == null) {
          findResponseInCacheSQL = "SELECT Result,InsTime,ConversationID,FailureReason FROM MEAI.veon_mfs_async_result WHERE OriginatorConversationID = '" + OriginatorConversationID + "'";
        } else {
          findResponseInCacheSQL = "SELECT Result,InsTime,ConversationID,FailureReason FROM MEAI.veon_mfs_async_result WHERE OriginatorConversationID = '" + OriginatorConversationID + "' and ConversationID ='" + Conversationid + "'";
        }
        msgCtx.setProperty("Query", findResponseInCacheSQL);
        logger.info("Query "+ findResponseInCacheSQL);
        Statement stmt = conn.createStatement();
        int step = 1;
        ResultSet resultstring = stmt.executeQuery(findResponseInCacheSQL);
        do
        {
          if ((!resultstring.next()) || (resultstring.getString("Result") == null) || (resultstring.getString("InsTime") == null))
          {
            Thread.sleep(200L);
            resultstring = stmt.executeQuery(findResponseInCacheSQL);
          }
          else
          {
            result = resultstring.getString("Result");
            Conversationid = resultstring.getString("ConversationID");
            FailureReason = resultstring.getString("FailureReason");
            
            break;
          }
          step++;
        } while (step != 49);
        if (result == null) {
          result = "Timeout no response Recived";
        }
        System.out.println("Result Value is =" + result);
        
        stmt.close();
        conn.close();
        msgCtx.setProperty("result", result);
        msgCtx.setProperty("conversationid", Conversationid);
        msgCtx.setProperty("FailureReason", FailureReason);
      }
    }
    catch (NamingException e)
    {
      e.printStackTrace();
      status = "NAMING EXCEPTION";
   	isException="TRUE";
   	exceptionDetail=e.toString(); 
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      status = "SQL EXCEPTION";
   	isException="TRUE";
   	exceptionDetail=e.toString();  
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
   	isException="TRUE";
   	exceptionDetail=e.toString();
    }
    msgCtx.setProperty("IsException", isException);
    msgCtx.setProperty("ExceptionDetail", exceptionDetail);
    return true;
  }
}
