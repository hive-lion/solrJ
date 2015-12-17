package com.bg.spider.web.dataimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.bg.spider.web.domain.Article;
import com.bg.spider.web.utils.HbaseUtils;
import com.bg.spider.web.utils.SolrUtil;
/**
 * ģ������Դ��solr����hbase�е�������
 * @author Administrator
 *
 */
public class DataImportAndIndex {
	
	public static void main(String[] args) throws java.lang.Exception {
		List<Article> arrayList = new ArrayList<Article>();
		//TODO ����������Դ��ȡ����
		File file = new File("F:\\article.txt");
		if(!file.exists()){
			System.err.println("�ļ������ڣ�");
		}
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
		
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String lineString = null;
		while((lineString = bufferedReader.readLine())!=null){
			Article article = new Article();
			String[] split = lineString.split("\t");
			article.setId(split[0]);
			article.setTitle(split[1]);
			article.setAuthor(split[2]);
			article.setDescribe(split[3]);
			article.setContent(split[3]);
			arrayList.add(article);
			
		}
		
		
		HbaseUtils hbaseUtils = new HbaseUtils();
		for (Article article : arrayList) {
			//�����ݲ���solr
			SolrUtil.addIndex(article);
			//�����ݲ���hbase
			try {
				hbaseUtils.put(hbaseUtils.TABLE_NAME, article.getId()+"", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_TITLE, article.getTitle());
				hbaseUtils.put(hbaseUtils.TABLE_NAME, article.getId()+"", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_AUTHOR, article.getAuthor());
				hbaseUtils.put(hbaseUtils.TABLE_NAME, article.getId()+"", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_DESCRIBE, article.getDescribe());
				hbaseUtils.put(hbaseUtils.TABLE_NAME, article.getId()+"", hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_CONTENT, article.getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		inputStreamReader.close();
		bufferedReader.close();
		
		
		
	}

}
