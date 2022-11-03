<script>
  import * as meiJS from "@tsepton/MeiJS";

  meiJS.enable({
    useEventSystem: true,
  });
</script>

<main>
  {#each Object.keys(meiJS) as ex}
    <div>{ex}</div>
  {/each}
</main>
