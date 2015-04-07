package br.com.min.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public abstract class BaseEntity implements Serializable{

	public abstract Serializable getId();
	
	@Transient
	private List<Link> links = new ArrayList<Link>();
	
	@Override
	public String toString() {
		return convertObjectToJsonBytes(this);
	}

	public String convertObjectToJsonBytes(BaseEntity entity) {
		String result = toJson(entity);
		return entity.getClass().getSimpleName() + result;
	}

	public String toJson() {
		return toJson(this);
	}

	public String toJson(BaseEntity entity) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Hibernate4Module hbm = new Hibernate4Module();
			hbm.enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
			mapper.registerModule(hbm);
			String result = mapper.writeValueAsString(entity);
			return result;
		} catch (IOException ex) {
			Logger.getLogger(BaseEntity.class.getName()).log(Level.SEVERE,
					null, ex);
			throw new RuntimeException(ex);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseEntity other = (BaseEntity) obj;

		if (getId() == null || ((BaseEntity) obj).getId() == null) {
			return false;
		}
		if (!getId().equals(other.getId())) {
			return false;
		}
		return true;
	}

	@Transient
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public void addLink(Link link){
		this.links.add(link);
	}
	
	public void addLink(String rel, String href, String type){
		this.links.add(new Link(rel, href, type));
	}
	
}
