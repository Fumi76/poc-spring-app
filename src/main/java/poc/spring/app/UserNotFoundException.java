package poc.spring.app;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User does not exist.")
public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private String userId;

	public UserNotFoundException(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
}