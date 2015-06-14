package com.echobond.gcm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.StartPage;
import com.echobond.entity.UserMsg;
import com.echobond.util.JSONUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

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
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, StartPage.class), 0);
        String text = "";
        JSONObject msgJSON = null;
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
			text = URLDecoder.decode(text,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("You've New Message")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(text)).setAutoCancel(true)
        .setContentText(text);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
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