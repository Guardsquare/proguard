package com.example.demo;

import com.example.demo.sub.TestComponent2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.example.demo");
		context.refresh();

		TestComponent ms   = context.getBean(TestComponent.class);
		TestComponent2 ms2 = context.getBean(TestComponent2.class);

		System.out.println("The answer is " + ms.getAnswer() + " " + ms2.getMessage());

		context.close();
	}

}
