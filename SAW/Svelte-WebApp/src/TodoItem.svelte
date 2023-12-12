<script lang="ts">
  import type { Todo } from "./todoStore";
  import { todos } from "./todoStore";

  let editing = false;
  let inputText = "";

  const onKeyUp = (e: KeyboardEvent) => {
    if (e.key == "Enter" && inputText !== "") {
      todos.updateTodo({ ...todo, text: inputText });
      editing = false;
    }
    if (e.key == "Escape") editing = false;
  };

  export let todo: Todo;
</script>

<div class="todo-item">
  #{#if editing}
    <input type="text" on:keyup={onKeyUp} bind:value={inputText} />
  {:else}
    <div>
      <input
        checked={todo.state === "done"}
        on:change={() =>
          todos.updateTodo({
            ...todo,
            state: todo.state === "done" ? "ongoing" : "done",
          })}
        type="checkbox"
      />
      <span
        on:dblclick={() => {
          editing = true;
          inputText = todo.text;
        }}
        class:done={todo.state === "done"}
        class:ongoing={todo.state === "ongoing"}>{todo.text}</span
      >
      <button on:click={() => todos.deleteTodo(todo.id)}>&times;</button>
    </div>
  {/if}
</div>
