package ibf2022.batch2.ssf.frontcontroller.respositories;

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

}
