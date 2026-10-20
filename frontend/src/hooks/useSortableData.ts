
import { useMemo, useState } from "react";

export type SortDirection = "asc" | "desc";

type SortValue = string | number | Date | null | undefined;

export function useSortableData<T, K extends string>(
    data: T[],
    getSortValue: (item: T, key: K) => SortValue,
    initialSortKey: K,
    initialDirection: SortDirection = "asc"
) {
    const [sortKey, setSortKey] = useState<K>(initialSortKey);
    const [sortDirection, setSortDirection] =
        useState<SortDirection>(initialDirection);

    const handleSort = (key: K) => {
        if (sortKey === key) {
            setSortDirection((current) =>
                current === "asc" ? "desc" : "asc"
            );
        } else {
            setSortKey(key);
            setSortDirection("asc");
        }
    };

    const sortedData = useMemo(() => {
        return [...data].sort((a, b) => {
            const valueA = getSortValue(a, sortKey);
            const valueB = getSortValue(b, sortKey);

            if (valueA == null && valueB == null) return 0;
            if (valueA == null) return 1;
            if (valueB == null) return -1;

            const normalise = (value: SortValue) =>
                value instanceof Date ? value.getTime() : value;

            const aValue = normalise(valueA);
            const bValue = normalise(valueB);

            let comparison: number;

            if (
                typeof aValue === "number" &&
                typeof bValue === "number"
            ) {
                comparison = aValue - bValue;
            } else {
                comparison = String(aValue).localeCompare(
                    String(bValue),
                    undefined,
                    { numeric: true, sensitivity: "base" }
                );
            }

            return sortDirection === "asc"
                ? comparison
                : -comparison;
        });
    }, [data, getSortValue, sortKey, sortDirection]);

    return {
        sortedData,
        sortKey,
        sortDirection,
        handleSort,
    };
}