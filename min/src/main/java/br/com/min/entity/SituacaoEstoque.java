package br.com.min.entity;

public enum SituacaoEstoque {

	BOA{
		public String getIcon() {
			return "&#61845;";
		}
		public String getCssClass(){
			return "success";
		}
		public String getTexto(){
			return "O estoque est� acima dos n�veis cr�ticos.";
		}
		public String getNome(){
			return "Boa";
		}
	},
	ALERTA {
		public String getIcon() {
			return "&#61730;";
		}
		public String getCssClass(){
			return "warning";
		}
		public String getTexto(){
			return "O estoque est� abaixo da quantidade m�nima aceit�vel.";
		}
		public String getNome(){
			return "Alerta";
		}
	},
	CRITICA {
		public String getIcon() {
			return "&#61907;";
		}
		public String getCssClass(){
			return "danger";
		}
		public String getTexto(){
			return "O estoque est� muito baixo da quantidade m�nima aceit�vel.";
		}
		public String getNome(){
			return "Cr�tica";
		}
	};
	
	public abstract String getIcon();
	public abstract String getCssClass();
	public abstract String getTexto();
	public abstract String getNome();
	
}
