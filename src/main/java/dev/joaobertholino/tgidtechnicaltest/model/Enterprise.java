package dev.joaobertholino.tgidtechnicaltest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "TB_ENTERPRISE")
public class Enterprise implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotBlank
	private String name;

	@CNPJ
	@Column(unique = true)
	private String cnpj;

	private BigDecimal balance;

	@OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL)
	private final List<Tax> taxList = new ArrayList<>();

	public Enterprise() {
	}

	public Enterprise(String name, String cnpj, BigDecimal balance) {
		this.name = name;
		this.cnpj = cnpj;
		this.balance = balance;
	}

	public Enterprise(BigDecimal balance, String cnpj, Integer id, String name) {
		this.balance = balance;
		this.cnpj = cnpj;
		this.id = id;
		this.name = name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public @CNPJ String getCnpj() {
		return cnpj;
	}

	public void setCnpj(@CNPJ String cnpj) {
		this.cnpj = cnpj;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public @NotBlank String getName() {
		return name;
	}

	public void setName(@NotBlank String name) {
		this.name = name;
	}

	public List<Tax> getTaxList() {
		return taxList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Enterprise that = (Enterprise) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
