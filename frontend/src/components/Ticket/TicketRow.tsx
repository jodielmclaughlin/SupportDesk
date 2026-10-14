import { useNavigate } from "react-router-dom";
import type {Ticket} from "../../types/ticket.ts";


interface TicketProps{
    ticket: Ticket;

}

function TicketRow({ticket}: TicketProps){
    const navigate = useNavigate();

    const handleEdit = () => {
        navigate(`/tickets/${ticket.id}`);
    }

    return(
        <tr data-testid={`ticket-row-${ticket.id}`}
            className="border-b border-gray-200 hover:bg-gray-50">
            <td className="px-6 py-4 font-medium text-gray-900">
                #{ticket.id}
            </td>
            <td className="px-6 py-4 text-gray-900">
                {ticket.title}
            </td>
            <td className="px-6 py-4 text-gray-600">
                {ticket.createdBy}
            </td>
            <td className="px-6 py-4 text-gray-600">
                {ticket.status}
            </td>

            <td className="px-6 py-4 text-gray-600">
                {ticket.priority}
            </td>
            <td className="px-6 py-4 text-gray-600">
                {ticket.category}
            </td>

            <td className="px-6 py-4 text-gray-600">
                {ticket.createdAt
                    ? new Date(ticket.createdAt).toLocaleDateString()
                    : "—"}
            </td>

            <td className="px-6 py-4 text-gray-600">
                {ticket.assignedTo || "Unassigned"}
            </td>

            <td className="px-6 py-4 text-right">
                <button
                    type="button"
                    data-testid={`edit-ticket-${ticket.id}`}
                    onClick={handleEdit}
                    className="font-medium text-indigo-600 hover:text-indigo-800 focus:outline-none focus:ring-2 focus:ring-indigo-500 rounded"
                >
                    View / Edit
                </button>
            </td>
        </tr>

    );
}

export default TicketRow
