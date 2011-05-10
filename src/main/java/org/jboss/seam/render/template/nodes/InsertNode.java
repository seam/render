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

import java.util.Queue;

import org.jboss.seam.render.template.CompositionContext;
import org.jboss.seam.render.template.Definition;
import org.jboss.seam.render.util.Tokenizer;
import org.mvel2.CompileException;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.EndNode;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.res.TerminalNode;
import org.mvel2.templates.util.TemplateOutputStream;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class InsertNode extends ContextualNode {
    private static final long serialVersionUID = -1285613595891138294L;

    private static final String DELIM = ",";

    public InsertNode() {
        super();
        terminus = new TerminalNode();
    }

    private Node defaultContent;

    @Override
    public Object eval(final TemplateRuntime runtime, final TemplateOutputStream appender, final Object ctx,
                       final VariableResolverFactory factory) {
        String line = new String(contents);
        Queue<String> tokens = Tokenizer.tokenize(DELIM, line);

        if (tokens.isEmpty()) {
            throw new CompileException("@define{ ... } expects 1 argument, got @define{" + line + "}", new char[]{}, 0);
        }

        CompositionContext context = CompositionContext.peek();
        Definition definition = context.get(line.trim());

        if (definition == null) {
            defaultContent.eval(runtime, appender, ctx, factory);
        } else {
            definition.eval(context, appender, ctx);
        }

        return next != null ? next.eval(runtime, appender, ctx, factory) : null;
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {

        Node n = defaultContent = next;

        while (n.getNext() != null) {
            n = n.next;
        }

        n.next = new EndNode();
        next = terminus;

        return false;
    }

    @Override
    public boolean isOpenNode() {
        return true;
    }

}
