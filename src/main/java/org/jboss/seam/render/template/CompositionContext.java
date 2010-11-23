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
package org.jboss.seam.render.template;

import java.util.Map;

import org.jboss.seam.render.spi.TemplateResource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CompositionContext extends TemplateContext<String, Definition>
{
   public static final String CONTEXT_KEY = "_defs";
   private final CompositionContext context;
   private TemplateResource<?> resource;

   public CompositionContext()
   {
      context = null;
   }

   @Override
   public Definition get(final String name)
   {
      Definition result = super.get(name);
      if ((result == null) && (context != null))
      {
         result = context.get(name);
      }
      return result;
   }

   public static CompositionContext extractFromMap(final Map<Object, Object> map)
   {
      return (CompositionContext) map.get(CONTEXT_KEY);
   }

   public static CompositionContext storeInMap(final Map<Object, Object> map, final CompositionContext context)
   {
      return (CompositionContext) map.put(CONTEXT_KEY, context);
   }

   public CompositionContext(final CompositionContext context)
   {
      this.context = context;
   }

   public void setTemplateResource(final TemplateResource<?> resource)
   {
      this.resource = resource;
   }

   public TemplateResource<?> getTemplateResource()
   {
      return resource;
   }

   public CompositionContext getWrapped()
   {
      return context;
   }
}
