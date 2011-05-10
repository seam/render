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
package org.jboss.seam.render.util;

import java.io.File;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class Paths {
    /**
     * Calculate a path using the given relative path and a base path.
     */
    public static String calculateRelativePath(String path, String relativePath) {
        Assert.notNull(path, "Path must not be null.");
        Assert.notNull(relativePath, "Relative path must not be null.");

        int index = path.lastIndexOf(File.separator);
        if (index > -1) {
            path = path.substring(0, index);
        }

        relativePath = relativePath.trim();
        while (relativePath.startsWith(".")) {
            if (relativePath.startsWith(".." + File.separator)) {
                relativePath.substring(3);
                index = path.lastIndexOf(File.separator);
                if (index > -1) {
                    path = path.substring(0, index);
                }
            } else if (relativePath.startsWith("." + File.separator)) {
                relativePath = relativePath.substring(2);
                index = path.lastIndexOf(File.separator);
                if (index > -1) {
                    path = path.substring(0, index);
                }
            } else if (relativePath.startsWith(".")) {
                relativePath = relativePath.substring(1);
            }

        }

        if (path.endsWith(File.separator) && relativePath.startsWith(File.separator)) {
            relativePath = relativePath.substring(1);
        }
        path = path + File.separator + relativePath;
        return path;
    }
}
