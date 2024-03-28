package com.learn.spring.examples.a2;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.learn.spring.examples.a2.service.BusinessCalculationService;

@Configuration
@ComponentScan("com.learn.spring.examples.a2.service")
public class BusinessCalculationLauncherApplication {

	public static void main(String[] args) {
		
		try (var context = new AnnotationConfigApplicationContext(BusinessCalculationLauncherApplication.class)) {
			
			Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
			
			System.out.println(context.getBean(BusinessCalculationService.class).findMax());
			
		}
		
	}

}
