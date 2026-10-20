import type {User} from "../../types/user.ts";
import {useState} from "react";
import UserDetailCard from "./UserDetailCard.tsx";

interface UserProps{
    user: User;

}

function UserRow({user}: UserProps){
    const [showDetails, setShowDetails] = useState(false);

    return(
        <tr data-testid={`user-row-${user.id}`}
            className="border-b border-gray-200 hover:bg-gray-50">
            <td className="px-6 py-4 font-medium text-gray-900">
                #{user.id}
            </td>
            <td className="px-6 py-4 text-gray-900">
                {user.name}
            </td>
            <td className="px-6 py-4 text-gray-600">
                {user.email}
            </td>
            <td className="px-6 py-4 text-gray-600">
                {user.role}
            </td>
            <td className="px-6 py-4 text-right">
                <button
                    type="button"
                    data-testid={`view-employee-${user.id}`}
                    onClick={() => setShowDetails(true)}
                    className="font-medium text-indigo-600 hover:text-indigo-800 focus:outline-none focus:ring-2 focus:ring-indigo-500 rounded"
                >
                    View
                </button>
                {showDetails && (
                    <div>
                        <UserDetailCard user={user} onClose={() => setShowDetails(false)}/>
                    </div>
                )}
            </td>
        </tr>

    );
}

export default UserRow
