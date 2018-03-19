package io.projectriff.sample.twitter;

import io.projectriff.sample.twitter.config.TwitterStreamProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author David Turanski
 **/
public class TwitterService {
	private final TwitterTemplate twitterTemplate;
	private final WebClient webClient;
	private Map<Integer, TwitterStreamMessageProducer> producers = new ConcurrentHashMap<>();
	private AtomicInteger count = new AtomicInteger();

	public TwitterService(TwitterTemplate twitterTemplate, WebClient webClient) {
		this.webClient = webClient;
		this.twitterTemplate = twitterTemplate;
	}

	public int start(TwitterStreamProperties properties) {

		TwitterStreamMessageProducer producer;

		FluxMessageChannel tweets = new FluxMessageChannel();

		producer = new TwitterStreamMessageProducer(twitterTemplate,
			properties == null ? new TwitterStreamProperties() : properties);
		producer.setOutputChannel(tweets);
		producer.afterPropertiesSet();
		producer.start();

		Flux<String> tweetStream = Flux.from(tweets).map(m -> (String) m.getPayload());

		tweetStream.subscribe(
			t -> webClient.post()
				.body(BodyInserters.fromObject(t))
				.retrieve()
				.bodyToMono(String.class)
				.subscribe());

		int id = count.incrementAndGet();
		producers.put(id, producer);
		return id;
	}

	public TwitterStreamMessageProducer producer(int id) {
		return producers.get(id);
	}

	public Map<Integer, TwitterStreamMessageProducer> producers() {
		return producers;
	}

	public boolean hasProducers() {
		return producers.size() > 0;
	}

	public void shutdownAll() {
		producers.values().stream().forEach(p -> destroy(p));
		producers.clear();
	}

	public synchronized void shutdown(int id) {
		if (producers.containsKey(id)) {
			destroy(producers.get(id));
			producers.remove(id);
		}
	}

	private void destroy(TwitterStreamMessageProducer producer) {
		if (producer.isRunning()) {
			producer.stop();
			try {
				producer.destroy();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
