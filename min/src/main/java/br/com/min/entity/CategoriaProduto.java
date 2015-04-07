package br.com.min.entity;

public enum CategoriaProduto {

	LOJA {
		public String getDescricao() {
			return "Loja";
		}
	},
	SALAO {
		public String getDescricao() {
			return "Salão";
		}
	};
	
	public abstract String getDescricao();
	
}
