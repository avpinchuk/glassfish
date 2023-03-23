/*
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.enterprise.web;

import com.sun.enterprise.deployment.EjbReferenceDescriptor;
import com.sun.enterprise.deployment.EnvironmentProperty;
import com.sun.enterprise.deployment.ErrorPageDescriptor;
import com.sun.enterprise.deployment.JspConfigDefinitionDescriptor;
import com.sun.enterprise.deployment.LocaleEncodingMappingDescriptor;
import com.sun.enterprise.deployment.LocaleEncodingMappingListDescriptor;
import com.sun.enterprise.deployment.MessageDestinationDescriptor;
import com.sun.enterprise.deployment.MessageDestinationReferenceDescriptor;
import com.sun.enterprise.deployment.ResourceReferenceDescriptor;
import com.sun.enterprise.deployment.WebBundleDescriptor;
import com.sun.enterprise.deployment.WebComponentDescriptor;
import com.sun.enterprise.deployment.types.EjbReference;
import com.sun.enterprise.deployment.web.AppListenerDescriptor;
import com.sun.enterprise.deployment.web.ContextParameter;
import com.sun.enterprise.deployment.web.CookieConfig;
import com.sun.enterprise.deployment.web.InitializationParameter;
import com.sun.enterprise.deployment.web.LoginConfiguration;
import com.sun.enterprise.deployment.web.MimeMapping;
import com.sun.enterprise.deployment.web.MultipartConfig;
import com.sun.enterprise.deployment.web.SecurityConstraint;
import com.sun.enterprise.deployment.web.SecurityRoleReference;
import com.sun.enterprise.deployment.web.ServletFilter;
import com.sun.enterprise.deployment.web.ServletFilterMapping;
import com.sun.enterprise.deployment.web.SessionConfig;
import com.sun.enterprise.deployment.web.WebResourceCollection;
import com.sun.enterprise.web.deploy.ContextEjbDecorator;
import com.sun.enterprise.web.deploy.ContextEnvironmentDecorator;
import com.sun.enterprise.web.deploy.ContextLocalEjbDecorator;
import com.sun.enterprise.web.deploy.ContextResourceDecorator;
import com.sun.enterprise.web.deploy.ErrorPageDecorator;
import com.sun.enterprise.web.deploy.FilterDefDecorator;
import com.sun.enterprise.web.deploy.LoginConfigDecorator;
import com.sun.enterprise.web.deploy.MessageDestinationDecorator;
import com.sun.enterprise.web.deploy.MessageDestinationRefDecorator;
import com.sun.enterprise.web.deploy.SecurityCollectionDecorator;
import com.sun.enterprise.web.deploy.SecurityConstraintDecorator;
import com.sun.enterprise.web.session.WebSessionCookieConfig;

import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.descriptor.JspPropertyGroupDescriptor;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.catalina.Container;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardWrapper;
import org.glassfish.security.common.Role;
import org.glassfish.web.LogFacade;
import org.glassfish.web.deployment.descriptor.WebBundleDescriptorImpl;

/**
 * This class decorates all <code>com.sun.enterprise.deployment.*</code>
 * objects in order to make them usuable by the Catalina container.
 * This avoid having duplicate memory representation of the web.xml (as well
 * as parsing the web.xml twice)
 *
 * @author Jean-Francois Arcand
 */
public class TomcatDeploymentConfig {

    private static final Logger logger = LogFacade.getLogger();


    /**
     * Configure a <code>WebModule</code> by applying web.xml information
     * contained in <code>WebBundleDescriptor</code>. This astatic void calling
     * Tomcat 5 internal deployment mechanism by re-using the DOL objects.
     */
    public static void configureWebModule(WebModule webModule, WebBundleDescriptorImpl webModuleDescriptor)
        throws LifecycleException {
        logger.log(Level.FINEST, "configureWebModule(webModule={0}, webModuleDescriptor.class={1})",
            new Object[] {webModule, webModuleDescriptor.getClass()});
        webModule.setDisplayName(webModuleDescriptor.getDisplayName());
        webModule.setDistributable(webModuleDescriptor.isDistributable());
        webModule.setReplaceWelcomeFiles(true);
        configureStandardContext(webModule,webModuleDescriptor);
        configureContextParam(webModule,webModuleDescriptor);
        configureApplicationListener(webModule,webModuleDescriptor);
        configureEjbReference(webModule,webModuleDescriptor);
        configureContextEnvironment(webModule,webModuleDescriptor);
        configureErrorPage(webModule,webModuleDescriptor);
        configureFilterDef(webModule,webModuleDescriptor);
        configureFilterMap(webModule,webModuleDescriptor);
        configureLoginConfig(webModule,webModuleDescriptor);
        configureMimeMapping(webModule,webModuleDescriptor);
        configureResourceRef(webModule,webModuleDescriptor);
        configureMessageDestination(webModule,webModuleDescriptor);
        configureContextResource(webModule,webModuleDescriptor);
        configureSecurityConstraint(webModule,webModuleDescriptor);
        configureJspConfig(webModule,webModuleDescriptor);
        configureSecurityRoles(webModule, webModuleDescriptor);
    }


