package br.com.min.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.min.entity.LancamentoComissao;
import br.com.min.entity.Pessoa;
import br.com.min.entity.TipoComissao;

@Repository
public class ComissaoDAO extends BaseDAO<LancamentoComissao> {

	public List<LancamentoComissao> findByPeriodo(Date periodoInicio, Date periodoFim, Pessoa funcionario){		
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(periodoInicio);
		inicio.set(Calendar.HOUR_OF_DAY, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);
		inicio.set(Calendar.MILLISECOND, 0);
		
		Calendar fim = Calendar.getInstance();
		fim.setTime(periodoFim);
		fim.set(Calendar.HOUR_OF_DAY, 23);
		fim.set(Calendar.MINUTE, 59);
		fim.set(Calendar.SECOND, 59);
		fim.set(Calendar.MILLISECOND, 999);
		
		Session session = getSession();
		Criteria criteria = session.createCriteria(LancamentoComissao.class);
		if(funcionario != null){
			criteria.add(Restrictions.eq("funcionario", funcionario));
		}
		criteria.add(Restrictions.between("dataCriacao", inicio.getTime(), fim.getTime()));
		criteria.add(Restrictions.ne("valor", 0d));
		
		List<LancamentoComissao> resultado = criteria.list();
		return resultado;
	}
	
	public List<LancamentoComissao> findLancamentosComissaoVendaDoMes(Pessoa vendedor, Date dataCriacao, TipoComissao tipo) {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(dataCriacao);
		inicio.set(Calendar.DAY_OF_MONTH, 1);
		inicio.set(Calendar.HOUR_OF_DAY, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);
		inicio.set(Calendar.MILLISECOND, 0);
		
		Calendar fim = Calendar.getInstance();
		fim.setTime(dataCriacao);
		fim.set(Calendar.DAY_OF_MONTH, fim.getActualMaximum(Calendar.DAY_OF_MONTH));
		fim.set(Calendar.HOUR_OF_DAY, 23);
		fim.set(Calendar.MINUTE, 59);
		fim.set(Calendar.SECOND, 59);
		fim.set(Calendar.MILLISECOND, 999);
		
		Session session = getSession();
		Criteria criteria = session.createCriteria(LancamentoComissao.class);
		if(vendedor != null){
			criteria.add(Restrictions.isNotNull("funcionario"));
			criteria.add(Restrictions.eq("funcionario", vendedor));
		}
		criteria.add(Restrictions.between("dataCriacao", inicio.getTime(), fim.getTime()));
		if(tipo != null){
			criteria.add(Restrictions.eq("tipo", tipo));
		}
		
		List<LancamentoComissao> resultado = criteria.list();
		
		return resultado;
	}

	public LancamentoComissao findById(Long id) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(LancamentoComissao.class);
		criteria.add(Restrictions.eq("id", id));
		
		LancamentoComissao result = (LancamentoComissao) criteria.uniqueResult();
		return result;
	}
	
}
