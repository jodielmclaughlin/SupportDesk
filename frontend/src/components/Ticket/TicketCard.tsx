import type {Ticket} from "../../types/ticket.ts";

interface TicketProps{
    ticket: Ticket;
}

function TicketCard({ticket}: TicketProps){
    return(
        <p>
            {ticket.title}
        </p>
    )
}

export default TicketCard