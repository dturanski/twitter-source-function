package io.projectriff.sample.twitter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author David Turanski
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TwitterSourceFunctionTest {

	@Autowired
	private TwitterSourceFunction function;

	@Test
	@Ignore
	//Can run with twitter credentials

	public void test() throws InterruptedException {

		function.get().subscribe(System.out::println);

		Thread.sleep(3000);
	}

}
