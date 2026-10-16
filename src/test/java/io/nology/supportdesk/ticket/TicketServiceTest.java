package io.nology.supportdesk.ticket;

import io.nology.supportdesk.common.exceptions.UnprocessableContentException;
import io.nology.supportdesk.ticket.dtos.CreateTicketDTO;
import io.nology.supportdesk.ticket.entity.Ticket;
import io.nology.supportdesk.user.UserService;
import io.nology.supportdesk.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserService userService;
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

    @Test
    public void getTicketById_callsFindById(){
        this.ticketService.getTicketById(1L);
        verify(this.ticketRepository).findById(1L);
    }
    @Test
    public void getTicketById_whenTicketExists_returnsTicket(){
        Ticket fakeTicket = new Ticket();
        fakeTicket.setId(1L);
        fakeTicket.setTitle("Fake Ticket");
        when(this.ticketRepository.findById(1L)).thenReturn(Optional.of(fakeTicket));
        Optional<Ticket> result = ticketService.getTicketById(1L);
        assertTrue(result.isPresent());
        assertEquals(fakeTicket.getTitle(), result.get().getTitle());
    }

    @Test
    public void getTicketById_whenTicketDoesntExist_returnsEmptyOptional(){
        when(ticketRepository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<Ticket> result = ticketService.getTicketById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void createTicket_whenUserDoesNotExist_throwsAndNeverSaves(){
        CreateTicketDTO dto = new CreateTicketDTO();
        dto.setAssignedTo(2L);
        dto.setCreatedBy(1L);
        when(this.userService.getUserByID(1L)).thenReturn(Optional.empty());

        assertThrows(UnprocessableContentException.class, () -> ticketService.createTicket(dto));
        verifyNoInteractions(this.modelMapper);
        verify(this.ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void createTicket_savesOutputOfMapperAndSetsUsers() {
        CreateTicketDTO dto = new CreateTicketDTO();
        dto.setCreatedBy(1L);
        dto.setAssignedTo(2L);
        User createdByUser = new User();
        createdByUser.setId(1L);
        User assignedToUser = new User();
        assignedToUser.setId(2L);
        Ticket fakeTicket = new Ticket();
        when(this.userService.getUserByID(1L)).thenReturn(Optional.of(createdByUser));
        when(this.userService.getUserByID(2L)).thenReturn(Optional.of(assignedToUser));
        when(this.modelMapper.map(dto, Ticket.class)).thenReturn(fakeTicket);

        Ticket result = this.ticketService.createTicket(dto);
        assertEquals(createdByUser, result.getCreatedBy());
        assertEquals(assignedToUser, result.getAssignedTo());
        verify(this.ticketRepository).save(fakeTicket);
        assertEquals(fakeTicket, result);
    }



}
