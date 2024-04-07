This repository contains my practice and notes from sections 3 and 4 of the Udemy course [Master Spring Boot 3 & Spring Framework 6 with Java](https://www.udemy.com/course/spring-boot-and-spring-framework-tutorial-for-beginners), created by Ranga Karanam, the founder of in28minutes.

# Section 3: Using Spring Framework to Create and Manage Your Java Objects

Getting Spring Framework to Create and Manage Your Java Objects:
* @Component annotation:
    * Spring will automatically create objects (or beans) from classes that have the @Component annotation
    * This is placed above the class declaration of a class you want to be handled by Spring
* @ComponentScan(“”) annotation:
    * Tells Spring what package, and sub-packages, to look in for components
    * This is placed above the class declaration of a class using other classes as parameters. Example for parameter “GamingConsole game”:
        * @Configuration
        * @ComponentScan("com.learn.spring.game")
        * public class GamingAppLauncherApplication {
        * 	@Bean
        * 	public GameRunner gameRunner(GamingConsole game) {
        * 		var gameRunner = new GameRunner(game);
        * 		return gameRunner;
        * 	}
        * 	public static void main(String[] args) {
        * 		try (var context = new AnnotationConfigApplicationContext(App03GamingSpringBeans.class)) {
        * 			context.getBean(GamingConsole.class).up();
        * 			context.getBean(GameRunner.class).run();
        * 		}
        * 	}
        * }
* The above example using the @ComponentScan(“”) annotation can be simplified. We can get Spring to create the “public GameRunner gameRunner(GamingConsole game)” Bean for us as well.
    * We do this by adding the “@Component” annotation to the “GameRunner” class so Spring knows what to do with it. And since the “GameRunner” class is in the “com.learn.spring.game” package we already have the correct “@ComponentScan("com.learn.spring.game”)” annotation from when we added the “@Component” annotation for the “PacMan” class. So now it looks like this:
        * @Configuration
        * @ComponentScan("com.learn.spring.game")
        * public class GamingAppLauncherApplication {
        * 	public static void main(String[] args) {
        * 		try (var context = new AnnotationConfigApplicationContext(App03GamingSpringBeans.class)) {
        * 			context.getBean(GamingConsole.class).up();
        * 			context.getBean(GameRunner.class).run();
        * 		}
        * 	}
        * }

Exploring Primary and Qualifier Annotations for Spring Components:
* @Primary annotation:
    * Spring will throw an exception if there’s multiple of the same interface being used in a component. To remedy this use “@Primary” so Spring knows which one to use.
* @Qualifier annotation:
    * The GameRunner class is using constructor injection for the GamingConsole object. We can use the @Qualifier in the SuperContraGame class and before the GamingConsole parameter in the GameRunner class to ensure it uses the SuperContraGame object. Examples:
        * SuperContraGame class:
            * @Component
            * @Qualifier("SuperContraGameQualifier")
            * public class SuperContraGame implements GamingConsole {
            * 	public void up() {
            * 		System.out.println("up");
            * 	}
            * 	public void down() {
            * 		System.out.println("Sit down");
            * 	}
            * 	public void left() {
            * 		System.out.println("Go back");
            * 	}
            * 	public void right() {
            * 		System.out.println("Shoot");
            * 	}
            * }
        * GameRunner class:
            * @Component
            * public class GameRunner {
            * 	private GamingConsole game;
            * 	public GameRunner(@Qualifier("SuperContraGameQualifier") GamingConsole game) {
            * 		this.game = game;
            * 	}
            * 	public void run() {
            * 		System.out.println("Running game: " + game);
            * 		game.up();
            * 		game.down();
            * 		game.left();
            * 		game.right();
            * 	}
            * }
* @Primary vs @Qualifier annotations; which one to use?:
    * ————————————————————————————————————————————————
    * @Component @Primary
    * class QuickSort implements SortingAlgorithm { }
    * 
    * @Component
    * class BubbleSort implements SortingAlgorithm { }
    * 
    * @Component @Qualifier(“RadixSortQualifier”)
    * class RadixSort implements SortingAlgorithm { }
    * 
    * @Component
    * class ComplexAlgorithm
    *         @Autowired
    *         private SortingAlgorithm algorithm;
    * 
    * @Component
    * class AnotherComplexAlgorithm
    *         @Autowired @Qualifier(“RadixSortQualifier”)
    *         Private SortingAlgorithm iWantToUseRadixSortOnly;
    * ————————————————————————————————————————————————
    * @Primary - A bean should be given preference when multiple candidates are qualified
    * @Qualifier - A specific bean should be auto-wired (name of the bean can be used as qualifier)
    * ALWAYS think from the perspective of the class using the SortingAlgorithm:
        * Using only “@Autowired”: Give me (preferred) SortingAlgorithm
        * Using “@Autowired” + “@Qualifier”: I only want to use specific SortingAlgorithm - RadixSort
        * REMEMBER @Qualifier has higher priority than @Primary

Exploring Spring Framework - Different Types of Dependency Injection:
* Dependency injection provides Inversion of Control (IOC)
* IOC means control is given to Spring rather than the programmer
    * IOC containers are ApplicationContext (used most often) and BeanFactory
* Constructor-based: Dependencies are set by creating the Bean using its Constructor
* Setter-based: Dependencies are set by calling setter methods on your Beans
* Field: No setter or constructor. Dependency is injected using reflection.
* Spring team recommends using Constructor-based injection as dependencies are automatically set when an object is created
* Removing the package name from @ComponentScan will scan in the current package
* @Autowired annotation:
    * Process of wiring in dependencies for a Spring Bean
    * Can use on a constructor, field, setter method, or config method to be automatically detected by Spring's dependency injection facilities
* Field injection example:
    * @Component
    * class YourBusinessClass {
    * 	@Autowired
    * 	Dependency1 dependency1;
    * 
    * 	@Autowired
    * 	Dependency2 dependency2;
    * }
    * 
    * @Component
    * class Dependency1 { }
    * 
    * @Component
    * class Dependency2 { }
* Setter injection example:
    * @Component
    * class YourBusinessClass {
    * 	Dependency1 dependency1;
    * 	Dependency2 dependency2;
    * 	
    * 	@Autowired
    * 	public void setDependency1(Dependency1 dependency1) {
    * 		this.dependency1 = dependency1;
    * 	}
    * 
    * 	@Autowired
    * 	public void setDependency2(Dependency2 dependency2) {
    * 		this.dependency2 = dependency2;
    * 	}
    * }
    * 
    * @Component
    * class Dependency1 { }
    * 
    * @Component
    * class Dependency2 { }
* Constructor injection example:
    * @Component
    * class YourBusinessClass {
    * 	Dependency1 dependency1;
    * 	Dependency2 dependency2;
    * 	
    * 	@Autowired
    * 	public YourBusinessClass(Dependency1 dependency1, Dependency2 dependency2) {
    * 		super();
    * 		this.dependency1 = dependency1;
    * 		this.dependency2 = dependency2;
    * 	}
    * }
    * 
    * @Component
    * class Dependency1 { }
    * 
    * @Component
    * class Dependency2 { }
* NOTE FOR CONSTRUCTOR INJECTION: Even if you don’t use the “@Autowired” annotation, Spring will still use Constructor injection automatically
* Java Spring Framework - Comparing @Component vs @Bean:
Heading	@Component	@Bean
Where?	Can be used on any Java class	Typically used on methods in Spring Configuration classes
Ease of use	Very easy. Just add an annotation.	You write all the code
Autowiring	Yes - Field, Setter, or Constructor Injection	Yes - method call or method parameters
Who creates beans?	Spring Framework	You write bean creation code
Recommended for	Instantiating Beans for Your Own Application Code	Custom Business Logic and Instantiating Beans for 3rd-party libraries. Example: To configure Spring Security to fit your needs, you would need to use @Bean, since you can’t go into Spring Security and add @Component.
		
    * Biggest take-a-way:
        * Use @Component for your own applications, unless you have a lot of business logic
        * Use @Bean if you have a lot of business logic or if you’re instantiating Beans for 3rd-party libraries such as Spring Security
* Why do we have dependencies in Java Spring Applications?:
    * Real world applications have multiple layers
        * Top Layer: Web
        * Middle Layer: Business
        * Bottom Layer: Data
    * Each Layer is dependent on the layer below it!
        * A class in the Web Layer might talk to a class in the Business Layer, and a class in the Business Layer might be talking to a class in the Data Layer
            * This means:
            * A Data Layer class is a dependency of a Business Layer class
            * A Business Layer class is a dependency of a Web Layer class
    * Every application has thousands of dependencies
    * With Spring Framework:
        * Instead of focusing on objects, their dependencies, and wiring
            * You can focus on the business logic of you application!
        * Spring manages lifecycle of objects:
            * Mark components using annotations: @Component (and others..)
            * Mark dependencies using @Autowired
            * Allow Spring to do its magic!

Example using an interface for constructor injection with multiple components:
@Configuration
@ComponentScan("com.learn.spring.service")
public class BusinessCalculationLauncherApplication {
public static void main(String[] args) {
		try (var context = new AnnotationConfigApplicationContext(BusinessCalculationLauncherApplication.class)) {
			Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
			System.out.println(context.getBean(BusinessCalculationService.class).findMax());
		}
	}
}

public interface DataService {
	int[] retrieveData();
}

@Component
public class BusinessCalculationService {
	private DataService dataService;
	public BusinessCalculationService(DataService dataService) {
		super();
		this.dataService = dataService;
	}
	public int findMax() {
		return Arrays.stream(dataService.retrieveData()).max().orElse(0);
	}
}

@Component
@Primary
public class MongoDbDataService implements DataService {
	@Override
	public int[] retrieveData() {
		return new int[] { 11, 22, 33, 44, 55 };
	}
}

@Component
public class MySQLDataService implements DataService {
	@Override
	public int[] retrieveData() {
		return new int[] { 1, 2, 3, 4, 5 };
	}
}


# Section 4: Exploring Spring Framework Advanced Features

Exploring Lazy and Eager Initialization of Spring Framework Beans
* Default initialization for Spring Beans: Eager
* Eager initialization is recommended
    * Errors in the configuration are discovered immediately at application startup
* @Lazy annotation:
    * It’s NOT recommended to use @Lazy annotation unless you have beans that are very rarely used in your app. This is because with Eager initialization all beans are initialized at startup and use more memory.
    * Can be used almost everywhere @Component and @Bean are used
    * Lazy-resolution proxy will be injected instead of actual dependency
    * Can be used on Configuration (@Configuration) class:
        * All @Bean methods within the @Configuration will be lazily initialized
    * The @Bean or @Component will not be initialized until referenced by another bean or explicitly retrieved from the enclosing ApplicationContext or BeanFactory
    * In the example below, the LazyInitializationLauncherApplication class needs to call the ClassB’s method doSomething() “context.getBean(ClassB.class).doSomething()” in order for the ClassB constructor to be executed
@Component
class ClassA { }

@Component
@Lazy
class ClassB {	
	private ClassA classA;
	public ClassB(ClassA classA) {
		System.out.println("Initialization Logic");
		this.classA = classA;
	}
	public void doSomething() {
		System.out.println("Do Something");
	}
}

@Configuration
@ComponentScan
public class LazyInitializationLauncherApplication {
	public static void main(String[] args) {
		try (var context = 
				new AnnotationConfigApplicationContext(
						LazyInitializationLauncherApplication.class)) {
			System.out.println("Initialization of context completed");
			context.getBean(ClassB.class).doSomething();
		}
	}
}

Exploring Java Spring Framework Bean Scopes - Prototype and Singleton
* Spring uses singletons by default
* Prototype:
    * @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE) annotation:
        * Each bean created from a class with this annotation is a new instance of the bean
        * In the example below, the output of the NormalClass bean shows the same Hash and PrototypeClass shows a different Hash
@Component
class NormalClass { }

@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
class PrototypeClass { }

@Configuration
@ComponentScan
public class BeanScopesLauncherApplication {
	public static void main(String[] args) {
		try (var context = 
				new AnnotationConfigApplicationContext(
						BeanScopesLauncherApplication.class)) {
			System.out.println(context.getBean(NormalClass.class));
			System.out.println(context.getBean(NormalClass.class));
			
			System.out.println(context.getBean(PrototypeClass.class));
			System.out.println(context.getBean(PrototypeClass.class));
			System.out.println(context.getBean(PrototypeClass.class));
		}
	}
}

Output:
com.learn.spring.examples.a4.NormalClass@28975c28
com.learn.spring.examples.a4.NormalClass@28975c28
com.learn.spring.examples.a4.PrototypeClass@3943a2be
com.learn.spring.examples.a4.PrototypeClass@343570b7
com.learn.spring.examples.a4.PrototypeClass@157853da

* Spring Bean Scopes:
    * Singleton - One object instance per Spring IoC container
        * Spring default
        * Use singletons when you want the same instance throughout the IoC container
    * Prototype - Possibly many object instances per Spring IoC container
        * Use prototype when you want a new instance each time you ask for a bean
    * Scopes applicable ONLY for web-aware Spring ApplicationContext:
        * Request - One object instance per single HTTP request
        * Session - One object instance per user HTTP Session
        * Application - One object instance per web application runtime
        * Websocket - One object instance per WebSocket instance
* Java Singleton (GOF) vs Spring Singleton
    * Spring Singleton: One object instance per Spring IoC container
    * Java Singleton (GOF): One object instance per Java Virtual Machine (JVM)
    * The main difference is, you can run multiple Spring IoC containers per JVM; although it’s abnormal to do so

Comparing Prototype vs Singleton - Spring Framework Bean Scopes:
Heading	Prototype	Singleton
Instance	Possibly many per Spring IOC container	One per Spring IOC container
Beans	New bean instance created every time the bean is referred to	Same bean instance reused
Default	NOT Default	Default
Code Snippet	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)	@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON) OR it’s used by Default
Usage	Rarely used	Very frequently used
Recommended Scenario	Stateful beans (Example: User information - needs a bean per user)	Stateless beans

