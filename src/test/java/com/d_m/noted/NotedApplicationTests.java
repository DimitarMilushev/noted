package com.d_m.noted;

import com.d_m.noted.users.UsersController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"WEB_CLIENT_ORIGIN=localhost"
})
class NotedApplicationTests {
	@Autowired
	private UsersController usersController;

	@Test
	void contextLoads() {
		assertThat(usersController).isNotNull();
	}

}
