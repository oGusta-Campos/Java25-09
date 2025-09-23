import java.util.Scanner;
import java.util.Stack;

public class InverterPalavrasComPilha {

    // Método para inverter uma palavra usando Pilha
    public static String inverterPalavra(String palavra) {
        Stack<Character> pilha = new Stack<>();
        for (char c : palavra.toCharArray()) {
            pilha.push(c);
        }

        StringBuilder invertida = new StringBuilder();
        while (!pilha.isEmpty()) {
            invertida.append(pilha.pop());
        }
        return invertida.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite uma frase:");
        String frase = sc.nextLine();

        // Divide a frase em palavras
        String[] palavras = frase.split(" ");
        StringBuilder resultado = new StringBuilder();

        // Inverte cada palavra individualmente
        for (String palavra : palavras) {
            resultado.append(inverterPalavra(palavra)).append(" ");
        }

        // Mostra a frase final sem espaço extra no final
        System.out.println("Resultado: " + resultado.toString().trim());

        sc.close();
    }
}
