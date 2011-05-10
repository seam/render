/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.seam.render.spi;

import java.io.InputStream;

import org.jboss.seam.render.template.resolver.TemplateResolverFactory;

/**
 * Handle to a template-able resource.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface TemplateResource<T> {
    /**
     * Get the unique path at which this resource can be located. A resource with a null path cannot be located by the
     * {@link TemplateResolverFactory}.
     */
    String getPath();

    /**
     * Get the underlying data for this {@link TemplateResource} in the form of an {@link InputStream}.
     */
    InputStream getInputStream();

    /**
     * Get the last modified time of this resource. If zero, the last modified time is not available.
     */
    long getLastModified();

    /**
     * Get the underlying {@link T} resource object for this {@link TemplateResource}
     */
    T getUnderlyingResource();

    /**
     * Get the {@link TemplateResolver} with which this {@link TemplateResource} was resolved.
     */
    TemplateResolver<T> getResolvedBy();
}
