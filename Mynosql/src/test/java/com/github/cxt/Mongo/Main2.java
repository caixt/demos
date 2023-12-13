package com.github.cxt.Mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.Test;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;



public class Main2 {

	@Test
	public void test() {
		ServerAddress sa = new ServerAddress("10.1.5.231", 27017);
        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
//        mongoCredentialList.add(MongoCredential.createCredential("admin", "admin", "Admin@123".toCharArray()));

        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
        options.connectionsPerHost(60);
        options.threadsAllowedToBlockForConnectionMultiplier(200);
        options.socketKeepAlive(true);
        options.socketTimeout(600000);
        options.connectTimeout(600000);
        options.maxWaitTime(600000);
        MongoClientOptions mongoClientOptions=options.build();
        MongoClient client = new MongoClient(sa, mongoCredentialList, mongoClientOptions);
        MongoDatabase db = client.getDatabase("caixt");
        
//        ClientSession session = client.startSession();
		MongoCollection<Document> collection = db.getCollection("test");
		
		
		Document doc1 = new Document("name", "John").append("age", 30).append("_id", 6);
	    collection.insertOne(doc1);
		
//		session.startTransaction();
//		try {
//		    // 向集合中插入一条数据
//		    Document doc1 = new Document("name", "John").append("age", 30).append("_id", 6);
//		    collection.insertOne(session, doc1);
//
//		    // 向集合中插入一条重复数据，引发唯一键冲突异常，使事务回滚
//		    Document doc2 = new Document("name", "John").append("age", 31).append("_id", 5);
//		    collection.insertOne(session, doc2);
//
//		    // 提交事务
//		    session.commitTransaction();
//		} catch (MongoException e) {
//		    // 回滚事务
//			e.printStackTrace();
//		    session.abortTransaction();
//		} finally {
//		    session.close();
//		}
        
        
	}

}
