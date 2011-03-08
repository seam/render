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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.seam.render.RenderTestBase;
import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.spi.TemplateResource;
import org.jboss.seam.render.template.CompiledTemplateResource;
import org.jboss.seam.render.template.resolver.ClassLoaderTemplateResolver;
import org.jboss.seam.render.template.resolver.TemplateResolverFactory;
import org.jboss.seam.render.template.resource.ClassLoaderTemplateResource;
import org.jboss.seam.render.template.resource.FileTemplateResource;
import org.jboss.seam.render.template.resource.InputStreamTemplateResource;
import org.jboss.seam.render.template.resource.StringTemplateResource;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class BuiltinTemplateResourceTest extends RenderTestBase
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
   public void testStringTemplateResource() throws Exception
   {
      TemplateResource<?> base = new StringTemplateResource(
               "@include{org/jboss/seam/render/views/el-resolver/single.xhtml}bar");

      Map<Object, Object> context = new HashMap<Object, Object>();

      CompiledTemplateResource view = compiler.compile(base);
      String output = view.render(context);

      assertEquals("Foobar", output);
   }

   @Test
   public void testInputStreamTemplateResource() throws Exception
   {
      TemplateResource<?> base = new InputStreamTemplateResource(new ByteArrayInputStream(
               "@include{org/jboss/seam/render/views/el-resolver/single.xhtml}bar".getBytes()));

      Map<Object, Object> context = new HashMap<Object, Object>();

      CompiledTemplateResource view = compiler.compile(base);
      String output = view.render(context);

      assertEquals("Foobar", output);
   }

   @Test
   public void testFileTemplateResource() throws Exception
   {
      File file = File.createTempFile("tempResource.render", "suffix");
      assertTrue(file.exists());

      FileWriter writer = new FileWriter(file);
      writer.append("@include{org/jboss/seam/render/views/el-resolver/single.xhtml}bar");
      writer.flush();

      TemplateResource<?> base = new FileTemplateResource(file);

      Map<Object, Object> context = new HashMap<Object, Object>();

      CompiledTemplateResource view = compiler.compile(base);
      String output = view.render(context);

      assertEquals("Foobar", output);

      System.gc();
      assertTrue(file.delete());
   }

   @Test
   public void testClassLoaderTemplateResource() throws Exception
   {

      TemplateResource<?> base = new ClassLoaderTemplateResource(loader, "/foo");

      Map<Object, Object> context = new HashMap<Object, Object>();

      CompiledTemplateResource view = compiler.compile(base);
      String output = view.render(context);

      assertEquals("Foobar", output);
   }

   @Test(expected = IllegalStateException.class)
   public void testClassLoaderTemplateResourceFailsOnMissingResource() throws Exception
   {
      TemplateResource<?> base = new ClassLoaderTemplateResource(loader, "/bar");
      compiler.compile(base);
      fail();
   }

   ClassLoader loader = new ClassLoader()
   {
      @Override
      public URL getResource(final String name)
      {
         if (!validName(name))
         {
            return null;
         }
         try
         {
            return new URL(name);
         }
         catch (MalformedURLException e)
         {
            throw new RuntimeException(e);
         }
      }

      @Override
      public InputStream getResourceAsStream(final String name)
      {
         if (!validName(name))
         {
            return null;
         }
         return new ByteArrayInputStream(
                  "@include{org/jboss/seam/render/views/el-resolver/single.xhtml}bar".getBytes());
      }

      private boolean validName(final String name)
      {
         return "/foo".equals(name);
      }
   };
}
