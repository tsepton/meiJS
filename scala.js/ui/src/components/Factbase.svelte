<script>
  import { FactBase } from "../../../target/scala-2.13/meijs-fastopt/main";
  import Fact from "./Fact.svelte";

  let facts = FactBase.facts;
  window.setInterval(() => (facts = FactBase.facts), 1000);
</script>

<div class="facts">
  <p class="title">Factbase</p>
  {#each facts as fact}
    <Fact {fact} />
  {/each}
</div>

<style>
  .facts {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    background-color: var(--background);
    border: 1px solid var(--normal);
    padding: 10px;
    gap: 10px;
  }

  .title {
    width: 100%;
  }
</style>
