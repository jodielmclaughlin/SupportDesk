import type {User} from "../../types/user.ts";
import { useCallback } from "react";
import SortableTableHeader from "../SortableTableHeader";
import { useSortableData } from "../../hooks/useSortableData";
import UserRow from "./UserRow.tsx";

interface UserTableProps{
    users: User[];
}

type SortKey =
    | "id"
    | "name"
    | "email"
    | "userRole";


function UserTable({users}: UserTableProps) {
    const getUserSortValue = useCallback(
        (user: User, key: SortKey) => {
            switch (key) {
                default:
                    return user[key] as string | number | null;
            }
        },
        []
    );

    const {
        sortedData: sortedUsers,
        sortKey,
        sortDirection,
        handleSort,
    } = useSortableData<User, SortKey>(
        users,
        getUserSortValue,
        "id",
        "desc"
    );


    return(
        <section className="w-full">
            <h1 className="text-2xl m-3 font-bold text-gray-900 mb-6">
                Users
            </h1>

            <div className="overflow-x-auto mx-3 rounded-xl border border-gray-200 bg-white shadow-sm">
                <table
                    data-testid="user-table"
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
                            label="Name"
                            sortKey="name"
                            activeSortKey={sortKey}
                            sortDirection={sortDirection}
                            onSort={handleSort}
                        />
                        <SortableTableHeader
                            label="Email"
                            sortKey="email"
                            activeSortKey={sortKey}
                            sortDirection={sortDirection}
                            onSort={handleSort}
                        />
                        <SortableTableHeader
                            label="Role"
                            sortKey="userRole"
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
                    {sortedUsers.length > 0 ? (
                        sortedUsers.map((user: User) => (
                            <UserRow
                                key={user.id}
                                user={user}
                            />
                        ))
                    ) : (
                        <tr>
                            <td
                                colSpan={7}
                                className="px-6 py-8 text-center text-gray-500"
                                data-testid="empty-user-list"
                            >
                                No users found.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </section>
    )
}

export default UserTable