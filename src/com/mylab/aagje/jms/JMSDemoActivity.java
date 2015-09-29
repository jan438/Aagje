/**
 * Copyright (c) 2007-2015, Kaazing Corporation. All rights reserved.
 */

package com.mylab.aagje.jms;

import java.net.URI;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.kaazing.gateway.jms.client.JmsConnectionFactory;
import com.mylab.aagje.MainActivity;
import com.mylab.aagje.ObjectItem;

public class JMSDemoActivity extends FragmentActivity {

	private static String TAG = "com.kaazing.gateway.jms.client.android.demo";
	private JmsConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private boolean send;
	private String scanned_articles = "Test message";
	private DispatchQueue dispatchQueue;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		scanned_articles = "";
		connection = null;
		session = null;
		int count = MainActivity.Cart.size();
		for (int i = 0; i < count; i++) {
			ObjectItem article = MainActivity.Cart.get(i);
			scanned_articles = scanned_articles + article.getItemId() + ":" + article.getItemName() + ":" + article.getCount();			
			if (i < count - 1) scanned_articles = scanned_articles + ",";
		}
		if (connectionFactory == null) {
			try {
				connectionFactory = JmsConnectionFactory
						.createConnectionFactory();
				dispatchQueue = new DispatchQueue("DispatchQueue");
				dispatchQueue.start();
				dispatchQueue.waitUntilReady();
				dispatchQueue.dispatchAsync(new Runnable() {
					public void run() {
						try {
							String location = "ws://192.168.1.31:8001/jms";
							connectionFactory.setGatewayLocation(URI
									.create(location));
							connection = connectionFactory.createConnection();
							connection.start();
							session = connection.createSession(false,
									Session.AUTO_ACKNOWLEDGE);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				int loop_count = 0;
				while ((connection == null) && (session == null) && (loop_count < 10)) {
					Thread.sleep(1000);
					loop_count = loop_count + 1;
				}
				if ((connection == null) || (session == null)) {
					Intent mainintent = new Intent(this, MainActivity.class);
					startActivity(mainintent);
					finish();
				}
				send = false;
				final boolean sendBinary = false;
				dispatchQueue.dispatchAsync(new Runnable() {
					public void run() {
						try {
							String destinationName = "/topic/destination";
							MessageProducer producer = session
									.createProducer(getDestination(destinationName));
							Message message;
							if (sendBinary) {
								BytesMessage bytesMessage = session
										.createBytesMessage();
								bytesMessage.writeUTF(scanned_articles);
								message = bytesMessage;
							} else {
								message = session.createTextMessage(scanned_articles);
							}
							producer.send(message);
							producer.close();
							send = true;
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				});
				loop_count = 0;
				while (!send && (loop_count < 10)) {
					Thread.sleep(1000);
					loop_count = loop_count + 1;
				}
				dispatchQueue.removePendingJobs();
				dispatchQueue.quit();
				new Thread(new Runnable() {
					public void run() {
						try {
							connection.close();
						} catch (JMSException e) {
							e.printStackTrace();
						} finally {
							connection = null;
						}
					}
				}).start();
				loop_count = 0;
				while ((connection != null) && (loop_count < 10)) {
					Thread.sleep(1000);
					loop_count = loop_count + 1;
				}
				Intent mainintent = new Intent(this, MainActivity.class);
				startActivity(mainintent);
				finish();
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void onPause() {
		if (connection != null) {
			dispatchQueue.dispatchAsync(new Runnable() {
				@Override
				public void run() {
					try {
						connection.stop();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
		}
		super.onPause();
	}

	public void onResume() {
		if (connection != null) {
			dispatchQueue.dispatchAsync(new Runnable() {
				@Override
				public void run() {
					try {
						connection.start();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
		}
		super.onResume();
	}

	public void onDestroy() {
		if (connection != null) {
			disconnect();
		}
		super.onDestroy();
	}

	private void disconnect() {
		dispatchQueue.removePendingJobs();
		dispatchQueue.quit();
		new Thread(new Runnable() {
			public void run() {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				} finally {
					connection = null;
				}
			}
		}).start();
	}

	private Destination getDestination(String destinationName)
			throws JMSException {
		Destination destination;
		if (destinationName.startsWith("/topic/")) {
			destination = session.createTopic(destinationName);
		} else if (destinationName.startsWith("/queue/")) {
			destination = session.createQueue(destinationName);
		} else {
			return null;
		}
		return destination;

	}
}