Exploring Spring Beans - PostConstruct and PreDestroy:
* Both are part of the jakarta.annotation library
* @PostConstruct annotation:
    * Add above a method that you want to execute after dependency injection is done
    * I think it needs to be in the same class doing the dependency injection to work properly
    * In the example below, “All dependencies are ready!” prints, and then “Some logic using SomeDependency” prints 
@Component
class SomeClass {
	private SomeDependency someDependency;
	public SomeClass(SomeDependency someDependency) {
		super();
		this.someDependency = someDependency;
		System.out.println("All dependencies are ready!");
	}
	@PostConstruct
	public void initialize() {
		someDependency.getReady();
	}	
}

@Component
class SomeDependency {
	public void getReady() {
		System.out.println("Some logic using SomeDependency");
	}	
}

@Configuration
@ComponentScan
public class PrePostAnnotationsContextLauncherApplication {
	public static void main(String[] args) {
		try (var context = 
				new AnnotationConfigApplicationContext(
						PrePostAnnotationsContextLauncherApplication.class)) {
			Arrays.stream(context.getBeanDefinitionNames())
				.forEach(System.out::println);	
		}	
	}
}

* @PreDestroy annotation:
    * Executes before a bean is removed from your context
    * Used to cleanup connections to a database, etc..
    * Releases resources it’s been holding
    * I think it needs to be in the same class doing the dependency injection to work properly
    * Example:
