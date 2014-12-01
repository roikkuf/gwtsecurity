/**
 * $Id$
 *
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.TextEscapeUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import com.google.gwt.user.client.rpc.impl.AbstractSerializationStream;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyProvider;
import com.gwt.ss.shared.GwtConst;

/**
 * Monitor &lt;form-login&gt; event.<br/>
 * When &lt;form-login&gt; come from GWT RPC, return void message when success and prone
 * {@link com.gwt.ss.client.exceptions.GwtBadCredentialsException GwtBadCredentialsException} when failed. Not
 * effect with web page form-login.<br/>
 * Note: Certification extracting code provide by Amit Khanna<br/>
 * 監控&lt;form-login&gt;登錄處理<br/>
 * 若使用GWT RPC進行登錄，本類別實例負責回應GWT RPC，否則交由Spring Security本身處理<br/>
 * 註:取出帳號資料之程式由Amit Khanna提供
 */
@Aspect
public class GwtUsernamePasswordAuthority implements ServletContextAware, InitializingBean, ApplicationContextAware,
ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    /**
     * Provide by Amit Khanna<br/>
     * 由Amit Khanna提供.
     */
    private static class DefaultSerializationPolicyProvider implements SerializationPolicyProvider {

        /** The instance. */
        private static DefaultSerializationPolicyProvider instance = new DefaultSerializationPolicyProvider();

        /**
         * Gets the instance.
         *
         * @return the instance
         */
        public static DefaultSerializationPolicyProvider getInstance() {
            return instance;
        }

        /** {@inheritDoc} */
        @Override
        public SerializationPolicy getSerializationPolicy(final String moduleBaseURL,
                final String serializationPolicyStrongName) {
            return RPC.getDefaultSerializationPolicy();
        }
    }

    /**
     * The Class PayloadInfo.
     */
    private static class PayloadInfo {

        /** The force logout. */
        private boolean forceLogout = false;

        /** The http holder. */
        private HttpHolder httpHolder;

        /** The password. */
        private String password;

        /** The remember me. */
        private boolean rememberMe = false;

        /** The username. */
        private String username;

        /**
         * Instantiates a new payload info.
         *
         * @param username the username
         * @param password the password
         * @param httpHolder the http holder
         * @param rememberMe the remember me
         * @param forceLogout the force logout
         */
        public PayloadInfo(final String username, final String password, final HttpHolder httpHolder,
                final boolean rememberMe, final boolean forceLogout) {
            this.username = username;
            this.password = password;
            this.httpHolder = httpHolder;
            this.rememberMe = rememberMe;
            this.forceLogout = forceLogout;
        }

        /**
         * Gets the http holder.
         *
         * @return the http holder
         */
        public HttpHolder getHttpHolder() {
            return httpHolder;
        }

        /**
         * Gets the password.
         *
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Gets the username.
         *
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * Checks if is the force logout.
         *
         * @return the force logout
         */
        public boolean isForceLogout() {
            return forceLogout;
        }

        /**
         * Checks if is the remember me.
         *
         * @return the remember me
         */
        public boolean isRememberMe() {
            return rememberMe;
        }

    }

    /**
     * The Class RememberMeRequestWrapper.
     */
    private static class RememberMeRequestWrapper extends HttpServletRequestWrapper {

        /** The remember me parameter. */
        private String rememberMeParameter = "_spring_security_remember_me";

        /**
         * Instantiates a new remember me request wrapper.
         *
         * @param request the request
         * @param rememberMeParameter the remember me parameter
         */
        public RememberMeRequestWrapper(final HttpServletRequest request, final String rememberMeParameter) {
            super(request);
            if (rememberMeParameter != null && !rememberMeParameter.isEmpty()) {
                this.rememberMeParameter = rememberMeParameter;
            }
        }

        /** {@inheritDoc} */
        @Override
        public String getParameter(final String name) {
            return name.equals(rememberMeParameter) ? "true" : super.getParameter(name);
        }
    }

    /** The Constant LOG. */
    protected static final Logger LOG = LoggerFactory.getLogger(GwtUsernamePasswordAuthority.class);

    /** The payload holder. */
    private static ThreadLocal<PayloadInfo> payloadHolder = new InheritableThreadLocal<PayloadInfo>();

    /** The application context. */
    private ApplicationContext applicationContext;

    /** The authentication manager. */
    private AuthenticationManager authenticationManager;

    /** The remember me parameter. */
    private String rememberMeParameter = "_spring_security_remember_me";

    /** The serialization policy provider. */
    private SerializationPolicyProvider serializationPolicyProvider = DefaultSerializationPolicyProvider.getInstance();

    /** The servlet context. */
    private ServletContext servletContext;

    /** The suppress login error messages. */
    private boolean suppressLoginErrorMessages = false;

    /** {@inheritDoc} */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
    }

    /**
     * Do filter.
     *
     * @param pjp the pjp
     * @return the object
     * @throws Throwable the throwable
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Around("execution(* org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(..))")
    public Object doFilter(final ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder httpHolder = HttpHolder.getInstance(pjp);
        AbstractAuthenticationProcessingFilter filter = (AbstractAuthenticationProcessingFilter) pjp.getTarget();
        if (httpHolder.isGwt() && requiresAuthentication(filter, httpHolder.getRequest())) {
            PayloadInfo pi = null;
            try {
                pi = extract(pjp);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    pi.getUsername(), pi.getPassword());
                Class<AbstractAuthenticationProcessingFilter> c = AbstractAuthenticationProcessingFilter.class;
                Field f = c.getDeclaredField("allowSessionCreation");
                f.setAccessible(true);
                boolean allowSessionCreation = (Boolean) f.get(filter);
                HttpSession session = httpHolder.getRequest().getSession(allowSessionCreation);
                if (session != null) {
                    httpHolder
                    .getRequest()
                    .getSession()
                    .setAttribute(GwtConst.SPRING_SECURITY_LAST_USERNAME_KEY,
                        TextEscapeUtils.escapeEntities(pi.getUsername()));
                }
                f = c.getDeclaredField("authenticationDetailsSource");
                f.setAccessible(true);
                authRequest.setDetails(((AuthenticationDetailsSource) f.get(filter)).buildDetails(httpHolder
                    .getRequest()));
                Authentication authResult = getAuthenticationManager().authenticate(authRequest);

                // Authentication was successful. Now make sure we haven't
                // violated the session authentication
                // strategy.
                f = c.getDeclaredField("sessionStrategy");
                f.setAccessible(true);
                SessionAuthenticationStrategy sessionStrategy = (SessionAuthenticationStrategy) f.get(filter);
                try {
                    sessionStrategy.onAuthentication(authResult, httpHolder.getRequest(), httpHolder.getResponse());
                } catch (SessionAuthenticationException e) {
                    if (pi.isForceLogout()) {
                        SessionRegistry registry = applicationContext.getBean(SessionRegistry.class);
                        if (registry != null) {
                            for (SessionInformation info : registry.getAllSessions(authResult.getPrincipal(), true)) {
                                info.expireNow();
                            }
                        }
                        sessionStrategy.onAuthentication(authResult, httpHolder.getRequest(), httpHolder.getResponse());
                    } else {
                        throw e;
                    }
                }

                // Everything looks good. Publish the authentication result.
                SecurityContextHolder.getContext().setAuthentication(authResult);
                filter.getRememberMeServices().loginSuccess(
                    pi.isRememberMe() ? new RememberMeRequestWrapper(httpHolder.getRequest(), rememberMeParameter)
                    : httpHolder.getRequest(), httpHolder.getResponse(), authResult);
                applicationContext.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
                return null;
            } catch (Exception e) {
                if (LOG.isErrorEnabled() && !isSuppressLoginErrorMessages()) {
                    LOG.error("Gwt login fail:", e);
                }
                GwtResponseUtil.processGwtException(servletContext, httpHolder.getRequest(), httpHolder.getResponse(),
                    e);
                return null;
            } finally {
                payloadHolder.remove();
            }
        } else {
            return pjp.proceed();
        }
    }

    private static final int FORCE_LOGOUT_POSITION = 3;

    /**
     * Provided by Amit Khanna.<br/>
     * 由Amit Khanna提供
     *
     * @param jp the jp
     * @return the payload info
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    private PayloadInfo extract(final JoinPoint jp) throws IOException, ServletException {
        PayloadInfo result = payloadHolder.get();
        HttpHolder httpHolder = HttpHolder.getInstance(jp);
        HttpServletRequest request = httpHolder.getRequest();
        String username = null;
        String password = null;
        boolean rememberMe = false;
        boolean forceLogout = false;
        if (result == null && request != null) {
            String payload = RPCServletUtils.readContentAsGwtRpc(request);
            RPCRequest rpcRequest = RPC.decodeRequest(payload, null, serializationPolicyProvider);
            Object[] requestParams = rpcRequest.getParameters();
            assert requestParams.length > 1 : "parameter count incorrect";
            username = (String) requestParams[0];
            password = (String) requestParams[1];
            if (requestParams.length > 2) {
                try {
                    rememberMe = (Boolean) requestParams[2];
                } catch (Exception e) {
                    LOG.debug(e.getMessage(), e);
                }
            }
            if (requestParams.length > FORCE_LOGOUT_POSITION) {
                try {
                    forceLogout = (Boolean) requestParams[FORCE_LOGOUT_POSITION];
                } catch (Exception e) {
                    LOG.debug(e.getMessage(), e);
                }
            }
            if (username != null && password != null) {
                result = new PayloadInfo(username, password, httpHolder, rememberMe, forceLogout);
                payloadHolder.set(result);
            }
        }
        return result;
    }

    /**
     * Gets the authentication manager.
     *
     * @return the authentication manager
     */
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    /**
     * Gets the remember me parameter.
     *
     * @return the remember me parameter
     */
    public String getRememberMeParameter() {
        return rememberMeParameter;
    }

    /**
     * Checks if is the suppress login error messages.
     *
     * @return the suppress login error messages
     */
    public boolean isSuppressLoginErrorMessages() {
        return suppressLoginErrorMessages;
    }

    /** {@inheritDoc} */
    @Override
    public void onApplicationEvent(final InteractiveAuthenticationSuccessEvent event) {
        PayloadInfo pi = payloadHolder.get();
        if (pi != null && pi.getHttpHolder().isGwt()) {
            HttpServletRequest request = pi.getHttpHolder().getRequest();
            Authentication authResult = event.getAuthentication();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Gwt authentication success. Updating SecurityContextHolder to contain: " + authResult);
            }
            GwtResponseUtil.writeResponse(servletContext, request, pi.getHttpHolder().getResponse(), String.format(
                "//OK[[],%s,%s]", AbstractSerializationStream.DEFAULT_FLAGS,
                AbstractSerializationStream.SERIALIZATION_STREAM_VERSION));
        }
    }

    /**
     * Requires authentication.
     *
     * @param filter the filter
     * @param request the request
     * @return true, if successful
     */
    protected boolean requiresAuthentication(final AbstractAuthenticationProcessingFilter filter,
            final HttpServletRequest request) {
        try {
            Class<AbstractAuthenticationProcessingFilter> c = AbstractAuthenticationProcessingFilter.class;
            Field f = c.getDeclaredField("requiresAuthenticationRequestMatcher");
            f.setAccessible(true);
            RequestMatcher matcher = (RequestMatcher) f.get(filter);
            return matcher.matches(request);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Sets the authentication manager.
     *
     * @param authenticationManager the new authentication manager
     */
    public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Sets the remember me parameter.
     *
     * @param rememberMeParameter the new remember me parameter
     */
    public void setRememberMeParameter(final String rememberMeParameter) {
        this.rememberMeParameter = rememberMeParameter;
    }

    /** {@inheritDoc} */
    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Sets the suppress login error messages.
     *
     * @param suppressLoginErrorMessages the new suppress login error messages
     */
    public void setSuppressLoginErrorMessages(final boolean suppressLoginErrorMessages) {
        this.suppressLoginErrorMessages = suppressLoginErrorMessages;
    }

}
