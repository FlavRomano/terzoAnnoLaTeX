<script lang="ts">
  import TodoItem from "./TodoItem.svelte";
  import { todos } from "./todoStore";
  let tts = [];
  const unsubscribe = todos.subscribe((x) => (tts = x));

  let text = "";

  const onKeyUp = (e: KeyboardEvent) => {
    if (e.key == "Enter" && text.trim() !== "") {
      todos.addTodo(text);
    }
  };
</script>

<h1>Todo</h1>
<section>
  <input
    on:keyup={onKeyUp}
    type="text"
    bind:value={text}
    placeholder="Insert text..."
  />
  <span class="counter"
    >{`${$todos.filter(({ state }) => state === "done").length}/${
      $todos.length
    }`}</span
  >
  <ul>
    {#each $todos as todo}
      <li>
        <TodoItem {todo} />
      </li>
    {/each}
  </ul>
</section>
