package br.com.min.entity;

public enum Role {

	ADMIN {
		public String getNome() {
			return "Administrador do sistema";
		}
		public String getDescricao() {
			return "Administrador do sistema. Tem acesso completo à todas as funcionalidades";
		}
	},	CAIXA{
		public String getNome() {
			return "Caixa";
		}
		public String getDescricao() {
			return "Pode fechar comandas, lançar pagamentos e tem acesso à relatórios financeiros.";
		}
	},
	OPERACIONAL{
		public String getNome() {
			return "Operacional";
		}
		public String getDescricao() {
			return "Pode lançar serviços e produtos e tem acesso à relatórios referentes a si mesmo.";
		}
	};
	
	public abstract String getNome();
	public abstract String getDescricao();
	
}
