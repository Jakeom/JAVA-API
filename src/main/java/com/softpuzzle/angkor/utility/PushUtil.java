package com.softpuzzle.angkor.utility;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import com.softpuzzle.angkor.database.mapper.api.user.UserMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.service.api.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushUtil {
	
	// 안드로이드 fcm 푸시
	public void sendAndroid(String token, JSONObject datas, String onoff, String isDev) throws Exception {	
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		post.setHeader("Authorization", "key="+GlobalObjects.getAndroid_servicekey());
		
		JSONObject message = new JSONObject();
				
//		message.put("to", "cSn1pOZWejN9W5yhc7kw2r:APA91bGiZa028ajhR5YX8DmQJo4f1742-zzG4YGwvVnIDh2eBOU4ctm9cOFp50HSSSHdCqZcfbDCzay4nMdSl5kzbkH-qt482GiZ5WOz6uvUyz6nzrCV3zOz00dymqP9H6i1MbxUOCIl");
		message.put("to", token);
		
		message.put("collapse_key", "type_a");
		
//		JSONObject notification = new JSONObject();
//		notification.put("title", datas.getString("message"));
//		notification.put("body", "");
//		message.put("notification", notification);
		
//		JSONObject sendbird  = new JSONObject();
//		sendbird.put("sendbird ", datas);
		message.put("data", datas);

		
		post.setEntity(new StringEntity(message.toString(), "UTF-8"));
		HttpResponse response = client.execute(post);

	}
	
	// IOS apns 푸시
	public void sendIos(String token,JSONObject datas, String onoff, String isDev) throws Exception {

		String basePath = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
		
		String pushUrl = ApnsClientBuilder.PRODUCTION_APNS_HOST;
		String p12FilePath = basePath + "/dist_cert.p12";
		String p12Filekey = "1234";
		if(isDev != null && "Y".equals(isDev)) {
			pushUrl = ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
			p12FilePath = basePath + "/dev_cert.p12";
		}
		
		JSONObject data = datas.getJSONObject("sendbird");
		String message = data.getString("message");
		String soundNm = data.getString("sound");
		int badgeCnt = data.getInt("unread_message_count");

		
		String isMute = data.getString("push_trigger_option"); // off 이면 무음처리 필요
		if("silent".equals(soundNm)) {
			soundNm = "";
		}else if("off".equals(isMute) || "Y".equals(onoff)) {
			message = "";
			soundNm = "";
		}

        final ApnsClient apnsClient = new ApnsClientBuilder()
                .setApnsServer(pushUrl)
                .setClientCredentials(new File(p12FilePath), p12Filekey)
                    .build();
 
        final SimpleApnsPushNotification pushNotification;
 
        final String payload = new SimpleApnsPayloadBuilder()
            		.setAlertTitle(message)
            		.setAlertBody(message)
            		.setAlertSubtitle("")
            		.setBadgeNumber(badgeCnt)
            		.setSound(soundNm)
            		.setContentAvailable(true)
            		.setMutableContent(true)
            		
                    .addCustomProperty("data",datas).build();            
        System.out.println(payload);
        pushNotification = new SimpleApnsPushNotification(token, "com.angkorchat.AngkorChat", payload);
        
        // 위에 만든 payload와 token, 그리고 topic까지 넣어서 noti 객체를 만들어준다
        
		try {
			PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = apnsClient.sendNotification(pushNotification).get();
			 
			if (pushNotificationResponse.isAccepted()) {
			    //성공했을때는 apns client를 종료하고 나감
//				System.out.println(1111);
			} else {
			    //실패했을때는 apns client를 종료하고 다음 ip로 전송함
//				System.out.println(222222);
			}
			apnsClient.close();
		} catch (final Exception e) {
		    e.printStackTrace();
		    apnsClient.close();
		}
        
	}
	
	public void push(String bodyString, UserMapper userDao) {
		
		JSONObject jsonObject = new JSONObject(bodyString);
		JSONObject channel = jsonObject.getJSONObject("channel");		
		JSONObject sender = jsonObject.getJSONObject("sender");		
		JSONObject payload = jsonObject.getJSONObject("payload");
		JSONArray members = jsonObject.getJSONArray("members");
		String channel_url = channel.getString("channel_url");
		Boolean silent =  jsonObject.getBoolean("silent");
		

		String message = payload.getString("message");
		if(message == null ) {
			message = "";
		}else if(message.length() > 400) {
			message = message.substring(0,400);
		}
		
		JSONObject sendData = new JSONObject();
		sendData.put("custom_type", jsonObject.getString("custom_type"));
		sendData.put("message", message);
		sendData.put("created_at", payload.getLong("created_at"));
		sendData.put("unread_message_count", 0);
		sendData.put("channel_type","");
		sendData.put("silent", silent);
		//
		JSONObject ch = new JSONObject();
		ch.put("channel_url", channel.getString("channel_url"));
		ch.put("name", channel.getString("name"));
		ch.put("custom_type", channel.getString("custom_type"));
		sendData.put("channel", ch);
		
		JSONObject sd = new JSONObject();
		sd.put("id", sender.getString("user_id"));
		sd.put("name", sender.getString("nickname"));
		sd.put("profile_url", sender.getString("profile_url"));
		sendData.put("sender", sd);

		// 메세지가 존재할 경우에만 출력
		if(payload.getString("custom_type") != null && payload.getString("custom_type").length() > 0) {

			for(int i = 0; i < members.length() ; i ++) {
				JSONObject member = (JSONObject)members.get(i);
				try {
					HashMap<String, Object> imp = new HashMap<String, Object>();
					imp.put("angkorid", member.getString("user_id"));
					imp.put("custom_type", payload.getString("custom_type"));
					imp.put("payload", payload.toString());
					imp.put("created_at", payload.getLong("created_at"));			
					userDao.isnertMessagePayload(imp);	
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 안읽은 메세지 보내기
				int badgeCnt = member.getInt("unread_message_count");
				
				sendData.put("unread_message_count", badgeCnt);
				sendData.put("push_trigger_option", member.getString("push_trigger_option"));
				
				// 보낸사람에게는 해당 작업하지 않음
				if(sender.get("user_id").equals(member.get("user_id"))) {
					continue;
				}

				try {
					
					HashMap p = new HashMap();
					p.put("angkorId", member.get("user_id"));
					p.put("senderId", sender.getString("user_id"));
					HashMap tokenInfo = userDao.getUserPushToken(p);
					
					if(tokenInfo == null) {
						continue;
					}
					
					JSONArray tmpArray = new JSONArray();
					
					String os = (String)tokenInfo.get("os");
					String token = (String)tokenInfo.get("token");
					String onoff = (String)tokenInfo.get("onoff");

					// 해당 채널 사운드 정보 취득
					HashMap tmp = new HashMap();
					tmp.put("channelUrl", channel_url);
					tmp.put("angkorId", member.getString("user_id"));
					
					String soundNm = userDao.getUserChatSound(tmp);
					if(soundNm == null) { soundNm = "angkor_angkor.mp3";}
					
					sendData.put("sound", soundNm);
					JSONObject sendbird = new  JSONObject();
					sendbird.put("sendbird", sendData);
					// 안드로이드 푸시
					if("a".equals(os)) {
						sendAndroid(token,sendbird,onoff,null);	
					}else if("i".equals(os)) { // ios 푸시
						sendIos(token,sendbird,onoff,null);	
					}else {
						
					}
						
					
				}catch (Exception e) {
					log.info("push Error:::::::::::::::::::::::::"+e);
					// TODO: handle exception
				}
				
			}
		}
	}
}
