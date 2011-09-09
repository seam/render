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

import org.jboss.seam.render.template.compiler.CustomTemplateCompiler;
import org.junit.Test;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateRuntime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author Pete Royle
 */
public class BundledHelpersTest {

    @Test
    public void testStringUtils() throws Exception {
        testBundledHelper("TheInpu...",             "@{render.abbreviate('TheInputString', 10)}");
        testBundledHelper("...putSt...",            "@{render.abbreviate('TheInputString', 5, 11)}");
        testBundledHelper("TheInputString",         "@{render.capitalize('theInputString')}");
        testBundledHelper("   TheInputString   ",   "@{render.center('TheInputString', 20)}");
        testBundledHelper("TheInputString",         "@{render.defaultString('TheInputString', 'BlahBlah')}");
        testBundledHelper("BlahBlah",               "@{render.defaultString('', 'BlahBlah')}");
        testBundledHelper("BlahBlah",               "@{render.defaultString(null, 'BlahBlah')}");
        testBundledHelper("The Input String",       "@{render.join(new String[]{'The', 'Input', 'String'}, ' ')}");
        testBundledHelper("      TheInputString",   "@{render.leftPad('TheInputString', 20)}");
        testBundledHelper("XXXXXXTheInputString",   "@{render.leftPad('TheInputString', 20, 'X')}");
        testBundledHelper("theinputstring",         "@{render.lowerCase('TheInputString')}");
        testBundledHelper("TheOutputString",        "@{render.replace('TheInputString', 'In', 'Out')}");
        testBundledHelper("TheInputString      ",   "@{render.rightPad('TheInputString', 20)}");
        testBundledHelper("TheInputStringXXXXXX",   "@{render.rightPad('TheInputString', 20, 'X')}");
        testBundledHelper(new String[]{"The", "Input", "String"}, "@{render.split('The Input String')}");
        testBundledHelper(new String[]{"TheI", "putStri", "g"}, "@{render.split('TheInputString', 'n')}");
        testBundledHelper("tHEiNPUTsTRING",         "@{render.swapCase('TheInputString')}");
        testBundledHelper("TheInputString",         "@{render.trim('  TheInputString    ')}");
        testBundledHelper("theInputString",         "@{render.uncapitalize('TheInputString')}");
        testBundledHelper("THEINPUTSTRING",         "@{render.upperCase('TheInputString')}");
    }

    private void testBundledHelper(Object expectedResult, String mvelExpression) throws SecurityException, NoSuchMethodException {
        CompiledTemplate template = CustomTemplateCompiler.compileTemplate(mvelExpression);
        Object result = TemplateRuntime.execute(template);
        if (result instanceof Object[]) {
            assertArrayEquals((Object[]) expectedResult, (Object[]) result);
        } else {
            assertEquals(expectedResult, result);
        }
    }

}
