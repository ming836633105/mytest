package cn.itcast.lucence;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexReadText {

	public static void main(String[] args) throws IOException{
	
		//指定索引库路径
		Directory directory = FSDirectory.open(new File("D:\\class297\\indexRepo"));
		
		//创建indexReader对象
		IndexReader indexReader = DirectoryReader.open(directory);
		//创建indexSearcher对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//创建查询查询索引库							查询的那个域，查询的内容
		Query query = new TermQuery(new Term("name","apache"));
		//执行查询
		//第一个参数是查询对象，第二个参数是查询结果返回的最大值
		TopDocs topDocs = indexSearcher.search(query, 10);
		//查询结果总条数
		System.out.println("查询结果的总条数"+topDocs.totalHits);
		//遍历查询结果
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			//scoreDoc.doc属性就是document对象的id
			//根据document的id找到document对象
			Document document = indexSearcher.doc(scoreDoc.doc);
			System.out.println("文件名"+document.get("filename"));
			System.out.println("文件内容"+document.get("content"));
			System.out.println("文件路径"+document.get("path"));
			System.out.println("文件大小"+document.get("size"));
		}
		//关闭资源
		indexReader.close();
	}

}
