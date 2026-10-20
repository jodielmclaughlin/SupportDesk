import type { SortDirection } from "../hooks/useSortableData";

interface SortableTableHeaderProps<K extends string> {
    label: string;
    sortKey: K;
    activeSortKey: K;
    sortDirection: SortDirection;
    onSort: (key: K) => void;
}

function SortableTableHeader<K extends string>({
                                 label,
                                 sortKey,
                                 activeSortKey,
                                 sortDirection,
                                 onSort,
                             }: SortableTableHeaderProps<K>) {
    const isActive = activeSortKey === sortKey;

    return (
        <th
            scope="col"
            aria-sort={
                isActive
                    ? sortDirection === "asc"
                        ? "ascending"
                        : "descending"
                    : "none"
            }
            className="px-6 py-3 text-left"
        >
            <button
                type="button"
                onClick={() => onSort(sortKey)}
                data-testid={`sort-by-${sortKey}`}
                className="rounded font-semibold hover:text-indigo-600 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
                {label}
                {isActive && (
                    sortDirection === "asc" ? " ↑" : " ↓"
                )}
            </button>
        </th>
    );
}

export default SortableTableHeader;