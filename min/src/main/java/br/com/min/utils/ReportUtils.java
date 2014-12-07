package br.com.min.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class ReportUtils {

	private static Configuration cfg;
	private static FopFactory fopFactory = FopFactory.newInstance();
	
	private static void configureFreemarker(){
		try {
			cfg = new Configuration();

			//URL url = ReportUtils.class.getResource("/templates/");
			//cfg.setDirectoryForTemplateLoading(new File(url.getFile()));
			cfg.setDirectoryForTemplateLoading(new File("/app/templates/"));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding("UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String processTemplate(Map<String, Object> values, String templateFile){
		String foFile = new String(processTemplateToByteArray(values, templateFile));
		return foFile;
	}
	
	public static byte[] processTemplateToByteArray(Map<String, Object> values, String templateFile){
		if(cfg == null){
			configureFreemarker();
		}
		try {
			Template template = cfg.getTemplate(templateFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer templateOut = new BufferedWriter(new OutputStreamWriter(baos));
			template.process(values, templateOut);
			byte[] result = baos.toByteArray();
			templateOut.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File generatePDF(String fileName, String foFile){
		try{
			Reader foReader = new StringReader(foFile);
			fileName += "-" + Utils.dateTimeSecondToFileFormat.format(new Date());
			File file = new File("/app/"+fileName+".pdf");
			OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
			
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(); 
			Source src = new StreamSource(foReader);
			
			Result res = new SAXResult(fop.getDefaultHandler());
			
			transformer.transform(src, res);
			out.close();
			foReader.close();
			return file;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
}
