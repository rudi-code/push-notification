package id.co.ogya.pushnotification.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor.Base;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.TopicManagementResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;

import id.co.ogya.pushnotification.model.UserToken;
import id.co.ogya.pushnotification.repository.NotificationRepository;

@Service
public class NotificationService {
	
	@Autowired
	NotificationRepository notificationRepository;

	  public NotificationService() {
		  
//	    Path p = Paths.get(settings.getServiceAccountFile());
//	    Path path = Paths.get("D:\\Work\\BCA\\eclipse-workspace\\push-notification\\src\\main\\resources\\json\\fir-pwa-springboot-firebase-adminsdk-zvvks-db2a39ea5f.json");
	    try  {
	    	
//	    	String path = System.getProperty("user.dir") + File.separator 
//	    			+ "src\\main\\resources\\json" + File.separator 
//	    			+ "service_account.json";
//	    	InputStream serviceAccount = new FileInputStream("josn/service_account.json");
	    	InputStream serviceAccount = this.getClass().getClassLoader().getResourceAsStream("service_account.json");
	    	FirebaseOptions options = new FirebaseOptions.Builder()
	          .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

	    	FirebaseApp.initializeApp(options);
	    }
	    catch (IOException e) {
	    }
	  }

	  public void sendNotification(String token, int alertType)
		      throws InterruptedException, ExecutionException {
		  
			/*alertType = 1 --> reset password
			  alertType = 2 --> update status
			  alertType = 3 --> data not valid
			  alertType = 4 --> data anulir*/
		  
		  	String webpush = "BKKBN";
			String notifMessage = "";
			
//			System.out.println(System.getProperty("user.dir"));
			
			if(alertType == 1) {
//				webpush = "password changed";
				notifMessage = "Your Password Has Been Changed";
			}else if (alertType == 2) {
//				webpush = "update status";
				notifMessage = "Success Update";
			}else if (alertType == 3) {
//				webpush = "status data";
				notifMessage = "Data Not Valid";
			}else if (alertType == 4) {
//				webpush = "status data";
				notifMessage = "Data Anulir";
			}
			
			WebpushNotification.Action a = new WebpushNotification.Action("google.com", webpush);

		    Message message = Message.builder().setToken(token)
		        .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
		            .setNotification(new WebpushNotification(webpush,notifMessage, "mail2.png"))
		            .build())
		        .build();
			  
			String response = FirebaseMessaging.getInstance().sendAsync(message).get();
		    System.out.println("Sending Alert Done.");
		  }


	  public boolean registerToken(UserToken userToken) {
		    try {
		    	
		    	if(notificationRepository.isExist(userToken.getUserName())) {
		    		notificationRepository.updateToken(userToken);
		    		System.out.println("Success update Token with Username "+userToken.getUserName());
		    	}else if(notificationRepository.registerToken(userToken)) {
			    	System.out.println("Success saved registration Token with Username "+userToken.getUserName());
		    	}else {
		    		return false;
		    	}
		    	return true;
		    }
		    catch (Exception e) {
		    	return false;
		    }
		  }
	  
	  public String getToken(String userName) {
			return notificationRepository.getTokenByUserName(userName); 
		}

	public List<UserToken> findAll() {
		// TODO Auto-generated method stub
		List<UserToken> listEntity = notificationRepository.findAll();
		return listEntity;
	}

	}