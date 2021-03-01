package poc.spring.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class FrontController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(path = "/front/api3", method = RequestMethod.GET)
	public String api3(@RequestHeader Map<String, String> headers) throws Exception {
		logger.info("START api3");
		headers.forEach((key, value) -> {
			logger.info(String.format("Header '%s'=%s", key, value));
	    });		
		logger.info("END   api3");
		return "{\"MESSAGE\":\"OK\"}";
	}
	
	/* Get all user */
	@RequestMapping(path = "/front/api1", method = RequestMethod.GET)
	public List<User> api1() throws Exception {
		logger.info("CALL api1");
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("http://user-service:80/api/user");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		try {
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			ObjectMapper mapper = new ObjectMapper();
			List<User> list = mapper.readValue(inputStream, new TypeReference<List<User>>() {
			});
			return list;
		} finally {
			response.close();
		}
	}

	/* Create a user */
	@RequestMapping(path = "/front/api2", method = RequestMethod.POST)
	public User api2(@RequestBody(required = false) User userbody) throws IOException {
		logger.info("CALL api2");
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("http://user-service:80/api/user");
		if (userbody != null && userbody.getName() != null) {
			httpPost.addHeader("Content-Type", "application/json");
			String json = "{\"name\":\""+userbody.getName()+"\"}";
			StringEntity entity = new StringEntity(json, "UTF-8");
			httpPost.setEntity(entity);
		}
	    CloseableHttpResponse response = httpclient.execute(httpPost);
		try {
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(inputStream, new TypeReference<User>() {
			});
			return user;
		} finally {
			response.close();
		}
	}

}
