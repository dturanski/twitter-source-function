package io.projectriff.sample.twitter;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.Lifecycle;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author David Turanski
 **/
@SpringBootApplication
public class TwitterSourceFunction implements Function<Publisher<String>, Publisher<String>> {
	@Autowired
	private Publisher<Message<String>> tweets;

	public static void main(String... args) {
		new SpringApplicationBuilder().sources(TwitterSourceFunction.class).bannerMode(Banner.Mode.OFF).run(args);
	}

	@Autowired
	private Lifecycle lifecycle;

	@Override
	public Publisher<String> apply(Publisher<String> input) {

		String command = Mono.from(input).block();

		switch (command) {
		case "start":
			lifecycle.start();
			return Flux.from(tweets).map(Message::getPayload).log();

		case "stop":
			lifecycle.stop();
			return Flux.just("");
		default:

		}
		return Flux.error(new IllegalArgumentException());
	}

}
