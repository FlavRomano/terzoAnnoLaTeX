import { KeyboardEvent, useState } from 'react'
import { uuid } from 'uuidv4';
import './App.css'

interface Todo {
  id: string, 
  text: string,
  state: "onGoing" | "done";
}

function App() {
  const [todos, setTodos] = useState([
      { id: "1", text: "ahahaha", state: "onGoing" },
      { id: "2", text: "ahahaha", state: "done" },
    ] as unknown as Todo[]);

  //  
  
  return (
    <>
    <h1>TODO Web App</h1>
      <section>
        <AddTodo />
        <TodoCounter />
        <TodoList todos={todos}/>
  </section>
    </>
  )
}

function AddTodo() {
  const [value, setValue] = useState("")
  const keyUpHandler = (e: KeyboardEvent) => {
    if (e.key == "Enter" && value !== "") {
      
    }
  }
  return <input
    onChange={(v) => setValue(v.target.value)}
    value={value}
    onKeyUp={keyUpHandler}
    type="text"
    placeholder="Inserisci todo..." />;
}

function TodoCounter() {
  return <span className=''>1/4</span>;
}

function TodoList({ todos }: { todos: Todo[] }) {
  return (
    <ul>
      {todos.map(
        (t) => (
          <li key={t.id}>
            <TodoItem todo={t}></TodoItem>
          </li>
        ))}
    </ul>
  )
}

function TodoItem({todo}: {todo: Todo}) {
  return (
    <div className="todo-item">
          <input type="checkbox" name="" id=""/>
      <span>{todo.text}</span>
          <button>&times;</button>
        </div>
  )
}

export default App
