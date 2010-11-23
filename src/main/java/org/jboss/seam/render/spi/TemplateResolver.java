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

import org.jboss.weld.extensions.util.service.ServiceLoader;

/**
 * Defines a strategy for resolving {@link TemplateResource} assets. Additional resolvers can be registering using the
 * {@link ServiceLoader} interface. This should be used to provide resolvers that know how to decode your specific
 * resource location scheme. For instance, a database or web-service loaded {@link TemplateResource} could be specified
 * using a custom path:
 * <p>
 * E.g: <code>"REST:/users/1/views/23"</code>
 * <p>
 * Next, in your application, create a service file containing the qualified class names of your
 * {@link TemplateResolver} implementations:
 * <p>
 * <strong>For example:</strong><br>
 * /META-INF/services/org.jboss.seam.render.spi.TemplateResolver<br>
 * -----<br>
 * <code>com.example.templates.CustomTemplateResolver</code> <br>
 * <code>com.example.templates.CustomTemplateResolver2</code> <br>
 * -----<br>
 * <p>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface TemplateResolver<T>
{
   public TemplateResource<T> resolve(String target);

   public TemplateResource<T> resolveRelative(TemplateResource<T> origin, String target);
}
