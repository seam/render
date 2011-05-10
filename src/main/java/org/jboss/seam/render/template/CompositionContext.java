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

import java.util.Stack;

import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.spi.TemplateResource;
import org.jboss.seam.render.util.Assert;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRegistry;
import org.mvel2.templates.TemplateRuntime;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class CompositionContext extends TemplateContext<String, Definition> {
    private final CompositionContext context;
    private final TemplateCompiler templateCompiler;
    private final VariableResolverFactory variableResolverFactory;
    private final TemplateResource<?> resource;
    private final TemplateRegistry templateRegistry;
    private TemplateRuntime templateRuntime;

    private static final ThreadLocal<Stack<CompositionContext>> stack;

    static {
        stack = new ThreadLocal<Stack<CompositionContext>>();
        stack.set(new Stack<CompositionContext>());
    }

    public CompositionContext(final TemplateCompiler templateCompiler,
                              final VariableResolverFactory factory,
                              final TemplateRegistry registry,
                              final TemplateResource<?> resource) {
        Assert.notNull(templateCompiler, "TemplateCompiler must not be null.");
        Assert.notNull(factory, "VariableResolverFactory must not be null.");
        Assert.notNull(registry, "TemplateRegistry must not be null.");
        Assert.notNull(resource, "TemplateResource must not be null.");

        this.templateCompiler = templateCompiler;
        this.context = null;
        this.variableResolverFactory = factory;
        this.templateRegistry = registry;
        this.resource = resource;
    }

    public CompositionContext(
            final TemplateResource<?> resource,
            final CompositionContext context) {
        Assert.notNull(resource, "TemplateResource must not be null.");
        Assert.notNull(context, "CompositionContext must not be null.");

        this.templateCompiler = context.getTemplateCompiler();
        this.variableResolverFactory = context.getVariableResolverFactory();
        this.templateRegistry = context.getTemplateRegistry();
        this.templateRuntime = context.getTemplateRuntime();
        this.resource = resource;
        this.context = context;
    }

    @Override
    public Definition get(final String name) {
        Definition result = super.get(name);
        if ((result == null) && (context != null)) {
            result = context.get(name);
        }
        return result;
    }

    public static CompositionContext peek() {
        if (!stack.get().isEmpty()) {
            return stack.get().peek();
        }
        return null;
    }

    public static CompositionContext push(final CompositionContext context) {
        return stack.get().push(context);
    }

    public static CompositionContext pop() {
        return stack.get().pop();
    }

    public TemplateResource<?> getTemplateResource() {
        return resource;
    }

    public CompositionContext getWrapped() {
        return context;
    }

    public VariableResolverFactory getVariableResolverFactory() {
        return variableResolverFactory;
    }

    public TemplateRegistry getTemplateRegistry() {
        return templateRegistry;
    }

    public TemplateRuntime getTemplateRuntime() {
        return templateRuntime;
    }

    public void setTemplateRuntime(final TemplateRuntime templateRuntime) {
        this.templateRuntime = templateRuntime;
    }

    public TemplateCompiler getTemplateCompiler() {
        return templateCompiler;
    }
}
