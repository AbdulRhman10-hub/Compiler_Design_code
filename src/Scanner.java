import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    // Enum to represent different types of tokens
    enum TokenType {
        KEYWORD, IDENTIFIER, OPERATOR, NUMERIC_CONSTANT, CHARACTER_CONSTANT, SPECIAL_CHARACTER, COMMENT, WHITESPACE, NEWLINE, UNKNOWN
    }

    // Class to represent a Token
    static class Token {
        TokenType type;
        String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("Token[type=%s, value='%s']", type, value);
        }
    }

    // List of C keywords
    private static final String[] keywords = {
            "int", "return", "if", "else", "while", "for", "float", "double", "char", "void"
    };

    // Regular expressions for different token types
    private static final Pattern pattern = Pattern.compile(
            "\\b(" + String.join("|", keywords) + ")\\b" +        // Keywords
                    "|\\b\\d+\\.?\\d*([eE][+-]?\\d+)?\\b" +               // Numeric constants
                    "|\\'.\\'" +                                          // Character constants
                    "|\\b[a-zA-Z_]\\w*\\b" +                              // Identifiers
                    "|[+\\-*/=<>!]+|\\|\\||&&|\\^|%" +                    // Operators
                    "|[{}();,]" +                                         // Special characters
                    "|//.*|/\\*.*?\\*/" +                                 // Comments
                    "|\\s+"                                               // White spaces
    );

    public static List<Token> scan(String code) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            String match = matcher.group();
            TokenType type;

            // Determine the token type
            if (isKeyword(match)) {
                type = TokenType.KEYWORD;
            } else if (match.matches("\\b\\d+\\.?\\d*([eE][+-]?\\d+)?\\b")) {
                type = TokenType.NUMERIC_CONSTANT;
            } else if (match.matches("\\'.\\'")) {
                type = TokenType.CHARACTER_CONSTANT;
            } else if (match.matches("\\b[a-zA-Z_]\\w*\\b")) {
                type = TokenType.IDENTIFIER;
            } else if (match.matches("[+\\-*/=<>!]+|\\|\\||&&|\\^|%")) {
                type = TokenType.OPERATOR;
            } else if (match.matches("[{}();,]")) {
                type = TokenType.SPECIAL_CHARACTER;
            } else if (match.matches("//.*|/\\*.*?\\*/")) {
                type = TokenType.COMMENT;
            } else if (match.matches("\\s+")) {
                if (match.contains("\n")) {
                    type = TokenType.NEWLINE;
                } else {
                    type = TokenType.WHITESPACE;
                }
            } else {
                type = TokenType.UNKNOWN;
            }

            tokens.add(new Token(type, match));
        }

        return tokens;
    }

    // Helper method to check if a word is a keyword
    private static boolean isKeyword(String word) {
        for (String keyword : keywords) {
            if (keyword.equals(word)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Scanner to get user input
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("Enter your C code :");

        // Read user input
        String userCode = scanner.nextLine();
        scanner.close();

        // Scan the input code
        List<Token> tokens = scan(userCode);

        // Print the tokens
        System.out.println("Scanned Tokens:");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}

