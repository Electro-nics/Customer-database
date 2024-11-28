package com.demoapplication;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;


public class TestContainerTest extends AbstractTestContainer {
	
	@Test
	void canStartPostgresDB() {
		assertThat(postgreSQLContainer.isRunning()).isTrue();
		assertThat(postgreSQLContainer.isCreated()).isTrue();
		
	}
	

}
