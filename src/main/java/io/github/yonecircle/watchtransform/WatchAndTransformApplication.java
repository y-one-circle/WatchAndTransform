package io.github.yonecircle.watchtransform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//スレッドプールは後で実装予定
public class WatchAndTransformApplication {

	public static void main(String[] args) {
		SpringApplication.run(WatchAndTransformApplication.class, args);
	}

}
