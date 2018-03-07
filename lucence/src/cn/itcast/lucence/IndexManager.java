package cn.itcast.lucence;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexManager {
	IndexWriter indexWriter = null;

	@Before
	public void init() throws Exception {
		// 指定索引库
		Directory directory = FSDirectory.open(new File("D:\\class297\\indexRepod"));
		// 创建分词器
		IKAnalyzer ikAnalyzer = new IKAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, ikAnalyzer);
		// 创建索引对象
		indexWriter = new IndexWriter(directory, indexWriterConfig);
	}

	// 全部删除
	@Test
	public void deleteAll() throws Exception {
		// 指定索引库
		Directory directory = FSDirectory.open(new File("D:\\class297\\indexRepod"));
		// 创建分词器
		IKAnalyzer ikAnalyzer = new IKAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, ikAnalyzer);
		// 创建索引对象
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		// 执行操作
		indexWriter.deleteAll(); // 慎用这个直接删除掉所有的库，一般不会用的
		indexWriter.close(); // 删除后还会剩下几个那几个应该是自带文件
		System.out.println("删除成功");
	}

	// 有条件删除
	@Test
	public void deleteBy() throws Exception {
		// 指定索引库
		Directory directory = FSDirectory.open(new File("D:\\class297\\indexRepod"));
		// 指定分词器
		IKAnalyzer ikAnalyzer = new IKAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, ikAnalyzer);
		// 创建索引对象
		IndexWriter indexWriter = new IndexWriter(directory, config);
		// 执行放法
		indexWriter.deleteDocuments(new Term("name", "spring"));
		indexWriter.close();
		System.out.println("删除成功");
	}

	// 修改
	@Test
	public void update() throws Exception {
		Term term = new Term("name", "spring");
		Document doc = new Document(); 
		doc.add(new TextField("name", "新闻的", Store.YES));
		doc.add(new TextField("content", "新文档内容", Store.YES));
		TextField textField = new TextField("name", "新文档新indexWriter新文档新文档新文updateDocument新文档新文档新文档新文档apache", Store.YES);
		textField.setBoost(10);
		indexWriter.updateDocument(term, doc);
		indexWriter.close();
	}

	// 查询
	@Test
	public void select() throws Exception {
		// 指定索引库位置
		Directory directory = FSDirectory.open(new File("D:\\class297\\indexRepod"));

		IndexReader indexReader = DirectoryReader.open(directory);

		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// 查询所有
		// MatchAllDocsQuery query = new MatchAllDocsQuery();
		// 条件查询，查询一定范围内的,查询这个大小范围的 有多少条
		// Query query = NumericRangeQuery.newLongRange("size", 10l, 1000l,true,
		// true);

		// 查询结果名字为apache 内容为spring的，
		/*
		 * +name:apache +content:spring 总记录数为：0
		 */
		// BooleanQuery query = new BooleanQuery();
		// TermQuery query1 = new TermQuery(new Term("name","apache"));
		// TermQuery query2 = new TermQuery(new Term("content","spring"));
		// query.add(query1,Occur.MUST);
		// Occur.MUST_NOT还也可以填这个，不为spring的
		// query.add(query2,Occur.MUST_NOT);

		// 打印结果name:lucene name:project name:apache
		//QueryParser需要导入第三方jar包
		// QueryParser queryParser = new QueryParser("name", new IKAnalyzer());
		// Query query = queryParser.parse("lucene is a project of apache");
		
		
		//打印结果(name:lucene content:lucene) (name:project content:project) (name:apache content:apache)
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[]{"name","content"}, new IKAnalyzer());
		 Query query = queryParser.parse("lucene is a project of apache");
		
		 
		 // 查询结果返回最大的值
		System.out.println(query);
		TopDocs topDocs = indexSearcher.search(query, 100);
		System.out.println("总记录数为：" + topDocs.totalHits);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			int docId = scoreDoc.doc;
			Document doc = indexSearcher.doc(docId);
			// System.out.println(doc.get("name"));
			System.out.println(doc.get("size"));
			// System.out.println(doc.get("path"));
			// System.out.println(doc.get("content"));
		}

		indexReader.close();
	}

	@Test
	public void create() throws Exception {
		// 1、创建指定索引为位置 文件路径
		Directory directory = FSDirectory.open(new File("D:\\class297\\indexRepod"));

		// 指定分词器
		IKAnalyzer analyzer = new IKAnalyzer(); // 最新版本 分词器
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);

		// 2、创建指定索引对象 位置 这里需要config所以创建一个config分词器
		IndexWriter indexWriter = new IndexWriter(directory, config);

		// 获取源文档
		File files = new File("D:\\Lucene&solr视频\\Lucene&solr-day01\\资料\\上课用的查询资料searchsource");
		File[] listFiles = files.listFiles();
		for (File file : listFiles) {
			Document doc = new Document();
			// 文件名称
			String fileNmae = file.getName();
			Field nameFile = new TextField("name", fileNmae, Store.YES);
			doc.add(nameFile);
			// 文件大小
			long sizeOf = FileUtils.sizeOf(file);
			// 因为是long类型需要转换成string类型
			Field sizeFile = new TextField("size", sizeOf + "", Store.YES);
			doc.add(sizeFile);
			// 文件路径
			String path = file.getPath();
			Field pathFile = new TextField("path", path, Store.YES);
			doc.add(pathFile);
			// 文件内容
			String fileread = FileUtils.readFileToString(file);
			Field readFile = new TextField("content", fileread, Store.YES);
			doc.add(readFile);
			// 3、 把文档写入索引 因为传入的是document所以要获取一个do,c
			indexWriter.addDocument(doc);
		}
		// 4、关闭资源
		indexWriter.close();
	}
}
