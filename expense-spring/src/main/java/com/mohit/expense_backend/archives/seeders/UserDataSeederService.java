package com.mohit.expense_backend.archives.seeders;

import com.mohit.expense_backend.Entities.Category;
import com.mohit.expense_backend.Entities.Enums.ApprovalStatus;
import com.mohit.expense_backend.Entities.Expense;
import com.mohit.expense_backend.Entities.Income;
import com.mohit.expense_backend.Entities.User;
import com.mohit.expense_backend.Repositories.CategoryRepository;
import com.mohit.expense_backend.Repositories.ExpenseRepository;
import com.mohit.expense_backend.Repositories.IncomeRepository;
import com.mohit.expense_backend.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@Component
public class UserDataSeederService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    private Random random = new Random();

    private BCryptPasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private final String hashedPassword;

    private static final String[] DOMAINS = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "outlook.com", "icloud.com", "yandex.com"};
    private static final String[] FIRST_NAMES = {"Aarav", "Satya","Mohit","Rachel","Daniel","Ashley","Léa","Lucas","Antoine","Chloe"};
    private static final String[] LAST_NAMES = {"Sharma","Patel","Reddy","Smith","Johnson","Brown","Dubois","Moreau","Lefevre","Garcia"};
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private String[] globalCategories = {"Transport", "Shopping", "Bills", "Entertainment", "Travel", "Education", "Health", "Fuel", "Other" };

    private String[] localCategories = {

            "Gym",
            "Coffee",
            "Gaming",
            "Snacks",
            "Pets",
            "Books",
            "Bike",
            "Family",
            "Office",
            "Movies"

    };

    private String[] incomeSources = {

            "Salary",
            "Freelance",
            "Bonus",
            "Stocks",
            "Rental",
            "Gift",
            "Interest",
            "Refund",
            "Business",
            "Cash"

    };

    private String[] expenseTitles = {

            "Lunch",
            "Dinner",
            "Groceries",
            "Movie",
            "Uber",
            "Electricity",
            "Petrol",
            "Medicine",
            "Laptop",
            "Shopping"

    };

    private String[] rejectionReasons = {

            "Receipt Missing",
            "Duplicate Entry",
            "Policy Violation",
            "Wrong Amount",
            "Invalid Category"

    };

    public UserDataSeederService(UserRepository userRepository,
                                 BCryptPasswordEncoder passwordEncoder,
                                 JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
        this.hashedPassword = passwordEncoder.encode("123");
    }

    public String addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User Added";
    }

    public void seedCategories(int count) {
        List<User> users = userRepository.findAll();
        // ---------- GLOBAL ----------
        for (int i = 0; i < globalCategories.length; i++) {
            if (categoryRepository.findByName(globalCategories[i]).isEmpty()) {
                Category c = new Category();
                c.setName(globalCategories[i]);
                c.setGlobalCategory(true);
                c.setUser(null);
                categoryRepository.save(c);
                System.out.println("Category Number - " + i + " - Added: ");
            }
        }
        // ---------- LOCAL ----------
        for (User user : users) {
            for (int i = 0; i < count; i++) {
                String categoryName = localCategories[random.nextInt(localCategories.length)];
                String fullName = user.getFirstName() + "_" + categoryName;
                if (categoryRepository.findByName(fullName).isEmpty()) {
                    Category c = new Category();
                    c.setGlobalCategory(false);
                    c.setUser(user);
                    c.setName(fullName);
                    categoryRepository.save(c);
                    System.out.println("Category Number - " + i + " - Added: " + fullName);
                }
            }
        }
        System.out.println("Categories Added");
    }

    public void seedIncome(int count){
        List<User> users = userRepository.findAll();
        for(int i=0;i<count;i++){
            Income income = new Income();
            User user = users.get(random.nextInt(users.size()));
            income.setUser(user);
            income.setAmount(10.0 + random.nextInt(991));
            income.setDate(randomDate());
            income.setSource(incomeSources[random.nextInt(incomeSources.length)]);
            int status = random.nextInt(3);
            if(status==0) income.setStatus(ApprovalStatus.PENDING);
            if(status==1) income.setStatus(ApprovalStatus.APPROVED);
            if(status==2){
                income.setStatus(ApprovalStatus.REJECTED);
                if(user.getRoles().contains("ADMIN")){
                    income.setRejectionReason(
                            rejectionReasons[random.nextInt(rejectionReasons.length)]
                    );
                }
            }
            incomeRepository.save(income);
        }
        System.out.println("Income Added");
    }

    public void seedExpenses(int count){
        List<User> users = userRepository.findAll();
        for(int i=0; i<count; i++){
            Expense expense = new Expense();
            User user = users.get(random.nextInt(users.size()));
            expense.setUser(user);
            expense.setAmount(10.0 + random.nextInt(991));
            expense.setDate(randomDate());
            expense.setTitle(expenseTitles[random.nextInt(expenseTitles.length)]);
            // Get only categories available to this user
            List<Category> categories = categoryRepository.getAvailableCategories(user.getId());
            Category category = categories.get(random.nextInt(categories.size()));
            expense.setCategory(category);
            int status = random.nextInt(3);
            if(status==0) expense.setStatus(ApprovalStatus.PENDING);
            if(status==1) expense.setStatus(ApprovalStatus.APPROVED);
            if(status==2){
                expense.setStatus(ApprovalStatus.REJECTED);
                if(user.getRoles().contains("ADMIN")){
                    expense.setRejectionReason(rejectionReasons[random.nextInt(rejectionReasons.length)]);
                }
            }
            expenseRepository.save(expense);
        }
        System.out.println("Expenses Added");
    }


    //    -- THREADS WITH COUNT VARIABLE --
    public void addMassUserUsingThreading(int totalCount, int threadCount, int batchSize) throws InterruptedException {
        long lastId = userRepository.findMaxId().orElse(0L);
        // Calculate how many users each thread should create
        int usersPerThread = totalCount / threadCount;
        int remainder = totalCount % threadCount; // extra users for last thread
        Thread[] threads = new Thread[threadCount];
        long startId = lastId + 1;
        for (int t = 0; t < threadCount; t++) {
            int threadNumber = t + 1;
            int countForThisThread = usersPerThread + (t == threadCount - 1 ? remainder : 0);
            long threadStartId = startId;
            startId += countForThisThread;
            threads[t] = new Thread(() -> {
                System.out.println("Thread " + threadNumber + " started");
                long id = threadStartId;
                int usersAdded = 0;
                while (usersAdded < countForThisThread) {
                    List<User> users = new ArrayList<>();
                    for (int i = 0; i < batchSize && usersAdded < countForThisThread; i++) {
                        users.add(createRandomUser(id));
                        id++;
                        usersAdded++;
                    }
                    System.out.println("Thread " + threadNumber + " saving " + users.size() +
                            " users (ID " + users.get(0).getId() + " to " +
                            users.get(users.size()-1).getId() + ")");
                    userRepository.saveAll(users);
                }
                System.out.println("Thread " + threadNumber + " finished");
            });
            threads[t].start();
        }
        // wait for all threads to finish
        for (Thread thread : threads) thread.join();
        System.out.println("All threads finished. Total users added: " + totalCount);
    }

    public User createRandomUser(long id){
        User tempUserObject = new User();
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        tempUserObject.setFirstName(firstName);
        tempUserObject.setLastName(lastName);

        String email = generateEmail(firstName, lastName, id);
        tempUserObject.setEmail(email);

//        // Optional: generate random age
//        tempUserObject.setAge(generateRandomAge());

        tempUserObject.setDateOfBirth(generateRandomDateOfBirth());

        tempUserObject.setPassword(hashedPassword);

        String[] roles = {"USER", "ADMIN"};
        tempUserObject.setRoles(roles[random.nextInt(roles.length)]);

        return tempUserObject;
    }

    private LocalDate randomDate(){
        int year = 2022 + random.nextInt(5);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28);
        return LocalDate.of(year,month,day);
    }

    private LocalDate generateRandomDateOfBirth() {
        int minAge = 10;
        int maxAge = 95;
        int randomAge = minAge + (random.nextInt(maxAge - minAge + 1));
        LocalDate baseDate = LocalDate.now().minusYears(randomAge);
        int randomDay = random.nextInt(365);
        return baseDate.minusDays(randomDay);
    }

    private int generateRandomAge() {
        int minAge = 10;
        int maxAge = 95;
        return minAge + random.nextInt(maxAge - minAge + 1);
    }

    //to create TRULY random names
//    private String generateRandomName(int minLen, int maxLen) {
//        int len = minLen + random.nextInt(maxLen - minLen + 1);
//        StringBuilder sb = new StringBuilder();
//        sb.append(Character.toUpperCase(ALPHABET.charAt(random.nextInt(ALPHABET.length()))));
//        for (int i = 1; i < len; i++) {
//            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
//        }
//        return sb.toString();
//    }

    private String generateEmail(String firstName, String lastName, long uniqueId) {
        String localPart = firstName.toLowerCase() + "." + lastName.toLowerCase() + uniqueId;
        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        return localPart + "@" + domain;
    }


}