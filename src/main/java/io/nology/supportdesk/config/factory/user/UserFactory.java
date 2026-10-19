package io.nology.supportdesk.config.factory.user;

import com.github.javafaker.Faker;
import io.nology.supportdesk.ticket.entity.Category;
import io.nology.supportdesk.user.UserRepository;
import io.nology.supportdesk.user.entity.User;
import io.nology.supportdesk.user.entity.UserRole;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Profile({"dev","test"})
public class UserFactory {
    private final Set<String> usedEmails = new HashSet<>();
    private final Faker faker = new Faker();
    private final UserRepository repo;

    public UserFactory(UserRepository repo) {
        this.repo = repo;
    }

    public boolean repoEmpty() {
        return this.repo.count() == 0;
    }

    public User create(UserFactoryOptions options){
        User newUser = new User();
        newUser.setName(options.name != null ? options.name : faker.name().firstName() + " " + faker.name().lastName());
        newUser.setEmail(generateUniqueEmail(options.email));
        newUser.setPasswordHash(options.passwordHash != null ? options.passwordHash : faker.internet().password());
        newUser.setRole(options.role != null ? options.role : randomEnum(UserRole.class));
        this.repo.saveAndFlush(newUser);
        return newUser;
    }

    public User create(){
        UserFactoryOptions empty = UserFactoryOptions.builder().build();
        return create(empty);
    }

    public List<User> create(UserFactoryOptions options, int n){
        ArrayList<User> users = new ArrayList<>();
        for(int i = 0; i < n; i++){
            User created = create(options);
            users.add(created);
        }
        return users;
    }

    private String generateUniqueEmail(String email) {
        if (email == null || email.isBlank()) {
            email = faker.internet().emailAddress();
        }
        while (usedEmails.contains(email)) {
            email = faker.internet().emailAddress();
        }
        return email;
    }

    private <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
    public List<User> getAll(){
        return this.repo.findAll();
    }

}
