import { useNavigate } from "react-router-dom";
import type {User} from "../../types/user.ts";


interface UserProps{
    user: User;

}

function UserRow({user}: UserProps){
    const navigate = useNavigate();

    const handleEdit = () => {
        navigate(`/users/${user.id}`);
    }

    return(
        <tr data-testid={`ticket-row-${user.id}`}
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
                {user.userRole}
            </td>
            <td className="px-6 py-4 text-right">
                <button
                    type="button"
                    data-testid={`edit-ticket-${user.id}`}
                    onClick={handleEdit}
                    className="font-medium text-indigo-600 hover:text-indigo-800 focus:outline-none focus:ring-2 focus:ring-indigo-500 rounded"
                >
                    View / Edit
                </button>
            </td>
        </tr>

    );
}

export default UserRow
