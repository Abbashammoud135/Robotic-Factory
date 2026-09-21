# Notes — mariane branch

Builds on Abbas's existing livelock fix (`isLivelyLocked()` / `findFreeNeighbouringPosition()`
in `Robot.java`, untouched here) by adding the parallel execution + synchronization part of the
exercise (PDF section "Running the Factory Components in Parallel", page 8).

## What was added

- **`Component.java`** — now `implements Runnable`, with a `run()` method that repeatedly calls
  `behave()` (pausing 50ms between calls) for as long as `isSimulationStarted()` is `true`.
- **`Factory.java`**
  - `startSimulation()` no longer loops — it calls `behave()` once, since the repeated
    behave()/sleep cycle now lives in each component's own thread.
  - `behave()` no longer calls each component's `behave()` directly — it starts a `Thread` per
    component instead (`new Thread(component).start()`), so each component drives itself.
- **`Robot.java`** — only change is wrapping the existing `moveToNextPathPosition()` body in
  `synchronized (getFactory())`. Now that robots run on separate threads, checking whether a
  target position is free and moving into it needs to happen as one atomic step — otherwise two
  robots can both see a position as free and move into it at the same time. Locking on the shared
  `Factory` instance (not `this`) is what makes this a real cross-robot lock. Nothing else in this
  file was touched — Abbas's livelock logic is unchanged.

## Tested

Using the target-path setup already in `SimulatorApplication.java` (both robots routed through
Machine 1 first — the scenario that produces contention):

- Ran the simulator multiple times in a row (manually, via Eclipse), watching for two robots
  overlapping at the same position or any console exception — none observed.
- Robots consistently pass the contested spot near Machine 1 and reach Conveyor 1 without
  crashing or freezing.
- Tried several other target-path combinations (head-on at Machine 2, identical full paths,
  opposite-direction paths) to stress the synchronization under different contention patterns —
  no overlaps or crashes in any of them.

## Known, pre-existing limitation (not a bug introduced here)

The Charging Room's door is created with `open = false` in `SimulatorApplication.java`, and
nothing in the codebase ever calls `door.open()`. A closed door counts as an obstacle for
pathfinding, so any robot targeting `chargingStation` will find no path and turn permanently red
(`blocked = true`, which is never reset). Confirmed this happens regardless of whether
`chargingStation` is a robot's first or a later target. This is starter-code behavior, unrelated
to the threading/synchronization work above.
