package io.nology.supportdesk.config;

import io.nology.supportdesk.config.factory.ticket.TicketFactory;
import io.nology.supportdesk.config.factory.ticket.TicketFactoryOptions;
import io.nology.supportdesk.config.factory.user.UserFactory;
import io.nology.supportdesk.config.factory.user.UserFactoryOptions;

import io.nology.supportdesk.user.UserRepository;
import io.nology.supportdesk.user.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"dev", "test"})
public class DataSeeder implements CommandLineRunner {
    private final TicketFactory ticketFactory;
    private final UserFactory userFactory;
    private final UserRepository userRepository;

    public DataSeeder(TicketFactory ticketFactory, UserFactory userFactory, UserRepository userRepository) {
        this.ticketFactory = ticketFactory;
        this.userFactory = userFactory;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userFactory.repoEmpty()) {
            UserFactoryOptions options = UserFactoryOptions.builder().build();
            userFactory.create(options, 10);
        }

        List<User> users = userRepository.findAll();

        if (ticketFactory.repoEmpty() && !users.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                User creator = users.get(i % users.size());
                User assignedTo = users.get((i + 1) % users.size());

                TicketFactoryOptions options = TicketFactoryOptions.builder()
                        .createdBy(creator)
                        .assignedTo(assignedTo)
                        .build();

                ticketFactory.create(options, 1);
            }
        }
    }
}
