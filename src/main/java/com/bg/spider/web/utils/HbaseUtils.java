package com.bg.spider.web.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.bg.spider.web.domain.Article;

public class HbaseUtils {
	
	/**
	 * HBASE ������
	 */
	public  final String TABLE_NAME = "article";
	/**
	 * �д�1 ������Ϣ
	 */
	public  final String COLUMNFAMILY_1 = "info";
	/**
	 * �д�1�е���
	 */
	public  final String COLUMNFAMILY_1_TITLE = "title";
	public  final String COLUMNFAMILY_1_AUTHOR = "author";
	public  final String COLUMNFAMILY_1_CONTENT = "content";
	public  final String COLUMNFAMILY_1_DESCRIBE = "describe";
	
	
	
	HBaseAdmin admin=null;
	Configuration conf=null;
	/**
	 * ���캯����������
	 */
	public HbaseUtils(){
		conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "suixingpay194:2181,suixingpay195:2181,suixingpay196:2181,suixingpay197:2181,suixingpay198:2181");
		conf.set("hbase.rootdir", "hdfs://ns1:9000/hbase");
		try {
			admin = new HBaseAdmin(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		HbaseUtils hbase = new HbaseUtils();
		//����һ�ű�
//		hbase.createTable("stu","cf");
//		//��ѯ���б���
//		hbase.getALLTable();
//		//���������һ����¼
//		hbase.addOneRecord("stu","key1","cf","name","zhangsan");
//		hbase.addOneRecord("stu","key1","cf","age","24");
//		//��ѯһ����¼
//		hbase.getKey("stu","key1");
//		//��ȡ�����������
//		hbase.getALLData("stu");
//		//ɾ��һ����¼
//		hbase.deleteOneRecord("stu","key1");
//		//ɾ����
//		hbase.deleteTable("stu");
		//scan��������ʹ��
//		hbase.getScanData("stu","cf","age");
		//rowFilter��ʹ��
		//84138413_20130313145955
		hbase.getRowFilter("waln_log","^*_201303131400\\d*$");
	}
	/**
	 * rowFilter��ʹ��
	 * @param tableName
	 * @param reg
	 * @throws Exception
	 */
	public void getRowFilter(String tableName, String reg) throws Exception {
		HTable hTable = new HTable(conf, tableName);
		Scan scan = new Scan();
//		Filter
		RowFilter rowFilter = new RowFilter(CompareOp.NOT_EQUAL, new RegexStringComparator(reg));
		scan.setFilter(rowFilter);
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result result : scanner) {
			System.out.println(new String(result.getRow()));
		}
	}
	
	public void getScanData(String tableName, String family, String qualifier) throws Exception {
	HTable hTable = new HTable(conf, tableName);
	Scan scan = new Scan();
	scan.addColumn(family.getBytes(), qualifier.getBytes());
	ResultScanner scanner = hTable.getScanner(scan);
	for (Result result : scanner) {
		if(result.raw().length==0){
			System.out.println(tableName+" ������Ϊ�գ�");
		}else{
			for (KeyValue kv: result.raw()){
				System.out.println(new String(kv.getKey())+"\t"+new String(kv.getValue()));
			}
		}
	}
	}
	private void deleteTable(String tableName) {
		try {
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				System.out.println(tableName+"��ɾ���ɹ���");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(tableName+"��ɾ��ʧ�ܣ�");
		}
		
	}
	/**
	 * ɾ��һ����¼
	 * @param tableName
	 * @param rowKey
	 */
	public void deleteOneRecord(String tableName, String rowKey) {
		HTablePool hTablePool = new HTablePool(conf, 1000);
		HTableInterface table = hTablePool.getTable(tableName);
		Delete delete = new Delete(rowKey.getBytes());
		try {
			table.delete(delete);
			System.out.println(rowKey+"��¼ɾ���ɹ���");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(rowKey+"��¼ɾ��ʧ�ܣ�");
		}
	}
	/**
	 * ��ȡ�����������
	 * @param tableName
	 */
	public void getALLData(String tableName) {
		try {
			HTable hTable = new HTable(conf, tableName);
			Scan scan = new Scan();
			ResultScanner scanner = hTable.getScanner(scan);
			for (Result result : scanner) {
				if(result.raw().length==0){
					System.out.println(tableName+" ������Ϊ�գ�");
				}else{
					for (KeyValue kv: result.raw()){
						System.out.println(new String(kv.getKey())+"\t"+new String(kv.getValue()));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// ��ȡһ����¼
		@SuppressWarnings({ "deprecation", "resource" })
		public  Article get(String tableName, String row) {
			HTablePool hTablePool = new HTablePool(conf, 1000);
			HTableInterface table = hTablePool.getTable(tableName);
			Get get = new Get(row.getBytes());
			Article article = null;
			try {
				
				Result result = table.get(get);
				KeyValue[] raw = result.raw();
				if (raw.length == 4) {
					article = new Article();
					article.setId(row);
					article.setTitle(new String(raw[3].getValue()));
					article.setAuthor(new String(raw[0].getValue()));
					article.setContent(new String(raw[1].getValue()));
					article.setDescribe(new String(raw[2].getValue()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return article;
		}
		
		
		// ���һ����¼
		public  void put(String tableName, String row, String columnFamily,
				String column, String data) throws IOException {
			HTablePool hTablePool = new HTablePool(conf, 1000);
			HTableInterface table = hTablePool.getTable(tableName);
			Put p1 = new Put(Bytes.toBytes(row));
			p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
					Bytes.toBytes(data));
			table.put(p1);
			System.out.println("put'" + row + "'," + columnFamily + ":" + column
					+ "','" + data + "'");
		}
	
	
	/**
	 * ��ѯ���б���
	 * @return
	 * @throws Exception
	 */
	public List<String> getALLTable() throws Exception {
		ArrayList<String> tables = new ArrayList<String>();
		if(admin!=null){
			HTableDescriptor[] listTables = admin.listTables();
			if (listTables.length>0) {
				for (HTableDescriptor tableDesc : listTables) {
					tables.add(tableDesc.getNameAsString());
					System.out.println(tableDesc.getNameAsString());
				}
			}
		}
		return tables;
	}
	/**
	 * ����һ�ű�
	 * @param tableName
	 * @param column
	 * @throws Exception
	 */
	public void createTable(String tableName, String column) throws Exception {
		if(admin.tableExists(tableName)){
			System.out.println(tableName+"���Ѿ����ڣ�");
		}else{
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			tableDesc.addFamily(new HColumnDescriptor(column.getBytes()));
			admin.createTable(tableDesc);
			System.out.println(tableName+"�����ɹ���");
		}
	}
}
