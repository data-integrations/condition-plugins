# Conditional

A control flow plugin that allows conditional execution within
pipelines. The conditions are specified as expressions and the
variables could include values specified as runtime arguments of
the pipeline, token from plugins prior to the condition and global
that includes global information about pipeline like stage, pipeline,
logical start time and plugin.

Condition plugin specifies a boolean expression to be evaluated.
During pipeline execution, the condition expression specified is
evaluated resulting in boolean value (either true or false).
Depending on the result of evaluation, either the downstream pipeline
connected along true path is executed or along the false path is
executed. At at point in time, only one path is executed.

More information can be found here

* [Constructing Expressions](https://github.com/hydrator/condition-plugins/wiki/Expression)
  * [About Variables](https://github.com/hydrator/condition-plugins/wiki/Variables)
  * [Allowed Operators](https://github.com/hydrator/condition-plugins/wiki/Operators)
* [Using Macros](https://github.com/hydrator/condition-plugins/wiki/Macros)

## A few examples for immediate reference :

* Checks if the `runtime` argument `filepath` contains `input`.
```
 runtime['filepath'] =~ ".*input_.*"
```

* Checks two `runtime` arguments `a` and `b`.
```
 runtime['a'] > runtime['b']
```

* Checks `output` record count of `File` plugin from `token` with `runtime` value `count`.
```
 token['File']['output'] > runtime['count']
```

* Checks `error` record count of `Data Quality plugin` from `token` with `runtime` value `max_error`
```
token['Data Quality']['error'] <= runtime['max_error']
(token['File']['output'] > runtime['count']) and (runtime['a'] > runtime['b'])
(token['File']['output'] > runtime['count']) || (runtime['a'] > runtime['b'])
token['File']['output'] < runtime['count'] && token['File']['error'] < 1
```

* Checks if a `runtime` argument `value` is provided and it's not `null` and plugin `File` `error` count is less than 1.
```
 !isnull(runtime['value']) && token['File']['error'] < 1
```

* Takes the `max` of plugin `File1` `output` count and `File` `output` count and checks if it's greater than `runtime` argument that is specified as a macro.
```
 math:max(toDouble(token['File1']['output']), toDouble(token['File2']['output'])) > runtime[$variable]
```