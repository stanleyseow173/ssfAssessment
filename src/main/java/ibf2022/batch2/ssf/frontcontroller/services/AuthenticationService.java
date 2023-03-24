package ibf2022.batch2.ssf.frontcontroller.services;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch2.ssf.frontcontroller.model.User;
import ibf2022.batch2.ssf.frontcontroller.respositories.AuthenticationRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class AuthenticationService {

	@Autowired
	AuthenticationRepository authRepo;

	public static final String AUTHENTICATE_URL = "https://auth.chuklee.com/api/authenticate";

	public static String ERROR_MSG;
	
	public void saveUser(User user){
		authRepo.saveUser(user);
	}

	public User findUser(String username){
		User user = authRepo.findUser(username);
		return user;
	}
	
	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public void authenticate(String username, String password) throws Exception {
		String jsonString = new JSONObject()
							.put("username", username)
							.put("password",password)
							.toString();

		RequestEntity<String> req = RequestEntity.post(AUTHENTICATE_URL)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(jsonString);

		System.out.println("The username sent was" + username);
		System.out.println("The password sent was" + password);

		ResponseEntity<String> resp;
		RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		//template.setErrorHandler(new MyErrorHandler());

		try {
			resp = template.exchange(req, String.class);
			String payload = resp.getBody();
			String respHeaders = resp.getHeaders().toString();
			System.out.println("The headers received is" + respHeaders);
			System.out.println("The response received is" + payload);
			User user = authRepo.findUser(username);
			user.setAuthenticated(true);
			authRepo.saveUser(user);
		} catch(HttpClientErrorException.Unauthorized hex){
			//System.out.println("status text:" + hex.getStatusText());
			//System.out.println("message text:" + hex.getMessage());
			//System.out.println("message text:" + hex.toString());
			System.out.println("Went into HTTP unauthorised exception");
			String jsonResponseStr = hex.getResponseBodyAsString();
			JsonReader reader = Json.createReader(new StringReader(jsonResponseStr));
			JsonObject json = reader.readObject();
			String responseMsg = json.getString("message");
			System.out.println("The response message is:  " + responseMsg);
			ERROR_MSG = responseMsg;
			//throw hex;
		} catch(HttpClientErrorException.BadRequest bex){
			System.out.println("Went into HTTP bad request exception" + bex.getResponseBodyAsString());
			String jsonResponseStr = bex.getResponseBodyAsString();
			JsonReader reader = Json.createReader(new StringReader(jsonResponseStr));
			JsonObject json = reader.readObject();
			String responseMsg = json.getString("message");
			ERROR_MSG = responseMsg;
		}
		catch (Exception ex) {
			System.out.println("Went into exception");
			ex.printStackTrace();
			throw ex;
		}

		

		//JsonReader reader = Json.createReader(new StringReader(payload));
	}

	public Float calc(int firstNum, int secondNum, String operator){
		Float answer;
		List<String> operatorList = Arrays.asList("+", "-" , "/" , "*");
		if (operator.equals("-")){
            answer = (float)firstNum - (float)secondNum;
        }else if (operator.equals("+")){
            answer = (float)firstNum + (float)secondNum;
        }else if (operator.equals("*")){
            answer = (float)firstNum * (float)secondNum;
        }else{
            answer = (float)firstNum / (float)secondNum;
        }
		return answer;
	}

	public void increaseAttempt(String username){
		authRepo.increaseAttempt(username);
	}

	public void resetAttempt(String username){
		authRepo.resetAttempt(username);
	}
	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
		authRepo.disableUser(username);
	}

	public String findDisable(String username){
		String user = authRepo.findDisable(username);
		return user;
	}


	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return false;
	}
}
