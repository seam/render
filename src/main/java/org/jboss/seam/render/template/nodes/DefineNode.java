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

import org.jboss.seam.render.template.CompositionContext;
import org.jboss.seam.render.template.Definition;
import org.mvel2.CompileException;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.EndNode;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.res.TerminalNode;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class DefineNode extends ContextualNode
{
   private static final long serialVersionUID = 3356732131663865976L;

   public DefineNode()
   {
      super();
      terminus = new TerminalNode();
   }

   @Override
   public Object eval(final TemplateRuntime runtime, final TemplateOutputStream appender, final Object ctx,
            final VariableResolverFactory factory)
   {
      return next != null ? next.eval(runtime, appender, ctx, factory) : null;
   }

   @Override
   public boolean demarcate(final Node terminatingNode, final char[] template)
   {

      Node definition = next;
      Node n = definition;

      while (n.getNext() != null)
      {
         n = n.next;
      }

      n.next = new EndNode();
      next = terminus;

      String key = new String(contents);
      if (key.isEmpty())
      {
         throw new CompileException("@define{ ... } expects 1 argument, got @define{" + key + "}", new char[] {}, 0);
      }

      CompositionContext context = CompositionContext.peek();
      context.put(key, new Definition(definition));

      return false;
   }

   @Override
   public boolean isOpenNode()
   {
      return true;
   }

}
