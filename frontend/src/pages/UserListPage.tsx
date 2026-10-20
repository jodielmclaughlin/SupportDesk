import {useEffect, useState} from "react";
import type {User} from "../types/user.ts";
import {getAllUsers} from "../services/users.ts";
import UserTable from "../components/Ticket/UserTable.tsx";

function UserListPage(){
    const [users, setUsers] = useState<User[]>([]);


    useEffect(()=> {
        getAllUsers().then(setUsers);
    }, []);

    return(
        <div>
            <UserTable users={users}/>
        </div>
    )
}
export default UserListPage