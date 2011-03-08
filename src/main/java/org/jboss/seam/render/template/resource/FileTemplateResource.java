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
package org.jboss.seam.render.template.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jboss.seam.render.spi.TemplateResolver;
import org.jboss.seam.render.spi.TemplateResource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class FileTemplateResource implements TemplateResource<File>
{
   private final File resource;
   private final TemplateResolver<File> resolvedBy;

   public FileTemplateResource(final File file)
   {
      this.resource = file;
      this.resolvedBy = null;
   }

   public FileTemplateResource(final File file, final TemplateResolver<File> resolvedBy)
   {
      this.resource = file;
      this.resolvedBy = resolvedBy;
   }

   @Override
   public File getUnderlyingResource()
   {
      return resource;
   }

   @Override
   public String getPath()
   {
      return resource.getAbsolutePath();
   }

   @Override
   public InputStream getInputStream()
   {
      try
      {
         return new FileInputStream(resource);
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException("Could not open " + this.getClass().getSimpleName() + " at ["
                  + resource.getAbsolutePath() + "]", e);
      }
   }

   @Override
   public TemplateResolver<File> getResolvedBy()
   {
      return resolvedBy;
   }

   @Override
   public long getLastModified()
   {
      return resource.lastModified();
   }

}
