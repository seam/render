/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.render.template.resource;

import java.io.InputStream;

import org.jboss.seam.render.spi.TemplateResolver;
import org.jboss.seam.render.spi.TemplateResource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class InputStreamTemplateResource implements TemplateResource<InputStream> {
    private final InputStream template;
    private final TemplateResolver<InputStream> resolvedBy;
    private String path = "";

    public InputStreamTemplateResource(final TemplateResolver<InputStream> resolvedBy, final InputStream template,
                                       final String path) {
        this.template = template;
        this.resolvedBy = resolvedBy;
        this.path = path;
    }

    public InputStreamTemplateResource(final InputStream template, final String path) {
        this.template = template;
        this.resolvedBy = null;
        this.path = path;
    }

    public InputStreamTemplateResource(final InputStream template) {
        this.template = template;
        this.resolvedBy = null;
        this.path = null;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public InputStream getInputStream() {
        return template;
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public InputStream getUnderlyingResource() {
        return template;
    }

    @Override
    public TemplateResolver<InputStream> getResolvedBy() {
        return resolvedBy;
    }

}
