/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.cn.anubis.filter;

import org.apache.fineract.cn.api.util.UserContextHolder;
import org.apache.fineract.cn.lang.config.Health;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Myrle Krantz
 */
public class UserContextFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain) throws ServletException, IOException {

    if (request.getRequestURI().contains(Health.HEALTH_URL_CONTEXT_PATH)) {
      filterChain.doFilter(request, response);
      return;
    }

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    final String principalName = authentication.getName();
    final Object credentials = authentication.getCredentials();

    if (principalName != null && credentials != null) {
      UserContextHolder.setAccessToken(principalName, credentials.toString());
    }

    filterChain.doFilter(request, response);
  }
}
