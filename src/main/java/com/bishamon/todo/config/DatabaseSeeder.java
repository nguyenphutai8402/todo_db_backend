package com.bishamon.todo.config;

import com.bishamon.todo.entity.User;
import com.bishamon.todo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner{
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            User userMocker = User.builder()
                    .email("test123@gmail.com")
                    .passwordHash("123456")
                    .fullName("admin")
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(userMocker);
        }
    }
}
