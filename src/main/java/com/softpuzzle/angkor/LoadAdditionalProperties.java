package com.softpuzzle.angkor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.softpuzzle.angkor.utility.CommonUtil;



public class LoadAdditionalProperties implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	private static final Logger log = LoggerFactory.getLogger("LoadAdditionalProperties");

	private ResourceLoader loader = new DefaultResourceLoader();
	private ConfigurableEnvironment environment;

	/**
	 * onApplicationEvent
	 * 
	 * @param ApplicationEnvironmentPreparedEvent
	 *            event
	 * @return none
	 */
	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

		String fileName = System.getProperty("user.dir");
		environment = event.getEnvironment();

		setCustomProperties();

		printProperties();
	}

	/**
	 * setCustomProperties
	 * 
	 * @param none
	 * @return none
	 */
	private void setCustomProperties() {

		Properties props = new Properties();

		String conf = System.getProperty("user.dir") + "/conf/ankor.properties";
		String log4j = System.getProperty("user.dir") + "/conf/ankor-log4j.properties";
		String lib = System.getProperty("user.dir") + "/libs";

		LocaleMessageFromResource(props, conf);
		LocaleMessageFromResource(props, log4j);

		PropertyConfigurator.configure(log4j);

		environment.getPropertySources().addLast(new PropertiesPropertySource("applicationConfig: [classpath:/application.properties]", props));
		System.setProperty("java.library.path", lib);

		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>> " + CommonUtil.getMethodName() + "_____________________________________________________");
	}

	/**
	 * LocaleMessageFromResource
	 * 
	 * @param Properties
	 *            props
	 * @param String
	 *            conf
	 * @return none
	 */
	private static void LocaleMessageFromResource(Properties props, String conf) {

		FileInputStream fis;

		try {
			log.info(">>> " + CommonUtil.getMethodName() + "Configuration File Loading Success _____________________ [" + conf + "]");
			fis = new FileInputStream(conf);
			props.load(fis);
			fis.close();

		} catch (FileNotFoundException e) {
			log.error(e.getLocalizedMessage());
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
	}

	/**
	 * printProperties
	 * 
	 * @param none
	 * @return none
	 */
	public void printProperties() {
		for (EnumerablePropertySource propertySource : findPropertiesPropertySources()) {
			String[] propertyNames = propertySource.getPropertyNames();
			Arrays.sort(propertyNames);
			for (String propertyName : propertyNames) {
				String resolvedProperty = environment.getProperty(propertyName);
				String sourceProperty = propertySource.getProperty(propertyName).toString();

				if (resolvedProperty.equals(sourceProperty)) {
					// if (propertyName.contains("config") ||
					// propertyName.contains("spring")) {
					System.setProperty(propertyName, resolvedProperty);
					// }
				} else {
					log.info("{}---------------{} OVERRIDDEN to {}", propertyName, sourceProperty, resolvedProperty);
				}
			}
		}
	}

	/**
	 * findPropertiesPropertySources
	 * 
	 * @param none
	 * @return List<EnumerablePropertySource> propertiesPropertySources
	 */
	private List<EnumerablePropertySource> findPropertiesPropertySources() {
		List<EnumerablePropertySource> propertiesPropertySources = new LinkedList<>();
		for (PropertySource<?> propertySource : environment.getPropertySources()) {
			if (propertySource instanceof EnumerablePropertySource) {
				propertiesPropertySources.add((EnumerablePropertySource) propertySource);
			}
		}
		return propertiesPropertySources;
	}
}
