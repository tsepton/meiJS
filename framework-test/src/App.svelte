<script>
  import * as meiJS from "@tsepton/MeiJS";

  meiJS.enable({
    useEventSystem: true,
  });

  document.addEventListener("put-that-there", (e) => {
    console.log("putting that there...");
    const clicks = e.source.occurrence.filter((occ) => occ.name === "click");
    const colours = clicks.map((click) => click.target.style.backgroundColor);
    clicks[0].target.style.backgroundColor = colours[1];
    clicks[1].target.style.backgroundColor = colours[0];
  });

  document.addEventListener("update-that-color", (e) => {
    console.log("updating color...");
    e.source.occurrence
      .filter((occ) => occ.name === "click")
      .forEach((clickEvent) => {
        clickEvent.target.style.backgroundColor = randomColor();
      });
  });

  function randomColor() {
    return (
      "#" + ((Math.random() * 0xffffff) << 0).toString(16).padStart(6, "0")
    );
  }
</script>

<main>
  <div class="container">
    {#each [...Array(96).keys()] as i}
      <div class="element" />
    {/each}
  </div>
</main>

<style>
  .container {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
  }
  .element {
    width: 10vw;
    height: 10vh;
    border: 1px solid grey;
  }
</style>
