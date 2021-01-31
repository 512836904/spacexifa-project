package com;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttReceriveCallback implements MqttCallback{
	
	private MyMqttClient myMqttClient;

	public MqttReceriveCallback(MyMqttClient myMqttClient) {
		this.myMqttClient = myMqttClient;
	}

	@Override
	public void connectionLost(Throwable cause) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		myMqttClient.webdata(new String(message.getPayload()));
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {

	}
}