# Etapa 1 — Levantamento de Requisitos (Casos de Uso)

## 1.1 Diagrama de Casos de Uso

O diagrama abaixo representa os atores do sistema — o Cliente e o Sistema BACEN — e os quatro casos de uso principais da Área Pix. Os casos de uso "Consultar Saldo", "Cadastrar Chave Pix" e "Realizar Pix" incluem (`<<include>>`) obrigatoriamente o caso de uso "Autenticar Usuário", pois nenhuma dessas ações pode ser executada sem que o cliente esteja previamente logado no aplicativo.

![Diagrama de Casos de Uso da Área Pix do IFBank](Casos%20de%20Uso%20IFbank.png)

*Figura 1 — Diagrama de Casos de Uso da Área Pix do IFBank*

## 1.2 Descrição Textual do Caso de Uso: Realizar Pix

### Identificação

| Campo | Descrição |
|---|---|
| **Caso de Uso** | Realizar Pix |
| **Ator Principal** | Cliente |
| **Ator Secundário** | Sistema BACEN |
| **Objetivo** | Permitir que o cliente transfira valores para uma chave Pix de destino de forma rápida e segura. |

### Pré-condições

- O cliente deve estar autenticado (logado) no aplicativo IFBank (caso de uso "Autenticar Usuário" já executado).
- O cliente deve possuir uma conta ativa com saldo disponível maior que zero.
- O aplicativo deve possuir conexão ativa com o sistema central do Banco Central (BACEN) para validação de chaves.

### Pós-condições

- O valor da transferência foi debitado da conta do cliente.
- Uma nova transação foi registrada no extrato da conta.
- Um comprovante da transação foi exibido na tela do cliente.

### Fluxo Principal (FP)

1. O cliente seleciona a opção "Realizar Pix" na Área Pix do aplicativo.
2. O sistema exibe a tela para informar a chave Pix de destino.
3. O cliente digita a chave Pix (CPF, CNPJ, e-mail, telefone ou chave aleatória) e confirma.
4. O sistema envia a chave para o Sistema BACEN para validação de existência e busca dos dados do destinatário.
5. O Sistema BACEN retorna os dados do destinatário (nome e instituição) associados à chave.
6. O sistema exibe os dados do destinatário para conferência do cliente.
7. O cliente confirma os dados e digita o valor a ser transferido.
8. O sistema verifica se o saldo da conta do cliente é suficiente para o valor informado.
9. O sistema solicita a confirmação final da operação (ex.: senha ou biometria).
10. O cliente confirma a operação.
11. O sistema debita o valor da conta do cliente.
12. O sistema registra a transação no extrato da conta.
13. O sistema gera e exibe o comprovante da transação na tela.

### Fluxos de Exceção (FE)

**[FE-01] Saldo Insuficiente**
- Ocorre após o passo 8 do Fluxo Principal, caso o valor informado seja maior que o saldo disponível.
- O sistema exibe a mensagem "Saldo insuficiente para realizar esta transação."
- O sistema retorna à tela de digitação do valor, mantendo a chave de destino já validada.
- O caso de uso é encerrado sem débito ou registro de transação.

**[FE-02] Chave Pix Inválida ou Inexistente**
- Ocorre após o passo 4, caso o Sistema BACEN não localize a chave informada.
- O sistema exibe a mensagem "Chave Pix não encontrada. Verifique e tente novamente."
- O sistema retorna à tela de digitação da chave Pix (passo 3).

**[FE-03] Falha de Comunicação com o BACEN**
- Ocorre durante o passo 4, caso o sistema não obtenha resposta do Sistema BACEN dentro do tempo esperado.
- O sistema exibe a mensagem "Não foi possível validar a chave no momento. Tente novamente mais tarde."
- O caso de uso é encerrado.

**[FE-04] Cliente Cancela a Operação**
- Pode ocorrer em qualquer passo entre o 2 e o 9.
- O sistema descarta os dados informados e retorna à tela inicial da Área Pix.