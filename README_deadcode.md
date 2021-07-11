DeadCode
-
This class can help track code that should not be executing but logging it limited times and not
spamming the logs/output.  Most modern IDEs can detect when method is not part of a call tree,
however, code can be called via reflection, JSP, conditional factory method, or library entry point
and it's unclear if this method is ever used.  Adding a trigger will perform some action
when triggered from a unique (based on call stack) location, but you only want to execute that action
once or a few times.

Most common use is to log something in the trigger and when it executes check
the logs and if it has not triggered for some time then the code is most likely not being used.

The trigger can also be used to track down how code is being called by providing a Throwable with
a stack trace, location and time of call.

**Initialize singleton and trigger**
```
// Done once somewhere in startup
DeadCode.initialize("io.my.base.package");

...

// In method that may be dead
DeadCode.trigger((accessPoint)-> LOGGER.warn("202103: Still in use"));
```

**Initialize instance and add trigger**
```
// Create
DeadCodeManager manager = new DeadCodeManager();
manager.addPackageToScan("io.my.base.package");

...

// In method that may be dead
manager.trigger((accessPoint)-> LOGGER.warn("202103: Still in use"));
```

**To trigger once for each unique way it is called and never again**
```
DeadCode.trigger((accessPoint)->{ 
    LOGGER.add("Method was called: " + accessPoint.getLocation() + " at " + accessPoint.getAccessTime()); 
});
```

**To trigger 5 times for each unique way it is called**
```
DeadCode.trigger((accessPoint)->{
  LOGGER.add("Method was called: " + accessPoint.getLocation() + " at " + accessPoint.getAccessTime(),
  5
);
});
```

See: /src/examples for more