@Component
class SomeClass {	
	private SomeDependency someDependency;
	public SomeClass(SomeDependency someDependency) {
		super();
		this.someDependency = someDependency;
		System.out.println("All dependencies are ready!");
	}
	@PostConstruct
	public void initialize() {
		someDependency.getReady();
	}
	@PreDestroy
	public void cleanup() {
		System.out.println("Cleanup");
	}	
}

Evolution of Jakarta EE - Comparing with J2EE and Java EE:
* Enterprise capabilities were initially built into Java Development Kit (JDK)
* Over time, they were separated out in the following order:
    * J2EE - Java 2 Platform Enterprise Edition
        * Versions 1.2, 1.3, 1.4
    * Java EE - Java Platform Enterprise Edition (Rebranding)
        * Versions 5, 6, 7, 8
    * Jakarta EE (Oracle game Java EE rights to the Eclipse Foundation)
        * Current, has many versions
        * Important Specifications:
            * Jakarta Server Pages (JSP) - Used to create views in Java applications
                * Used to be called Java Server Pages
            * Jakarta Standard Tag Library (JSTL) - Tag libraries used to show dynamic information in web pages
                * Used to be called Java Standard Tag Library
            * Jakarta Enterprise Beans (EJB)
            * Jakarta RESTful Web Services (JAX-RS)
            * Jakarta Bean Validation
            * Jakarta Contexts and Dependency Injection (CDI) - the API for dependency injection
            * Jakarta Persistence (JPA) - deals with how to interact with relational databases
        * Supported by Spring 6 and Spring Boot 3
            * That’s why we use jakarta. packages (instead of javax.)

