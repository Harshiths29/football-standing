package com.FootbalStanding.Footbal.Standing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = FootbalStandingApplication.class,
		properties = {
				"api.key=dummy-key",
				"api.base-url=https://apiv2.apifootball.com/",
				"app.offline-mode=true"
		}
)
class FootbalStandingApplicationTests {

	@Test
	void contextLoads() {
	}
}
