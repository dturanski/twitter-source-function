package io.projectriff.sample.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author David Turanski
 **/
@SpringBootApplication
public class TwitterSourceApplication {

	@Autowired
	private ConfigurableApplicationContext context;

	public static void main(String... args) {
		new SpringApplicationBuilder().sources(TwitterSourceApplication.class)
				.bannerMode(Banner.Mode.OFF).run(args);
	}

	@PostConstruct
	public void init() {
		context.registerShutdownHook();
	}
}
