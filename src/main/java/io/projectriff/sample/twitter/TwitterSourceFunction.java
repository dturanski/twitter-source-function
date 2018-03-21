package io.projectriff.sample.twitter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Lifecycle;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author David Turanski
 **/

@Component
public class TwitterSourceFunction implements Function<Flux<String>, Flux<String>> {
	private static Log logger = LogFactory.getLog(TwitterSourceFunction.class);
	@Autowired
	private Publisher<Message<String>> tweets;

	@Autowired
	private Lifecycle lifecycle;

	@Override
	public Flux<String> apply(Flux<String> input) {

		String command = Mono.from(input).block();

		logger.info("Function received command " + command);

		switch (command) {
		case "start":
			logger.info("Starting stream");
			lifecycle.start();
			return Flux.from(tweets).map(Message::getPayload).log();

		case "stop":
			lifecycle.stop();
			return Flux.just("");
		default:

		}
		logger.error("Unknown command " + command);
		return Flux.error(new IllegalArgumentException());
	}

}
