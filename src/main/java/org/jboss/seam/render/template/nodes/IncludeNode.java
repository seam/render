/**
 * MVEL 2.0
 * Copyright (C) 2007 The Codehaus
 * Mike Brock, Dhanji Prasanna, John Graham, Mark Proctor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.render.template.nodes;

import java.util.Map;

import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.template.CompiledTemplateResource;
import org.jboss.seam.render.template.CompositionContext;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;
import org.mvel2.templates.util.TemplateOutputStream;
import org.mvel2.templates.util.TemplateTools;
import org.mvel2.util.ParseTools;

public class IncludeNode extends ContextualNode {
    private static final long serialVersionUID = 4842874368447237513L;

    private CompositionContext compositionContext;
    private String requestedTemplate;
    private CompiledTemplateResource compiledView;

    private char[] includeExpression;
    private char[] preExpression;

    @Override
    public void setContents(final char[] contents) {
        int mark;
        this.includeExpression = ParseTools.subset(contents, 0, mark = TemplateTools.captureToEOS(contents, 0));
        if (mark != contents.length)
            this.preExpression = ParseTools.subset(contents, ++mark, contents.length - mark);
        setup();
        this.contents = contents;
    }

    private void setup() {
        // This is processed when the @extends{} node is found and initialized
        requestedTemplate = new String(includeExpression).trim();
        if (requestedTemplate.isEmpty()) {
            throw new CompileException("@" + getName()
                    + "{ ...template... } requires 1 parameter, instead received @" + getName() + "{"
                    + requestedTemplate + "}", new char[]{}, 0);
        }

        compositionContext = CompositionContext.peek();
        compositionContext = new CompositionContext(
                compositionContext.getTemplateCompiler(),
                compositionContext.getVariableResolverFactory(),
                compositionContext.getTemplateRegistry(),
                compositionContext.getTemplateResource());
        CompositionContext.push(compositionContext);
        /*
        * TODO a TemplateCache system could be used to handle dynamic template includes/extends
        *
        * Pre-compile template expression Load compiled template from cache based on result of expression See:
        * MVEL.compileExpression(...)
        */
        TemplateCompiler compiler = compositionContext.getTemplateCompiler();
        compiledView = compiler.compileRelative(compositionContext.getTemplateResource(), requestedTemplate);
        CompositionContext.pop();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object eval(final TemplateRuntime runtime, final TemplateOutputStream appender, final Object ctx,
                       final VariableResolverFactory factory) {
        if (this.preExpression != null) {
            MVEL.eval(preExpression, ctx, factory);
        }

        compositionContext.setTemplateRuntime(runtime);
        CompositionContext.push(compositionContext);

        Map<Object, Object> context = (Map<Object, Object>) ctx;
        compiledView.render(runtime, appender, context, factory);

        CompositionContext.pop();

        return next != null ? next.eval(runtime, appender, ctx, factory) : null;
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

}
