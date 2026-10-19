package io.nology.supportdesk.config.factory.ticket;

import io.nology.supportdesk.ticket.entity.Category;
import io.nology.supportdesk.ticket.entity.Priority;
import io.nology.supportdesk.ticket.entity.Status;
import io.nology.supportdesk.user.entity.User;

public class TicketFactoryOptions {
    String title;
    String description;
    Category category;
    Priority priority;
    Status status;
    User createdBy;
    User assignedTo;

    private TicketFactoryOptions(Builder builder){
        this.title = builder.title;
        this.description = builder.description;
        this.category = builder.category;
        this.priority = builder.priority;
        this.status = builder.status;
        this.createdBy = builder.createdBy;
        this.assignedTo = builder.assignedTo;

    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private String description;
        private Category category;
        private Priority priority;
        private Status status;
        private User createdBy;
        private User assignedTo;



        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Builder category(Category category) {
            this.category = category;
            return this;
        }
        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }
        public Builder status(Status status) {
            this.status = status;
            return this;
        }
        public Builder createdBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }
        public Builder assignedTo(User assignedTo) {
            this.assignedTo = assignedTo;
            return this;
        }

        public TicketFactoryOptions build(){
            return new TicketFactoryOptions(this);
        }
    }
}
