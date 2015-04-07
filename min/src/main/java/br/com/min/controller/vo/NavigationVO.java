package br.com.min.controller.vo;

import java.io.Serializable;

import br.com.min.entity.BaseEntity;

public class NavigationVO<T> extends BaseEntity{

	public NavigationVO() {
		super();
	}

	public NavigationVO(T data) {
		super();
		this.data = data;
	}

	private T data;

	@Override
	public Serializable getId() {
		return null;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
