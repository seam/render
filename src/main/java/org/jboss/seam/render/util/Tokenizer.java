package org.jboss.seam.render.util;

import java.util.LinkedList;
import java.util.Queue;

public class Tokenizer {
    public static Queue<String> tokenize(final String delim, final String line) {
        Queue<String> tokens = new LinkedList<String>();

        for (String tok : line.split(delim)) {
            tokens.add(tok);
        }

        return tokens;
    }
}