    /**
     * Configures EJB resource reference for a web application, as
     * represented in a <code>&lt;ejb-ref&gt;</code> and
     * <code>&lt;ejb-local-ref&gt;</code>element in the
     * deployment descriptor.
     */
    protected static void configureEjbReference(WebModule webModule, WebBundleDescriptorImpl wmd) {
        for (EjbReference ejbDescriptor : wmd.getEjbReferenceDescriptors()) {
            if (ejbDescriptor.isLocal()) {
                configureContextLocalEjb(webModule, (EjbReferenceDescriptor) ejbDescriptor);
            } else {
                configureContextEjb(webModule, (EjbReferenceDescriptor) ejbDescriptor);
            }
        }
    }


    /**
     * Configures EJB resource reference for a web application, as
     * represented in a <code>&lt;ejb-ref&gt;</code> in the
     * deployment descriptor.
     */
    protected static void configureContextLocalEjb(WebModule webModule, EjbReferenceDescriptor ejbDescriptor) {
        ContextLocalEjbDecorator decorator = new ContextLocalEjbDecorator(ejbDescriptor);
        webModule.addLocalEjb(decorator);

    }


    /**
     * Configures EJB resource reference for a web application, as
     * represented in a <code>&lt;ejb-local-ref&gt;</code>element in the
     * deployment descriptor.
     */
    protected static void configureContextEjb(WebModule webModule, EjbReferenceDescriptor ejbDescriptor) {
        ContextEjbDecorator decorator = new ContextEjbDecorator(ejbDescriptor);
        webModule.addEjb(decorator);
    }


    /**
     * Configure application environment entry, as represented by
     * an <code>&lt;env-entry&gt;</code> element in the deployment descriptor.
     */
    protected static void configureContextEnvironment(WebModule webModule, WebBundleDescriptorImpl wmd) {
        for (ContextParameter envRef : wmd.getContextParameters()) {
            webModule.addEnvironment(new ContextEnvironmentDecorator((EnvironmentProperty) envRef));
        }
    }


    /**
     * Configure error page element for a web application,
     * as represented by a <code>&lt;error-page&gt;</code> element in the
     * deployment descriptor.
     */
    protected static void configureErrorPage(WebModule webModule, WebBundleDescriptorImpl wmd) {
        for (ErrorPageDescriptor descriptor : wmd.getErrorPageDescriptors()) {
            webModule.addErrorPage(new ErrorPageDecorator(descriptor));
        }
    }


    /**
     * Configure filter definition for a web application, as represented
     * by a <code>&lt;filter&gt;</code> element in the deployment descriptor.
     */
    protected static void configureFilterDef(WebModule webModule, WebBundleDescriptorImpl wmd) {
        List<ServletFilter> filters = wmd.getServletFilters();
        for (ServletFilter servletFilter : filters) {
            FilterDefDecorator filterDef = new FilterDefDecorator(servletFilter);
            webModule.addFilterDef(filterDef);
        }
    }


    /**
     * Configure filter mapping for a web application, as represented
     * by a <code>&lt;filter-mapping&gt;</code> element in the deployment
     * descriptor. Each filter mapping must contain a filter name plus either
     * a URL pattern or a servlet name.
     */
    protected static void configureFilterMap(WebModule webModule, WebBundleDescriptorImpl wmd) {
        List<ServletFilterMapping> mappings = wmd.getServletFilterMappings();
        for (ServletFilterMapping mapping : mappings) {
            webModule.addFilterMap(mapping);
        }
    }


    /**
     * Configure context initialization parameter that is configured
     * in the server configuration file, rather than the application deployment
     * descriptor.  This is convenient for establishing default values (which
     * may be configured to allow application overrides or not) without having
     * to modify the application deployment descriptor itself.
     */
    protected static void configureApplicationListener(WebModule webModule, WebBundleDescriptorImpl wmd) {
        List<AppListenerDescriptor> listeners = wmd.getAppListenersCopy();
        for (AppListenerDescriptor listener : listeners) {
            webModule.addApplicationListener(listener.getListener());
        }
    }


