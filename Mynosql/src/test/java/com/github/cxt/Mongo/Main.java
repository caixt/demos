package com.github.cxt.Mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Main {

	private MongoClient mongoClient = null;
	private MongoDatabase mongoDatabase = null;
	@Before
	public void before(){
        mongoClient = new MongoClient("10.1.50.149", 27017);
        mongoDatabase = mongoClient.getDatabase("caixt_test"); 
	}
	
	@After
	public void after(){
		mongoClient.close();
	}
	
	
	@Test
	public void insert(){
		MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        //插入文档  
        /** 
        * 1. 创建文档 org.bson.Document 参数为key-value的格式 
        * 2. 创建文档集合List<Document> 
        * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document) 
        * */
		Document document = null;
		List<Document> documents = new ArrayList<Document>();
       	document = new Document("title", "我是第三条数据");
       	document.append("col1", "字符串");
       	document.append("col2", 4448888);
       	document.append("col3", false);
       	document.append("col4", "aaa");
        documents.add(document);  
        collection.insertMany(documents);  
	}
	
	
	@Test
	public void test(){
		 MongoCollection<Document> collection = mongoDatabase.getCollection("test");
         //检索所有文档  
         /** 
         * 1. 获取迭代器FindIterable<Document> 
         * 2. 获取游标MongoCursor<Document> 
         * 3. 通过游标遍历检索出的文档集合 
         * */  
		 
		 BasicDBObject key=new BasicDBObject("title1", 1);
         FindIterable<Document> findIterable = collection.find().projection(key);
         MongoCursor<Document> mongoCursor = findIterable.iterator();  
         while(mongoCursor.hasNext()){  
            System.out.println(mongoCursor.next());  
         }  
	}
}
