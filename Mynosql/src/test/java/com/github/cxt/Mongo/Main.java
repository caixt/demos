package com.github.cxt.Mongo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;

public class Main {

	private MongoClient mongoClient = null;
	private MongoDatabase mongoDatabase = null;
	@Before
	public void before(){
        mongoClient = new MongoClient("10.1.50.130", 27017);
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

        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();

        mongoCredentialList.add(MongoCredential.createScramSha1Credential("root", "admin","root_123".toCharArray()));
        MongoClient client = new MongoClient(sends, mongoCredentialList);
        
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