    /**
     * Configure <code>jsp-config</code> element contained in the deployment descriptor
     */
    protected static void configureJspConfig(WebModule webModule, WebBundleDescriptorImpl wmd) {
        webModule.setJspConfigDescriptor(wmd.getJspConfigDescriptor());

        JspConfigDefinitionDescriptor jspConfig = wmd.getJspConfigDescriptor();
        if (jspConfig != null) {
            for (JspPropertyGroupDescriptor jspGroup : jspConfig.getJspPropertyGroups()) {
                for (String urlPattern : jspGroup.getUrlPatterns()) {
                    webModule.addJspMapping(urlPattern);
                }
            }
        }
    }


    /**
     * Configure a login configuration element for a web application,
     * as represented by a <code>&lt;login-config&gt;</code> element in the
     * deployment descriptor.
     */
    protected static void configureLoginConfig(WebModule webModule,
                                               WebBundleDescriptorImpl wmd) {
        LoginConfiguration loginConf = wmd.getLoginConfiguration();
        if ( loginConf == null ){
            return;
        }

        LoginConfigDecorator decorator = new LoginConfigDecorator(loginConf);
        webModule.setLoginConfig(decorator);
    }


    /**
     * Configure mime-mapping defined in the deployment descriptor.
     */
    protected static void configureMimeMapping(WebModule webModule, WebBundleDescriptorImpl wmd) {
        for (MimeMapping mimeMapping : wmd.getMimeMappings()) {
            webModule.addMimeMapping(mimeMapping.getExtension(), mimeMapping.getMimeType());
        }
    }


    /**
     * Configure resource-reference defined in the deployment descriptor.
     */
    protected static void configureResourceRef(WebModule webModule, WebBundleDescriptorImpl wmd) {
        for (EnvironmentProperty envEntry : wmd.getEnvironmentProperties()) {
            webModule.addResourceEnvRef(envEntry.getName(), envEntry.getType());
        }
    }


    /**
     * Configure context parameter defined in the deployment descriptor.
     */
    protected static void configureContextParam(WebModule webModule, WebBundleDescriptorImpl wmd) {
        for (ContextParameter ctxParam : wmd.getContextParameters()) {
            if ("com.sun.faces.injectionProvider".equals(ctxParam.getName())
                && "com.sun.faces.vendor.GlassFishInjectionProvider".equals(ctxParam.getValue())) {
                // Ignore, see IT 9641
                continue;
            }
            webModule.addParameter(ctxParam.getName(), ctxParam.getValue());
        }
    }


    /**
     * Configure of a message destination for a web application, as
     * represented in a <code>&lt;message-destination&gt;</code> element
     * in the deployment descriptor.
     */
    protected static void configureMessageDestination(WebModule webModule, WebBundleDescriptorImpl wmd) {
        for (MessageDestinationDescriptor msgDrd : wmd.getMessageDestinations()) {
            webModule.addMessageDestination(new MessageDestinationDecorator(msgDrd));
        }
    }


    /**
     * Representation of a message destination reference for a web application,
     * as represented by a <code>&lt;message-destination-ref&gt;</code> element
     * in the deployment descriptor.
     */
    protected static void configureMessageDestinationRef(
            WebModule webModule, WebBundleDescriptorImpl wmd) {
        for (MessageDestinationReferenceDescriptor msgDrd :
                wmd.getMessageDestinationReferenceDescriptors()) {
            webModule.addMessageDestinationRef(
                new MessageDestinationRefDecorator(msgDrd));
        }
    }


    /**
     * Configure a resource reference for a web application, as
     * represented in a <code>&lt;resource-ref&gt;</code> element in the
     * deployment descriptor.
     */
    protected static void configureContextResource(WebModule webModule,
                                                   WebBundleDescriptorImpl wmd) {
        for (ResourceReferenceDescriptor resRefDesc :
                wmd.getResourceReferenceDescriptors()) {
            webModule.addResource(new ContextResourceDecorator(resRefDesc));
        }
    }


