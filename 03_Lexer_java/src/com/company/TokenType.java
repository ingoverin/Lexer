package com.company;



public enum TokenType {
    kLeftParenthesis,    // (
    kRightParenthesis,   // )
    kXor,
    kOr,
    kAnd,
    kAssignment,         // :=
    kIf,
    kThen,
    kElse,
    kIdentifier,        // переменная
    kSemicolon,         // ;
    kInteger,           // число
    kEOF;

    static String to_string( TokenType type) {
        switch (type) {
            case kLeftParenthesis -> { return "LeftParenthesis";}
            case kRightParenthesis -> { return "RightParenthesis";}
            case kXor -> { return "Xor"; }
            case kOr -> { return "Or"; }
            case kAnd -> { return "And"; }
            case kAssignment -> { return "Assignment"; }
            case kIf -> { return "If"; }
            case kThen -> { return "Then"; }
            case kElse -> { return "Else"; }
            case kIdentifier -> { return "Identifier"; }
            case kSemicolon -> { return "Semicolon"; }
            case kInteger -> { return "Integer"; }
            case kEOF -> { return "EOF"; }
        }
        return null;
    }
}

