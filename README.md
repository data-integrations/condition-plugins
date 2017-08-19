# Condition Plugin

A control flow plugin that allows conditional execution within pipelines. The conditions are specified as expressions and the variables could include values specified as `runtime` arguments of the pipeline, `token` from plugins prior to the condition  and `global` that includes global information about pipeline like stage, pipeline, logical start time and plugin.

Condition plugin specifies a boolean expression to be evaluated. During pipeline execution, the condition expression specified is evaluated resulting in boolean value (either `true` or `false`). Depending on the result of evaluation, either the downstream pipeline connected along `true` path is executed or along the `false` path is executed. At at point in time, only one path is executed. 

<img src="docs/condition-plugin.png">

## Variables

In the conditional expression, there are three types of `map` variables available to be used within an expression :

* Runtime Variables (E.g. `runtime['<variable-name>']`),
* Token Variables (E.g. `token['<plugin-name>']['input'|'output'|'error']`) & 
* Global Variables (E.g. `global['pipeline'|'namespace'|'logical_start_time'|'plugin']`)
