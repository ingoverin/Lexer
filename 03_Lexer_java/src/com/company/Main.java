package com.company;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        String str = "  a := 0 ; b := 3; c := 42; if(a and b or c xor a) then() else(s) := ; xor4 := 4;  6";
        try {
            // При желании можно считать из файла примерно так
            // File file = new File("filename");
            // InputStream is = new FileInputStream(file);

            InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));

            Lexer lexer = new Lexer(is);
            Lexer.Token token;

            do {
                token = lexer.next();
                System.out.println(TokenType.to_string(token.type) + " " + token.value);
            } while (token.type != TokenType.kEOF);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
