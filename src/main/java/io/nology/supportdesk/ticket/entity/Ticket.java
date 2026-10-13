package io.nology.supportdesk.ticket.entity;

import io.nology.supportdesk.common.BaseEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity {

    @Column (nullable = false)
    private String title;

    @Column (nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column
    private  Category category;

    @Enumerated(EnumType.STRING)
    @Column
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Column (nullable = false)
    private String createdBy;

    @Column (nullable = false)
    private String assignedTo;

    @Column
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
