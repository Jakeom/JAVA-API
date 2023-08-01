package com.softpuzzle.angkor;

import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GracefulShutdownHook implements Runnable {
	private final ConfigurableApplicationContext applicationContext;

    public GracefulShutdownHook(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void run() {
        log.info("tSafeOn System wait for 3 seconds before shutdown SpringContext !!!!!");
        log.info("Spring Application context starting to shutdown !!");
        applicationContext.stop();
        applicationContext.close();
        log.info("Spring Application context is shutdown !!");
    }
}
