package com.bg.spider.web.domain;

import org.apache.solr.client.solrj.beans.Field;


/**
 * 
 * @author bbaiggey
 * @date 2015��5��18������3:27:09
 * @DESC �����Ϣ
 */
public class IdeInfoIndexBean {

	/**
	 * ideInfo rowkey
	 */
	@Field
	private String id;
	/**
	 * ��ݱ��
	 */
	@Field
	private String ideNo;
	/**
	 * ��ѯ����
	 */
	@Field
	private String collect;



	public IdeInfoIndexBean() {
		super();
		// TODO Auto-generated constructor stub
	}



	public IdeInfoIndexBean(String id, String ideNo, String collect) {
		super();
		this.id = id;
		this.ideNo = ideNo;
		this.collect = collect;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}




	public String getIdeNo() {
		return ideNo;
	}



	public void setIdeNo(String ideNo) {
		this.ideNo = ideNo;
	}



	public String getCollect() {
		return collect;
	}


	public void setCollect(String collect) {
		this.collect = collect;
	}













	@Override
	public String toString() {
		return "IdeInfoIndexBean [id=" + id + ", ideNo=" + ideNo + ", collect="
				+ collect + "]";
	}

	
	
}