Exploring Jakarta CDI with Spring Framework and Java:
* Jakarta Contexts and Dependency Injection (CDI):
    * This is the API for dependency injection
    * Spring Framework V1 was released in 2004
    * CDI specification introduced into Java EE 6 platform in December 2009
    * Now called Jakarta Contexts and Dependency Injection (CDI)
    * CDI is a specification (interface)
        * Spring Framework Implements CDI
    * Important Inject API Annotations:
        * Inject (~Autowired in Spring)
        * Named (~Component in Spring)
        * Qualifier
        * Scope
        * Singleton
    * To use CDI, add the dependency to the POM file:
<dependency>
	<groupId>jakarta.inject</groupId>
	<artifactId>jakarta.inject-api</artifactId>
	<version>2.0.1</version>
</dependency>

    * The following annotation are interchangeable:
        * @Component = @Named
            * @Component comes from “org.springframework.stereotype.Component”
            * @Named comes from “jakarta.inject.Named”
        * @Autowired = @Inject
            * @Autowired comes from “org.springframework.beans.factory.annotation.Autowired”
            * @Inject comes from “jakarta.inject.Inject”

Exploring Java Spring XML Configuration:
* XML configuration is not used often these days. Newer projects typically use Java annotations instead.
* Up to this point we’ve been only using Java configuration, in this section we’re going to learn how to use XML configuration
    * The “@Configuration” annotation we’ve been using indicates that we’ve been using Java configuration
