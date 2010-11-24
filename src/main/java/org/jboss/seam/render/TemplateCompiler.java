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
package org.jboss.seam.render;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.seam.render.spi.TemplateResolver;
import org.jboss.seam.render.spi.TemplateResource;
import org.jboss.seam.render.template.CompiledView;
import org.jboss.seam.render.template.nodes.DefineNode;
import org.jboss.seam.render.template.nodes.ExtendsNode;
import org.jboss.seam.render.template.nodes.InsertNode;
import org.jboss.seam.render.template.nodes.ParamNode;
import org.jboss.seam.render.template.resolver.TemplateResolverFactory;
import org.jboss.seam.render.util.Assert;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.SimpleTemplateRegistry;
import org.mvel2.templates.TemplateRegistry;
import org.mvel2.templates.res.Node;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class TemplateCompiler
{
   private final TemplateRegistry registry;
   private final VariableResolverFactory variableFactory;
   private final TemplateResolverFactory resolverFactory;

   private final Map<String, Class<? extends Node>> nodes = new HashMap<String, Class<? extends Node>>();

   @Inject
   public TemplateCompiler(final VariableResolverFactory variableFactory,
            final TemplateResolverFactory resolverFactory)
   {
      this.variableFactory = variableFactory;
      this.resolverFactory = resolverFactory;
      this.registry = new SimpleTemplateRegistry();
      addNode("param", ParamNode.class);
      addNode("extends", ExtendsNode.class);
      addNode("define", DefineNode.class);
      addNode("insert", InsertNode.class);
      // create a map resolve to hold the functions we want to inject, and chain
      // the ELVariableResolverFactory to this factory.
   }

   /**
    * Resolve the given path using any configured {@link TemplateResolver} and compile it with the default nodes. Return
    * the result as a {@link CompiledView}.
    */
   public CompiledView compile(final String path)
   {
      TemplateResource<?> resource = resolverFactory.resolve(path);
      return compile(resource);
   }

   /**
    * Resolve the given path using any configured {@link TemplateResolver} instances and compile it with the given map
    * of named {@link Node} types. Return the result as a {@link CompiledView}.
    */
   public CompiledView compile(final String path,
            final Map<String, Class<? extends Node>> nodes)
   {
      TemplateResource<?> resource = resolverFactory.resolve(path);
      return compile(resource, nodes);
   }

   public CompiledView compile(final TemplateResource<?> templateResource)
   {
      Assert.notNull(templateResource, "Cannot compile a null TemplateResource.");
      Map<String, Class<? extends Node>> nodes = getNodes();
      return compile(templateResource, nodes);

   }

   public CompiledView compile(final TemplateResource<?> templateResource,
            final Map<String, Class<? extends Node>> nodes)
   {
      CompiledView view = new CompiledView(variableFactory, registry, templateResource, nodes);
      return view;
   }

   public CompiledView compileRelative(final TemplateResource<?> originResource, final String relativePath,
            final Map<String, Class<? extends Node>> nodes)
   {
      TemplateResource<?> resource = resolverFactory.resolveRelative(originResource, relativePath);
      return compile(resource, nodes);
   }

   public CompiledView compileRelative(final TemplateResource<?> originResource, final String relativePath)
   {
      TemplateResource<?> resource = resolverFactory.resolveRelative(originResource, relativePath);
      return compile(resource);
   }

   public Map<String, Class<? extends Node>> getNodes()
   {
      return nodes;
   }

   public void addNode(final String name, final Class<? extends Node> type)
   {
      nodes.put(name, type);
   }

   public TemplateResolverFactory getTemplateResolverFactory()
   {
      return resolverFactory;
   }
}
