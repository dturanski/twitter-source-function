package io.projectriff.sample.twitter;

import io.projectriff.sample.twitter.config.TwitterStreamProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author David Turanski
 **/
@SpringBootApplication
@RestController
public class TwitterSourceApplication {

	public static void main(String... args) {
		SpringApplication.run(TwitterSourceApplication.class, args);
	}

	@Autowired
	private TwitterService twitterService;

	@PostMapping("/streams")
	public Mono<ResponseEntity<Integer>> create(@RequestBody(required = false) TwitterStreamProperties properties) {
		return Mono.just(new ResponseEntity(twitterService.start(properties), HttpStatus.CREATED));
	}

	@DeleteMapping("/streams")
	public Mono<ResponseEntity<?>> deleteAll() {
		if (!twitterService.hasProducers()) {
			return Mono.just(new ResponseEntity(HttpStatus.NOT_FOUND));
		}
		twitterService.shutdownAll();
		return Mono.just(new ResponseEntity(HttpStatus.OK));
	}

	@DeleteMapping(value = "/stream/{id}")
	public Mono<ResponseEntity<?>> delete(@PathVariable int id) {

		if (!twitterService.hasProducers()) {
			return Mono.just(new ResponseEntity(HttpStatus.NOT_FOUND));
		}
		if (twitterService.producer(id) == null) {
			return Mono.just(new ResponseEntity(HttpStatus.NOT_FOUND));
		}

		twitterService.shutdown(id);
		return Mono.just(new ResponseEntity(HttpStatus.OK));
	}

	@GetMapping(value = "/stream/{id}")
	public Mono<?> getStream(@PathVariable int id) {
		if (!twitterService.hasProducers()) {
			return Mono.just(new ResponseEntity(HttpStatus.NOT_FOUND));
		}
		if (twitterService.producer(id) == null) {
			return Mono.just(new ResponseEntity(HttpStatus.NOT_FOUND));
		}
		return Mono.just(twitterService.producer(id).getProperties());
	}

	@GetMapping(value = "/streams")
	public Mono<ResponseEntity<?>> listStreams() {
		if (!twitterService.hasProducers()) {
			return Mono.just(new ResponseEntity(HttpStatus.NOT_FOUND));
		}

		Map<Integer, TwitterStreamProperties> twitterStreams = twitterService.producers().entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getProperties()));
		return Mono.just(new ResponseEntity<Object>(twitterStreams, HttpStatus.OK));
	}
}