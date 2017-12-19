package com.matrix.microservice.admin.util.szy;


import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URLEncoder;

public class DocToHtmlUtil {


    //docx转html入口
    public void WordToHtml(String DocumentPath,String HtmlPath,String FileName,String FileNameSuffix) throws TransformerException, IOException, ParserConfigurationException {
        //检测并创建html目录
        if(!new File(HtmlPath+"/"+FileNameSuffix).exists())   {
            new File(HtmlPath+"/"+FileNameSuffix).mkdirs();
        }
        Word2Html(DocumentPath, HtmlPath, FileName,FileNameSuffix);
    }

    public void WriteFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if(!file.exists()){
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(content);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }

    /**
     * 将word转换成html
     * 仅支持 .docx
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public String  Word2Html(String DocumentPath,String HtmlPath,String FileName,String FileNameSuffix)
            throws TransformerException, IOException,
            ParserConfigurationException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        /**
         * word2007和word2003的构建方式不同，
         * 前者的构建方式是xml，后者的构建方式是dom树。
         * 文件的后缀也不同，前者后缀为.docx，后者后缀为.doc
         * 相应的，apache.poi提供了不同的实现类。
         */
        if("docx".equals(FileNameSuffix)){
            //step 1 : load DOCX into XWPFDocument
            InputStream inputStream = new FileInputStream(new File(DocumentPath+"/"+FileNameSuffix+"/"+FileName+"."+FileNameSuffix));
            XWPFDocument document = new XWPFDocument(inputStream);

            //step 2 : prepare XHTML options
            final String imageUrl = "";   //路径word/media前的保存目录

            XHTMLOptions options = XHTMLOptions.create();

            options.setExtractor(new FileImageExtractor(new File("../"+FileNameSuffix+"/"+FileName+"/" + imageUrl)));
            options.setIgnoreStylesIfUnused(false);
            options.setFragment(true);
            options.URIResolver(new IURIResolver() {
                public String resolve(String uri) {
                    return HtmlPath+"/"+FileNameSuffix+"/"+FileName+"/"+uri; //路径  看看是tomcat的还是本地
                }
            });
            //step 3 : convert XWPFDocument to XHTML
            XHTMLConverter.getInstance().convert(document, out, options);
        }else{
        }
        out.close();

        WriteFile(new String(out.toByteArray()), HtmlPath+"/"+FileNameSuffix+"/"+FileName+".html");
        return new String(out.toByteArray());
    }


    /**保存文件
     * @param stream
     * @param path
     * @param filename
     * @throws IOException
     */
    public void Save(InputStream stream,String path,String filename) throws IOException
    {
        //检测并创建docx目录
        if(!new File(path).exists())   {
            new File(path).mkdirs();
        }

        FileOutputStream fs=new FileOutputStream( path + filename);
        byte[] buffer =new byte[1024*1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread=stream.read(buffer))!=-1)
        {
            bytesum+=byteread;
            fs.write(buffer,0,byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }

    /**读取文件
     * @param
     * @param path
     * @param filename
     * @throws IOException
     */
    public void Read(HttpServletResponse response, String path, String filename, String realname) throws IOException
    {
        InputStream in = new FileInputStream(new File(path+filename));//获取文件输入流
        int len = 0;
        byte[] buffer = new byte[1024];
        response.setHeader("content-disposition", "attachment;filename="+ URLEncoder.encode(realname, "UTF-8"));
        OutputStream out = response.getOutputStream();
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer,0,len);//将缓冲区的数据输出到客户端浏览器
        }
        response.setContentType("application/pdf;charset=UTF-8");
        out.flush();
        out.close();
        in.close();
    }

}


