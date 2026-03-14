package com.bishamon.todo.config;

import com.bishamon.todo.entity.User;
import com.bishamon.todo.enumeration.GlobalRole;
import com.bishamon.todo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseSeeder implements CommandLineRunner{
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            User userMocker = User.builder()
                    .email("test123@gmail.com")
                    .passwordHash("123456")
                    .fullName("admin")
                    .globalRole(GlobalRole.ADMIN)
                    .build();
            userRepository.save(userMocker);
        }
    }
}
