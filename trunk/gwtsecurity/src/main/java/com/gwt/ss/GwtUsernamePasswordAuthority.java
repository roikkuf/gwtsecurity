package com.gwt.ss;

import com.google.gwt.user.client.rpc.impl.AbstractSerializationStream;
import com.google.gwt.user.server.rpc.RPCServletUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.TextEscapeUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

/**
 * Monitor &lt;form-login&gt;  event.<br/>
 * When  &lt;form-login&gt; come from GWT RPC, return void message when success and
 * prone {@link  com.gwt.ss.client.GwtBadCredentialsException GwtBadCredentialsException} when failed.
 * Not effect with web page form-login.<br/>
 * 監控&lt;form-login&gt;登錄處理<br/>
 * 若使用GWT RPC進行登錄，本類別實例負責回應GWT RPC，否則交由Spring Security本身處理
 */
@Aspect
public class GwtUsernamePasswordAuthority implements ServletContextAware, InitializingBean,
        ApplicationContextAware, ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    protected static Logger logger = LoggerFactory.getLogger(GwtUsernamePasswordAuthority.class);
    private ServletContext servletContext;
    private static ThreadLocal<PayloadInfo> payloadHolder = new InheritableThreadLocal<PayloadInfo>();
    private ApplicationContext applicationContext;
    private AuthenticationManager authenticationManager;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private PayloadInfo extract(JoinPoint jp) throws IOException, ServletException {
        PayloadInfo result = payloadHolder.get();
        HttpHolder httpHolder = HttpHolder.getInstance(jp);
        HttpServletRequest request = httpHolder.getRequest();
        String username = null;
        String password = null;
        if (result == null && request != null) {
            String payload = RPCServletUtils.readContentAsGwtRpc(request);
            if (logger.isDebugEnabled()) {
                logger.debug("login payload is [" + payload + "]");
            }
            StringTokenizer st = new StringTokenizer(payload, "|");
            int idx = 0;
            while (st.hasMoreTokens()) {
                switch (++idx) {
                    case 9:
                        username = st.nextToken();
                        break;
                    case 10:
                        password = st.nextToken();
                        break;
                    default:
                        st.nextToken();
                }
            }
            if (username != null && password != null) {
                result = new PayloadInfo(username, password, httpHolder);
                payloadHolder.set(result);
            }
        }
        return result;
    }

    @Around("execution(* org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(..))")
    public Object doFilter(ProceedingJoinPoint pjp) throws Throwable {
        HttpHolder httpHolder = HttpHolder.getInstance(pjp);
        AbstractAuthenticationProcessingFilter filter = (AbstractAuthenticationProcessingFilter) pjp.getTarget();
        if (httpHolder.isGwt() && requiresAuthentication(filter, httpHolder.getRequest())) {
            PayloadInfo pi = null;
            try {
                pi = extract(pjp);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(pi.getUsername(), pi.getPassword());
                HttpSession session = httpHolder.getRequest().getSession(false);
                Class c = AbstractAuthenticationProcessingFilter.class;
                Field f = c.getDeclaredField("allowSessionCreation");
                f.setAccessible(true);
                boolean allowSessionCreation = (Boolean) f.get(filter);
                if (session != null || allowSessionCreation) {
                    httpHolder.getRequest().getSession().setAttribute(GwtResponseUtil.SPRING_SECURITY_LAST_USERNAME_KEY,
                            TextEscapeUtils.escapeEntities(pi.getUsername()));
                }
                authRequest.setDetails(filter.getAuthenticationDetailsSource().buildDetails(httpHolder.getRequest()));
                Authentication authResult = getAuthenticationManager().authenticate(authRequest);
                SecurityContextHolder.getContext().setAuthentication(authResult);
                filter.getRememberMeServices().loginSuccess(httpHolder.getRequest(), httpHolder.getResponse(), authResult);
                applicationContext.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
                return null;
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Gwt login fail:", e);
                }
                GwtResponseUtil.processGwtException(servletContext, httpHolder.getRequest(), httpHolder.getResponse(), e);
                return null;
            } finally {
                payloadHolder.remove();
            }
        } else {
            return pjp.proceed();
        }
    }

    protected boolean requiresAuthentication(AbstractAuthenticationProcessingFilter filter, HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (logger.isDebugEnabled()) {
            logger.debug("RequiresAuthentication check for \n"
                    + "url = " + uri + "\ncontext path = " + request.getContextPath() + "\nprocessUrl = " + filter.getFilterProcessesUrl());
        }
        if (uri == null || uri.isEmpty()) {
            return false;
        } else {
            int pathParamIndex = uri.indexOf(';');

            if (pathParamIndex > 0) {
                // strip everything after the first semi-colon
                uri = uri.substring(0, pathParamIndex);
            }

            if ("".equals(request.getContextPath())) {
                return uri.endsWith(filter.getFilterProcessesUrl());
            }
            return uri.endsWith(request.getContextPath() + filter.getFilterProcessesUrl());
        }
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        PayloadInfo pi = payloadHolder.get();
        if (pi != null && pi.getHttpHolder().isGwt()) {
            HttpServletRequest request = pi.getHttpHolder().getRequest();
            Authentication authResult = event.getAuthentication();
            if (logger.isDebugEnabled()) {
                logger.debug("Gwt authentication success. Updating SecurityContextHolder to contain: " + authResult);
            }
            GwtResponseUtil.writeResponse(servletContext, request, pi.getHttpHolder().getResponse(),
                    String.format("//OK[[],%d,%d]", AbstractSerializationStream.DEFAULT_FLAGS,
                    AbstractSerializationStream.SERIALIZATION_STREAM_VERSION));
        }
    }

    private class PayloadInfo {

        private String username;
        private String password;
        private HttpHolder httpHolder;

        public PayloadInfo(String username, String password, HttpHolder httpHolder) {
            this.username = username;
            this.password = password;
            this.httpHolder = httpHolder;
        }

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }

        public HttpHolder getHttpHolder() {
            return httpHolder;
        }

        public void setHttpHolder(HttpHolder httpHolder) {
            this.httpHolder = httpHolder;
        }
    }
}
