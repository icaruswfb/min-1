package br.com.min.entity;

public enum FormaPagamento {

	Dinheiro {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Dinheiro";
		}
		public boolean isCartaoCredito() {
			return false;
		}
	},	
	Visa {
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "Visa";
		}
		public boolean isCartaoCredito() {
			return true;
		}
	},
	VisaElectron {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Visa Electron";
		}
		public boolean isCartaoCredito() {
			return false;
		}
	},
	MasterCard {
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "MasterCard";
		}
		public boolean isCartaoCredito() {
			return true;
		}
	},
	Maestro {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Maestro";
		}
		public boolean isCartaoCredito() {
			return false;
		}
	},	
	HiperCard{
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "HiperCard";
		}
		public boolean isCartaoCredito() {
			return true;
		}
	},
	
	Diners {
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "Diners";
		}
		public boolean isCartaoCredito() {
			return true;
		}
	},
	Amex {
		public boolean isParcelavel() {
			return true;
		}
		public String getNome() {
			return "Amex";
		}
		public boolean isCartaoCredito() {
			return true;
		}
	},
	Cheque {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Cheque";
		}
		public boolean isCartaoCredito() {
			return false;
		}
	},
	Credito {
		public boolean isParcelavel() {
			return false;
		}
		public String getNome() {
			return "Crédito";
		}
		public boolean isCartaoCredito() {
			return false;
		}
	};
	
	public abstract boolean isParcelavel();
	public abstract String getNome();
	public abstract boolean isCartaoCredito();
}
