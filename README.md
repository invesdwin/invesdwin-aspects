# invesdwin-aspects

These are a few aspects that make development easier.

## Maven

Releases and snapshots are deployed to this maven repository:
```
http://invesdwin.de/artifactory/invesdwin-oss-remote
```

Dependency declaration:
```xml
<dependency>
	<groupId>de.invesdwin</groupId>
	<artifactId>invesdwin-aspects</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Aspects

You can have a look at the included test cases for samples on how to use these aspects more specifically. Otherwise here are summaries about them.

#### `PropertyChangeSupportedAspect`
This aspect handles the `PropertyChangeSupport` for all setters in classes extending `AValueObject` or `APropertyChangeSupported` from the [invesdwin-util](https://github.com/subes/invesdwin-util) project, so you do not have to implement `firePropertyChange(...)` yourself everywhere.

#### `AssertNoSynchronizedScheduledAspect`
When using `@Scheduled` from spring, you should not try to make the scheduled method synchronized in order to prevent parallel executions (e.g. when both a startup hook and the scheduler might launch it on application startup), or else the scheduling thread pool might get a deadlock and stop scheduling any other tasks. This aspect will throw a runtime exception when it detects this code smell. See the `SkipParallelExecutionAspect` for a better solution.

#### `SkipParallelExecutionAspect`
This aspect will skip any parallel executions of methods that are annotated with `@Scheduled` or `@SkipParallelExecution`. Only methods with a void return type should be annotated with this, since return values do not make sense for scheduled methods.

#### `EventDispatchThreadAspect`
This aspect handles the `@EventDispatchThread` which allows you to wrap any swing UI method to be run inside the event dispatch thread (EDT). Possible `InvocationTypes` are:
* `INVOKE_AND_WAIT`: if running in EDT, call directly, otherwise run in EDT and wait for it to finish.
* `INVOKE_LATER`: schedule the task to be run later by the EDT, returning before it actually happens.
* `INVOKE_LATER_IF_NOT_IN_EDT`: if running in EDT, call directly, otherwise schedule the tasks to be run later by the EDT, returning before it actually happens.

For all other cases where you want to wrap only parts of methods, you can use the included `EventDispatchThreadUtil` directly. This annotation and util helps not to get exceptions about code already running in EDT. Also it makes code that should run in EDT much cleaner since you spare yourself lots of boiler plate code. 

#### `MonitoredAspect`
This aspect provides support for a `@Monitored` annotation to do lightweight specific performance measurements in your code using the popular [JAMon](http://jamonapi.sourceforge.net/) library. You could even do service level agreement (SLA) monitoring with this in a lightweight and non code-intrusive fashion. Or just use it for your tests to gather some additional information about your code executions where needed.

#### `ConstructorFinishedHookAspect`
Ever wanted an interface that works like `@Configurable` in combination with `InitializingBean` from spring which gets invoked after the object has been constructor but without the performance overhead of the dependency injection? Just implement `IConstructorFinishedHook` instead and enjoy it. With this you can initiailize abstract classes *after* the implementing constructor is finished.

## Support

If you need further assistance or have some ideas for improvements and don't want to create an issue here on github, feel free to start a discussion in our [invesdwin-platform](https://groups.google.com/forum/#!forum/invesdwin-platform) mailing list.
