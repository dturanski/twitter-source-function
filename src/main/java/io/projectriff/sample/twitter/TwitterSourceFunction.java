package io.projectriff.sample.twitter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Lifecycle;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

/**
 * @author David Turanski
 **/

@Component
public class TwitterSourceFunction implements Supplier<Flux<String>> {
	private static Log logger = LogFactory.getLog(TwitterSourceFunction.class);
	@Autowired
	private Publisher<Message<String>> tweets;

	@Autowired
	private Lifecycle lifecycle;

	@Override
	public Flux<String> get() {
		logger.info("Starting stream...");
		lifecycle.start();
		return Flux.from(tweets).map(Message::getPayload);
	}
}
