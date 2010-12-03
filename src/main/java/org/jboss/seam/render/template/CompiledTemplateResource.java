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

import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.render.spi.TemplateResource;
import org.jboss.seam.render.util.Assert;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRegistry;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 * A {@link TemplateResource} that has been compiled and is ready to be rendered.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CompiledTemplateResource
{

   private final CompiledTemplate template;
   private final VariableResolverFactory factory;
   private final TemplateRegistry registry;

   public CompiledTemplateResource(final CompositionContext context,
            final TemplateResource<?> resource,
            final Map<String, Class<? extends Node>> nodes)
   {
      this(context.getVariableResolverFactory(), context.getTemplateRegistry(), resource, nodes);
   }

   public CompiledTemplateResource(final VariableResolverFactory factory,
            final TemplateRegistry registry,
            final TemplateResource<?> resource,
            final Map<String, Class<? extends Node>> nodes)
   {
      Assert.notNull(factory, "VariableResolverFactory must not be null.");
      Assert.notNull(registry, "TemplateRegistry must not be null.");
      Assert.notNull(resource, "TemplateResource must not be null.");

      this.factory = new MapVariableResolverFactory(new HashMap<String, Object>(), factory);
      this.registry = registry;

      CompositionContext context = CompositionContext.peek();
      if (context != null)
      {
         CompositionContext.push(new CompositionContext(resource, context));
      }
      else
      {
         CompositionContext.push(new CompositionContext(factory, registry, resource));
      }

      if (nodes == null)
      {
         template = TemplateCompiler.compileTemplate(resource.getInputStream());
      }
      else
      {
         template = TemplateCompiler.compileTemplate(resource.getInputStream(), nodes);
      }
      CompositionContext.pop();
   }

   public String render(final Map<Object, Object> context)
   {
      String result = (String) TemplateRuntime.execute(template, context, factory, registry);
      return result;
   }

   public void render(final TemplateRuntime runtime, final TemplateOutputStream appender,
            final Map<Object, Object> context,
            final VariableResolverFactory factory)
   {
      String result = (String) TemplateRuntime.execute(template, context, runtime.getNamedTemplateRegistry());
      appender.append(result);
   }

}
