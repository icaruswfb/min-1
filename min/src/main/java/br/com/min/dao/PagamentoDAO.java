package br.com.min.dao;

import br.com.min.entity.FluxoPagamento;
import br.com.min.entity.Pagamento;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PagamentoDAO
{
  @Autowired
  private SessionFactory sessionFactory;
  
  public List<Pagamento> findPagamentos(Date inicio, Date fim, FluxoPagamento fluxo)
  {
    Session session = this.sessionFactory.openSession();
    Criteria criteria = session.createCriteria(Pagamento.class);
    
    Calendar dataInicio = Calendar.getInstance();
    dataInicio.setTime(inicio);
    dataInicio.set(Calendar.HOUR_OF_DAY, 0);
    dataInicio.set(Calendar.MINUTE, 0);
    dataInicio.set(Calendar.SECOND, 0);
    dataInicio.set(Calendar.MILLISECOND, 0);
    
    Calendar dataFim = Calendar.getInstance();
    dataFim.setTime(fim);
    dataFim.set(Calendar.HOUR_OF_DAY, 23);
    dataFim.set(Calendar.MINUTE, 59);
    dataFim.set(Calendar.SECOND, 59);
    dataFim.set(Calendar.MILLISECOND, 999);
    
    criteria.add(Restrictions.between("data", dataInicio.getTime(), dataFim.getTime()));
    if (fluxo != null) {
      criteria.add(Restrictions.eq("fluxoPagamento", fluxo));
    }
    criteria.addOrder(Order.asc("data"));
    List<Pagamento> entities = criteria.list();
    return entities;
  }
}
