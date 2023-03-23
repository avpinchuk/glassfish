/*
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.web.deployment.io.runtime;

import com.sun.enterprise.deployment.io.ConfigurationDeploymentDescriptorFile;
import com.sun.enterprise.deployment.io.ConfigurationDeploymentDescriptorFileFor;
import com.sun.enterprise.deployment.io.DescriptorConstants;

import java.util.List;
import java.util.Map;

import org.glassfish.api.deployment.archive.WarArchiveType;
import org.glassfish.deployment.common.Descriptor;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.web.deployment.descriptor.WebBundleDescriptorImpl;
import org.glassfish.web.deployment.node.runtime.gf.GFWebBundleRuntimeNode;
import org.jvnet.hk2.annotations.Service;

/**
 * This class is responsible for handling the XML configuration information
 * for the Glassfish Web Container
 */
@ConfigurationDeploymentDescriptorFileFor(WarArchiveType.ARCHIVE_TYPE)
@Service
@PerLookup
public class GFWebRuntimeDDFile extends ConfigurationDeploymentDescriptorFile<WebBundleDescriptorImpl> {

    @Override
    public String getDeploymentDescriptorPath() {
        return DescriptorConstants.GF_WEB_JAR_ENTRY;
    }


    @Override
    public GFWebBundleRuntimeNode getRootXMLNode(Descriptor descriptor) {
        if (descriptor instanceof WebBundleDescriptorImpl) {
            return new GFWebBundleRuntimeNode((WebBundleDescriptorImpl) descriptor);
        }
        return null;
    }


    @Override
    public void registerBundle(
        Map<String, Class<?>> rootNodesMap,
        Map<String, String> publicIDToDTD,
        Map<String, List<Class<?>>> versionUpgrades
    ) {
        String bundle = GFWebBundleRuntimeNode.registerBundle(publicIDToDTD, versionUpgrades);
        rootNodesMap.put(bundle, GFWebBundleRuntimeNode.class);
    }
}
