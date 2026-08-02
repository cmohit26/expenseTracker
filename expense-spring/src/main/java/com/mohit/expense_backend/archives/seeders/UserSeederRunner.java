package com.mohit.expense_backend.archives.seeders;


import com.mohit.expense_backend.ExpenseBackendApplication;
import com.mohit.expense_backend.archives.seeders.UserDataSeederService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class UserSeederRunner {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(ExpenseBackendApplication.class, args);

        UserDataSeederService seeder = context.getBean(UserDataSeederService.class);

        int numberOfUsers = 1;
        int threadCount = 10;
        int batchSize = 50;

        long startTime = System.nanoTime();

//        seeder.addMassUserUsingThreading(numberOfUsers, threadCount, batchSize);
        seeder.seedCategories(10);
//        seeder.seedIncome(100);
//        seeder.seedExpenses(100);

        long endTime = System.nanoTime();
        double durationSeconds = (endTime - startTime) / 1_000_000_000.0;

        System.out.println("Execution time: " + durationSeconds + " seconds");

        // VERY IMPORTANT → shut down app after work
        SpringApplication.exit(context);
    }
}
