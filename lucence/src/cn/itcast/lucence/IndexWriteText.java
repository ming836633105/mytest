package cn.itcast.lucence;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexWriteText {

	public static void main(String[] args) throws IOException {
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
