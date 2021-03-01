package poc.spring.app;

import java.net.URL;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ECSPlugin;
import com.amazonaws.xray.strategy.FixedSegmentNamingStrategy;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;

@Configuration
public class WebConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Value("${myServiceName}")
    private String myServiceName;
	
	@Bean
	public Filter TracingFilter() {
		logger.info("myServiceName=["+myServiceName+"]");
		// Segment name
		return new AWSXRayServletFilter(new FixedSegmentNamingStrategy(myServiceName));
	}

	static {
		AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withPlugin(new EC2Plugin()).withPlugin(new ECSPlugin());

		URL ruleFile = WebConfig.class.getResource("/sampling-rules.json");
		builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));

		AWSXRay.setGlobalRecorder(builder.build());

		AWSXRay.beginSegment("Scorekeep");
		
		if (System.getenv("NOTIFICATION_EMAIL") != null) {
			try {
				Sns.createSubscription();
			} catch (Exception e) {
				logger.warn("Failed to create subscription for email " + System.getenv("NOTIFICATION_EMAIL"));
			}
		}

		AWSXRay.endSegment();
	}
}
