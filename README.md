[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Join CDAP community](https://cdap-users.herokuapp.com/badge.svg?t=wrangler)](https://cdap-users.herokuapp.com?t=1)

# Condition Plugin

A control flow plugin that allows conditional execution within pipelines. The conditions are specified as expressions and the variables could include values specified as `runtime` arguments of the pipeline, `token` from plugins prior to the condition  and `global` that includes global information about pipeline like stage, pipeline, logical start time and plugin.

Condition plugin specifies a boolean expression to be evaluated. During pipeline execution, the condition expression specified is evaluated resulting in boolean value (either `true` or `false`). Depending on the result of evaluation, either the downstream pipeline connected along `true` path is executed or along the `false` path is executed. At at point in time, only one path is executed. 

<img src="docs/condition-plugin.png">

## Expression

Conditions are specifed as boolean expression. A Boolean expression is a logical statement that is either TRUE or FALSE . Expressions can compare data of any type as long as both parts of the expression have the same basic data type or compatible data type. You can test data to see if it is equal to, greater than, or less than other data. Following are some of the example of an expression that can be specified. 

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

### Variables

In the conditional expression, there are three types of `map` variables available to be used within an expression :

* **Runtime Variables** (E.g. `runtime['<variable-name>']`) -- Is a map variable that contain values  ,
* **Token Variables** (E.g. `token['<plugin-name>']['input'|'output'|'error']`) & 
* **Global Variables** (E.g. `global['pipeline'|'namespace'|'logical_start_time'|'plugin']`)

### **Operators**

Following are the boolean operators that can be used within the boolean expression specifying the condition

| Type | Description |
| ---- | ----------- |
| **Boolean** `and` or `&` | The operator `&&` can be used as well as the word `and` to specify composing conditions, e.g. ```cond1 and cond2``` and ```cond1 && cond2```|
| **Boolean** `or` or `\|\|` | The operator `\|\|` can be used as well as the word `or` to specify composing conditions, e.g. ```cond1 or cond2``` and ```cond1 \|\| cond2``` |
| **Boolean** `not` or `!` | The operator `!` can be used as well as the word `not` to specify composing conditions, e.g. ```!cond``` and ```not cond``` | 
| **Boolean** `&` | The bitwise operator `&` is used as follows ```!cond``` and ```not cond``` |
| **Ternary conditional** `?:`	 | The ternary conditional operator `condition ? if_true : if_false` operator can be used as well as the abbreviation value `?:` `if_false` which returns the value if its evaluation is defined, non-null and non-false.The condition will evaluate to false when it refers to an undefined variable or null. e.g. ```val1 ? val1 : val2``` and ```val1 ?: val2 ```, Where `val1` and `val2` could be `true` or `false`. | 
| **Equality** `==` or `eq` | The usual `==` operator can be used as well as the abbreviation `eq`. For example ```val1 == val2``` and ```val1 eq val2``` `null` is only ever equal to null, that is if you compare `null` to any non-null value, the result is `false`. |
| **InEquality** `!=` or `ne` | The usual `!=` operator can be used as well as the abbreviation `ne`. For example```val1 != val2``` and ```val1 ne val2``` | 
| **Less Than** `<` or `lt` | The usual `<` operator can be used as well as the abbreviation `lt`. For example ```val1 < val2``` and ```val1 lt val2``` |
| **Less Than or Equal To** `<=` or `le` | The usual `<=` operator can be used as well as the abbreviation `lt`. For example ```val1 <= val2``` and ```val1 le val2``` |
| **Greater Than** `>` or `gt` | The usual `>` operator can be used as well as the abbreviation `gt`. For example ```val1 > val2``` and ```val1 gt val2``` |
| **Greater Than or Equal To** `>=` or `ge` | The usual `>=` operator can be used as well as the abbreviation `gt`. For example ```val1 >= val2``` and ```val1 ge val2``` |
| **In or Match** `=~` | `=~` operator can be used to check that a string matches a regular expression. For example `"abcdef" =~ "abc.*` returns `true`. It also checks whether any collection, set or map (on keys) contains a value or not; in that case, it behaves as an "in" operator. Note that arrays and user classes exposing a public 'contains' method will allow their instances to behave as right-hand side operands of this operator. `"a" =~ ["a","b","c","d","e",f"]` returns `true` | 
| **Not-In or Not-Match** `!~` | `!~` operator can be used to check that a string does not match a regular expression. For example `"abcdef" !~ "abc.*` returns `false`. It also checks whether any collection, set or map (on keys) does not contain a value; in that case, it behaves as "not in" operator. Note that arrays and user classes exposing a public 'contains' method will allow their instances to behave as right-hand side operands of this operator. `"a" !~ ["a","b","c","d","e",f"]` returns `true` |
| **Starts With** `=^` | The `=^` operator is a short-hand for the 'startsWith' method. For example, `"abcdef" =^ "abc"` returns `true` |
| **Not Starts With** `!^` | This is the negation of the 'starts with' operator. `a !^ "abc"` is equivalent to `!(a =^ "abc")` |
| **Ends With** `=$` | The `=$` operator is a short-hand for the 'endsWith' method. For example, `"abcdef" =$ "def"` returns `true` | 
| **Not Ends With** `!$` | This is the negation of the 'ends with' operator. `a !$ "abc"` is equivalent to `!(a =$ "abc")` |
| **If/Then** | Traditional `if` ... `then` could be used, but they have to return either `true` or `false`, e.g. `if ((runtime['x'] * 2) == token['DQ']['output']) { false; } else { true; } |

## Macros

## Transient Dataset

## Contact

### Mailing Lists

CDAP User Group and Development Discussions:

* [cdap-user@googlegroups.com](https://groups.google.com/d/forum/cdap-user)

The *cdap-user* mailing list is primarily for users using the product to develop
applications or building plugins for appplications. You can expect questions from
users, release announcements, and any other discussions that we think will be helpful
to the users.

### IRC Channel

CDAP IRC Channel: [#cdap on irc.freenode.net](http://webchat.freenode.net?channels=%23cdap)

### Slack Team

CDAP Users on Slack: [cdap-users team](https://cdap-users.herokuapp.com)


## License and Trademarks

Copyright © 2016-2017 Cask Data, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
either express or implied. See the License for the specific language governing permissions
and limitations under the License.

Cask is a trademark of Cask Data, Inc. All rights reserved.

Apache, Apache HBase, and HBase are trademarks of The Apache Software Foundation. Used with
permission. No endorsement by The Apache Software Foundation is implied by the use of these marks.
