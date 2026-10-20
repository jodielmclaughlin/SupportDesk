import type {User} from "../../types/user.ts";
import {useNavigate} from "react-router-dom";

interface UserDetailProp{
    user: User
    onClose: () => void;
}

function UserDetailCard({user, onClose}: UserDetailProp) {
    const navigate = useNavigate();

    const handleEdit = () => {
        navigate(`/users/${user.id}/edit`);
    };

        return(
            <div data-testid={`user-detail-card-${user.id}`}
                 className="rounded-xl fixed inset-0 flex items-center justify-center bg-black/50 px-4">
                <div className="w-full max-w-md rounded-xl bg-white p-6 shadow-xl">
                    <h2 className="text-xl text-center font-bold text-gray-900">
                        {user.name}
                    </h2>
                    <div className="mt-4 text-left">
                        <p className="mt-1 text-gray-700">Email: {user.email} </p>
                        <p className="mt-1 text-gray-500">Role: {user.role}</p>
                    </div>
                    <div className="mt-6 flex justify-end gap-3">
                        <button
                            type="button"
                            data-testid={`edit-user-${user.id}`}
                            onClick={handleEdit}
                            className="font-medium text-indigo-600 hover:text-indigo-800 focus:outline-none focus:ring-2 focus:ring-indigo-500 rounded"
                        >
                            Edit
                        </button>
                        <button
                            type="button"
                            data-testid={`close-view-user-${user.id}`}
                            onClick={onClose}
                            className="font-medium text-indigo-600 hover:text-indigo-800 focus:outline-none focus:ring-2 focus:ring-indigo-500 rounded"
                        >
                            Close
                        </button>
                    </div>
                </div>
            </div>
    )
}

export default UserDetailCard
