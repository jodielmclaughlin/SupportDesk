import type {Category, Priority, Status, Ticket} from "../types/ticket.ts";

interface CreateTicketDTO{
    title: string;
    description: string;
    status: Status;
    category: Category;
    priority: Priority;
    createdBy: string;
    assignedTo: string;
    resolvedAt: string;
}
interface UpdateTicketDTO{
    title: string;
    description: string;
    status: Status;
    category: Category;
    priority: Priority;
    createdBy: string;
    assignedTo: string;
    resolvedAt: string;
}

export async function getAllTickets(){
    const response = await fetch("http://localhost:8080/tickets", {

    });
    if (!response.ok){
        throw new Error("Could not fetch tickets")
    }
    return (await response.json()) as Ticket[];
}

export async function getTicket(id: number){
    const response = await fetch(`http://localhost:8080/tickets/${id}`, {

    });
    if (!response.ok){
        throw new Error("Could not fetch ticket")
    }
    return (await response.json()) as Ticket;
}

export async function createTicket(ticketData: CreateTicketDTO) {
    const response = await fetch("http://localhost:8080/tickets", {
        method: "POST",
        body: JSON.stringify(ticketData),
    });

    if(response.status === 403){
        throw new Error("You do not have permission to create a ticket.");
    }

    if(!response.ok){
        throw new Error("Could not create ticket");
    }
    return (await response.json()) as Ticket;
}

export async function deleteTicket(id: number){
    const response = await fetch(`http://localhost:8080/tickets/${id}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error("Failed to delete ticket");
    }
}

export async function editTicket(id:number, ticketData: UpdateTicketDTO){
    const response = await fetch(`http://localhost:8080/tickets/${id}`, {
        method: "PATCH",
        body: JSON.stringify(ticketData),

    });

    if (!response.ok) {
        throw new Error("Failed to update ticket");
    }

}