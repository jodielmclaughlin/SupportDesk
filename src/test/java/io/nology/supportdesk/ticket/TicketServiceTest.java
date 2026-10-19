package io.nology.supportdesk.ticket;

import io.nology.supportdesk.common.exceptions.UnprocessableContentException;
import io.nology.supportdesk.ticket.dtos.CreateTicketDTO;
import io.nology.supportdesk.ticket.dtos.UpdateTicketDTO;
import io.nology.supportdesk.ticket.entity.Ticket;
import io.nology.supportdesk.user.UserService;
import io.nology.supportdesk.user.entity.User;
import io.nology.supportdesk.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;

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

    @Test
    public void updateTicket_whenTicketIdExists_returnsUpdatedTicket(){
        UpdateTicketDTO dto = new UpdateTicketDTO();
        Ticket fakeTicket = new Ticket();
        when(this.ticketRepository.findById(1L)).thenReturn(Optional.of(fakeTicket));
        Optional<Ticket> result = this.ticketService.updateTicket(1L, dto);
        verify(this.modelMapper).map(dto, fakeTicket);
        verify(this.ticketRepository).save(fakeTicket);
        assertTrue(result.isPresent());
        assertEquals(fakeTicket, result.get());
    }

    @Test
    public void updateTicket_whenTicketIdDoesNotExists_returnsEmptyOptionalDoNotSave(){
        when(this.ticketRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Ticket> result = this.ticketService.updateTicket(1L, new UpdateTicketDTO());
        assertTrue(result.isEmpty());
        verifyNoInteractions(this.modelMapper);
        verify(this.ticketRepository, never()).save(any(Ticket.class));

    }

    @Test
    public void deleteTicket_whenTicketDoesNotExist_returnsFalseAndDoesNotCallDelete(){
        when(this.ticketRepository.findById(1L)).thenReturn(Optional.empty());
        boolean result = this.ticketService.deleteTicket(1L);
        assertFalse(result);
        verify(this.ticketRepository, never()).delete(any(Ticket.class));
    }

    @Test
    public void deleteTicket_whenTicketExists_returnsTrueAndCallsDelete(){
        Ticket fakeTicket = new Ticket();
        when(this.ticketRepository.findById(1L)).thenReturn(Optional.of(fakeTicket));
        boolean result = this.ticketService.deleteTicket(1L);
        assertTrue(result);
        verify(this.ticketRepository).delete(fakeTicket);
    }
    //TODO add tests back once auth is in place
//    @Test
//    public void updateTicket_whenUserIsAgent_returnsUpdatedTicket(){
//        Long ticketId = 1L;
//
//        User agent = new User();
//        agent.setRole(UserRole.AGENT);
//
//        Ticket existingTicket = new Ticket();
//        existingTicket.setId(ticketId);
//
//        UpdateTicketDTO dto = new UpdateTicketDTO();
//
//        when(ticketRepository.findById(ticketId))
//                .thenReturn(Optional.of(existingTicket));
//
//        when(ticketRepository.save(existingTicket))
//                .thenReturn(existingTicket);
//
//        Optional<Ticket> result =
//                ticketService.updateTicket(ticketId, dto, agent);
//
//        assertTrue(result.isPresent());
//        assertEquals(existingTicket, result.get());
//
//        verify(modelMapper).map(dto, existingTicket);
//        verify(ticketRepository).save(existingTicket);
//    }
//    @Test
//    public void updateTicket_whenUserIsAdmin_returnsUpdatedTicket(){
//        Long ticketId = 1L;
//
//        User admin = new User();
//        admin.setRole(UserRole.ADMIN);
//
//        Ticket existingTicket = new Ticket();
//        existingTicket.setId(ticketId);
//
//        UpdateTicketDTO dto = new UpdateTicketDTO();
//
//        when(ticketRepository.findById(ticketId))
//                .thenReturn(Optional.of(existingTicket));
//
//        when(ticketRepository.save(existingTicket))
//                .thenReturn(existingTicket);
//
//        Optional<Ticket> result =
//                ticketService.updateTicket(ticketId, dto, admin);
//
//        assertTrue(result.isPresent());
//        assertEquals(existingTicket, result.get());
//
//        verify(modelMapper).map(dto, existingTicket);
//        verify(ticketRepository).save(existingTicket);
//    }

//    @Test
//    public void updateTicket_whenCreatorOrOtherUserAttemptsUpdate_throwsAccessDenied(){
//        Long ticketId = 1L;
//
//        User requester = new User();
//        requester.setRole(UserRole.REQUESTER);
//
//        Ticket existingTicket = new Ticket();
//
//        UpdateTicketDTO dto = new UpdateTicketDTO();
//
//        when(ticketRepository.findById(ticketId))
//                .thenReturn(Optional.of(existingTicket));
//
//        assertThrows(
//                AccessDeniedException.class,
//                () -> ticketService.updateTicket(ticketId, dto, requester)
//        );
//
//        verify(ticketRepository, never()).save(any(Ticket.class));
//        verifyNoInteractions(modelMapper);
//    }
//
//
//    @Test
//    public void deleteTicket_whenUserIsNotCreatorAgentOrAdmin_doesNotDeleteTicket(){
//
//    }
//
//    @Test
//    public void deleteTicket_whenUserIsCreatorAgentOrAdmin_deletesTicket(){
//
//    }





}
