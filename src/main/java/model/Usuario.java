package model;

import java.sql.Date;

/**
 * 
 */
public class Usuario {
	
	private int id;
	private String nome;
	private String email;
	private String senha;
	private String perfil;
	private boolean ativo;
	private Date data_cadastro;
	
	public Usuario() {
		
	}
	
	public Usuario(int id, String nome, String email, String senha, String perfil, 
			boolean ativo, Date data_cadastro) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.perfil = perfil;
		this.ativo = ativo;
		this.data_cadastro = data_cadastro;
		
	}



	public int getId() {
		return id;
	}
	
	public void setId(int i) {
		this.id = i;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public Date getData_cadastro() {
		return data_cadastro;
	}
	
	public void setData_cadastro(Date data) {
		this.data_cadastro = data;
	}

	/** Nome do perfil para exibir nas telas. */
	public String getPerfilRotulo() {
		if (perfil == null) return "";
		switch (perfil) {
			case "ADMIN": return "Administrador";
			case "TECNICO": return "Técnico";
			default: return "Solicitante";
		}
	}

}
