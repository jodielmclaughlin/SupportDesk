export type Priority =
    | "LOW"
    | "MEDIUM"
    | "HIGH"
    | "CRITICAL";

export type Category =
    | "HARDWARE"
    | "SOFTWARE"
    | "NETWORK"
    | "ACCESS"
    | "OTHER";

export type Status =
    | "OPEN"
    | "IN_PROGRESS"
    | "RESOLVED";


export interface Ticket {
    id: number;
    title: string;
    description: string;
    status: Status;
    category: Category;
    priority: Priority;
    createdBy: string;
    assignedTo: string;
    createdAt: string;
    updatedAt: string;
    resolvedAt: string;
}