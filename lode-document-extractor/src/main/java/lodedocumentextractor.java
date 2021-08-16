/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2010-2013, Silvio Peroni <essepuntato@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

// import com.github.dissys.*;

import it.essepuntato.lode.MimeType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

//import it.essepuntato.lode.LodeServlet;
//import it.essepuntato.lode.SourceExtractor;

public class lodedocumentextractor {

    //private final String xsltURL = "http://lode.sourceforge.net/xslt";
    //private final String cssLocation = "http://lode.sourceforge.net/css/";
    //String sbol_vo = "http://synbiodex.github.io/sbol-visual-ontology/sbol-vo.rdf";

    public static void main(String[] args) throws IOException, TransformerException {

        System.out.println("...");

        File rdf = new File("../sbol-vo.rdf");
        File html = new File("../sbol-vo.html");
        // Document doc = Jsoup.parse(ontology, "UTF-8");

        // generateLodeHTMLDocument(rdf, html);
        //File file = new File("./extraction.xsl");

        String xsltURL = "../lode-document_files/extraction.xsl";
        //File xsltURL = new File("lode-document_files/extraction.xsl");
        String cssLocation = "../lode-document_files/lode_files" + File.separator;
       // File cssLocation = new File("lode-document_files/lode_files");


//        String xsltURL = "resources/extraction.xsl";
//        String cssLocation = "resources/lode_files" + File.separator;

        SourceExtractor extractor = new SourceExtractor();
        extractor.addMimeTypes(MimeType.mimeTypes);
        // response.setCharacterEncoding("UTF-8");

        String content = "";
        content = extractor.exec(rdf);

        // content = LodeServlet.applyXSLTTransformation(content); //TODO: fix as necessary

        TransformerFactory tfactory = new net.sf.saxon.TransformerFactoryImpl();

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Transformer transformer = tfactory.newTransformer(new StreamSource(xsltURL));

        transformer.setParameter("css-location", cssLocation);
        //transformer.setParameter("css-location", cssLocation + File.separator);
        transformer.setParameter("lang", "en");
        transformer.setParameter("ontology-url", rdf);
        //transformer.setParameter("source", String.format("%s%ssource", cssLocation,File.separator));
        transformer.setParameter("source", String.format("%ssource", cssLocation));
        StreamSource inputSource = new StreamSource(new StringReader(content));

        transformer.transform(inputSource, new StreamResult(output));

        content = output.toString();

        FileWriter writer = new FileWriter(html);
        writer.write(content);
        writer.close();

        // FileUtils.writeStringToFile(source, doc.outerHtml(), "UTF-8");
        System.out.println("done!");

    }


    /*
    private void generateLodeHTMLDocument(URL request, File response) throws IOException {
        //PrintWriter out = response.getWriter();
        SourceExtractor extractor = new SourceExtractor();
        extractor.addMimeTypes(MimeType.mimeTypes);
        // response.setCharacterEncoding("UTF-8");

        String content = "";
        content = extractor.exec(request);

        content = applyXSLTTransformation(content); //TODO: fix as necessary
        FileWriter writer = new FileWriter(response);
        writer.write(content);
    }
     */

    //private String applyXSLTTransformation(String source, String ontologyUrl) throws TransformerException {
//    private String applyXSLTTransformation(String source) throws TransformerException {
//        TransformerFactory tfactory = new net.sf.saxon.TransformerFactoryImpl();
//
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//        Transformer transformer = tfactory.newTransformer(new StreamSource(xsltURL));
//
//        transformer.setParameter("css-location", cssLocation);
//        transformer.setParameter("lang", "en");
//        // transformer.setParameter("ontology-url", ontologyUrl);
//        transformer.setParameter("source", cssLocation + "source");
//
//        StreamSource inputSource = new StreamSource(new StringReader(source));
//
//        transformer.transform(inputSource, new StreamResult(output));
//
//        return output.toString();
//    }


}
