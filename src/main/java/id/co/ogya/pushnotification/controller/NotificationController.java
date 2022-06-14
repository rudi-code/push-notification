package id.co.ogya.pushnotification.controller;


import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import id.co.ogya.pushnotification.model.ApiResponse;
import id.co.ogya.pushnotification.model.UserToken;
import id.co.ogya.pushnotification.service.NotificationService;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class NotificationController {

	private final NotificationService notificationService;
	private final WebClient webClient;
	private int seq = 0;

	public NotificationController(NotificationService notificationService, WebClient webClient) {
	    this.notificationService = notificationService;
	    this.webClient = webClient;
	} 
	
	@GetMapping("/findAll")
	public ResponseEntity<List<UserToken>> findAll(){
		List<UserToken> listEntity = notificationService.findAll();
		return new ResponseEntity<>(listEntity, HttpStatus.OK);
	}

	@GetMapping("/sendNotification")
	public ResponseEntity<ApiResponse> sendNotification(@RequestParam String userName, 
			@RequestParam int alertType) throws InterruptedException {
		
		ApiResponse apiResponse = new ApiResponse();
    	String token = notificationService.getToken(userName);
    	if (token == null) {
    		apiResponse.setDate(new Date());
    		apiResponse.setMessage("UserName not registered, Register first.");
		} else if (alertType > 4) {
			apiResponse.setDate(new Date());
    		apiResponse.setMessage("Not in Alert.");
		} else {
		    try {
		    	System.out.println("Sending Notif...");
				this.notificationService.sendNotification(token, alertType);
				
				apiResponse.setDate(new Date());
				apiResponse.setMessage("Success sending notification.");
				
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				apiResponse.setDate(new Date());
				apiResponse.setMessage("Failed sending notification.");
			}
		}
    	
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK); 
	}

	@PostMapping("/register")
	public  ResponseEntity<ApiResponse> register(@RequestBody UserToken userToken) {
		ApiResponse apiResponse = new ApiResponse();
		try {
			if(notificationService.registerToken(userToken)) {
				apiResponse.setDate(new Date());
				apiResponse.setMessage("Success save/update token.");
			}else {
				apiResponse.setDate(new Date());
				apiResponse.setMessage("Failed save/update token.");
			}
		  } catch (Exception e) {
			  apiResponse.setDate(new Date());
			  apiResponse.setMessage("Failed save/update token.");
		}  
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
}
