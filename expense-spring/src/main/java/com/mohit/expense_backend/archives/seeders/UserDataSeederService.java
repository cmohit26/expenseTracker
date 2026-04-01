package com.mohit.expense_backend.archives.seeders;

import com.mohit.expense_backend.entities.User;
import com.mohit.expense_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class UserDataSeederService {

    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private final String hashedPassword;

    private static final String[] DOMAINS = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "outlook.com", "icloud.com", "yandex.com"};
    private static final String[] FIRST_NAMES = {"Aarav", "Satya","Mohit","Rachel","Daniel","Ashley","Léa","Lucas","Antoine","Chloe"};
    private static final String[] LAST_NAMES = {"Sharma","Patel","Reddy","Smith","Johnson","Brown","Dubois","Moreau","Lefevre","Garcia"};
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private Random random = new Random();

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

        // Optional: generate random age
        tempUserObject.setAge(generateRandomAge());

        tempUserObject.setPassword(hashedPassword);

        String[] roles = {"USER", "ADMIN"};
        tempUserObject.setRoles(roles[random.nextInt(roles.length)]);

        return tempUserObject;
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