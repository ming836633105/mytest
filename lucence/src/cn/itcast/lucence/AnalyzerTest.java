package cn.itcast.lucence;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class AnalyzerTest {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {

		// java自带的分词器
		// Analyzer anlyzer = new StandardAnalyzer();
		// 第三方的分词器
		// 需要导入jar IKAnalyzer2012FF_u1.jar

		IKAnalyzer analyzer = new IKAnalyzer();
		// 两个参数，第一个参数文件名，第二个文件内容
		//TokenStream tokenStream = analyzer.tokenStream(null, "Learn how to create a web page with Spring MVC.");
		TokenStream tokenStream = analyzer.tokenStream(null, "apache 全文检索是将整本书java、整篇文章中的任意内容信息查找出来的检索，java，王冬婷，大美喵");
		// 分词器分解出来的内容
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			System.out.println(charTermAttribute);
		}
	}
}
