package poc.spring.app;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserFactory factory = new UserFactory();
	private final UserModel model = new UserModel();

	/* Create a user */
	@RequestMapping(method = RequestMethod.POST)
	public User newUser(@RequestBody(required = false) User userbody) throws IOException {
		logger.info("CALL newUser");
		User user;
		if (userbody == null || userbody.getName() == null) {
			logger.error("", new Exception("これはテスト例外です。"));
			user = factory.newUser();
		} else {
			user = factory.newUser(userbody.getName());
		}
		return user;
	}

	/* Update a user */
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public User updateUser(@PathVariable String userId, @RequestBody User user) {
		model.saveUser(user);
		return user;
	}

	/* Get all user */
	@RequestMapping(method = RequestMethod.GET)
	public List<User> getUsers() {
		logger.info("CALL getUsers");
		return factory.getUsers();
	}

	/* Get a user */
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public User getUser(@PathVariable String userId) throws UserNotFoundException {
		logger.info("CALL getUser userId="+userId);
		return factory.getUser(userId);
	}

	/* Delete a user */
	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable String userId) throws UserNotFoundException {
		model.deleteUser(userId);
	}
}
