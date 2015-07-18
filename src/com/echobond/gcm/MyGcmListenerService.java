package com.echobond.gcm;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.ChatPage;
import com.echobond.activity.MainPage;
import com.echobond.application.MyApp;
import com.echobond.dao.ChatDAO;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.UserMsg;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.google.android.gms.gcm.GcmListenerService;

import android.app.Activity;
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
public class MyGcmListenerService extends GcmListenerService {
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public MyGcmListenerService() {
        super();
    }

    @Override
    public void onCreate() {
    	super.onCreate();
    }
    
    @Override
    public void onMessageReceived(String from, Bundle extras) {
    	JSONObject data = null;
    	try {
    		String msg = extras.getString("data");
			data = new JSONObject(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        // Post notification of received message.
        sendNotification(data);
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
		
		/* acking */
		String url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_ack_msgs);
		JSONObject body = new JSONObject();
		try{
			body.put("msg", JSONUtil.fromObjectToJSON(msg));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}		
		String method = RawHttpRequest.HTTP_METHOD_POST;
		RawHttpRequest request = new RawHttpRequest(url, method, null, body, true);
		RawHttpResponse response = null;
		JSONObject result = null;
		try{
			response = HTTPUtil.getInstance().send(request);
		} catch (SocketTimeoutException e){
			e.printStackTrace();
		} catch (ConnectException e){
			e.printStackTrace();
		}
		if(null != response){
			try {
				result = new JSONObject(response.getMsg());
				if(null == result || result.getInt("success") != 1){
					stopSelf();
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		/* foreground activity handling */
		int index = MyApp.getCurrentActivityIndex();
		if(-1 != index){
			Activity activity = MyApp.getCurrentActivity();
			if(null != activity){
				if(activity instanceof ChatPage){
					String guestId = ((ChatPage)activity).getGuestId();
					if(guestId.equals(msg.getSenderId())){
						Intent chatIntent = new Intent(MyApp.BROADCAST_CHAT_WITH);
						chatIntent.putExtra("id", msg.getId());
						chatIntent.putExtra("guestId", guestId);
						LocalBroadcastManager.getInstance(this).sendBroadcast(chatIntent);
						return;
					}
				}
			}
		}
		Intent msgListIntent = new Intent(MyApp.BROADCAST_UPDATE_MSGLIST);
		LocalBroadcastManager.getInstance(this).sendBroadcast(msgListIntent);
		Intent notifyIntent = new Intent(MyApp.BROADCAST_NOTIFICATION);
		notifyIntent.putExtra(MyApp.INTENT_MSG_NEW_MSG, true);
		LocalBroadcastManager.getInstance(this).sendBroadcast(notifyIntent);

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
        Integer notificationId = MyApp.notificationId.get(msg.getSenderId());
        if(null == notificationId){
        	MyApp.notificationId.put(msg.getSenderId(), MyApp.currentNotificationId);
        	notificationId = MyApp.currentNotificationId++;
        }
        mNotificationManager.notify(notificationId, mBuilder.build());

        NotificationCompat.Builder mBuilder2 = 
        		new NotificationCompat.Builder(this);
        Notification notification = mBuilder2.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Integer type = (Integer) SPUtil.get(this, MyApp.PREF_TYPE_SYSTEM, MyApp.SYS_NOTIFY_TYPE, 0, Integer.class);
        switch(type){
        case MyApp.NOTIFICATION_VIB_ALARM:
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        	break;
        case MyApp.NOTIFICATION_ALARM:
            notification.defaults |= Notification.DEFAULT_SOUND;
            break;
        case MyApp.NOTIFICATION_VIB:
        	notification.defaults |= Notification.DEFAULT_VIBRATE;
        	break;
    	default:
    		break;
        }
        NotificationManager nm = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(MyApp.ALARM_NOTIFICATION_ID, notification);
    }
}