package ibf2022.batch2.ssf.frontcontroller.services;

import java.io.StringReader;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch2.ssf.frontcontroller.model.User;
import ibf2022.batch2.ssf.frontcontroller.respositories.AuthenticationRepository;
import jakarta.json.Json;
import jakarta.json.JsonReader;

@Service
public class AuthenticationService {

	@Autowired
	AuthenticationRepository authRepo;

	public static final String AUTHENTICATE_URL = "https://auth.chuklee.com/api/authenticate";
	
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

		ResponseEntity<String> resp;
		RestTemplate template = new RestTemplate();
		try {
			resp = template.exchange(req, String.class);
		} catch (Exception ex) {
			throw ex;
		}

		String payload = resp.getBody();
		System.out.println("The username sent was" + username);
		System.out.println("The password sent was" + password);
		System.out.println("The response received is" + payload);
		//JsonReader reader = Json.createReader(new StringReader(payload));
	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return false;
	}
}
