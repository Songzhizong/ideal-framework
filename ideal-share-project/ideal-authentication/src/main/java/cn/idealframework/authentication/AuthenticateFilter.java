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
package cn.idealframework.authentication;

import cn.idealframework.json.JsonUtils;
import cn.idealframework.lang.StringUtils;
import cn.idealframework.trace.TraceContextHolder;
import cn.idealframework.transmission.ResMsg;
import cn.idealframework.transmission.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Nonnull;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/6/23
 */
@CommonsLog
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class AuthenticateFilter implements Filter {
  /** 签名类型请求头 */
  public static final String SIGNER_TYPE_HEADER_NAME = "Ideal-Signer-Type";
  private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
  private static final String DEFAULT_SIGNER_TYPE = BasicAuthenticator.SIGNER_TYPE;
  private final Set<String> patterns;
  private final AuthenticatorRegistry authenticatorRegistry;

  @Override
  public void doFilter(@Nonnull ServletRequest request,
                       @Nonnull ServletResponse response,
                       @Nonnull FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String requestURI = httpServletRequest.getRequestURI();
    for (String pattern : patterns) {
      boolean match = PATH_MATCHER.match(pattern, requestURI);
      if (match) {
        String signerType = httpServletRequest.getHeader(SIGNER_TYPE_HEADER_NAME);
        if (StringUtils.isBlank(signerType)) {
          signerType = DEFAULT_SIGNER_TYPE;
        }
        Optional<Authenticator> authenticatorOpt = authenticatorRegistry.get(signerType);
        if (!authenticatorOpt.isPresent()) {
          log.info("Invalid signer type: " + signerType);
          writeUnauthorizedRes((HttpServletResponse) response, "Invalid signer type");
          return;
        }
        Authenticator authenticator = authenticatorOpt.get();
        try {
          authenticator.authenticate(httpServletRequest);
          break;
        } catch (AuthenticateException e) {
          writeUnauthorizedRes((HttpServletResponse) response, e.message());
          return;
        }
      }
    }
    chain.doFilter(request, response);
  }

  private void writeUnauthorizedRes(@Nonnull HttpServletResponse response,
                                    @Nonnull String message) throws IOException {
    int code = ResMsg.UNAUTHORIZED.code();
    Result<Void> result = Result.message(false, code, message);
    TraceContextHolder.current().ifPresent(context -> result.setTraceId(context.getTraceId()));
    response.setStatus(401);
    response.setContentType("application/json; charset=UTF-8");
    try (PrintWriter writer = response.getWriter()) {
      writer.write(JsonUtils.toJsonString(result));
      writer.flush();
    }
  }
}
