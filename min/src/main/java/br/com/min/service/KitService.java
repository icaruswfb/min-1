package br.com.min.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.KitDAO;
import br.com.min.entity.Kit;

@Service
public class KitService extends BaseService<Kit, KitDAO> {

	@Autowired
	private KitDAO dao;
	@Autowired
	private GenericDAO genericDao;
	
	public Kit persist(Kit kit){
		return (Kit) genericDao.persist(kit);
	}
	
	public Kit findById(Long id){
		Kit kit = new Kit();
		kit.setId(id);
		List<Kit> result = dao.find(kit);
		if(result.isEmpty()){
			return null;
		}else{
			return cleanResult(result.get(0));
		}
	}
	
	public List<Kit> listar(){
		return cleanResult(dao.find(null));
	}
	
	public void delete(Long id){
		Kit kit = new Kit();
		kit.setId(id);
		genericDao.delete(kit);
	}
	
}
