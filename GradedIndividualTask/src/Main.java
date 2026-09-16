import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) {

        Produto coxinha = new Produto("P001", "Coxinha", "Lanche", 6.50, 20);
        Produto suco = new Produto("P002", "Suco de Laranja", "Bebida", 5.00, 15);
        Produto brigadeiro = new Produto("P003", "Brigadeiro", "Doce", 3.00, 30);

        System.out.println("=== Cardápio ===");
        System.out.println(coxinha);
        System.out.println(suco);
        System.out.println(brigadeiro);

        Pedido pedido = new Pedido();
        pedido.adicionarItem(coxinha, 2);
        pedido.adicionarItem(suco, 1);

        double total = pedido.calcularTotal();

        System.out.println("\n=== Itens do Pedido ===");
        for (ItemPedido item : pedido.getItens()) {
            System.out.println(item);
        }
        System.out.printf("Total do pedido: R$ %.2f%n", total);

        Pagamento pagamento = new Pagamento("dinheiro", 20.00);

        boolean sucesso = pedido.finalizar(pagamento);

        System.out.println("\n=== Comprovante ===");
        if (sucesso) {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            System.out.println("Venda finalizada com sucesso!");
            System.out.println("Data/Hora: " + pedido.getDataHora().format(formato));
            System.out.println(pedido.getPagamento());
            System.out.printf("Troco: R$ %.2f%n", pagamento.calcularTroco(total));

            System.out.println("\n=== Estoque atualizado ===");
            System.out.println(coxinha);
            System.out.println(suco);
        } else {
            System.out.println("Venda não pôde ser finalizada.");
        }
    }
}