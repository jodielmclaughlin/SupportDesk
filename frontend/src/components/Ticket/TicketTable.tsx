import type {Ticket} from "../../types/ticket.ts";
import TicketRow from "./TicketRow.tsx";
import { useCallback } from "react";
import {useSortableData} from "../../hooks/useSortableData";
import SortableTableHeader from "../SortableTableHeader.tsx";

interface TicketTableProps{
    tickets: Ticket[];
}

type SortKey =
    | "id"
    | "title"
    | "createdBy"
    | "status"
    | "priority"
    | "category"
    | "createdAt"
    | "assignedTo";


function TicketTable({tickets}: TicketTableProps) {
    const getTicketSortValue = useCallback(
        (ticket: Ticket, key: SortKey) => {
            switch (key) {
                case "createdBy":
                    return ticket.createdBy;
                case "assignedTo":
                    return ticket.assignedTo;
                case "createdAt":
                    return ticket.createdAt
                        ? new Date(ticket.createdAt)
                        : null;
                default:
                    return ticket[key] as string | number | null;
            }
        },
        []
    );

    const {
        sortedData: sortedTickets,
        sortKey,
        sortDirection,
        handleSort,
    } = useSortableData<Ticket, SortKey>(
        tickets,
        getTicketSortValue,
        "id",
        "desc"
    );


    return(
        <section className="w-full">
            <h1 className="text-2xl m-3 font-bold text-gray-900 mb-6">
                Support Desk Tickets
            </h1>

            <div className="overflow-x-auto mx-3 rounded-xl border border-gray-200 bg-white shadow-sm">
               <table
                   data-testid="ticket-table"
                   className="w-full text-sm text-left text-gray-700">
                <thead className="bg-gray-50 text-xs uppercase text-gray-500">
                <tr>
                    <SortableTableHeader
                        label="ID"
                        sortKey="id"
                        activeSortKey={sortKey}
                        sortDirection={sortDirection}
                        onSort={handleSort}
                    />

                    <SortableTableHeader
                        label="Title"
                        sortKey="title"
                        activeSortKey={sortKey}
                        sortDirection={sortDirection}
                        onSort={handleSort}
                    />
                    <SortableTableHeader
                        label="User"
                        sortKey="createdBy"
                        activeSortKey={sortKey}
                        sortDirection={sortDirection}
                        onSort={handleSort}
                    />
                    <SortableTableHeader
                        label="Status"
                        sortKey="status"
                        activeSortKey={sortKey}
                        sortDirection={sortDirection}
                        onSort={handleSort}
                    />
                    <SortableTableHeader
                        label="Priority"
                        sortKey="priority"
                        activeSortKey={sortKey}
                        sortDirection={sortDirection}
                        onSort={handleSort}
                    />
                    <SortableTableHeader
                        label="Category"
                        sortKey="category"
                        activeSortKey={sortKey}
                        sortDirection={sortDirection}
                        onSort={handleSort}
                    />
                    <SortableTableHeader
                        label="Created"
                        sortKey="createdAt"
                        activeSortKey={sortKey}
                        sortDirection={sortDirection}
                        onSort={handleSort}
                    />
                    <SortableTableHeader
                        label="Assigned To"
                        sortKey="assignedTo"
                        activeSortKey={sortKey}
                        sortDirection={sortDirection}
                        onSort={handleSort}
                    />

                    <th scope="col" className="px-6 py-3 text-right">
                        Actions
                    </th>
                </tr>
               </thead>
                <tbody>
                {sortedTickets.length > 0 ? (
                    sortedTickets.map((ticket) => (
                        <TicketRow
                            key={ticket.id}
                            ticket={ticket}
                        />
                    ))
                ) : (
                    <tr>
                        <td
                            colSpan={7}
                            className="px-6 py-8 text-center text-gray-500"
                            data-testid="empty-ticket-list"
                        >
                            No tickets found.
                        </td>
                    </tr>
                )}
                </tbody>
               </table>
            </div>
        </section>
    )
}

export default TicketTable