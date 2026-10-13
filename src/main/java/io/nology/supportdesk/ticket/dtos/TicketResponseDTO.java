package io.nology.supportdesk.ticket.dtos;

import io.nology.supportdesk.ticket.entity.Category;
import io.nology.supportdesk.ticket.entity.Priority;
import io.nology.supportdesk.ticket.entity.Status;
import io.nology.supportdesk.ticket.entity.Ticket;

import java.util.Date;
import java.util.List;

public record TicketResponseDTO(Long id, String title, String description, Category category, Priority priority,
                                Status status, String createdBy, String assignedTo, Date createdAt, Date updatedAt, Date resolvedAt) {
    public static TicketResponseDTO fromEntity(Ticket ticket){
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedBy(),
                ticket.getAssignedTo(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getResolvedAt());
    }

    public static List<TicketResponseDTO> fromEntity(List<Ticket> ticketList){
        return ticketList.stream().map(TicketResponseDTO::fromEntity).toList();
    }
}
