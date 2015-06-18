package com.echobond.gcm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.ChatPage;
import com.echobond.activity.MainPage;
import com.echobond.activity.StartPage;
import com.echobond.application.MyApp;
import com.echobond.dao.ChatDAO;
import com.echobond.entity.UserMsg;
import com.echobond.util.JSONUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Intent service to handle incoming GCM messages, to be waked up when they arrive
 * @author Luck
 *
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    public void onCreate() {
    	super.onCreate();
    }
    
    @SuppressLint("NewApi")
	@Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        JSONObject data = null;
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	// Bundle to JSON
            	try {
            		String msg = extras.getString("data");
					data = new JSONObject(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
                // Post notification of received message.
                sendNotification(data);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(JSONObject data) {
        String text = "";
        JSONObject msgJSON = null;
    	mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

		/* extraction */
        try {
			msgJSON = data.getJSONObject("msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}
        UserMsg msg = (UserMsg) JSONUtil.fromJSONToObject(msgJSON, UserMsg.class);
        text += msg.getUserName() + ": " + msg.getContent() + "\n" + msg.getTime();
        /* decoding */
		try {
			msg.setUserName(URLDecoder.decode(msg.getUserName(), "UTF-8"));
			msg.setContent(URLDecoder.decode(msg.getContent(), "UTF-8"));
			text = URLDecoder.decode(text,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/* storing */
		ContentValues values = msg.putValues();
		getContentResolver().insert(ChatDAO.CONTENT_URI, values);
		
		/* foreground activity handling */
		int index = MyApp.getCurrentActivityIndex();
		if(-1 != index){
			Activity activity = MyApp.getCurrentActivity();
			if(null != activity){
				if(activity instanceof ChatPage){
					String guestId = ((ChatPage)activity).getGuestId();
					if(guestId.equals(msg.getSenderId())){
						Intent intent = new Intent("chatWith" + guestId);
						LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
						//((ChatPage)activity).updateUI();
						return;
					}
				}
			}
		}

		/* intent setting */
		Intent backIntent = new Intent(getApplicationContext(), MainPage.class);
		backIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Intent notificationIntent = new Intent(this, ChatPage.class);
        notificationIntent.putExtra("guestId", msg.getSenderId());
        notificationIntent.putExtra("userName", msg.getUserName());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivities(getApplicationContext(), new Random().nextInt(), 
        		new Intent[]{backIntent, notificationIntent}, 0);

        /* notification */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("You've New Message")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(text)).setAutoCancel(true)
        .setContentText(text);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        NotificationCompat.Builder mBuilder2 = 
        		new NotificationCompat.Builder(this);
        Notification notification = mBuilder2.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND; // To play default notification sound
        NotificationManager nm = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) System.currentTimeMillis(), notification);
    }
    
    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, StartPage.class), 0);

        try {
			msg = URLDecoder.decode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("EchoBond New Message")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}