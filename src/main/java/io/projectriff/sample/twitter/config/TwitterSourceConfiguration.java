package io.projectriff.sample.twitter.config;

import io.projectriff.sample.twitter.TwitterSourceFunction;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * @author David Turanski
 **/
@Configuration
@EnableConfigurationProperties({ TwitterCredentials.class, TwitterStreamProperties.class })
public class TwitterSourceConfiguration {

	@Autowired
	private TwitterTemplate twitterTemplate;

	@Autowired
	private TwitterStreamProperties twitterStreamProperties;

	@Bean
	public DirectChannel output() {
		return new DirectChannel();
	}

	@Bean
	@Primary
	public TwitterStreamMessageProducer twitterStream() {
		TwitterStreamMessageProducer messageProducer = new TwitterStreamMessageProducer(twitterTemplate,
			twitterStreamProperties);
		messageProducer.setAutoStartup(false);
		messageProducer.setOutputChannel(output());
		return messageProducer;
	}

	@Bean
	TwitterSourceFunction twitterSourceFunction() {
		return new TwitterSourceFunction();
	}

	@Bean
	@ConditionalOnMissingBean
	public TwitterTemplate twitterTemplate(TwitterCredentials credentials) {
		return new TwitterTemplate(credentials.getConsumerKey(), credentials.getConsumerSecret(),
			credentials.getAccessToken(), credentials.getAccessTokenSecret());
	}

	@Bean
	Publisher<Message<String>> tweets() {
		return IntegrationFlows.from(output()).filter(h -> true).toReactivePublisher();
	}

}
