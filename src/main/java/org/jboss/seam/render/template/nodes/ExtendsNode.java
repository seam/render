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
package org.jboss.seam.render.template.nodes;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.spi.TemplateResource;
import org.jboss.seam.render.template.CompiledView;
import org.jboss.seam.render.template.CompositionContext;
import org.jboss.seam.render.template.util.NullTemplateOutputStream;
import org.jboss.seam.render.util.Assert;
import org.mvel2.CompileException;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.res.TerminalNode;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ExtendsNode extends ContextualNode
{
   private static final long serialVersionUID = -9214745288595708748L;

   @Inject
   private TemplateCompiler compiler;

   private Node inside;

   public ExtendsNode()
   {
      super();
      terminus = new TerminalNode();
   }

   @Override
   public Object eval(final TemplateRuntime runtime, final TemplateOutputStream appender, final Object ctx,
            final VariableResolverFactory factory)
   {
      String requestedTemplate = new String(contents);
      if ((requestedTemplate == null) || requestedTemplate.isEmpty())
      {
         throw new CompileException("@" + getName()
                  + "{ ...template... } requires 1 parameter, instead received @" + getName() + "{"
                  + requestedTemplate + "}");
      }

      next = next.next;
      Node saved = next.next;
      next.next = null;

      executeInside(runtime, appender, ctx, factory, requestedTemplate);

      next.next = saved;

      return next != null ? next.eval(runtime, appender, ctx, factory) : null;
   }

   @SuppressWarnings("unchecked")
   private void executeInside(final TemplateRuntime runtime, final TemplateOutputStream appender, final Object ctx,
            final VariableResolverFactory factory, final String requestedTemplate)
   {
      // TODO consider evaluating this for dynamic template names
      String requested = requestedTemplate.trim();

      if (requested != null)
      {
         Map<Object, Object> clone = cloneContextMap((Map<Object, Object>) ctx);

         CompositionContext thisContext = CompositionContext.extractFromMap(clone);
         CompositionContext newContext = storeBindingsInNewCompositionContext(runtime, clone, factory, thisContext,
                  appender);

         TemplateResource<?> templateResource = thisContext.getTemplateResource();
         CompiledView composite = compiler.compileRelative(templateResource, requested);
         // TODO this needs to append directly to the stream, not using a string as a buffer
         String render = composite.render(newContext, clone);

         // CompositionContext.storeInMap((Map<Object, Object>) ctx, savedContext);

         Assert.equals(thisContext, CompositionContext.extractFromMap((Map<Object, Object>) ctx),
                  "CompositionContext was modified during evaluation. This is not allowed.");

         appender.append(render);
      }
   }

   private CompositionContext storeBindingsInNewCompositionContext(final TemplateRuntime runtime,
            final Map<Object, Object> contextMap,
            final VariableResolverFactory factory, final CompositionContext compositionContext,
            final TemplateOutputStream appender)
   {
      CompositionContext newContext = new CompositionContext(compositionContext);
      newContext.setTemplateResource(compositionContext.getTemplateResource());
      CompositionContext.storeInMap(contextMap, newContext);
      inside.eval(runtime, new NullTemplateOutputStream(), contextMap, factory);
      return newContext;
   }

   private Map<Object, Object> cloneContextMap(final Map<Object, Object> ctx)
   {
      Map<Object, Object> result = new HashMap<Object, Object>();
      result.putAll(ctx);
      return result;
      // return ctx;
   }

   @Override
   public boolean demarcate(final Node terminatingNode, final char[] template)
   {
      this.inside = next;
      next = terminus;
      return true;
   }

   @Override
   public boolean isOpenNode()
   {
      return true;
   }

}
