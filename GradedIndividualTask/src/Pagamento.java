
public class Pagamento {

    private String tipo; // ex.: "dinheiro", "pix", "cartao de credito", "cartao de debito"
    private double valorPago;

    public Pagamento(String tipo, double valorPago) {
        this.tipo = tipo;
        this.valorPago = valorPago;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValorPago() {
        return valorPago;
    }

    public boolean isSuficiente(double total) {
        return valorPago >= total;
    }


    public double calcularTroco(double total) {
        if (!isSuficiente(total)) {
            throw new IllegalStateException("Pagamento insuficiente, não há troco a calcular.");
        }
        return valorPago - total;
    }

    @Override
    public String toString() {
        return String.format("Pagamento: %s - Valor pago: R$ %.2f", tipo, valorPago);
    }
}