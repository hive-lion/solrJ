package com.bg.spider.web.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bg.spider.web.domain.IdeInfoIndexBean;
/**
 * 
 * @author bbaiggey
 * @date 2015年5月19日下午2:54:05
 * @DESC SolrColud工具类
 */
public class SolrColudUtil {
	static final Logger logger = LoggerFactory.getLogger(SolrColudUtil.class);
	static final String zkHost = "suixingpay194:2181,suixingpay195:2181,suixingpay196:2181,suixingpay197:2181,suixingpay198:2181";
	static CloudSolrServer server = null ;
	static  {
		try {
		server = new CloudSolrServer(zkHost );
		server.setZkClientTimeout(100000);
		server.setZkConnectTimeout(20000);
		server.setDefaultCollection("collection1");
		
		}catch (Exception e) {
			logger.error("请检查tomcat服务器或端口是否开启!{}",e);
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 建立索引
	 * @throws Exception
	 */
	public static void addIndex(Object obj) {
		try {
			server.addBean(obj);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param key  要删除的属性名
	 * @param val  要删除的值
	 */
	public static void DelByQuery(String key,String val) {
		try {
			server.deleteByQuery(key+":"+val);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	public static List<IdeInfoIndexBean> search(String key,String val) throws Exception {
		SolrQuery params = new SolrQuery();
		if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(val)){
			params.set("q", key+":"+val);
		}else{
			return null;
		}
	
		QueryResponse response = server.query(params);
		
		List<IdeInfoIndexBean> results = response.getBeans(IdeInfoIndexBean.class);
		
		return results;
	}
	
	
	
	
	
	
}
