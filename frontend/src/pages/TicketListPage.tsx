import TicketTable from "../components/Ticket/TicketTable.tsx";
import {useEffect, useState} from "react";
import type {Ticket} from "../types/ticket.ts";
import {getAllTickets} from "../services/tickets.ts";

function TicketListPage() {
    const [tickets, setTickets] = useState<Ticket[]>([]);

    useEffect(() => {
        getAllTickets().then(setTickets);
    }, []);

    return(
        <div>
            <TicketTable tickets={tickets}/>
        </div>
    )
}
export default TicketListPage