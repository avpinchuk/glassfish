/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
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

package com.sun.enterprise.glassfish.bootstrap;

import com.sun.enterprise.glassfish.bootstrap.cfg.GFBootstrapProperties;
import com.sun.enterprise.glassfish.bootstrap.osgi.impl.ClassPathBuilder;

import java.io.IOException;
import java.nio.file.Path;

class ClassLoaderBuilder {

    private final ClassPathBuilder cpBuilder;
    private final GFBootstrapProperties ctx;

    ClassLoaderBuilder(GFBootstrapProperties ctx) {
        this.ctx = ctx;
        this.cpBuilder = new ClassPathBuilder();
    }

    void addPlatformDependencies() throws IOException {
        OsgiPlatformFactory.getOsgiPlatformAdapter(ctx).addFrameworkJars(cpBuilder);
    }

    ClassLoader build(ClassLoader delegate) {
        return cpBuilder.create(delegate);
    }

    void addLauncherDependencies() throws IOException {
        cpBuilder.addJar(ctx.getFileUnderInstallRoot(Path.of("modules", "glassfish.jar")));
    }

    void addServerBootstrapDependencies() throws IOException {
        cpBuilder.addJar(ctx.getFileUnderInstallRoot(Path.of("modules", "simple-glassfish-api.jar")));
        cpBuilder.addJar(ctx.getFileUnderInstallRoot(Path.of("lib", "bootstrap", "glassfish-jul-extension.jar")));
    }
}