    /**
     * Configure the <code>WebModule</code> instance by creating
     * <code>StandardWrapper</code> using the information contained
     * in the deployment descriptor (Welcome Files, JSP, Servlets etc.)
     */
    protected static void configureStandardContext(WebModule webModule,
                                                   WebBundleDescriptorImpl wmd) {
        StandardWrapper wrapper;
        SecurityRoleReference securityRoleReference;
        for (WebComponentDescriptor webComponentDesc : wmd.getWebComponentDescriptors()) {
            if (!webComponentDesc.isEnabled()) {
                continue;
            }

            wrapper = (StandardWrapper)webModule.createWrapper();
            wrapper.setName(webComponentDesc.getCanonicalName());

            String impl = webComponentDesc.getWebComponentImplementation();
            if (impl != null && !impl.isEmpty()) {
                if (webComponentDesc.isServlet()){
                    wrapper.setServletClassName(impl);
                } else {
                    wrapper.setJspFile(impl);
                }
            }

            /*
             * Add the wrapper only after we have set its
             * servletClassName, so we know whether we're dealing with
             * a JSF app
             */
            webModule.addChild(wrapper);

            Enumeration<InitializationParameter> initParams = webComponentDesc.getInitializationParameters();
            InitializationParameter initP = null;
            while (initParams.hasMoreElements()) {
                initP = initParams.nextElement();
                wrapper.addInitParameter(initP.getName(), initP.getValue());
            }

            if (webComponentDesc.getLoadOnStartUp() != null) {
                wrapper.setLoadOnStartup(webComponentDesc.getLoadOnStartUp());
            }
            if (webComponentDesc.isAsyncSupported() != null) {
                wrapper.setIsAsyncSupported(webComponentDesc.isAsyncSupported());
            }

            if (webComponentDesc.getRunAsIdentity() != null) {
                wrapper.setRunAs(webComponentDesc.getRunAsIdentity().getRoleName());
            }

            for (String pattern : webComponentDesc.getUrlPatternsSet()) {
                webModule.addServletMapping(pattern,
                    webComponentDesc.getCanonicalName());
            }

            Enumeration<SecurityRoleReference> enumeration = webComponentDesc.getSecurityRoleReferences();
            while (enumeration.hasMoreElements()){
                securityRoleReference = enumeration.nextElement();
                wrapper.addSecurityReference(
                    securityRoleReference.getRoleName(),
                    securityRoleReference.getSecurityRoleLink().getName());
            }

            MultipartConfig mpConfig = webComponentDesc.getMultipartConfig();
            if (mpConfig != null) {
                wrapper.setMultipartLocation(mpConfig.getLocation());
                wrapper.setMultipartMaxFileSize(mpConfig.getMaxFileSize());
                wrapper.setMultipartMaxRequestSize(mpConfig.getMaxRequestSize());
                wrapper.setMultipartFileSizeThreshold(mpConfig.getFileSizeThreshold());
            }
        }

        SessionConfig sessionConfig = wmd.getSessionConfig();

        // <session-config><session-timeout>
        webModule.setSessionTimeout(sessionConfig.getSessionTimeout());

        // <session-config><cookie-config>
        CookieConfig cookieConfig = sessionConfig.getCookieConfig();
        if (cookieConfig != null) {
            SessionCookieConfig sessionCookieConfig = webModule.getSessionCookieConfig();
            /*
             * Unlike a cookie's domain, path, and comment, its name
             * will be empty (instead of null) if left unspecified
             * inside <session-config><cookie-config>
             */
            if (cookieConfig.getName() != null && !cookieConfig.getName().isEmpty()) {
                sessionCookieConfig.setName(cookieConfig.getName());
            }
            sessionCookieConfig.setDomain(cookieConfig.getDomain());
            sessionCookieConfig.setPath(cookieConfig.getPath());
            sessionCookieConfig.setComment(cookieConfig.getComment());
            sessionCookieConfig.setHttpOnly(cookieConfig.isHttpOnly());
            sessionCookieConfig.setSecure(cookieConfig.isSecure());
            sessionCookieConfig.setMaxAge(cookieConfig.getMaxAge());
        }

        // <session-config><tracking-mode>
        if (!sessionConfig.getTrackingModes().isEmpty()) {
            webModule.setSessionTrackingModes(sessionConfig.getTrackingModes());
        }

        // glassfish-web.xml override the web.xml
        com.sun.enterprise.web.session.SessionCookieConfig gfSessionCookieConfig =
                webModule.getSessionCookieConfigFromSunWebXml();
        if (gfSessionCookieConfig != null) {
            WebSessionCookieConfig sessionCookieConfig = (WebSessionCookieConfig) webModule.getSessionCookieConfig();

            if (gfSessionCookieConfig.getName() != null && !gfSessionCookieConfig.getName().isEmpty()) {
                sessionCookieConfig.setName(gfSessionCookieConfig.getName());
            }

            if (gfSessionCookieConfig.getPath() != null) {
                sessionCookieConfig.setPath(gfSessionCookieConfig.getPath());
            }

            if (gfSessionCookieConfig.getMaxAge() != null) {
                sessionCookieConfig.setMaxAge(gfSessionCookieConfig.getMaxAge());
            }

            if (gfSessionCookieConfig.getDomain() != null) {
                sessionCookieConfig.setDomain(gfSessionCookieConfig.getDomain());
            }

            if (gfSessionCookieConfig.getComment() != null) {
                sessionCookieConfig.setComment(gfSessionCookieConfig.getComment());
            }

            if (gfSessionCookieConfig.getSecure() != null) {
                sessionCookieConfig.setSecure(gfSessionCookieConfig.getSecure());
            }

            if (gfSessionCookieConfig.getHttpOnly() != null) {
                sessionCookieConfig.setHttpOnly(gfSessionCookieConfig.getHttpOnly());
            }
        }

        for (String welcomeFile : wmd.getWelcomeFiles()) {
            webModule.addWelcomeFile(welcomeFile);
        }

        LocaleEncodingMappingListDescriptor lemds = wmd.getLocaleEncodingMappingListDescriptor();
        if (lemds != null) {
            for (LocaleEncodingMappingDescriptor lemd : lemds.getLocaleEncodingMappingSet()) {
                webModule.addLocaleEncodingMappingParameter(lemd.getLocale(), lemd.getEncoding());
            }
        }

        webModule.setOrderedLibs(wmd.getOrderedLibs());

        String[] majorMinorVersions = wmd.getSpecVersion().split("\\.");
        if (majorMinorVersions.length != 2) {
            throw new IllegalArgumentException("Illegal Servlet spec version");
        }
        webModule.setEffectiveMajorVersion(Integer.parseInt(majorMinorVersions[0]));
        webModule.setEffectiveMinorVersion(Integer.parseInt(majorMinorVersions[1]));
    }


