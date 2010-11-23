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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.seam.render.RenderTestBase;
import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.template.CompiledView;
import org.jboss.seam.render.template.resolver.ClassLoaderTemplateResolver;
import org.jboss.seam.render.template.resolver.TemplateResolverFactory;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class CompositeTemplateTest extends RenderTestBase
{
   @Inject
   private TemplateCompiler compiler;

   @Inject
   protected void init(final TemplateResolverFactory factory)
   {
      compiler.getTemplateResolverFactory().addResolver(
               new ClassLoaderTemplateResolver(this.getClass().getClassLoader()));
   }

   @Test
   public void testDefinitionsExtracted() throws Exception
   {
      String name = "name";
      String value = "lb3";

      Map<Object, Object> context = new HashMap<Object, Object>();
      context.put(name, new String[] { value });

      CompiledView view = compiler.compile("org/jboss/seam/render/views/composite/definitions.xhtml");
      String output = view.render(context);

      System.out.println(output);
      assertNotNull(output);
      assertTrue(output.contains("@view{}"));
      assertTrue(output.contains("<html>"));
      assertTrue(output.contains("Default is rendered if no def is found."));
      assertFalse(output.contains("This should be ignored."));
   }

   @Test
   public void testDefinitionsUsedInContext() throws Exception
   {
      Map<Object, Object> context = new HashMap<Object, Object>();

      CompiledView view = compiler.compile("org/jboss/seam/render/views/composite/multi-definitions.xhtml");
      String output = view.render(context);

      assertEquals("t1def1,t2def2,titlebody,t3t4hi", output);
   }
}
