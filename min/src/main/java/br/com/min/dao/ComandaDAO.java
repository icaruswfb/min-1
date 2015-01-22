package br.com.min.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Comanda;
import br.com.min.entity.LancamentoProduto;
import br.com.min.entity.LancamentoServico;
import br.com.min.entity.Pagamento;
import br.com.min.entity.Pessoa;

@Repository
public class ComandaDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Pagamento pagar(Pagamento pagamento){
		Session session = sessionFactory.openSession();
		pagamento = (Pagamento)session.merge(pagamento);
		session.flush();
		return pagamento;
	}
	
	public Double findTotalCobradoPorCliente(Long clienteId){
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select sum(valorCobrado) from comanda where cliente_id = " + clienteId);
		Double result = (Double) query.uniqueResult();
		if(result == null){
			return 0d;
		}
		return result;
	}
	public Double findTotalPagoPorCliente(Long clienteId){
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select sum(p.valor) from pagamento p join comanda c on p.comanda_id = c.id where p.formaPagamento != 'Crédito' and c.cliente_id = " + clienteId);
		Double result = (Double) query.uniqueResult();
		if(result == null){
			return 0d;
		}
		return result;
	}
	
	public Long findUltimaAtualizacao(Long comandaId){
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select c.ultimaAtualizacao from Comanda c where c.id = " + comandaId);
		Long result = ((BigInteger) query.uniqueResult()).longValue();
		return result;
	}
	
	public LancamentoServico findLancamentoServicoById(Long id){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(LancamentoServico.class);
		criteria.add(Restrictions.eq("id", id));
		return (LancamentoServico) criteria.uniqueResult();
	}
	
	public LancamentoProduto findLancamentoProdutoById(Long id) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(LancamentoProduto.class);
		criteria.add(Restrictions.eq("id", id));
		return (LancamentoProduto) criteria.uniqueResult();
	}
	
	public LancamentoServico findLancamentoServicoByLancamentoProdutoId(Long id){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(LancamentoServico.class);
		criteria.createCriteria("produtosUtilizados").add(Restrictions.eq("id", id));
		return (LancamentoServico) criteria.uniqueResult();
	}
	
	public List<Comanda> findFechamento(Date dataInicio, Date dataFim){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Comanda.class);
		
		Calendar dataInicioPesquisa = Calendar.getInstance();
		dataInicioPesquisa.setTime(dataInicio);
		dataInicioPesquisa.set(Calendar.HOUR_OF_DAY, 0);
		dataInicioPesquisa.set(Calendar.MINUTE, 0);
		dataInicioPesquisa.set(Calendar.SECOND, 0);
		dataInicioPesquisa.set(Calendar.MILLISECOND, 0);
		Calendar dataFimPesquisa = Calendar.getInstance();
		dataFimPesquisa.setTime(dataFim);
		dataFimPesquisa.set(Calendar.HOUR_OF_DAY, 23);
		dataFimPesquisa.set(Calendar.MINUTE, 59);
		dataFimPesquisa.set(Calendar.SECOND, 59);
		dataFimPesquisa.set(Calendar.MILLISECOND, 999);
		
		criteria = criteria.add(
				Restrictions.or(Restrictions.between("abertura", dataInicioPesquisa.getTime(), dataFimPesquisa.getTime())));
		
		criteria.addOrder(Order.desc("abertura"));
		
		List<Comanda> entities = criteria.list();
		return entities;
	}
	
	public Comanda findComandaAberta(Long clienteId){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Comanda.class);
		criteria.add(Restrictions.eq("cliente.id", clienteId));
		criteria.add(Restrictions.isNull("fechamento"));
		List<Comanda> entities = criteria.list();
		if(entities.isEmpty()){
			return null;
		}
		return entities.get(0);
	}
	
	public List<Comanda> find(Date inicio, Date fim, boolean toExport){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Comanda.class);
		
		Calendar calendarInicio = Calendar.getInstance();
		calendarInicio.setTime(inicio);
		calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
		calendarInicio.set(Calendar.MINUTE, 0);
		calendarInicio.set(Calendar.SECOND, 0);
		calendarInicio.set(Calendar.MILLISECOND, 0);
		
		Calendar calendarFim = Calendar.getInstance();
		calendarFim.setTime(fim);
		calendarFim.set(Calendar.HOUR_OF_DAY, 23);
		calendarFim.set(Calendar.MINUTE, 59);
		calendarFim.set(Calendar.SECOND, 59);
		calendarFim.set(Calendar.MILLISECOND, 999);
		
		criteria.add(Restrictions.between("fechamento", calendarInicio.getTime(), calendarFim.getTime()));
		if(toExport){
			criteria.add(Restrictions.isNotNull("numeroNota"));
			criteria.add(Restrictions.ne("numeroNota", ""));
		}
		
		List<Comanda> entities = criteria.list();
		return entities;
	}
	
	public List<Comanda> find(Comanda entity, Boolean fechadas){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Comanda.class);
		if(entity != null){
			List<Criterion> predicates = new ArrayList<>();
			if(entity.getId() != null){
				criteria = criteria.add(Restrictions.eq("id", entity.getId()));
			}
			if(entity.getCliente() != null){
				criteria = criteria.add(Restrictions.eq("cliente", entity.getCliente()));
			}
			if(fechadas != null && fechadas){
				criteria = criteria.add(Restrictions.isNotNull("fechamento"));
			}
			Criterion[] predicatesArray = new Criterion[predicates.size()];
			predicatesArray = predicates.toArray(predicatesArray);
			criteria = criteria.add(Restrictions.or(predicatesArray));
			criteria.addOrder(Order.desc("abertura"));
		}
		List<Comanda> entities = criteria.list();
		return entities;
	}
	
	public List<Comanda> findComandasByFuncionario(Long funcionarioId){
		Session session = sessionFactory.openSession();
		//Criteria criteria = session.createCriteria(Comanda.class);
		Query query = session.createQuery("select distinct c from Comanda c join c.servicos ls where ls.funcionario.id = " + funcionarioId);
		List<Comanda> entities = query.list();
		return entities;
	}
	
	public List<LancamentoServico> findLancamentoServico(Date inicio, Date fim){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(LancamentoServico.class);
		
		Calendar lo = Calendar.getInstance();
		lo.setTime(inicio);
		lo.set(Calendar.HOUR_OF_DAY, 0);
		lo.set(Calendar.MINUTE, 0);
		lo.set(Calendar.SECOND, 0);
		lo.set(Calendar.MILLISECOND, 0);
		
		Calendar hi = Calendar.getInstance();
		hi.set(Calendar.HOUR_OF_DAY, 23);
		hi.set(Calendar.MINUTE, 59);
		hi.set(Calendar.SECOND, 59);
		hi.set(Calendar.MILLISECOND, 999);
				
		criteria = criteria.add(Restrictions.between("dataCriacao", lo.getTime(), hi.getTime()));
		
		List<LancamentoServico> servicos = criteria.list();
		
		return servicos;
	}

	public List<Comanda> listComandasCliente(Pessoa cliente) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Comanda.class);
		criteria.add(Restrictions.eq("cliente.id", cliente.getId()));
		criteria.addOrder(Order.asc("abertura"));
		List<Comanda> comandas = criteria.list();
		session.close();
		return comandas;
	}
	
}
