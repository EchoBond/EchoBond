package com.echobond.connector;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.MainPage;
import com.echobond.application.MyApp;
import com.echobond.dao.ChatDAO;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.entity.User;
import com.echobond.entity.UserMsg;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Intent service to fetch data
 * @author Luck
 *
 */
public class DataFetchIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public DataFetchIntentService() {
        super("DataFetchIntentService");
    }

    @Override
    public void onCreate() {
    	super.onCreate();
    }
    
    @SuppressLint("NewApi")
	@Override
    protected void onHandleIntent(Intent intent) {
    	/* request */
		User user = new User();
		user.setId((String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class));
		String url = HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_fetch_data);
		String method = RawHttpRequest.HTTP_METHOD_POST;
		JSONObject body = new JSONObject();
		try {
			body.put("user", JSONUtil.fromObjectToJSON(user));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
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
			try{
				result = new JSONObject(response.getMsg());
				JSONObject count = result.getJSONObject("count");
				int msgCount = count.getInt("msgCount");
				if(msgCount > 0)
					sendNotification(result);
			} catch (JSONException e){
				e.printStackTrace();
			}
		}
		stopSelf();
    }

    @SuppressWarnings("unchecked")
	private void sendNotification(JSONObject data) {
    	JSONObject count;
    	int userCount = 0, msgCount = 0;
		try {
			count = data.getJSONObject("count");
			userCount = count.getInt("userCount");
			msgCount = count.getInt("msgCount");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        String text = msgCount + " new messages from " + userCount + " people!";
        
    	mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

		/* extraction */
		TypeToken<ArrayList<UserMsg>> token = new TypeToken<ArrayList<UserMsg>>(){};
		ArrayList<UserMsg> msgList = null;
		try {
			msgList = (ArrayList<UserMsg>) JSONUtil.fromJSONArrayToList(data.getJSONArray("msgs"), token);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		/* storing */
		ContentValues[] contentValues = new ContentValues[msgList.size()];
		int i = 0;
		try {
			for(UserMsg msg: msgList){
				msg.setUserName(URLDecoder.decode(msg.getUserName(), "UTF-8"));
				msg.setContent(URLDecoder.decode(msg.getContent(), "UTF-8"));
				text = URLDecoder.decode(text,"UTF-8");
				contentValues[i++] = msg.putValues();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		getContentResolver().bulkInsert(ChatDAO.CONTENT_URI, contentValues);
		
		/* intent setting */
        Intent notificationIntent = new Intent(this, MainPage.class);
        notificationIntent.putExtra("fromDataFetch", true);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivities(getApplicationContext(), new Random().nextInt(), 
        		new Intent[]{notificationIntent}, 0);

        /* notification */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("You've New Messages")
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
    
}