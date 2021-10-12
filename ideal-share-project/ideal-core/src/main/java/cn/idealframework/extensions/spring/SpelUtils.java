/*
 * Copyright 2021 cn.idealframework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.idealframework.extensions.spring;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @author 宋志宗 on 2021/6/4
 */
public final class SpelUtils {
  /**
   * 用于SpEL表达式解析.
   */
  private static final SpelExpressionParser parser = new SpelExpressionParser();
  /**
   * 用于获取方法参数定义名字.
   */
  private static final LocalVariableTableParameterNameDiscoverer discoverer
    = new LocalVariableTableParameterNameDiscoverer();


  @Nullable
  public static String parseSpel(@Nonnull String spel,
                                 @Nonnull Method method,
                                 @Nullable Object[] args) {
    String[] parameterNames = discoverer.getParameterNames(method);
    StandardEvaluationContext context = new StandardEvaluationContext();
    if (parameterNames != null && args != null) {
      int length = parameterNames.length;
      if (length > 0) {
        for (int i = 0; i < length; i++) {
          context.setVariable(parameterNames[i], args[i]);
        }
      }
    }
    return parser.parseExpression(spel).getValue(context, String.class);
  }
}
