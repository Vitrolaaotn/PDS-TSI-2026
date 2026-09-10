# Dossiê de Análise de Sistemas — App IFBank (Módulo Pix)

*Estudo de caso integrado: Elicitação de Requisitos (Casos de Uso), Comportamento (Diagrama de Sequência) e Estrutura (Diagrama de Classes).*

---

## 1. Introdução

Este documento apresenta o dossiê de análise da funcionalidade "Área do Pix" do aplicativo IFBank, elaborado a partir do estudo de caso proposto. O objetivo é demonstrar, de forma integrada, as três etapas fundamentais da modelagem de um sistema orientado a objetos: o levantamento de requisitos por meio de Casos de Uso, a modelagem do comportamento dinâmico por meio do Diagrama de Sequência e a modelagem da estrutura estática por meio do Diagrama de Classes.

O escopo contempla as funcionalidades de consulta de saldo, cadastro de chaves Pix e realização de transferências via Pix, respeitando as regras de negócio definidas: autenticação prévia do cliente, validação da chave de destino junto ao Banco Central (BACEN), verificação de saldo suficiente e o registro da transação com emissão de comprovante.

---

## 2. Etapa 1 — Levantamento de Requisitos (Casos de Uso)

### 2.1 Diagrama de Casos de Uso

O diagrama abaixo representa os atores do sistema — o Cliente e o Sistema BACEN — e os quatro casos de uso principais da Área Pix. Os casos de uso "Consultar Saldo", "Cadastrar Chave Pix" e "Realizar Pix" incluem (`<<include>>`) obrigatoriamente o caso de uso "Autenticar Usuário", pois nenhuma dessas ações pode ser executada sem que o cliente esteja previamente logado no aplicativo.

![Diagrama de Casos de Uso da Área Pix do IFBank](Casos%20de%20Uso%20IFbank.png)

*Figura 1 — Diagrama de Casos de Uso da Área Pix do IFBank*

### 2.2 Descrição Textual do Caso de Uso: Realizar Pix

#### Identificação

| Campo | Descrição |
|---|---|
| **Caso de Uso** | Realizar Pix |
| **Ator Principal** | Cliente |
| **Ator Secundário** | Sistema BACEN |
| **Objetivo** | Permitir que o cliente transfira valores para uma chave Pix de destino de forma rápida e segura. |

#### Pré-condições

- O cliente deve estar autenticado (logado) no aplicativo IFBank (caso de uso "Autenticar Usuário" já executado).
- O cliente deve possuir uma conta ativa com saldo disponível maior que zero.
- O aplicativo deve possuir conexão ativa com o sistema central do Banco Central (BACEN) para validação de chaves.

#### Pós-condições

- O valor da transferência foi debitado da conta do cliente.
- Uma nova transação foi registrada no extrato da conta.
- Um comprovante da transação foi exibido na tela do cliente.

#### Fluxo Principal (FP)

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

#### Fluxos de Exceção (FE)

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

---

## 3. Etapa 2 — Comportamento (Diagrama de Sequência)

O diagrama a seguir detalha a troca de mensagens entre os objetos do sistema durante o Fluxo Principal do caso de uso "Realizar Pix", a partir do momento em que o cliente já informou a chave de destino e o valor. As setas contínuas com ponta preenchida representam chamadas síncronas de método; as setas tracejadas representam os respectivos retornos. O bloco `alt` (alternativo) mostra a bifurcação de comportamento conforme o resultado da verificação de saldo.

![Diagrama de Sequência do Fluxo Principal de Realizar Pix](Diagrama%20Sequencial%20IFbank.png)

*Figura 2 — Diagrama de Sequência do Fluxo Principal de "Realizar Pix"*

### Leitura do diagrama

- O Cliente informa a chave Pix e o valor à `:TelaPix`, que delega a operação ao `:ControladorPix`.
- O `:ControladorPix` consulta o `:SistemaBACEN` para validar a chave e obter os dados do destinatário.
- O `:ControladorPix` solicita à `:Conta` a verificação de saldo por meio de `verificarSaldo(valor)`.
- Se o saldo for suficiente (ramo `alt`), a `:Conta` é debitada, a transação é registrada e um comprovante é retornado à tela.
- Se o saldo for insuficiente (ramo `else`), o controlador retorna uma mensagem de erro, que é exibida ao cliente, sem que ocorra débito.

---

## 4. Etapa 3 — Estrutura (Diagrama de Classes)

O diagrama de classes a seguir representa o núcleo estrutural do sistema, derivado diretamente das entidades e ações identificadas nas Etapas 1 e 2. Os atributos foram definidos como privados (`-`), seguindo o princípio de encapsulamento, e expostos por meio de métodos públicos (`+`).

![Diagrama de Classes do núcleo do sistema IFBank](Diagrama%20de%20Classes%20IFbank.png)

*Figura 3 — Diagrama de Classes do núcleo do sistema IFBank (Área Pix)*

### 4.1 Classes e responsabilidades

- **Cliente** — representa o titular da conta; é responsável por se autenticar no sistema.
- **Conta** — representa a conta bancária do cliente; concentra as regras de saldo (verificar, debitar, creditar) e mantém o extrato de transações e as chaves Pix cadastradas.
- **Transacao** — representa cada movimentação (ex.: um Pix enviado), registrando valor, data, tipo e a chave de destino; é responsável por gerar o comprovante.
- **ChavePix** — representa uma chave cadastrada pelo cliente, com seu tipo, valor, data de cadastro e status (ativa/inativa).
- **TipoChave** — enumeração que restringe o tipo de uma ChavePix aos valores CPF, CNPJ, EMAIL, TELEFONE ou ALEATORIA.

### 4.2 Relacionamentos e multiplicidades

- **Cliente 1 — 1..\* Conta**: um cliente pode possuir uma ou mais contas no banco, e cada conta pertence a exatamente um cliente.
- **Conta 1 — 0..\* Transacao**: uma conta acumula zero ou várias transações em seu extrato ao longo do tempo; cada transação pertence a uma única conta.
- **Conta 1 — 0..\* ChavePix**: uma conta pode ter zero ou várias chaves Pix cadastradas; cada chave pertence a uma única conta.
- **ChavePix — TipoChave**: cada chave Pix está associada a exatamente um tipo, representado pela enumeração TipoChave.

### 4.3 Rastreabilidade com o Diagrama de Sequência

Os métodos definidos nas classes correspondem diretamente às mensagens trocadas no Diagrama de Sequência da Etapa 2: `verificarSaldo(valor)` e `debitar(valor)` em `Conta` atendem às chamadas do `:ControladorPix`; `gerarComprovante()` em `Transacao` corresponde à geração do comprovante exibido ao final do Fluxo Principal; e `autenticar(senha)` em `Cliente` sustenta o caso de uso incluído "Autenticar Usuário".

---

## 5. Entregas

Em atendimento ao solicitado, este dossiê reúne os quatro itens exigidos:

1. Diagrama de Casos de Uso (Figura 1, seção 2.1).
2. Descrição textual detalhada do caso de uso "Realizar Pix" (seção 2.2).
3. Diagrama de Sequência do Fluxo Principal do Pix (Figura 2, seção 3).
4. Diagrama de Classes do núcleo do sistema (Figura 3, seção 4).