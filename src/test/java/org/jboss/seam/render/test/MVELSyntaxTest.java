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
package org.jboss.seam.render.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class MVELSyntaxTest
{
   @Test
   public void testIf() throws Exception
   {
      CompiledTemplate template = TemplateCompiler.compileTemplate("@if{true}hi@end{}");
      String result = (String) TemplateRuntime.execute(template);

      assertEquals("hi", result);
   }

   @Test
   public void testDynamicArrayVariableAssignment() throws Exception
   {
      CompiledTemplate template = TemplateCompiler
               .compileTemplate("@code{ arr = {1, 2, 3, 4, 5} }@foreach{i:arr}@{i}@end{}");
      Map<String, Object> map = new HashMap<String, Object>();
      String result = (String) TemplateRuntime.execute(template, map, new MapVariableResolverFactory(map));

      assertEquals("12345", result);
   }
}