* You can create an XML file in your “src/main/resources” folder that holds all your Spring configuration (Beans, etc.).
    * Copy and paste “the Context Schema” at this link to use context tags that deal with ApplicationContext configuration that relates to plumbing - that is, not usually beans that are important to an end-user but rather beans that do a lot of grunt work in Spring
    * I fixed the cannot download external resources error in Eclipse by doing this: 
        * Go to Eclipse > Settings > xml (wild web developer) > select “Download external resources like referenced DTD, XSD”
* The code used in java for using the XML configuration is “new ClassPathXmlApplicationContext("contextConfiguration.xml")”
    * The old code was “new AnnotationConfigApplicationContext(XmlConfigurationContextLauncherApplication.class))”, which is used to launch a Java configuration
* The example xml configuration below defines two Beans and does a component scan on the “com.learn.spring.game” package:
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns=…> 
 	<bean id="name" class="java.lang.String">
 		<constructor-arg value="Caden"/>
 	</bean>
 	<bean id="age" class="java.lang.Integer">
 		<constructor-arg value="36"/>
 	</bean>
 	<context:component-scan base-package="com.learn.spring.game"/>
</beans>

* The component scan will scan a package and add all the beans in that package to the ApplicationContext. Here’s an example of the Java and Output:
@Configuration
@ComponentScan
public class XmlConfigurationContextLauncherApplication {
	public static void main(String[] args) {
		try (var context = new ClassPathXmlApplicationContext("contextConfiguration.xml")) {
			Arrays.stream(context.getBeanDefinitionNames())
				.forEach(System.out::println);
			System.out.println(context.getBean("name"));
			System.out.println(context.getBean("age"));	
		}	
	}
}

Output:
gameRunner
gamingAppLauncherApplication
marioGame
pacManGame
superContraGame
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
Caden
36

* You can also create individual beans of any java class. In the example below, we create a bean named “game” from our “PacMan” class. We then use it in our constructor injection into the next bean we created called “gameRunner” of the “com.learn.spring.game.GameRunner” class.
<bean id="game" class="com.learn.spring.game.PacManGame"/>
<bean id="gameRunner" class="com.learn.spring.game.GameRunner">
 	<constructor-arg ref="game"/>
