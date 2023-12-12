import { writable } from "svelte/store";
import { v4 as uuid } from "uuid"

export interface Todo {
    id: string,
    text: string,
    state: "done" | "ongoing"
}

function createTodoStore() {
    const { subscribe, set, update } = writable<Todo[]>([
            {id: "1", text: "git add .", state: "done"},
            { id: "2", text: "git commit", state: "ongoing"},
        ])

    const addTodo = (text: string) => {
        const todo: Todo = {
            text,
            id: uuid(),
            state: "ongoing",
        }
        update(old_todos => [...old_todos, todo])
    }

    const updateTodo = (t: Todo) => {
        update(old_todos => old_todos.map(old_todo => old_todo.id === t.id ? t : old_todo))
    }
    
    const deleteTodo = (id: string) => {
        update(old_todos => old_todos.filter(old_todo => old_todo.id !== id))
    }

    return {
        subscribe,
        set,
        update,
        addTodo,
        updateTodo,
        deleteTodo,
    }
}

export const todos = createTodoStore();