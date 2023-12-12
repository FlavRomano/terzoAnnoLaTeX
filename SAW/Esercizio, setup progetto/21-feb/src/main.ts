import './style.css'

export interface Todo {
    id: string;
    text: string;
    state: "done" | "onGoing";
}

function createTodoElement({ id, text, state }: Todo) {
    const li = document.createElement("li");
    const div = document.createElement("div");
    div.classList.add("todo-item");
    const innerDiv = document.createElement("div")

    const checkbox = document.createElement("checkbox");
    checkbox.setAttribute("type", "checkbox");

    const span = document.createElement("span");
    span.innerHTML = text;
        
    const button = document.createElement("button");

    const editingInput = document.createElement("input");
    editingInput.setAttribute("type", "text");

    innerDiv.appendChild(checkbox)
    innerDiv.appendChild(span)
    innerDiv.appendChild(button)

    div.appendChild(innerDiv)

    div.appendChild(editingInput)

    li.appendChild(div)

    return div;
    // <div class="todo-item">
    //       <input type="checkbox" name="" id="">
    //       <span>Todo Item</span>
    //       <button>&times;</button>
    // </div>
}

const todo: Todo = {
    state: "onGoing",
    text: "createTodoElement",
    id: "69"
}
const li = createTodoElement(todo);
document.querySelector("ul")?.append(li);