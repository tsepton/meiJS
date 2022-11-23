<script>
  import * as meiJS from "@tsepton/MeiJS";
  import { eventSystem } from "@tsepton/MeiJS";
  import ExportedLib from "./component/ExportedLib.svelte";

  meiJS.enable({
    useEventSystem: true,
  });

  // meiJS.AddModality("mouseClick", MouseEvent("Click"));

  // meiJS.Subscribe("mouseClick + voicePut", callback);

  // const click = new Mouse("click")
  // const put = new Voice("put")

  // put
  //   .then(click.and(put).and().and())
  //   .then(and(click, put, , ))

  eventSystem.subscribe("put-that-there", (e) => {
    console.log("putting that there...");
    const clicks = e.source.occurrence.filter((occ) => occ.name === "click");
    const colours = clicks.map((click) => click.target.style.backgroundColor);
    clicks[0].target.style.backgroundColor = colours[1];
    clicks[1].target.style.backgroundColor = colours[0];
  });

  eventSystem.subscribe("update-that-color", function (e) {
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
  <ExportedLib />
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
