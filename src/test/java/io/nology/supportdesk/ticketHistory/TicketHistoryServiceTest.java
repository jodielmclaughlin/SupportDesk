package io.nology.supportdesk.ticketHistory;

public class TicketHistoryServiceTest {

}
//Test	Expected behaviour
//recordHistory — valid change	Saves a history record
//recordHistory — status changes	Records the old and new statuses
//recordHistory — assignment changes	Records the assignment change
//getHistoryByTicketId — records exist	Returns history for that ticket
//getHistoryByTicketId — no records	Returns an empty list
//recordHistory — invalid ticket/user	Rejects invalid references, if this validation belongs to the service