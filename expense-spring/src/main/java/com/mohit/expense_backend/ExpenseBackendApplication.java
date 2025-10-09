package com.mohit.expense_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ExpenseBackendApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ExpenseBackendApplication.class, args);

//		System.out.println("Beans created by Spring Boot:");
//		String[] beanNames = context.getBeanDefinitionNames();
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}

	}

}
