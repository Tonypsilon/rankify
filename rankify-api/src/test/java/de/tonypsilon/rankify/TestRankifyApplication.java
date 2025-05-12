package de.tonypsilon.rankify;

import org.springframework.boot.SpringApplication;

public class TestRankifyApplication {

	public static void main(String[] args) {
		SpringApplication.from(RankifyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
