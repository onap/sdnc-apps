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
package org.onap.sdnc.apps.pomba.networkdiscovery.unittest.service;

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
        // TODO Implement getAttribute
        throw new UnsupportedOperationException("getAttribute");
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        // TODO Implement getAttributeNames
        throw new UnsupportedOperationException("getAttributeNames");
    }

    @Override
    public String getCharacterEncoding() {
        // TODO Implement getCharacterEncoding
        throw new UnsupportedOperationException("getCharacterEncoding");
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        // TODO Implement setCharacterEncoding
        throw new UnsupportedOperationException("setCharacterEncoding");
    }

    @Override
    public int getContentLength() {
        // TODO Implement getContentLength
        throw new UnsupportedOperationException("getContentLength");
    }

    @Override
    public long getContentLengthLong() {
        // TODO Implement getContentLengthLong
        throw new UnsupportedOperationException("getContentLengthLong");
    }

    @Override
    public String getContentType() {
        // TODO Implement getContentType
        throw new UnsupportedOperationException("getContentType");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // TODO Implement getInputStream
        throw new UnsupportedOperationException("getInputStream");
    }

    @Override
    public String getParameter(String name) {
        // TODO Implement getParameter
        throw new UnsupportedOperationException("getParameter");
    }

    @Override
    public Enumeration<String> getParameterNames() {
        // TODO Implement getParameterNames
        throw new UnsupportedOperationException("getParameterNames");
    }

    @Override
    public String[] getParameterValues(String name) {
        // TODO Implement getParameterValues
        throw new UnsupportedOperationException("getParameterValues");
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        // TODO Implement getParameterMap
        throw new UnsupportedOperationException("getParameterMap");
    }

    @Override
    public String getProtocol() {
        // TODO Implement getProtocol
        throw new UnsupportedOperationException("getProtocol");
    }

    @Override
    public String getScheme() {
        // TODO Implement getScheme
        throw new UnsupportedOperationException("getScheme");
    }

    @Override
    public int getServerPort() {
        // TODO Implement getServerPort
        throw new UnsupportedOperationException("getServerPort");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        // TODO Implement getReader
        throw new UnsupportedOperationException("getReader");
    }

    @Override
    public String getRemoteHost() {
        // TODO Implement getRemoteHost
        throw new UnsupportedOperationException("getRemoteHost");
    }

    @Override
    public void setAttribute(String name, Object obj) {
        // TODO Implement setAttribute
        throw new UnsupportedOperationException("setAttribute");
    }

    @Override
    public void removeAttribute(String name) {
        // TODO Implement removeAttribute
        throw new UnsupportedOperationException("removeAttribute");
    }

    @Override
    public Locale getLocale() {
        // TODO Implement getLocale
        throw new UnsupportedOperationException("getLocale");
    }

    @Override
    public Enumeration<Locale> getLocales() {
        // TODO Implement getLocales
        throw new UnsupportedOperationException("getLocales");
    }

    @Override
    public boolean isSecure() {
        // TODO Implement isSecure
        throw new UnsupportedOperationException("isSecure");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        // TODO Implement getRequestDispatcher
        throw new UnsupportedOperationException("getRequestDispatcher");
    }

    @Override
    public String getRealPath(String path) {
        // TODO Implement getRealPath
        throw new UnsupportedOperationException("getRealPath");
    }

    @Override
    public int getRemotePort() {
        // TODO Implement getRemotePort
        throw new UnsupportedOperationException("getRemotePort");
    }

    @Override
    public String getLocalName() {
        // TODO Implement getLocalName
        throw new UnsupportedOperationException("getLocalName");
    }

    @Override
    public String getLocalAddr() {
        // TODO Implement getLocalAddr
        throw new UnsupportedOperationException("getLocalAddr");
    }

    @Override
    public int getLocalPort() {
        // TODO Implement getLocalPort
        throw new UnsupportedOperationException("getLocalPort");
    }

    @Override
    public ServletContext getServletContext() {
        // TODO Implement getServletContext
        throw new UnsupportedOperationException("getServletContext");
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        // TODO Implement startAsync
        throw new UnsupportedOperationException("startAsync");
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IllegalStateException {
        // TODO Implement startAsync
        throw new UnsupportedOperationException("startAsync");
    }

    @Override
    public boolean isAsyncStarted() {
        // TODO Implement isAsyncStarted
        throw new UnsupportedOperationException("isAsyncStarted");
    }

    @Override
    public boolean isAsyncSupported() {
        // TODO Implement isAsyncSupported
        throw new UnsupportedOperationException("isAsyncSupported");
    }

    @Override
    public AsyncContext getAsyncContext() {
        // TODO Implement getAsyncContext
        throw new UnsupportedOperationException("getAsyncContext");
    }

    @Override
    public DispatcherType getDispatcherType() {
        // TODO Implement getDispatcherType
        throw new UnsupportedOperationException("getDispatcherType");
    }

    @Override
    public String getAuthType() {
        // TODO Implement getAuthType
        throw new UnsupportedOperationException("getAuthType");
    }

    @Override
    public Cookie[] getCookies() {
        // TODO Implement getCookies
        throw new UnsupportedOperationException("getCookies");
    }

    @Override
    public long getDateHeader(String name) {
        // TODO Implement getDateHeader
        throw new UnsupportedOperationException("getDateHeader");
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        // TODO Implement getHeaders
        throw new UnsupportedOperationException("getHeaders");
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        // TODO Implement getHeaderNames
        throw new UnsupportedOperationException("getHeaderNames");
    }

    @Override
    public int getIntHeader(String name) {
        // TODO Implement getIntHeader
        throw new UnsupportedOperationException("getIntHeader");
    }

    @Override
    public String getMethod() {
        // TODO Implement getMethod
        throw new UnsupportedOperationException("getMethod");
    }

    @Override
    public String getPathInfo() {
        // TODO Implement getPathInfo
        throw new UnsupportedOperationException("getPathInfo");
    }

    @Override
    public String getPathTranslated() {
        // TODO Implement getPathTranslated
        throw new UnsupportedOperationException("getPathTranslated");
    }

    @Override
    public String getContextPath() {
        // TODO Implement getContextPath
        throw new UnsupportedOperationException("getContextPath");
    }

    @Override
    public String getQueryString() {
        // TODO Implement getQueryString
        throw new UnsupportedOperationException("getQueryString");
    }

    @Override
    public String getRemoteUser() {
        // TODO Implement getRemoteUser
        throw new UnsupportedOperationException("getRemoteUser");
    }

    @Override
    public boolean isUserInRole(String role) {
        // TODO Implement isUserInRole
        throw new UnsupportedOperationException("isUserInRole");
    }

    @Override
    public Principal getUserPrincipal() {
        // TODO Implement getUserPrincipal
        throw new UnsupportedOperationException("getUserPrincipal");
    }

    @Override
    public String getRequestedSessionId() {
        // TODO Implement getRequestedSessionId
        throw new UnsupportedOperationException("getRequestedSessionId");
    }

    @Override
    public StringBuffer getRequestURL() {
        // TODO Implement getRequestURL
        throw new UnsupportedOperationException("getRequestURL");
    }

    @Override
    public String getServletPath() {
        // TODO Implement getServletPath
        throw new UnsupportedOperationException("getServletPath");
    }

    @Override
    public HttpSession getSession(boolean create) {
        // TODO Implement getSession
        throw new UnsupportedOperationException("getSession");
    }

    @Override
    public HttpSession getSession() {
        // TODO Implement getSession
        throw new UnsupportedOperationException("getSession");
    }

    @Override
    public String changeSessionId() {
        // TODO Implement changeSessionId
        throw new UnsupportedOperationException("changeSessionId");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        // TODO Implement isRequestedSessionIdValid
        throw new UnsupportedOperationException("isRequestedSessionIdValid");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        // TODO Implement isRequestedSessionIdFromCookie
        throw new UnsupportedOperationException("isRequestedSessionIdFromCookie");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        // TODO Implement isRequestedSessionIdFromURL
        throw new UnsupportedOperationException("isRequestedSessionIdFromURL");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        // TODO Implement isRequestedSessionIdFromUrl
        throw new UnsupportedOperationException("isRequestedSessionIdFromUrl");
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        // TODO Implement authenticate
        throw new UnsupportedOperationException("authenticate");
    }

    @Override
    public void login(String username, String password) throws ServletException {
        // TODO Implement login
        throw new UnsupportedOperationException("login");
    }

    @Override
    public void logout() throws ServletException {
        // TODO Implement logout
        throw new UnsupportedOperationException("logout");
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        // TODO Implement getParts
        throw new UnsupportedOperationException("getParts");
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        // TODO Implement getPart
        throw new UnsupportedOperationException("getPart");
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass)
            throws IOException, ServletException {
        // TODO Implement upgrade
        throw new UnsupportedOperationException("upgrade");
    }
    /////////////////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // Class variables
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // Instance variables
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // Constructors
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // Public methods
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // [interface name] implementation
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // [super class] override methods
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // Package protected methods
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // Protected methods
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // Private methods
    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    // Inner classes
    /////////////////////////////////////////////////////////////////////////////
}
