package io.projectriff.sample.twitter.config;

import io.projectriff.sample.twitter.TwitterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author David Turanski
 **/
@Configuration
@EnableConfigurationProperties({ TwitterCredentials.class, RiffGatewayProperties.class })
public class TwitterSourceConfiguration {

	@Bean
	public WebClient webClient(RiffGatewayProperties properties) {
		return WebClient.builder().baseUrl(properties.url())
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
			.build();
	}

	@Bean
	public TwitterService twitterService(TwitterTemplate twitterTemplate, WebClient webClient) {
		return new TwitterService(twitterTemplate, webClient);
	}

	@Bean
	@ConditionalOnMissingBean
	public TwitterTemplate twitterTemplate(TwitterCredentials credentials) {
		return new TwitterTemplate(credentials.getConsumerKey(), credentials.getConsumerSecret(),
			credentials.getAccessToken(), credentials.getAccessTokenSecret());
	}

}
