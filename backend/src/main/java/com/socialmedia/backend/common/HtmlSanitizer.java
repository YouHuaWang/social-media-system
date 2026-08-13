package com.socialmedia.backend.common;

import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {

    public String sanitize(String input) {

        if (input == null) {
            return null;
        }

        return input
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
}