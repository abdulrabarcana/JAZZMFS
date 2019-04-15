package com.arcanainfo.mediators.mfs.ussdservices;


import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import com.wso2.esb.mfs.jaxb.Result;
import com.wso2.esb.mfs.jaxb.ResultParameter;
import com.wso2.esb.mfs.jaxb.ResultParameters;
public class ParseCPSResult extends AbstractMediator {

	Log logger = LogFactory.getLog(ParseCPSResult.class);
	@Override
	public boolean mediate(MessageContext context) {
		// TODO Auto-generated method stub
		String CPSResultCData = (String)context.getProperty("CPS_RESULT_CDATA");
		 this.logger.info("[CPS_RESULT_CDATA] : " + CPSResultCData);
		 
		 try
		    {
		      JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { Result.class });
		      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		      Result res = (Result)unmarshaller.unmarshal(new ByteArrayInputStream(CPSResultCData.getBytes()));
		      
		      String originatorConversationID = res.getOriginatorConversationID();
		      context.setProperty("P_ORIGINATOR_CONVERSATION_ID", originatorConversationID);
		      context.setProperty("P_CONVERSATION_ID", res.getConversationID());
		      String resultCode = res.getResultCode();
		      context.setProperty("P_RESULT_CODE", resultCode);
		      if (!resultCode.equals("0"))
		      {
		        ResultParameters resultParams = res.getResultParameters();
		        if (resultParams != null)
		        {
		          List<ResultParameter> resultParamList = resultParams.getResultParameter();
		          for (ResultParameter p : resultParamList) {
		            if (p.getKey().equals("FailedReason"))
		            {
		              context.setProperty("P_FAILURE_REASON", p.getValue());
		              break;
		            }
		          }
		        }
		      }
		      else
		      {
		        context.setProperty("FAILURE_REASON", "");
		      }
		    }
		    catch (JAXBException e)
		    {
		      e.printStackTrace();
		    }
		return true;
	}

}
