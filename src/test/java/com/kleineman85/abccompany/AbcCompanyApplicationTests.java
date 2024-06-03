package com.kleineman85.abccompany;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbcCompanyApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	// todo
	@Test
	@Disabled
	void myIntegrationTest() {
		String url = "/api/v1/register";

		restTemplate.postForObject(url, null, Object.class);
		System.out.println("end of test");
	}


}
