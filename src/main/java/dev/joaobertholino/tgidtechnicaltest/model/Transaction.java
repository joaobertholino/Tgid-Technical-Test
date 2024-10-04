package dev.joaobertholino.tgidtechnicaltest.model;

import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_TRANSACTION")
public class Transaction implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(nullable = false, name = "enterprise_id")
	private Enterprise enterprise;

	@ManyToOne
	@JoinColumn(nullable = false, name = "client_id")
	private Client client;

	@Enumerated(value = EnumType.ORDINAL)
	@Column(nullable = false)
	private TransactionType transactionType;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal value;

	@Column(nullable = false, scale = 2)
	private BigDecimal taxValue;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal subTotal;

	public Transaction() {
	}

	public Transaction(Enterprise enterprise, Client client, TransactionType transactionType, BigDecimal value, BigDecimal taxValue, BigDecimal subTotal) {
		this.enterprise = enterprise;
		this.client = client;
		this.transactionType = transactionType;
		this.value = value;
		this.taxValue = taxValue;
		this.subTotal = subTotal;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(BigDecimal taxValue) {
		this.taxValue = taxValue;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Transaction that = (Transaction) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
