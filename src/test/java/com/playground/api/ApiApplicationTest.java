package com.playground.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = com.ecommerce.api.ApiApplication.class)
@ActiveProfiles("test")
class ApiApplicationTest {

	@Test
	void contextLoads() {}

}
