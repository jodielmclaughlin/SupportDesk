package io.nology.supportdesk.ticket;

import io.nology.supportdesk.common.exceptions.NotFoundException;
import io.nology.supportdesk.ticket.dtos.CreateTicketDTO;
import io.nology.supportdesk.ticket.dtos.TicketResponseDTO;
import io.nology.supportdesk.ticket.dtos.UpdateTicketDTO;
import io.nology.supportdesk.ticket.entity.Ticket;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private static final Logger log = LogManager.getLogger(TicketController.class);
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(){
        List<Ticket> all = this.ticketService.getAllTickets();
        return ResponseEntity.ok(TicketResponseDTO.fromEntity(all));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getByID(@PathVariable Long id){
        log.info("Getting ticket with ID: " + id);
        Ticket foundTicket = this.ticketService.getTicketById(id).orElseThrow(() -> new NotFoundException("Ticket not found with ID: " + id));
        return ResponseEntity.ok(foundTicket);
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody @Valid CreateTicketDTO data){
        Ticket created = this.ticketService.createTicket(data);
        return new ResponseEntity<>(TicketResponseDTO.fromEntity(created), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateTicketDTO data){
        Ticket updated = this.ticketService.updateTicket(id, data).orElseThrow(() -> new NotFoundException("Ticket not found with ID: " + id));

        return ResponseEntity.ok(TicketResponseDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean successfullyDeleted = this.ticketService.deleteTicket(id);

        if(!successfullyDeleted){
            throw new NotFoundException("Ticket not found with ID: " + id);
        }

        return ResponseEntity.noContent().build();
    }
}
