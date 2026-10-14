package io.nology.supportdesk.ticket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ModelMapper modelMapper;

    @Spy
    @InjectMocks
    private TicketService ticketService;

    @Test
    public void getAllTickets_CallsFindAllTicketsOnRepo(){
        this.ticketService.getAllTickets();
        verify(this.ticketRepository).findAll();
    }



}
