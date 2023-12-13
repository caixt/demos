package com.github.cxt.Mongo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.mongodb.client.ClientSession;

public class Main {

	private MongoClient mongoClient = null;
	private MongoDatabase mongoDatabase = null;
	@Before
	public void before(){
        mongoClient = new MongoClient("10.1.5.231", 27017);
        mongoDatabase = mongoClient.getDatabase("caixt_test"); 
	}
	
	@After
	public void after(){
		mongoClient.close();
	}
	
	
	@Test
	public void testBson() {
		Bson bson = Filters.and(Filters.eq("_id", new ObjectId("650abe8837919e303cc8dfc5")), Filters.eq("a", new Date()),  
				Filters.eq("b", 2), Filters.or(Filters.eq("c", 3.14F), Filters.eq("d", 4L)));
		BsonDocument bsonDoc = bson.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
        System.out.println(bsonDoc);
        bson = BasicDBObject.parse(bsonDoc.toJson());
        System.out.println(bson.toString());
	}
	
	@Test
	public void insert() throws ParseException{
		MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        //插入文档  
		Document document = null;
		List<Document> documents = new ArrayList<Document>();
       	document = new Document("title", "我是第一条数据");
       	document.append("col1", "字符串");
       	document.append("col2", 1);
       	document.append("col3", false);
       	document.append("col4", 123.45);
       	document.append("col5", new Date());
        documents.add(document);  
       	document = new Document("title", "我是第一.1条数据");
       	document.append("col1", "字符串");
       	document.append("col2", 1);
       	document.append("col3", false);
       	document.append("col4", 123.56);
       	document.append("col5", new Date());
        documents.add(document);  
        document = new Document("title", "我是第二条数据");
       	document.append("col1", "字符串");
       	document.append("col2", 2);
       	document.append("col3", false);
       	document.append("col4", 123.45);
       	document.append("col5", new Date());
        documents.add(document); 
        document = new Document("title", "我是第三条数据");
       	document.append("col1", "字符串");
       	document.append("col2", 3);
       	document.append("col3", false);
       	document.append("col4", 123.45);
       	document.append("col5", new Date());
        documents.add(document); 
        collection.insertMany(documents);  
	}
	
	
	@Test
	public void insert2() throws ParseException{
		MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        //插入文档  
		Document document = null;
		List<Document> documents = new ArrayList<Document>();
       	document = new Document("title", "我是第一条数据");
       	document.append("col1", "字符串");
       	document.append("col2", 1);
       	document.append("col3", false);
       	document.append("col4", 123.45);
       	document.append("col5", new Date());
       	document.append("_id", 4);
        documents.add(document);  
    	document = new Document("title", "我是第一条数据");
       	document.append("col1", "字符串");
       	document.append("col2", 1);
       	document.append("col3", false);
       	document.append("col4", 123.45);
       	document.append("col5", new Date());
       	document.append("_id", 3);
        documents.add(document);  
        collection.insertMany(documents);  
	}
	
	
	@Test
	public void insert3() throws ParseException{
		ClientSession session = mongoClient.startSession();
		MongoCollection<Document> collection = mongoDatabase.getCollection("test");
		session.startTransaction();
		try {
		    // 向集合中插入一条数据
		    Document doc1 = new Document("name", "John").append("age", 30).append("_id", 1);
		    collection.insertOne(session, doc1);

		    // 向集合中插入一条重复数据，引发唯一键冲突异常，使事务回滚
		    Document doc2 = new Document("name", "John").append("age", 31).append("_id", 1);
		    collection.insertOne(session, doc2);

		    // 提交事务
		    session.commitTransaction();
		} catch (MongoException e) {
		    // 回滚事务
			e.printStackTrace();
		    session.abortTransaction();
		} finally {
		    session.close();
		}
	}
	
	
	@Test
	public void test(){
		 MongoCollection<Document> collection = mongoDatabase.getCollection("test");
		 BasicDBObject query =new BasicDBObject("col1", "字符串");
		 BasicDBObject key = new BasicDBObject("col1", 1).append("col2", 1).append("col4", 1).append("_id", 0);
		 BasicDBObject sort = new BasicDBObject("col2", 1).append("col4", -1);
         FindIterable<Document> findIterable = collection.find(query).projection(key).sort(sort);
         MongoCursor<Document> mongoCursor = findIterable.iterator();  
         while(mongoCursor.hasNext()){  
            System.out.println(mongoCursor.next());  
         }  
	}
	
	//https://www.cnblogs.com/jasonminghao/p/13179629.html
	//aggregate
	
	@Test
	public void test3() throws IOException{
		InputStream is = new FileInputStream(new File("pom.xml"));
		GridFSBuckets.create(mongoDatabase).uploadFromStream("/a/bc/< >&a.txt", is);
		is.close();
	}
	
	@Test
	public void test4() throws IOException{
		OutputStream out = new FileOutputStream(new File("test.xml"));
		GridFSDownloadStream stream = GridFSBuckets.create(mongoDatabase).openDownloadStream("/a/bc/< >&a.txt");
		GridFSFile file = stream.getGridFSFile();
        int size = file.getChunkSize();
        System.out.println(file.getFilename());
        int len = (int)file.getLength();
        int cnt = len / size + (len % size == 0 ? 0 : 1);
        byte[] bts = new byte[Math.min(len, size)];
        try {
            while (cnt-- > 0) {
                int tmp = stream.read(bts);
                out.write(bts, 0, tmp);
            }
            out.flush();
        } catch (IOException e) {
           e.printStackTrace();
        }
        out.close();
	}
	
	@Test
	public void test5() throws IOException{
		MongoCursor<GridFSFile> iterator = GridFSBuckets.create(mongoDatabase).find().iterator();
		while(iterator.hasNext()){
			GridFSFile file = iterator.next();
			System.out.println(file.getFilename());
			System.out.println(file.getMD5());
		}
	}
	
	@Test
	public void test6() throws IOException{
		List<ServerAddress> sends = new ArrayList<ServerAddress>();
        sends.add(new ServerAddress("10.1.240.24", 11111));
        sends.add(new ServerAddress("10.1.240.24", 11112));
        sends.add(new ServerAddress("10.1.240.24", 11113));

        MongoCredential mongoCredential = MongoCredential.createCredential("root", "admin","root_123".toCharArray());

        MongoClient client = new MongoClient(sends, mongoCredential, new MongoClientOptions.Builder().build());
        
        MongoDatabase mongoDatabase = client.getDatabase("cxt");
        
        MongoCollection<Document> collection = mongoDatabase.getCollection("cccc");
	    FindIterable<Document> findIterable = collection.find();
	    MongoCursor<Document> mongoCursor = findIterable.iterator();  
	    while(mongoCursor.hasNext()){  
	       System.out.println(mongoCursor.next());  
	    }
	    client.close();
	}	
}
