/*
 * Copyright © 2017 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.condition;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlException;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class <code>EL</code> is a Expression Language compiler and executor that wraps
 * Apache Commons JEXL.
 *
 * <p>This class exposes methods for compiling an expression, executing
 * the expression given context variables, retrieves variables specified in the expression
 * upon compilation.</p>
 */
public final class EL {
  private static final Logger LOG = LoggerFactory.getLogger(EL.class);
  private Set<List<String>> variables = new HashSet<>();
  private final JexlEngine engine;
  private JexlScript script = null;

  public EL(ELRegistration registration) {
    engine = new JexlBuilder()
      .namespaces(registration.functions())
      .silent(false)
      .cache(1024)
      .strict(true)
      .logger(new NullLogger())
      .create();
  }

  /**
   * Compiles an expression provided. <p></p>
   * @param expression
   * @throws ELException
   */
  public void compile(String expression) throws ELException {
    variables.clear();
    try {
      script = engine.createScript(expression);
      variables = script.getVariables();
      for (List<String> vars : variables) {
        if (!vars.get(0).contentEquals("token")
          && !vars.get(0).contentEquals("runtime")
          && !vars.get(0).contentEquals("global")) {
          throw new ELException("Condition can only specify either 'token', 'runtime', or 'global' in an expression.");
        }
        if (vars.get(0).equalsIgnoreCase("token")) {
          if (vars.size() != 3) {
            throw new ELException("Incorrect 'token' access, a token is represented as " +
                                    "token['<stage-name>']['input'|'output'|'error']");
          }
        }
        if (vars.get(0).equalsIgnoreCase("runtime")) {
          if (vars.size() != 2) {
            throw new ELException("Incorrect 'runtime' access, a runtime is represented as runtime['<field-name>']");
          }
        }
        if (vars.get(0).equalsIgnoreCase("global")) {
          if (vars.size() != 2) {
            throw new ELException("Incorrect 'global' access, a global is represented as global['<field-name>']");
          }
        }
      }
    } catch (JexlException e) {
      if (e.getCause() != null) {
        throw new ELException(e.getCause().getMessage());
      } else {
        throw new ELException(e.getMessage());
      }
    } catch (Exception e) {
      throw new ELException(e.getMessage());
    }
  }

  public Set<List<String>> variables() {
    return variables;
  }

  public ELResult execute(ELContext context) throws ELException {
    try {
      Object value = script.execute(context);
      ELResult variable = new ELResult(value);
      return variable;
    } catch (JexlException e) {
      // Generally JexlException wraps the original exception, so it's good idea
      // to check if there is a inner exception, if there is wrap it in 'DirectiveExecutionException'
      // else just print the error message.
      if (e.getCause() != null) {
        throw new ELException(e.getCause().getMessage());
      } else {
        throw new ELException(e.getMessage());
      }
    } catch (NumberFormatException e) {
      throw new ELException("Type mismatch. Change type of constant " +
                              "or convert to right data type using conversion functions available. Reason : "
                              + e.getMessage());
    } catch (Exception e) {
      if (e.getCause() != null) {
        throw new ELException(e.getCause().getMessage());
      } else {
        throw new ELException(e.getMessage());
      }
    }
  }

  /**
   * @return List of registered functions.
   */
  public static final class DefaultFunctions implements ELRegistration {
    @Override
    public Map<String, Object> functions() {
      Map<String, Object> functions = new HashMap<>();
      functions.put(null, Global.class);
      return functions;
    }

  }

  private final class NullLogger implements Log {
    @Override
    public void debug(Object o) {

    }

    @Override
    public void debug(Object o, Throwable throwable) {

    }

    @Override
    public void error(Object o) {

    }

    @Override
    public void error(Object o, Throwable throwable) {

    }

    @Override
    public void fatal(Object o) {

    }

    @Override
    public void fatal(Object o, Throwable throwable) {

    }

    @Override
    public void info(Object o) {

    }

    @Override
    public void info(Object o, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled() {
      return false;
    }

    @Override
    public boolean isErrorEnabled() {
      return false;
    }

    @Override
    public boolean isFatalEnabled() {
      return false;
    }

    @Override
    public boolean isInfoEnabled() {
      return false;
    }

    @Override
    public boolean isTraceEnabled() {
      return false;
    }

    @Override
    public boolean isWarnEnabled() {
      return false;
    }

    @Override
    public void trace(Object o) {

    }

    @Override
    public void trace(Object o, Throwable throwable) {

    }

    @Override
    public void warn(Object o) {

    }

    @Override
    public void warn(Object o, Throwable throwable) {

    }
  }
}
