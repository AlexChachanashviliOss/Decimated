Decimate
-
Allows control of how frequently code gets executed, skipping when not needed.
Useful for throttling logs, chatty output or anything that can be ignored every so often.


Few Simple Examples
-

**Log only once when first happens, ignore all other occurrences:**

`Decimated.once( ()->LOGGER.warn("Something happened") );`


**Log up to 2 times every 30 seconds, ignore all other occurrences:**

`Decimated.nTimes(2, ()->LOGGER.info("Event triggered"), 30_000);`


**Skip first 9 events, then log once (repeat):**

`Decimated.skipThenExecute(9, ()->LOGGER.info("Something frequent happening"));`


**Randomly trigger 25% of the time, never more than once every 500 milliseconds**

`Decimated.random(0.25, ()->LOGGER.debug("Something frequent happening"), 500);`

See: /src/examples for more