    /**
     * Configure security constraint element for a web application, as represented
     * by a <code>&lt;security-constraint&gt;</code> element in the deployment descriptor.
     * Configure a web resource collection for a web application's security constraint,
     * as represented by a <code>&lt;web-resource-collection&gt;</code>
     * element in the deployment descriptor.
     */
    protected static void configureSecurityConstraint(WebModule webModule, WebBundleDescriptor wmd) {
        Set<SecurityConstraint> constraints = wmd.getSecurityConstraints();
        SecurityConstraintDecorator decorator;
        SecurityCollectionDecorator secCollDecorator;
        for (SecurityConstraint constraint : constraints) {
            decorator = new SecurityConstraintDecorator(constraint, webModule);
            for (WebResourceCollection wrc : constraint.getWebResourceCollections()) {
                secCollDecorator = new SecurityCollectionDecorator(wrc);
                decorator.addCollection(secCollDecorator);
            }
            webModule.addConstraint(decorator);
        }
    }


    /**
     * Validate the usage of security role names in the web application
     * deployment descriptor.  If any problems are found, issue warning
     * messages (for backwards compatibility) and add the missing roles.
     * (To make these problems fatal instead, simply set the <code>ok</code>
     * instance variable to <code>false</code> as well).
     */
    protected static void configureSecurityRoles(WebModule webModule, WebBundleDescriptorImpl wmd) {
        Set<Role> roles = wmd.getRoles();
        for (Role role : roles) {
            webModule.addSecurityRole(role.getName());
        }

        // Check role names used in <security-constraint> elements
        Iterator<org.apache.catalina.deploy.SecurityConstraint> iter = webModule.getConstraints().iterator();
        while (iter.hasNext()) {
            String[] roleNames = iter.next().findAuthRoles();
            for (String role : roleNames) {
                if (!"*".equals(role) && !webModule.hasSecurityRole(role)) {
                    logger.log(Level.WARNING, LogFacade.ROLE_AUTH, role);
                    webModule.addSecurityRole(role);
                }
            }
        }

        // Check role names used in <servlet> elements
        Container[] wrappers = webModule.findChildren();
        for (Container wrapper2 : wrappers) {
            Wrapper wrapper = (Wrapper) wrapper2;
            String runAs = wrapper.getRunAs();
            if (runAs != null && !webModule.hasSecurityRole(runAs)) {
                logger.log(Level.WARNING, LogFacade.ROLE_RUNAS, runAs);
                webModule.addSecurityRole(runAs);
            }
            String[] names = wrapper.findSecurityReferences();
            for (String name : names) {
                String link = wrapper.findSecurityReference(name);
                if (link != null && !webModule.hasSecurityRole(link)) {
                    logger.log(Level.WARNING, LogFacade.ROLE_LINK, link);
                    webModule.addSecurityRole(link);
                }
            }
        }
    }
}
