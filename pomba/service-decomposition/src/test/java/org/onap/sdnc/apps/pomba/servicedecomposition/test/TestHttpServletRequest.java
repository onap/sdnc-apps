/*
 *  ============LICENSE_START===================================================
 * Copyright (c) 2018 Amdocs
 * ============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=====================================================
 */

package org.onap.sdnc.apps.pomba.servicedecomposition.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

public class TestHttpServletRequest implements HttpServletRequest {
    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return "localhost";
    }

    @Override
    public String getServerName() {
        return "localhost";
    }

    @Override
    public String getRequestURI() {
        return "/test";
    }


    @Override
    public Object getAttribute(String name) {
        throw new UnsupportedOperationException("getAttribute");
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException("getAttributeNames");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("getCharacterEncoding");
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("setCharacterEncoding");
    }

    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException("getContentLength");
    }

    @Override
    public long getContentLengthLong() {
        throw new UnsupportedOperationException("getContentLengthLong");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("getContentType");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException("getInputStream");
    }

    @Override
    public String getParameter(String name) {
        throw new UnsupportedOperationException("getParameter");
    }

    @Override
    public Enumeration<String> getParameterNames() {
        throw new UnsupportedOperationException("getParameterNames");
    }

    @Override
    public String[] getParameterValues(String name) {
        throw new UnsupportedOperationException("getParameterValues");
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        throw new UnsupportedOperationException("getParameterMap");
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("getProtocol");
    }

    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("getScheme");
    }

    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("getServerPort");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("getReader");
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException("getRemoteHost");
    }

    @Override
    public void setAttribute(String name, Object obj) {
        throw new UnsupportedOperationException("setAttribute");
    }

    @Override
    public void removeAttribute(String name) {
        throw new UnsupportedOperationException("removeAttribute");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("getLocale");
    }

    @Override
    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException("getLocales");
    }

    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException("isSecure");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException("getRequestDispatcher");
    }

    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException("getRealPath");
    }

    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException("getRemotePort");
    }

    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException("getLocalName");
    }

    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException("getLocalAddr");
    }

    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("getLocalPort");
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException("getServletContext");
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new UnsupportedOperationException("startAsync");
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IllegalStateException {
        throw new UnsupportedOperationException("startAsync");
    }

    @Override
    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException("isAsyncStarted");
    }

    @Override
    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException("isAsyncSupported");
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException("getAsyncContext");
    }

    @Override
    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException("getDispatcherType");
    }

    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException("getAuthType");
    }

    @Override
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException("getCookies");
    }

    @Override
    public long getDateHeader(String name) {
        throw new UnsupportedOperationException("getDateHeader");
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        throw new UnsupportedOperationException("getHeaders");
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        throw new UnsupportedOperationException("getHeaderNames");
    }

    @Override
    public int getIntHeader(String name) {
        throw new UnsupportedOperationException("getIntHeader");
    }

    @Override
    public String getMethod() {
        throw new UnsupportedOperationException("getMethod");
    }

    @Override
    public String getPathInfo() {
        throw new UnsupportedOperationException("getPathInfo");
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException("getPathTranslated");
    }

    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException("getContextPath");
    }

    @Override
    public String getQueryString() {
        throw new UnsupportedOperationException("getQueryString");
    }

    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("getRemoteUser");
    }

    @Override
    public boolean isUserInRole(String role) {
        throw new UnsupportedOperationException("isUserInRole");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("getUserPrincipal");
    }

    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("getRequestedSessionId");
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException("getRequestURL");
    }

    @Override
    public String getServletPath() {
        throw new UnsupportedOperationException("getServletPath");
    }

    @Override
    public HttpSession getSession(boolean create) {
        throw new UnsupportedOperationException("getSession");
    }

    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("getSession");
    }

    @Override
    public String changeSessionId() {
        throw new UnsupportedOperationException("changeSessionId");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException("isRequestedSessionIdValid");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException("isRequestedSessionIdFromCookie");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException("isRequestedSessionIdFromURL");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException("isRequestedSessionIdFromUrl");
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        throw new UnsupportedOperationException("authenticate");
    }

    @Override
    public void login(String username, String password) throws ServletException {
        throw new UnsupportedOperationException("login");
    }

    @Override
    public void logout() throws ServletException {
        throw new UnsupportedOperationException("logout");
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        throw new UnsupportedOperationException("getParts");
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        throw new UnsupportedOperationException("getPart");
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass)
            throws IOException, ServletException {
        throw new UnsupportedOperationException("upgrade");
    }
}
