package io.projectriff.sample.twitter;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Lifecycle;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author David Turanski
 **/
public class TwitterSourceFunction implements Function<Publisher<String>, Publisher<String>> {
	@Autowired
	private Publisher<Message<String>> tweets;

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
