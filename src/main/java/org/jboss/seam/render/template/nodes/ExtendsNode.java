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

import java.util.Map;

import javax.inject.Inject;

import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.template.CompiledTemplateResource;
import org.jboss.seam.render.template.CompositionContext;
import org.jboss.seam.render.template.util.NullTemplateOutputStream;
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

   private String requestedTemplate;

   private CompiledTemplateResource compiledView;

   private CompositionContext compositionContext;

   private Node inside;

   public ExtendsNode()
   {
      super();
      this.terminus = new TerminalNode();
   }

   @Override
   public void setContents(final char[] contents)
   {
      super.setContents(contents);
      setup();
   }

   private void setup()
   {
      // This is processed when the @extends{} node is found and initialized
      requestedTemplate = new String(contents).trim();
      if (requestedTemplate.isEmpty())
      {
         throw new CompileException("@" + getName()
                  + "{ ...template... } requires 1 parameter, instead received @" + getName() + "{"
                  + requestedTemplate + "}");
      }

      compositionContext = CompositionContext.peek();
      compositionContext = new CompositionContext(
               compositionContext.getVariableResolverFactory(),
               compositionContext.getTemplateRegistry(),
               compositionContext.getTemplateResource());
      CompositionContext.push(compositionContext);
      compiledView = compiler.compileRelative(compositionContext.getTemplateResource(), requestedTemplate);
   }

   @Override
   @SuppressWarnings("unchecked")
   public Object eval(final TemplateRuntime runtime, final TemplateOutputStream appender, final Object ctx,
            final VariableResolverFactory factory)
   {
      Map<Object, Object> context = (Map<Object, Object>) ctx;

      Node n = inside.next;
      while ((n != terminus) && (n != null))
      {
         n = n.next;
      }
      Node saved = null;
      if (n != null)
      {
         saved = n.next;
         n.next = null;
      }
      inside.eval(runtime, new NullTemplateOutputStream(), ctx, factory);
      next = saved;

      compositionContext.setTemplateRuntime(runtime);
      CompositionContext.push(compositionContext);
      compiledView.render(runtime, appender, context, factory);
      CompositionContext.pop();

      return next != null ? next.eval(runtime, appender, ctx, factory) : null;
   }

   @Override
   public boolean demarcate(final Node terminatingNode, final char[] template)
   {
      inside = next;
      next = terminus;
      CompositionContext.pop();
      return true;
   }

   @Override
   public boolean isOpenNode()
   {
      return true;
   }

}
