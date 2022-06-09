package com.company;



import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class Lexer {
    private final InputStream is;
    private int symbol;
    private int position;

    public static class Token {
        String value;
        TokenType type;
    }

    private static final Map<String, TokenType> KEYWORDS;

    static {
        KEYWORDS = new HashMap<String, TokenType>();
        KEYWORDS.put("(", TokenType.kLeftParenthesis);
        KEYWORDS.put(")", TokenType.kRightParenthesis);
        KEYWORDS.put("xor", TokenType.kXor);
        KEYWORDS.put("or", TokenType.kOr);
        KEYWORDS.put("and", TokenType.kAnd);
        KEYWORDS.put(":=", TokenType.kAssignment);
        KEYWORDS.put("if", TokenType.kIf);
        KEYWORDS.put("then", TokenType.kThen);
        KEYWORDS.put("else", TokenType.kElse);
        KEYWORDS.put(";", TokenType.kSemicolon);
        KEYWORDS.put("@eof", TokenType.kEOF);
    }

    private static final Set<String> SEPARATORS;

    static {
        SEPARATORS = new HashSet<String>();
        SEPARATORS.add("(");
        SEPARATORS.add(")");
        SEPARATORS.add(":=");
        SEPARATORS.add(";");

    }

    public static final Pattern identifier = compile("[a-z_A-Z]([a-zA-Z_]|[0-9])*");

    // Число не может начинаться с 0. т.е первый символ 1-9 далее сколько угодно цифр или просто 0
    private static final Pattern integer = compile("[1-9]\\d*|0");

    public Lexer(InputStream is) throws ParseException, IOException {
        this.is = is;
        position = 0;
        read();
    }

    private void read() throws ParseException {
        position++;
        try {
            symbol = is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), position);
        }
    }

    private
    String eat() throws ParseException {
        StringBuilder result = new StringBuilder();
        String tmp = "";

        while (Character.isWhitespace(symbol)) read();
        if (symbol == -1) return "@eof"; // конец последовательности. Для простоты вернем такую строку

        while (!(Character.isWhitespace(symbol) || symbol == -1)) {
            tmp += (char) symbol;

            // Встретился разделитель. Мы должны прочесть следующий сивол для гарантии продвижения. и вернуть этот самый разделитель
            if (SEPARATORS.contains(tmp)) { read(); return tmp; }

            // Проверяем нужно ли накапливать токен дальше
            if (!isSubtoken(tmp)) return result.toString();

            result.append((char) symbol);
            read();
        }
        return result.toString();
    }

    private boolean isSubtoken( String str) {
        // Проверяем что текущая строка является подпоследовательностью токена
        if (str.equals(":")) return true;
        //все ключевые слова входят в подпоследовательность identifier (кроме скобок, но они обработаны отдельно как SEPARATORS)
        return identifier.matcher(str).matches() || integer.matcher(str).matches();
    }

    // Ищем следующий токен
    public Token next() throws ParseException {
        Token result = new Token();
        result.value = eat();

        if (KEYWORDS.containsKey(result.value))
            result.type = KEYWORDS.get(result.value);
        else if (identifier.matcher(result.value).find())
            result.type = TokenType.kIdentifier;
        else if (integer.matcher(result.value).find())
            result.type = TokenType.kInteger;
        else {
            // Встетился неизвестный токен Кидаем ошибку
            throw new AssertionError(String.format("Illegal character '%s' at positon %d", (char) symbol, position));
        }
        return result;
    }
}
