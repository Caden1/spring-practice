package com.learn.spring.examples.a7;

import java.util.Arrays;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.learn.spring.game.GameRunner;

@Configuration
@ComponentScan
public class XmlConfigurationContextLauncherApplication {

	public static void main(String[] args) {
		
		try (var context = new ClassPathXmlApplicationContext("contextConfiguration.xml")) {
			
			Arrays.stream(context.getBeanDefinitionNames())
				.forEach(System.out::println);
			
			System.out.println(context.getBean("name"));
			
			System.out.println(context.getBean("age"));
			
			context.getBean(GameRunner.class).run();
			
		}
		
	}

}
