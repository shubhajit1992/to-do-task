package com.shubhajit.todotask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodotaskApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodCoverage() {
		TodotaskApplication.main(new String[]{});
	}

}
