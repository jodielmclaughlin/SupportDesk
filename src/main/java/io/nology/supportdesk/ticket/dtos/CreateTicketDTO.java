package io.nology.supportdesk.ticket.dtos;

import io.nology.supportdesk.ticket.entity.Category;
import io.nology.supportdesk.ticket.entity.Priority;
import io.nology.supportdesk.ticket.entity.Status;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class CreateTicketDTO {

    @NotBlank(message = "Title must not be empty")
    private String title;

    @NotBlank(message = "Description must not be empty")
    private String description;


    private Category category;


    private Priority priority;


    private Status status;

    @NotBlank(message = "First name must not be empty")
    private String createdBy;

    @NotBlank(message = "First name must not be empty")
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
