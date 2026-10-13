package io.nology.supportdesk.ticket;

import io.nology.supportdesk.ticket.dtos.CreateTicketDTO;
import io.nology.supportdesk.ticket.dtos.UpdateTicketDTO;
import io.nology.supportdesk.ticket.entity.Ticket;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    public TicketService(TicketRepository ticketRepository, ModelMapper modelMapper) {
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
    }

    public List<Ticket> getAllTickets(){
        return this.ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id){
        return this.ticketRepository.findById(id);
    }

    public Ticket createTicket(CreateTicketDTO data){
        Ticket newTicket = modelMapper.map(data, Ticket.class);
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
        modelMapper.map(data, toUpdate);
        this.ticketRepository.save(toUpdate);
        return Optional.of(toUpdate);
    }
}
