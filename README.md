[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Join CDAP community](https://cdap-users.herokuapp.com/badge.svg?t=wrangler)](https://cdap-users.herokuapp.com?t=1)

# Condition Plugin

A control flow plugin that allows conditional execution within pipelines. The conditions are specified as expressions and the variables could include values specified as `runtime` arguments of the pipeline, `token` from plugins prior to the condition  and `global` that includes global information about pipeline like stage, pipeline, logical start time and plugin.

Condition plugin specifies a boolean expression to be evaluated. During pipeline execution, the condition expression specified is evaluated resulting in boolean value (either `true` or `false`). Depending on the result of evaluation, either the downstream pipeline connected along `true` path is executed or along the `false` path is executed. At at point in time, only one path is executed. 

**Read more about how to specify expression** [here](https://github.com/hydrator/condition-plugins/wiki)

# Contact

## Mailing Lists

CDAP User Group and Development Discussions:

* [cdap-user@googlegroups.com](https://groups.google.com/d/forum/cdap-user)

The *cdap-user* mailing list is primarily for users using the product to develop
applications or building plugins for appplications. You can expect questions from
users, release announcements, and any other discussions that we think will be helpful
to the users.

## Slack Team

CDAP Users on Slack: [cdap-users team](https://cdap-users.herokuapp.com)


# License and Trademarks

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
