import {useMemo, useState} from 'react';
import type {Ticket} from "../../types/ticket.ts";
import TicketRow from "./TicketRow.tsx";

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

type SortDirection = "asc" | "desc"

function TicketTable({tickets}: TicketTableProps) {
    const [sortKey, setSortKey] = useState<SortKey>('id');
    const [sortDirection, setSortDirection] = useState<SortDirection>('desc');

    const handleSort = (key: SortKey) => {
        if (sortKey === key) {
            setSortDirection((current) =>
                current === "asc" ? "desc" : "asc"
            );
        } else {
            setSortKey(key);
            setSortDirection("asc");
        }
    };

    const sortedTickets = useMemo(() => {
        return [...tickets].sort((a, b) => {
            const valueA = a[sortKey];
            const valueB = b[sortKey];

            if (valueA == null && valueB == null) return 0;
            if (valueA == null) return 1;
            if (valueB == null) return -1;

            let comparison: number;

            if (
                typeof valueA === "number" &&
                typeof valueB === "number"
            ) {
                comparison = valueA - valueB;
            } else {
                comparison = String(valueA).localeCompare(
                    String(valueB),
                    undefined,
                    { numeric: true, sensitivity: "base" }
                );
            }

            return sortDirection === "asc"
                ? comparison
                : -comparison;
        });
    }, [tickets, sortKey, sortDirection]);

    const renderSortIndicator = (key: SortKey) => {
        if (sortKey !== key) return null;

        return sortDirection === "asc" ? " ↑" : " ↓";
    };

    const renderSortHeader = (
        label: string,
        key: SortKey
    ) => (
        <th
            scope="col"
            aria-sort={
                sortKey === key
                    ? sortDirection === "asc"
                        ? "ascending"
                        : "descending"
                    : "none"
            }
            className="px-6 py-3 text-left"
        >
            <button
                type="button"
                onClick={() => handleSort(key)}
                data-testid={`sort-by-${key}`}
                className="font-semibold hover:text-indigo-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 rounded"
            >
                {label}
                {renderSortIndicator(key)}
            </button>
        </th>
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
                    {renderSortHeader("ID", "id")}
                    {renderSortHeader("Title", "title")}
                    {renderSortHeader("User", "createdBy")}
                    {renderSortHeader("Status", "status")}
                    {renderSortHeader("Priority", "priority")}
                    {renderSortHeader("Category", "category")}
                    {renderSortHeader("Created", "createdAt")}
                    {renderSortHeader("Assigned To", "assignedTo")}

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