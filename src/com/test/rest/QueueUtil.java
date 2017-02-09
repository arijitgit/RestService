package com.test.rest;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.json.JSONObject;

public class QueueUtil {

	private static String serverUrl = " tcp://dfwlndtibem-09.supermedia.com:7222,tcp://dfwlndtibem-11.supermedia.com:7222 | tcp://dfwlndtibem-10.supermedia.com:7222,tcp://dfwlndtibem-12.supermedia.com:7222"; // values changed
	private static String userName = "vision";
	private static String password = "vision";	
	private static Integer maxLimitToConsume   = 200;
	public static String queueName = "queue.sample";

	/**
	 * 
	 * @param queueName
	 * @param messageStr
	 * @throws JMSException 
	 */
	public static void sendQueueMessage(String queueName, JSONObject jsonObj) throws Exception {

		Connection connection 		= null;
		Session session 	 		= null;
		MessageProducer msgProducer = null;
		Destination destination 	= null;
		TextMessage msg				= null;

		try {

			System.out.println("Publishing to destination '" + queueName);

			ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory(serverUrl);
			connection = factory.createConnection(userName, password);


			session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);

			msgProducer = session.createProducer(null);
			msg = session.createTextMessage();
			msg.setStringProperty("SourceId", userName);
			msg.setStringProperty("BusinessEvent", password);

			msg.setText(jsonObj.toString());
			msgProducer.send(destination, msg);

			System.out.println("Published message >> " + jsonObj.toString());

		}catch (JMSException e) {
			e.printStackTrace();
			throw e;
		}catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}finally {
			try {
				if(connection != null)
					connection.close();
			} catch (Exception e) {
				
				e.printStackTrace();
				throw e;
			}
		}
	}

	public static JSONObject retrieveQueueMessage(String queueName ) throws Exception{
		return retrieveQueueMessage(queueName , maxLimitToConsume);
	}

	/**
	 * 
	 * @param topicName
	 * @param messageStr
	 * @throws JMSException 
	 */
	public static JSONObject retrieveQueueMessage(String queueName ,Integer maxLimit) throws Exception {

		Connection connection 	= null;
		Session session 		= null;     
		Destination destination = null;
		TextMessage msg         = null;   
		JSONObject consumeResult = null; 
		Map<String, String> objLst = null;

		try {
			
			System.out.println("Retrieving from destination -> '" + queueName+ "'\n");
			ConnectionFactory factory = new com.tibco.tibjms.TibjmsConnectionFactory(serverUrl);
			connection = factory.createConnection(userName, password);
			session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			MessageConsumer msgConsumer = session.createConsumer(destination);            


			connection.start();
			
			consumeResult = new JSONObject(); 
			objLst = new HashMap<String, String>();

			int i = 1;
			/* read messages */
			while (true)
			{
				/* receive the message */
				
				msg = (TextMessage) msgConsumer.receiveNoWait();
				if (msg == null ||  (maxLimit!= null && i == maxLimit))
					break;
				
				

				/*if (ackMode == Session.CLIENT_ACKNOWLEDGE ||
                    ackMode == Tibjms.EXPLICIT_CLIENT_ACKNOWLEDGE ||
                    ackMode == Tibjms.EXPLICIT_CLIENT_DUPS_OK_ACKNOWLEDGE)
                {
                    msg.acknowledge();
                }*/
				msg.acknowledge();
				System.out.println("Received message:: @@@@@@  "+ msg.getText() );
				
				
				objLst.put("Message_"+i,msg.getText());
				/*Enumeration msgRetrieveProp = msg.getPropertyNames();
				while(msgRetrieveProp.hasMoreElements()) {
					Object aa = msgRetrieveProp.nextElement();
					System.out.println(aa);
				}*/
				i++;
			}

			consumeResult.put("consumeResult",objLst);

		}catch (JMSException e) {
			e.printStackTrace();
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			try {
				if(connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		return consumeResult;
	}


	public static void main(String[] args) throws Exception {
		/*for (int i=0; i<20; i++) {
			JSONObject result = new JSONObject(); 

			Map<String, Object> objLst = new HashMap<String, Object>();
			objLst.put("ItemId",new  Integer(i));
			objLst.put("price",i*10);
			objLst.put("message","sucess");
			result.put("result ", objLst);
			QueueUtil.sendQueueMessage("queue.sample",result);
		}*/
		
		QueueUtil.retrieveQueueMessage("queue.sample");
	}
}
