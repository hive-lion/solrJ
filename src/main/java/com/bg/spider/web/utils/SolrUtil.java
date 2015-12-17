package com.bg.spider.web.utils;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bg.spider.web.domain.Article;

/**
 * solr工具类
 *
 */
public class SolrUtil {
	static final Logger logger = LoggerFactory.getLogger(SolrUtil.class);
	private static final String SOLR_URL = "http://192.168.120.199:8983/solr/collection1"; // 服务器地址
	private static HttpSolrServer server = null;

	static {
		try {
			server = new HttpSolrServer(SOLR_URL);
			server.setAllowCompression(true);
			server.setConnectionTimeout(10000);
			server.setDefaultMaxConnectionsPerHost(100);
			server.setMaxTotalConnections(100);
			/*
			 * Article ac = new Article(); ac.setAuthor("ttt");
			 * ac.setContent("ssss"); ac.setDescribe("ssss"); ac.setId("5555");
			 * ac.setTitle("556666"); addIndex(ac);
			 */

			/**
			 * 连接solrCloud
			 */
			/*
			 * String zkHost = "192.168.1.170:2181,192.168.1.171:2181";
			 * 
			 * CloudSolrServer server = new CloudSolrServer(zkHost );
			 * 
			 * server.setDefaultCollection("collection1");
			 */

		} catch (Exception e) {
			logger.error("请检查tomcat服务器或端口是否开启!{}", e);
			e.printStackTrace();
		}

	}

	/**
	 * 建立索引
	 * 
	 * @throws Exception
	 */
	public static void addIndex(Article article) {
		try {
			server.addBean(article);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据Id进行删除
	 */
	public static void DelById() {
		try {
			server.deleteById("IW-02");
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过查询条件进行删除
	 */
	private static void DelByQuery() {
		try {
			server.deleteByQuery("id:F*");
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		DelById();

	}

}
