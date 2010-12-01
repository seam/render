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

import javax.el.ELContext;
import javax.el.ValueExpression;

import org.jboss.weld.extensions.el.Expressions;
import org.mvel2.integration.VariableResolver;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ELVariableResolver implements VariableResolver
{
   private static final long serialVersionUID = 7393032041693001458L;

   private final Expressions expressions;
   private final String name;
   private final Class<?> type;
   private final ELContext context;

   public ELVariableResolver(final Expressions expressions, final String name, final Class<?> type)
   {
      this.expressions = expressions;
      context = expressions.getELContext();
      this.name = name;
      this.type = type;
   }

   public ELVariableResolver(final Expressions expressions, final String name)
   {
      this.expressions = expressions;
      this.context = expressions.getELContext();
      this.name = name;
      this.type = Object.class;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public Class<?> getType()
   {
      return type;
   }

   @Override
   @SuppressWarnings("rawtypes")
   public void setStaticType(final Class type)
   {

   }

   @Override
   public int getFlags()
   {
      return 0;
   }

   @Override
   public Object getValue()
   {
      ValueExpression expression = expressions.getExpressionFactory().createValueExpression(context,
               expressions.toExpression(name), type);
      Object value = expression.getValue(context);
      return value;
   }

   @Override
   public void setValue(final Object value)
   {
      ValueExpression expression = expressions.getExpressionFactory().createValueExpression(context,
               expressions.toExpression(name), type);
      expression.setValue(context, value);
   }

}
