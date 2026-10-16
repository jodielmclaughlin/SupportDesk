package io.nology.supportdesk.ticket.dtos;

import io.nology.supportdesk.ticket.entity.Category;
import io.nology.supportdesk.ticket.entity.Priority;
import io.nology.supportdesk.ticket.entity.Status;
import jakarta.validation.constraints.Min;
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

    @Min(value = 1)
    private Long createdBy;

    @Min(value = 1)
    private Long assignedTo;


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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
