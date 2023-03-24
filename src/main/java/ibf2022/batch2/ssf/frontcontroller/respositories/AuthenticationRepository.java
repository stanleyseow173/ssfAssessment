package ibf2022.batch2.ssf.frontcontroller.respositories;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.ssf.frontcontroller.model.User;

@Repository
public class AuthenticationRepository {

	@Autowired
    RedisTemplate<String, Object> redisTemplate;

	private static final String USER_LIST = "userlist";

	public void saveUser(User user){
		redisTemplate.opsForHash().put(USER_LIST+"_Map", user.getUsername(),user);
	}

	public User findUser(String username){
		User user = (User) redisTemplate.opsForHash().get(USER_LIST+"_Map", username);
		return user;
	}
	// TODO Task 5
	// Use this class to implement CRUD operations on Redis

	public void increaseAttempt(String username){
		User user = findUser(username);
		System.out.println("uSERNAME " + user.getUsername());
		int attempts = user.getAttempts();
		System.out.println("Printing attempts + " + String.valueOf(attempts));
		user.setAttempts(attempts+1);
		this.saveUser(user);
		System.out.println("Now attempts after adding and saving is " + String.valueOf(findUser(username).getAttempts()));
	}

	public void disableUser(String username){
		redisTemplate.opsForValue().setIfAbsent(username, username, Duration.ofMinutes(30));
	}

	public String findDisable(String username){
		String user = (String)redisTemplate.opsForValue().get(username);
		return user;
	}

	public void resetAttempt(String username){
		User user = findUser(username);
		user.setAttempts(0);
		this.saveUser(user);
	}

}
