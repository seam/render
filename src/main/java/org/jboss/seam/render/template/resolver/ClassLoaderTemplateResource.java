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
package org.jboss.seam.render.template.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.jboss.seam.render.spi.TemplateResolver;
import org.jboss.seam.render.spi.TemplateResource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ClassLoaderTemplateResource implements TemplateResource<ClassLoader>
{

   private final ClassLoaderTemplateResolver resolver;
   private final ClassLoader loader;
   private final String path;

   /**
    * @param classLoaderTemplateResolver
    * @param resource
    */
   public ClassLoaderTemplateResource(final ClassLoaderTemplateResolver resolver, final ClassLoader loader,
            final String path)
   {
      this.resolver = resolver;
      this.loader = loader;
      this.path = path;
   }

   @Override
   public String getPath()
   {
      return path;
   }

   @Override
   public InputStream getInputStream()
   {
      return loader.getResourceAsStream(path);
   }

   @Override
   public ClassLoader getUnderlyingResource()
   {
      return loader;
   }

   @Override
   public TemplateResolver<ClassLoader> getResolvedBy()
   {
      return resolver;
   }

   @Override
   public long getLastModified()
   {
      URLConnection connection = null;
      try
      {
         connection = loader.getResource(path).openConnection();
         return connection.getLastModified();
      }
      catch (IOException e)
      {
         throw new RuntimeException("Could not determine last modified time for resource [" + path + "]", e);
      }
   }

}
