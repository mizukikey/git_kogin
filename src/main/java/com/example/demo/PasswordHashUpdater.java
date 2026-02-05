package com.example.demo;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Entity_customer;
import com.example.demo.model.Entity_manager;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ManagerRepository;

//@Component
//public class PasswordHashUpdater implements CommandLineRunner {
//
//    private final CustomerRepository customerRepository;
//    private final CustomerRepository managerRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public PasswordHashUpdater(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
//        this.customerRepository = customerRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        List<Entity_customer> all = customerRepository.findAll();
//        for (Entity_customer c : all) {
//            String raw = c.getPassword();
//            // すでにハッシュ化されている場合はスキップ
//            if (!raw.startsWith("$2a$")) { // BCrypt のハッシュは $2a$ で始まる
//                c.setPassword(passwordEncoder.encode(raw));
//                customerRepository.save(c);
//            }
//        }
//        System.out.println("既存ユーザーのパスワードをハッシュ化しました");
//    }
//    
//    @Override
//    public void run1(String... args) throws Exception {
//        List<Entity_manager> all = managerRepository.findAll();
//        for (Entity_manager c : all) {
//            String raw = c.getPassword();
//            // すでにハッシュ化されている場合はスキップ
//            if (!raw.startsWith("$2a$")) { // BCrypt のハッシュは $2a$ で始まる
//                c.setPassword(passwordEncoder.encode(raw));
//                managerRepository.save(c);
//            }
//        }
//        System.out.println("既存ユーザーのパスワードをハッシュ化しました");
//    }

@Component
public class PasswordHashUpdater implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordHashUpdater(CustomerRepository customerRepository,
                               ManagerRepository managerRepository,
                               PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Customer
        List<Entity_customer> allCustomers = customerRepository.findAll();
        for (Entity_customer c : allCustomers) {
            String raw = c.getPassword();
            if (!raw.startsWith("$2a$")) {
                c.setPassword(passwordEncoder.encode(raw));
                customerRepository.save(c);
            }
        }
        System.out.println("既存カスタマーのパスワードをハッシュ化しました");

        // Manager
        List<Entity_manager> allManagers = managerRepository.findAll();
        for (Entity_manager m : allManagers) {
            String raw = m.getPassword();
            if (!raw.startsWith("$2a$")) {
                m.setPassword(passwordEncoder.encode(raw));
                managerRepository.save(m);
            }
        }
        System.out.println("既存マネージャーのパスワードをハッシュ化しました");
    }
}

