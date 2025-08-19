import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Lista de opções disponíveis para o usuário
        List<List<String>> opcoes = ClienteExchangeRate.obterMoedasSuportadas();

        boolean continuar = true;
        while (continuar) {
            System.out.println("==== Conversor de Moedas ====");
            // Exibe as opções de moedas suportadas
            for (int i = 0; i < opcoes.size(); i++) {
                List<String> opcao = opcoes.get(i);
                System.out.printf("%d: %s - %s%n", i + 1, opcao.get(0), opcao.get(1));
            }
            // Opção extra para sair
            System.out.printf("%d: Encerrar programa%n", opcoes.size() + 1);

            // Seleção da moeda de origem
            System.out.print("Escolha o número da moeda de ORIGEM (ou escolha para encerrar): ");
            int opcaoOrigem = scanner.nextInt();
            if (opcaoOrigem == opcoes.size() + 1) {
                System.out.println("Programa encerrado!");
                break;
            }
            if (opcaoOrigem < 1 || opcaoOrigem > opcoes.size()) {
                System.out.println("Opção inválid, Tente novamente!");
                continue;
            }

            // Seleção da moeda de destino
            System.out.print("Escolha o número da moeda de DESTINO: ");
            int opcaoDestino = scanner.nextInt();
            if (opcaoDestino < 1 || opcaoDestino > opcoes.size()) {
                System.out.println("Opção inválida! Tente novamente.");
                continue;
            }

            // Solicita o valor a ser convertido
            System.out.print("Digite o valor na moeda de origem que deseja converter: ");
            double valor = scanner.nextDouble();

            String codigoMoedaOrigem = opcoes.get(opcaoOrigem - 1).get(0);
            String descricaoMoedaOrigem = opcoes.get(opcaoOrigem - 1).get(1);
            String codigoMoedaDestino = opcoes.get(opcaoDestino - 1).get(0);
            String descricaoMoedaDestino = opcoes.get(opcaoDestino - 1).get(1);

            double valorConvertido = ClienteExchangeRate.converter(valor, codigoMoedaOrigem, codigoMoedaDestino);

            System.out.printf("O valor %2.2f em \"%s\" equivale a: %.2f em \"%s\"%n",
                    valor, descricaoMoedaOrigem, valorConvertido, descricaoMoedaDestino);

            System.out.println("----------------------------------------");

            // Pausa e pergunta se o usuário deseja continuar
            System.out.print("Deseja realizar outra conversão? (S/N): ");
            String resposta = scanner.next();
            if (!resposta.equalsIgnoreCase("S")) {
                continuar = false;
                System.out.println("Programa encerrado!");
            }
        }

        scanner.close();
    }
}