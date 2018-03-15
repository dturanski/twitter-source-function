package io.projectriff.sample.twitter;

import org.reactivestreams.Publisher;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.Lifecycle;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author David Turanski
 **/
@SpringBootApplication
public class TwitterSourceFunction {

	public static void main(String... args) {
		new SpringApplicationBuilder()
			.sources(TwitterSourceFunction.class)
			.bannerMode(Banner.Mode.OFF)
			.web(WebApplicationType.NONE)
			.run(args);
	}


	private final Publisher<Message<?>> tweets;

	private final Lifecycle lifecycle;

	public TwitterSourceFunction(Publisher<Message<?>> tweets, Lifecycle lifecycle) {
		this.tweets = tweets;
		this.lifecycle = lifecycle;
	}


	public Flux<?> twitterStream(Mono<String> command) {

		switch (command.block()) {
		case "start":
			lifecycle.start();
			return Flux.from(tweets).map(Message::getPayload);

		case "stop":
			lifecycle.stop();
			return Flux.just("");
		default:

		}
		return Flux.error(new IllegalArgumentException());
	}
}