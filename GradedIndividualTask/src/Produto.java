
public class Produto {

    private String codigo;
    private String nome;
    private String categoria;
    private double precoUnitario;
    private int quantidadeEstoque;

    public Produto(String codigo, String nome, String categoria, double precoUnitario, int quantidadeEstoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.categoria = categoria;
        this.precoUnitario = precoUnitario;
        this.quantidadeEstoque = quantidadeEstoque;
    }


    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.precoUnitario = precoUnitario;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }


    public boolean temEstoque(int quantidade) {
        return quantidadeEstoque >= quantidade;
    }


    public void baixarEstoque(int quantidade) {
        if (!temEstoque(quantidade)) {
            throw new IllegalStateException("Estoque insuficiente para o produto " + nome + ".");
        }
        this.quantidadeEstoque -= quantidade;
    }

    public void atualizarEstoque(int quantidade) {
        int novoEstoque = this.quantidadeEstoque + quantidade;
        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Estoque não pode ficar negativo.");
        }
        this.quantidadeEstoque = novoEstoque;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) - R$ %.2f - Estoque: %d",
                codigo, nome, categoria, precoUnitario, quantidadeEstoque);
    }
}
