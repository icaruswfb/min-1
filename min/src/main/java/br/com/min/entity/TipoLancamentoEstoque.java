package br.com.min.entity;

public enum TipoLancamentoEstoque {

	ENTRADA {
		public String getDescricao() {
			return "Entrada";
		}
	},
	SAIDA {
		public String getDescricao() {
			return "Saída";
		}
	};
	
	public abstract String getDescricao();
	
}
