package io.nology.supportdesk.ticket;

import io.nology.supportdesk.common.exceptions.UnprocessableContentException;
import io.nology.supportdesk.ticket.dtos.CreateTicketDTO;
import io.nology.supportdesk.ticket.dtos.UpdateTicketDTO;
import io.nology.supportdesk.ticket.entity.Ticket;
import io.nology.supportdesk.user.UserService;
import io.nology.supportdesk.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public TicketService(TicketRepository ticketRepository, UserService userService, ModelMapper modelMapper) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<Ticket> getAllTickets(){
        return this.ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id){
        return this.ticketRepository.findById(id);
    }

    private User resolveUser(Long id) {
        Optional<User> result = this.userService.getUserByID(id);
        if (result.isEmpty()) {
            throw new UnprocessableContentException("User with id: " + id + " does not exist");

        }
        return result.get();
    }

    public Ticket createTicket(CreateTicketDTO data){
        User createdBy = resolveUser(data.getCreatedBy());
        User assignedTo = resolveUser(data.getAssignedTo());

        Ticket newTicket = modelMapper.map(data, Ticket.class);
        newTicket.setCreatedBy(createdBy);
        newTicket.setAssignedTo(assignedTo);
        this.ticketRepository.save(newTicket);
        return newTicket;
    }

    public boolean deleteTicket(Long id){
        Optional<Ticket> result = this.ticketRepository.findById(id);

        if(result.isEmpty()){
            return false;
        }
        this.ticketRepository.delete(result.get());
        return true;
    }

    public Optional<Ticket> updateTicket(Long id, UpdateTicketDTO data){

        Optional<Ticket> result = this.ticketRepository.findById(id);

        if (result.isEmpty()){
            return result;
        }

        Ticket toUpdate = result.get();

        if(data.getCreatedBy() != null){
            User foundUser = resolveUser(data.getCreatedBy());
            toUpdate.setCreatedBy(foundUser);
        }

        if(data.getAssignedTo() != null){
            User foundUser = resolveUser(data.getAssignedTo());
            toUpdate.setAssignedTo(foundUser);
        }

        modelMapper.map(data, toUpdate);
        this.ticketRepository.save(toUpdate);
        return Optional.of(toUpdate);
    }
}
