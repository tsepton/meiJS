<script>
  import * as meiJS from "@tsepton/MeiJS";
  import { dsl } from "@tsepton/MeiJS";
  import { eventSystem } from "@tsepton/MeiJS";
  import ExportedLib from "./component/ExportedLib.svelte";
  //import * as Leap from "leapjs";

  // Leap.loop(function (frame, e) {
  //   console.log(frame.hands.length);
  // });

  meiJS.enable({
    useEventSystem: true,
  });

  const expr = dsl
    .voice("update")
    .then(dsl.voice("click").and(dsl.voice("that")))
    .then(dsl.voice("color"));

  const updateThatColor = new meiJS.CompositeEvent("update-that-color", expr);

  eventSystem.subscribe(updateThatColor, function (e) {
    e.occurrences
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
