package com.learn.spring.examples.a1;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
class YourBusinessClass {
	
	Dependency1 dependency1;
	Dependency2 dependency2;
	
	public YourBusinessClass(Dependency1 dependency1, Dependency2 dependency2) {
		super();
		this.dependency1 = dependency1;
		this.dependency2 = dependency2;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Using ");
		sb.append(dependency1);
		sb.append(" and ");
		sb.append(dependency2);
		return sb.toString();
	}
}

@Component
class Dependency1 {
	
}

@Component
class Dependency2 {
	
}


@Configuration
@ComponentScan
public class DepInjectionLauncherApplication {

	public static void main(String[] args) {
		
		try (var context = 
				new AnnotationConfigApplicationContext(
						DepInjectionLauncherApplication.class)) {
			
			Arrays.stream(context.getBeanDefinitionNames())
				.forEach(System.out::println);
			
			System.out.println(context.getBean(YourBusinessClass.class));
			
		}
		
	}

}
