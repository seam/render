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
import java.util.Queue;

import org.jboss.seam.render.util.Tokenizer;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ParamNode extends ContextualNode
{
   private static final long serialVersionUID = 3356732131663865976L;
   private static final String DELIM = "=";

   public ParamNode()
   {
      super();
      // terminus = new TerminalNode();
   }

   @Override
   @SuppressWarnings("unchecked")
   public Object eval(final TemplateRuntime runtime, final TemplateOutputStream appender, final Object ctx,
            final VariableResolverFactory factory)
   {
      String line = new String(contents);
      Queue<String> tokens = Tokenizer.tokenize(DELIM, line);

      if (tokens.size() != 2)
      {
         throw new CompileException("@" + getName()
                  + "{ param " + DELIM + " value } requires two parameters, instead received @" + getName()
                  + "{" + line + "}");
      }

      String name = tokens.remove().trim();
      String el = tokens.remove().trim();
      Object result = MVEL.eval(el, ctx, factory);
      if (result != null)
      {
         ((Map<Object, Object>) ctx).put(name, result);
      }

      return next != null ? next.eval(runtime, appender, ctx, factory) : null;
   }

   @Override
   public boolean demarcate(final Node terminatingNode, final char[] template)
   {
      return false;
   }
}
