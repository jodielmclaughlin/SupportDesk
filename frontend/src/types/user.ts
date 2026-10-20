export type UserRole =
    | "ADMIN"
    | "AGENT"
    | "REQUESTER";

export interface User {
    id: number;
    name: string;
    email: string;
    passwordHash: string;
    role: UserRole;
}