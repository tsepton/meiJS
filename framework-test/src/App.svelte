<script>
  import * as meiJS from "@tsepton/MeiJS";
</script>

<main>
  {#each Object.keys(meiJS) as ex}
    <div>{ex}</div>
  {/each}
</main>
