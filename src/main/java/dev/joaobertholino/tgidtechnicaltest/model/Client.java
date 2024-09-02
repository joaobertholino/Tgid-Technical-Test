package dev.joaobertholino.tgidtechnicaltest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "TB_CLIENT")
public class Client implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank
	private String name;

	@Email
	@Column(unique = true)
	private String email;

	@CPF
	@Column(unique = true)
	private String cpf;

	public Client() {
	}

	public Client(String name, String email, String cpf) {
		this.name = name;
		this.email = email;
		this.cpf = cpf;
	}

	public Client(String cpf, String email, Integer id, String name) {
		this.cpf = cpf;
		this.email = email;
		this.id = id;
		this.name = name;
	}

	public @CPF String getCpf() {
		return cpf;
	}

	public void setCpf(@CPF String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Client client = (Client) o;
		return id.equals(client.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
