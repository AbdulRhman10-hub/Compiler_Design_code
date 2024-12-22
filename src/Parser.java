import java.util.*;

public class Parser {

    public static void main(String[] args) {
        String startSymbol = "S";
        Map<String, List<String>> grammarRules;

        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            grammarRules = enterGrammar(scanner);
            if (isSimpleGrammar(grammarRules)) {
                System.out.println("\n✅ The Grammar is Simple ✅");
                break;
            } else {
                System.out.println("\n❌ The Grammar is NOT Simple. Please enter a valid Simple Grammar.");
            }
        }

        while (true) {
            System.out.println("\n1-Enter another grammar ");
            System.out.println("2-Enter a String to Check");
            System.out.println("3-Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                while (true) {
                    grammarRules = enterGrammar(scanner);
                    if (isSimpleGrammar(grammarRules)) {
                        System.out.println("\n✅ The new Grammar is Simple ✅");
                        break;
                    } else {
                        System.out.println("\n❌ The Grammar is NOT Simple. Please enter a valid Simple Grammar.");
                    }
                }
            } else if (choice == 2) {
                System.out.print("Enter the string to be checked: ");
                String inputString = scanner.nextLine();
                boolean result = parseString(grammarRules, startSymbol, inputString);
                if (result) {
                    System.out.println("\n🎉 The input string is Accepted.");
                } else {
                    System.out.println("\n❌ The input string is Rejected.");
                }
            } else if (choice == 3) {
                System.out.println("Exiting... Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
        scanner.close();
    }

    private static Map<String, List<String>> enterGrammar(java.util.Scanner scanner) {
        System.out.println("\n👇 Enter Your Grammar Rules 👇");
        Map<String, List<String>> grammarRules = new HashMap<>();
        String[] nonTerminals = {"S", "B"};

        for (String nonTerminal : nonTerminals) {
            List<String> rules = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                System.out.print("Rule Number " + (i + 1) + " for non-terminal '" + nonTerminal + "': ");
                String rule = scanner.nextLine().trim();
                rules.add(rule);
            }
            grammarRules.put(nonTerminal, rules);
        }
        return grammarRules;
    }

    private static boolean isSimpleGrammar(Map<String, List<String>> rules) {
        for (Map.Entry<String, List<String>> entry : rules.entrySet()) {
            Set<Character> terminalsSeen = new HashSet<>();
            for (String production : entry.getValue()) {
                if (production.isEmpty() || !Character.isLowerCase(production.charAt(0))) {
                    return false;
                }
                char firstTerminal = production.charAt(0);
                if (terminalsSeen.contains(firstTerminal)) {
                    return false;
                }
                terminalsSeen.add(firstTerminal);
            }
        }
        return true;
    }

    private static boolean parseString(Map<String, List<String>> rules, String startSymbol, String string) {
        Stack<String> stack = new Stack<>();
        stack.push(startSymbol);
        int index = 0;
        System.out.println("\nThe input String: " + Arrays.asList(string.toCharArray()));

        while (!stack.isEmpty()) {
            String top = stack.pop();
            if (index >= string.length()) {
                System.out.println("❌ No more input to check.");
                return false;
            }
            char currentChar = string.charAt(index);
            if (Character.isLowerCase(top.charAt(0))) {
                if (top.charAt(0) == currentChar) {
                    index++;
                } else {
                    System.out.println("❌ Mismatch: Expected '" + top + "' but found '" + currentChar + "'.");
                    return false;
                }
            } else if (Character.isUpperCase(top.charAt(0))) {
                boolean expanded = false;
                for (String production : rules.get(top)) {
                    if (production.charAt(0) == currentChar) {
                        for (int i = production.length() - 1; i >= 0; i--) {
                            stack.push(String.valueOf(production.charAt(i)));
                        }
                        expanded = true;
                        break;
                    }
                }
                if (!expanded) {
                    System.out.println("❌ No valid rule for Non-Terminal '" + top + "' with '" + currentChar + "' at position " + index + ".");
                    return false;
                }
            }
        }
        return index == string.length();
    }
}
