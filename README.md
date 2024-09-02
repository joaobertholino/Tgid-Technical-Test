
# Teste técnico desenvolvedor Java Back-End TGID

API de transações entre Cliente e Empresa, referente ao teste técnico para a vaga de desenvolvedor Back-End da empresa TGID, utilizando Spring Data JPA com database PostgreSQL para persistencia de dados e Spring Java Mail Sender com MailTrap para simulação de envio de emails para Clientes.


## Documentação dos endpoints

#### Realiza uma transação de Cliente para Empresa

```POST
  POST /transaction?enterpriseId={ID}&clientId={ID}&value={VALUE}&transactionType={TYPE}
```

| Parâmetro       | Tipo            | Obrigatório | Descrição                                                                            |
|-----------------|-----------------|-------------|--------------------------------------------------------------------------------------|
| `enterpriseId`  | `Integer`       | Sim         | O ID da empresa na qual a transação será realizada.                                   |
| `clientId`      | `Integer`       | Sim         | O ID do cliente que está realizando a transação.                                      |
| `value`         | `BigDecimal`    | Sim         | O valor da transação. Deve ser um número decimal positivo.                            |
| `transactionType`| `TransactionType` | Sim     | O tipo de transação a ser realizada. Os valores possíveis são `DEPOSIT` ou `WITHDRAWAL`. |

## Interações com serviços externos

Ao realizar uma transação, seja do tipo `DEPOIST` ou `WITHDRAWAL`, é enviado um email de confirmação ao Cliente utilizando o servidor SMTP [MailTrap](https://mailtrap.io/home), simulando um envio de notificações, juntamente com o envio de um callback utilizando o [webhook.site](https://webhook.site/).
