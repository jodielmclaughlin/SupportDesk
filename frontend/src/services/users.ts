import type {User, UserRole} from "../types/user.ts";

interface CreateUserDTO{
    name: string;
    email: string;
    passwordHash: string;
    role: UserRole;
}

interface UpdateUserDTO{
    name: string;
    email: string;
    role: UserRole;
}

export async function getAllUsers(){
    const response = await fetch("http://localhost:8080/users", {

    });
    if (!response.ok){
        throw new Error("Could not fetch users")
    }
    return (await response.json()) as User[];
}

export async function getUser(id: number){
    const response = await fetch(`http://localhost:8080/users/${id}`, {

    });
    if (!response.ok){
        throw new Error("Could not fetch user")
    }
    return (await response.json()) as User;
}

export async function createUser(userData: CreateUserDTO) {
    const response = await fetch("http://localhost:8080/users", {
        method: "POST",
        body: JSON.stringify(userData),
    });

    if(response.status === 403){
        throw new Error("You do not have permission to create a user.");
    }
    if(!response.ok){
        throw new Error("Could not create user");
    }
    return (await response.json()) as User;
}

export async function deleteUser(id: number){
    const response = await fetch(`http://localhost:8080/users/${id}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error("Failed to delete user");
    }
}

export async function editUser(id:number, userData: UpdateUserDTO){
    const response = await fetch(`http://localhost:8080/users/${id}`, {
        method: "PATCH",
        body: JSON.stringify(userData),

    });

    if (!response.ok) {
        throw new Error("Failed to update user");
    }

}

