import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private List<ItemPedido> itens;
    private LocalDateTime dataHora;
    private Pagamento pagamento;
    private boolean finalizado;

    public Pedido() {
        this.itens = new ArrayList<>();
        this.finalizado = false;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (finalizado) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido já finalizado.");
        }
        if (!produto.temEstoque(quantidade)) {
            throw new IllegalStateException("Produto " + produto.getNome() + " sem estoque suficiente.");
        }
        itens.add(new ItemPedido(produto, quantidade));
    }

    public double calcularTotal() {
        double total = 0.0;
        for (ItemPedido item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public boolean finalizar(Pagamento pagamento) {
        if (itens.isEmpty()) {
            System.out.println("Não é possível finalizar: pedido sem itens.");
            return false;
        }

        for (ItemPedido item : itens) {
            if (!item.getProduto().temEstoque(item.getQuantidade())) {
                System.out.println("Não é possível finalizar: estoque insuficiente para " + item.getProduto().getNome());
                return false;
            }
        }

        double total = calcularTotal();
        if (!pagamento.isSuficiente(total)) {
            System.out.println("Não é possível finalizar: pagamento insuficiente.");
            return false;
        }

        for (ItemPedido item : itens) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }

        this.pagamento = pagamento;
        this.dataHora = LocalDateTime.now();
        this.finalizado = true;
        return true;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public boolean isFinalizado() {
        return finalizado;
    }
}
