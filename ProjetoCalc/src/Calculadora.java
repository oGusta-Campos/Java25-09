import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Calculadora {

    // --- MÉTODOS DE LÓGICA PRINCIPAL ---

    /**
     * Converte uma expressão Infixa para Pós-fixa.
     * Exemplo: ( 5 + 3 ) * 2  ->  5 3 + 2 *
     */
    private static String infixParaPosfixa(String expressao) {
        StringBuilder resultado = new StringBuilder();
        Stack<String> pilha = new Stack<>();
        for (String token : expressao.trim().split("\\s+")) {
            if (ehOperador(token)) {
                while (!pilha.isEmpty() && obterPrecedencia(pilha.peek()) >= obterPrecedencia(token)) {
                    resultado.append(pilha.pop()).append(" ");
                }
                pilha.push(token);
            } else if (token.equals("(")) {
                pilha.push(token);
            } else if (token.equals(")")) {
                while (!pilha.peek().equals("(")) {
                    resultado.append(pilha.pop()).append(" ");
                }
                pilha.pop(); // Descarta o "("
            } else { // É um número
                resultado.append(token).append(" ");
            }
        }
        while (!pilha.isEmpty()) {
            resultado.append(pilha.pop()).append(" ");
        }
        return resultado.toString().trim();
    }

    /**
     * Converte uma expressão Pré-fixa para Pós-fixa.
     * Exemplo: * + 5 3 2  ->  5 3 + 2 *
     */
    private static String prefixoParaPosfixa(String expressao) {
        Stack<String> pilha = new Stack<>();
        String[] tokens = expressao.trim().split("\\s+");
        Collections.reverse(Arrays.asList(tokens)); // Lê a expressão da direita para a esquerda

        for (String token : tokens) {
            if (ehOperador(token)) {
                String op1 = pilha.pop();
                String op2 = pilha.pop();
                pilha.push(op1 + " " + op2 + " " + token);
            } else {
                pilha.push(token);
            }
        }
        return pilha.pop();
    }

    /**
     * MÉTODO UNIFICADO: Converte uma expressão Pós-fixa para Infixa ou Pré-fixa.
     */
    private static String converterPosfixa(String expressaoPosfixa, String formatoDestino) {
        Stack<String> pilha = new Stack<>();
        for (String token : expressaoPosfixa.trim().split("\\s+")) {
            if (ehOperador(token)) {
                String op2 = pilha.pop();
                String op1 = pilha.pop();
                if (formatoDestino.equals("infixa")) {
                    pilha.push("( " + op1 + " " + token + " " + op2 + " )");
                } else { // "prefixa"
                    pilha.push(token + " " + op1 + " " + op2);
                }
            } else {
                pilha.push(token);
            }
        }
        return pilha.pop();
    }

    /**
     * Calcula o resultado de uma expressão Pós-fixa.
     */
    private static double calcularPosfixa(String expressao) {
        Stack<Double> pilha = new Stack<>();
        for (String token : expressao.trim().split("\\s+")) {
            if (ehOperador(token)) {
                double op2 = pilha.pop();
                double op1 = pilha.pop();
                switch (token) {
                    case "+": pilha.push(op1 + op2); break;
                    case "-": pilha.push(op1 - op2); break;
                    case "*": pilha.push(op1 * op2); break;
                    case "/": pilha.push(op1 / op2); break;
                }
            } else {
                pilha.push(Double.parseDouble(token));
            }
        }
        return pilha.pop();
    }

    // --- MÉTODOS AUXILIARES ---

    private static boolean ehOperador(String token) {
        return "+-*/".contains(token) && token.length() == 1;
    }

    private static int obterPrecedencia(String op) {
        switch (op) {
            case "+": case "-": return 1;
            case "*": case "/": return 2;
            default: return -1; // Para parênteses ou números
        }
    }

    // --- MÉTODO PRINCIPAL (EXECUÇÃO) ---

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("## CALCULADORA DE NOTAÇÕES  ##");
        System.out.println("Instruções: Separe todos os elementos (números, operadores, parênteses) com espaços.");
        System.out.print("Digite o tipo de notação (infixa, posfixa, prefixa): ");
        String tipo = scanner.nextLine().toLowerCase();

        System.out.print("Digite a expressão: ");
        String expressao = scanner.nextLine();

        try {
            // 1. Converte a expressão de entrada para Pós-fixa, que é a nossa base.
            String expressaoPosfixa = "";
            switch (tipo) {
                case "infixa":
                    expressaoPosfixa = infixParaPosfixa(expressao);
                    break;
                case "posfixa":
                    expressaoPosfixa = expressao;
                    break;
                case "prefixa":
                    expressaoPosfixa = prefixoParaPosfixa(expressao);
                    break;
                default:
                    System.out.println("Tipo de notação inválido!");
                    scanner.close();
                    return;
            }

            // 2. Calcula o resultado usando a expressão Pós-fixa.
            double resultado = calcularPosfixa(expressaoPosfixa);

            // 3. Converte a Pós-fixa para os outros dois formatos para exibição.
            String expressaoInfixa = converterPosfixa(expressaoPosfixa, "infixa");
            String expressaoPrefixa = converterPosfixa(expressaoPosfixa, "prefixa");

            // 4. Exibe tudo.
            System.out.println("\n--- RESULTADOS ---");
            System.out.println("Cálculo Final........: " + resultado);
            System.out.println("--------------------");
            System.out.println("Notação Infixa.....: " + expressaoInfixa);
            System.out.println("Notação Pós-fixa...: " + expressaoPosfixa);
            System.out.println("Notação Pré-fixa...: " + expressaoPrefixa);

        } catch (Exception e) {
            System.out.println("\nErro! A expressão parece ser inválida ou está mal formatada.");
            // e.printStackTrace(); // Descomente para depurar o erro exato
        }

        scanner.close();
    }
}