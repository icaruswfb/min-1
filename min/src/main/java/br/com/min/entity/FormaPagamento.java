package br.com.min.entity;

public enum FormaPagamento {

	Dinheiro {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Dinheiro";
		}
	},	
	Visa {
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "Visa";
		}
	},
	VisaElectron {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Visa Electron";
		}
	},
	MasterCard {
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "MasterCard";
		}
	},
	Maestro {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Maestro";
		}
	},
	Diners {
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "Diners";
		}
	},
	Amex {
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "Amex";
		}
	},
	Cheque {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Cheque";
		}
	},
	Credito {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Crédito";
		}
	};
	
	public abstract boolean isParcelavel();
	public abstract String getNome();
}
