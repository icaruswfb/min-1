package br.com.min.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.HistoricoDAO;
import br.com.min.dao.HorarioDAO;
import br.com.min.entity.Historico;
import br.com.min.entity.Horario;
import br.com.min.utils.Utils;

@Service
public class HorarioService extends BaseService<Horario, HorarioDAO> {
	
	@Autowired
	private HorarioDAO dao;
	@Autowired
	private HistoricoDAO historicoDao;
	@Autowired
	private GenericDAO genericDao;
	
	@Transactional
	public void persist(Horario horario){
		horario.setDataCriacao(new Date());
		horario = (Horario)genericDao.persist(horario);
		criarHistoricoAgendamento(horario);
	}
	
	private void criarHistoricoAgendamento(Horario horario){
		Historico historico = criarHistorico(horario);
		StringBuffer texto = new StringBuffer();
		texto.append("Agendamento para dia ").append(Utils.dateFormat.format(horario.getInicio()))
					.append(" das ").append(Utils.timeFormat.format(horario.getInicio()))
					.append(" até as ").append(Utils.timeFormat.format(horario.getTermino()))
					.append(" ").append(horario.getServicos().toString());
		historico.setTexto(texto.toString());
		
		genericDao.persist(historico);
	}
	
	@Transactional
	public List<Horario> findHorario(Horario horario){
		return cleanResult(dao.findHorario(horario));
	}
	
	public List<Horario> listar(){
		Horario horario = new Horario();
		return cleanResult(dao.findHorario(horario));
	}
	
	public Horario findById(Long id){
		Horario horario = new Horario();
		horario.setId(id);
		List<Horario> result = dao.findHorario(horario);
		if(result.isEmpty()){
			return null;
		}else{
			return result.get(0);
		}
	}

	public void delete(Long id) {
		Horario horario = new Horario();
		horario = findById(id);
		criarHistoricoCancelamento(horario);
		horario = new Horario();
		horario.setId(id);
		genericDao.delete(horario);
	}
	
	private Historico criarHistorico(Horario horario ){
		Historico historico = new Historico();
		historico.setData(new Date());
		historico.setCliente(horario.getCliente());
		historico.setFuncionario(horario.getFuncionario());
		StringBuffer textoPequeno = new StringBuffer();
		textoPequeno.append(Utils.dateTimeFormat.format(historico.getData()));
		textoPequeno.append(" - criado por TODO");
		historico.setTextoPequeno(textoPequeno.toString());
		return historico;
	}
	
	private void criarHistoricoCancelamento(Horario horario){
		Historico historico = criarHistorico(horario);
		StringBuffer texto = new StringBuffer();
		texto.append("Cancelamento do agendamento para dia ").append(Utils.dateFormat.format(horario.getInicio()))
					.append(" das ").append(Utils.timeFormat.format(horario.getInicio()))
					.append(" at� as ").append(Utils.timeFormat.format(horario.getTermino()));
		historico.setTexto(texto.toString());
		
		genericDao.persist(historico);
	}
	
}
