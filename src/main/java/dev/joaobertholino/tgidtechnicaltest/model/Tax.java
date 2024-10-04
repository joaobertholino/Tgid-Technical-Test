package dev.joaobertholino.tgidtechnicaltest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Tax implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@ManyToOne
	@JoinColumn(name = "enterprise_id")
	private Enterprise enterprise;

	@DecimalMax(value = "1.0")
	@DecimalMin(value = "0.0")
	private BigDecimal percent;

	public Tax() {
	}

	public Tax(TransactionType transactionType, Enterprise enterprise, BigDecimal percent) {
		this.transactionType = transactionType;
		this.enterprise = enterprise;
		this.percent = percent;
	}

	public Tax(Enterprise enterprise, Integer id, BigDecimal percent, TransactionType transactionType) {
		this.enterprise = enterprise;
		this.id = id;
		this.percent = percent;
		this.transactionType = transactionType;
	}

	@JsonIgnore
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

	public @DecimalMax(value = "1.0") @DecimalMin(value = "0.0") BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(@DecimalMax(value = "1.0") @DecimalMin(value = "0.0") BigDecimal percent) {
		this.percent = percent;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tax tax = (Tax) o;
		return id.equals(tax.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