</bean>

* We can now access the gameRunner and call methods on it, example:
context.getBean(GameRunner.class).run();

Explore Java Annotations vs XML Configuration - Java Spring Framework:
* Annotations vs XML Configuration

Heading	Annotations	XML Configuration
Ease of Use	Very Easy (defined close to source - class, method, and/or variable)	Cumbersome
Short and Concise	Yes	No
Clean POJOs	No. POJOs are polluted with Spring Annotations	Yes. No change in Java code.
Easy to Maintain	Yes	No
Usage Frequency	Almost all recent projects	Rarely
Debugging Difficulty	Hard (Need a good understanding of Spring framework)	Medium (It’s verbose. There’s a lot defined in the xml file, and therefor easier to track down where issues are coming from.)
* The recommendation is either of them, but be consistent. Don’t mix both. 
* Annotations are used more often now days

Exploring Spring Framework Stereotype Annotations - Component and more:
* @Component - Generic annotation applicable for any class
    * Base for all Spring Stereotype Annotations
    * Specializations of @Component (these can be used in place of @Component):
        * @Service - Indicates that an annotated class has business logic
        * @Controller - Indicates that an annotated class is a “Controller” (e.g. a web controller)
            * Used to define controllers in your web applications and REST API
        * @Repository - Indicates that an annotated class is used to retrieve and/or manipulate data in a database
* (Instructors Recommendation) Use the most specific annotation possible
    * @Service for business logic
    * @Controller for Controllers
    * @Repository for manipulating data in a database
    * @Component for everything else
    * Why?
        * By using a specific annotation, you are giving more information to the framework about your intentions
        * You can use Aspect Oriented Programming (AOP) at a later point to add additional behavior
            * Example: For @Repository, Spring automatically wires in JDBC Exception translation features

Exploring Spring Big Picture - Framework, Modules and Projects:
* So far we’ve been learning about Spring Core
* Spring Core: IOC Container, Dependency Injection, Auto Wiring, ..
    * These are fundamental building blocks to:
        * Building web applications
        * Creating REST APIs
        * Implementing authentication and authorization
        * Talking to a database
        * Integrating with other systems
        * Writing great unit tests
* Let’s now get a Spring Big Picture:
    * Spring Framework
    * Spring Modules
    * Spring Projects
* Spring Framework and Spring Modules:
    * Spring Framework contains multiple Spring Modules
    * Fundamental Features: Core (IOC Container, Dependency Injection, Auto Wiring, ..)
    * Web: Spring MVC, etc (Web applications, REST API)
    * Web Reactive: Spring WebFlux, etc
    * Data Access: JDBC, JPA, etc
    * Integration: JMS, etc
    * Testing: Mock Obejcts, Spring MVC Test, etc
    * Why is Spring Framework decided into modules?:
        * Offers flexibility
        * Different applications have different needs
        * Each application can choose modules they want to make use of
        * They do not need to make use of everything in Spring framework!
* Spring Projects:
    * Application architectures evolve continuously 
        * Web > REST API > Microservices > Cloud > …
    * Spring remains one of the most popular frameworks today because Spring evolves through the use of Spring Projects
        * First Project: Spring Framework
        * Spring Security: Secure your web application, REST API, or microservice
        * Spring Data: Integrate the same way with different types of databases (NoSQL and Relational)
        * Spring Integration: Address challenges with integration with other applications
        * Spring Boot: Popular framework to build microservices
            * Today this is the number one framework for microservices
        * Spring Cloud: Build cloud native applications
* Hierarchy: Spring Projects > Spring Framework > Spring Modules
* Why is Spring Eco system popular?
    * Loose Coupling: Spring manages creation and wiring of beans and dependencies
        * Makes it easy to build loosely coupled applications
        * Makes writing unit tests easy (Spring Unit Testing)
    * Reduced Boilerplate Code: Focus on Business Logic
        * Example: No need for exception handling in each method
            * All Checked Exceptions are converted to Runtime or Unchecked Exceptions
    * Architectural Flexibility: Spring Module and Projects
        * You can pick and choose which ones to use (You DON’T need to use all of them)
    * Evolution with Time: Microservices and Cloud
        * Spring Boot, Spring Cloud, etc


