package io.projectriff.sample.twitter;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author David Turanski
 **/
@SpringBootApplication
public class TwitterSourceApplication {

	public static void main(String... args) {
		new SpringApplicationBuilder().sources(TwitterSourceApplication.class)
				.bannerMode(Banner.Mode.OFF).run(args);
	}
}
