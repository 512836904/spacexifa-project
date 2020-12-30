package com;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttReceriveCallback implements MqttCallback{
	
	private MyMqttClient myMqttClient;

	public MqttReceriveCallback(MyMqttClient myMqttClient) {
		// TODO Auto-generated constructor stub
		this.myMqttClient = myMqttClient;
	}

	@Override
	public void connectionLost(Throwable cause) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		//System.out.println("com.Client ������Ϣ���� : " + new String(message.getPayload()));

		myMqttClient.webdata(new String(message.getPayload()));
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {

	}
}