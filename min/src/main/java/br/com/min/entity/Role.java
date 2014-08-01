package br.com.min.entity;

public enum Role {

	ADMIN {
		public String getNome() {
			return "Administrador do sistema";
		}
		public String getDescricao() {
			return "Administrador do sistema. Tem acesso completo � todas as funcionalidades";
		}
	},	CAIXA{
		public String getNome() {
			return "Caixa";
		}
		public String getDescricao() {
			return "Pode fechar comandas, lan�ar pagamentos e tem acesso � relat�rios financeiros.";
		}
	},
	OPERACIONAL{
		public String getNome() {
			return "Operacional";
		}
		public String getDescricao() {
			return "Pode lan�ar servi�os e produtos e tem acesso � relat�rios referentes a si mesmo.";
		}
	};
	
	public abstract String getNome();
	public abstract String getDescricao();
	
}
