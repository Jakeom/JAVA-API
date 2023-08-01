package com.softpuzzle.angkor;


import com.softpuzzle.angkor.utility.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@RequiredArgsConstructor
@EnableScheduling
@ServletComponentScan
@SpringBootApplication
@Slf4j
public class AnkorAoiApplication implements CommandLineRunner{
	
	private static ConfigurableApplicationContext applicationContext = null;

	public static void main(String[] args) {
		
		BasicConfigurator.configure();

		applicationContext = new SpringApplicationBuilder(AnkorAoiApplication.class) //                                                                                                        
				.registerShutdownHook(false) //
				.listeners(new ApplicationPidFileWriter(System.getProperty("user.dir") + "/application.pid")) //
				.listeners(new LoadAdditionalProperties()).build() //
				.run(args);
	}


	@Override
	public void run(String... args) throws Exception {
	}

}
