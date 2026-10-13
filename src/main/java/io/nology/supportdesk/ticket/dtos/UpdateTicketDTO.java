package io.nology.supportdesk.ticket.dtos;

import io.nology.supportdesk.ticket.entity.Category;
import io.nology.supportdesk.ticket.entity.Priority;
import io.nology.supportdesk.ticket.entity.Status;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

public class UpdateTicketDTO {
    @Pattern(regexp = ".*\\S.*", message = "Title must not be empty")
    private String title;

    @Pattern(regexp = ".*\\S.*", message = "Description must not be empty")
    private String description;


    private Category category;


    private Priority priority;


    private Status status;

    @Pattern(regexp = ".*\\S.*", message = "Created by field must not be empty")
    private String createdBy;

    @Pattern(regexp = ".*\\S.*", message = "Assigned to field must not be empty")
    private String assignedTo;


    private Date resolvedAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
