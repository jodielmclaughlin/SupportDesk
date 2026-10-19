package io.nology.supportdesk.config.factory.ticket;

import com.github.javafaker.Faker;
import io.nology.supportdesk.config.factory.user.UserFactory;
import io.nology.supportdesk.ticket.TicketRepository;
import io.nology.supportdesk.ticket.entity.Category;
import io.nology.supportdesk.ticket.entity.Priority;
import io.nology.supportdesk.ticket.entity.Status;
import io.nology.supportdesk.ticket.entity.Ticket;
import io.nology.supportdesk.user.entity.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Profile({"dev","test"})
public class TicketFactory {
    private final TicketRepository repo;
    private final UserFactory userFactory;

    public TicketFactory(TicketRepository repo, UserFactory userFactory) {
        this.repo = repo;
        this.userFactory = userFactory;
    }

    public boolean repoEmpty() {
        return this.repo.count() == 0;
    }

    public Ticket create(){
        User createdBy = this.userFactory.create();
        User assignedTo = this.userFactory.create();
        var opts = TicketFactoryOptions.builder().createdBy(createdBy).assignedTo(assignedTo).build();
        return create(opts);
    }

    public Ticket create(TicketFactoryOptions options){
        if(options.createdBy == null){
            throw new IllegalStateException("Options must have a created by user");
        }
        if(options.assignedTo == null){
            throw new IllegalStateException("Options must have a assigned to user");
        }

        String[] titles = {
                "Unable to connect to VPN",
                "Password reset required",
                "Laptop running slowly",
                "Access denied to shared folder",
                "Email application not working",
                "Software installation request",
                "Monitor displaying a blank screen",
                "Printer not responding"
        };

        Ticket createdTicket = new Ticket();
        createdTicket.setCreatedBy(options.createdBy);
        createdTicket.setAssignedTo(options.assignedTo);
        createdTicket.setTitle(options.title !=null ? options.title : titles[ThreadLocalRandom.current().nextInt(titles.length)]);
        createdTicket.setDescription(options.description !=null ? options.description : titles[ThreadLocalRandom.current().nextInt(titles.length)]);
        createdTicket.setCategory(options.category != null ? options.category : randomEnum(Category.class));
        createdTicket.setPriority(options.priority != null ? options.priority : randomEnum(Priority.class));
        createdTicket.setStatus(options.status != null ? options.status : Status.OPEN);
        this.repo.saveAndFlush(createdTicket);

        return createdTicket;
    }

    public List<Ticket> create(TicketFactoryOptions options, int n){
        ArrayList<Ticket> tickets = new ArrayList<>();

        for (int i = 0; i < n; i++){
            Ticket created = create(options);
            tickets.add(created);
        }
        return tickets;
    }
    private <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
