package com.vitalioleksenko.csp.repositories;


import com.vitalioleksenko.csp.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void UserRepository_Save(){
        User user1 = User.builder().userId(1).email("test1@test.com").name("test1").passwordHash("hash").build();
        User user2 = User.builder().userId(2).email("test2@test.com").name("test1").passwordHash("hash").build();

        usersRepository.save(user1);
        usersRepository.save(user2);

        List<User> list = usersRepository.findAll();

        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}
