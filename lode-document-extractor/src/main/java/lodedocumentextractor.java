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

import it.essepuntato.lode.MimeType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class lodedocumentextractor {

    //private final String xsltURL = "http://lode.sourceforge.net/xslt";
    //private final String cssLocation = "http://lode.sourceforge.net/css/";
    //String sbol_vo = "http://synbiodex.github.io/sbol-visual-ontology/sbol-vo.rdf";

    public static void main(String[] args) throws IOException, TransformerException {

        System.out.println("...");

        File input = new File("./sbol-vo.rdf");//TODO: pass as parameter
        File output = new File("./sbol-vo.html");//TODO: pass as parameter

        String xsltURL = "./lode-document_files/extraction.xsl"; //TODO: pass as parameter
        String cssLocation = "./lode-document_files/lode_files" + File.separator;

        SourceExtractor extractor = new SourceExtractor();
        extractor.addMimeTypes(MimeType.mimeTypes);


        String content = "";
        content = extractor.exec(input);

        // content = LodeServlet.applyXSLTTransformation(content); //TODO: fix as necessary

        TransformerFactory tfactory = new net.sf.saxon.TransformerFactoryImpl();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Transformer transformer = tfactory.newTransformer(new StreamSource(xsltURL));

        transformer.setParameter("css-location", cssLocation);
        transformer.setParameter("lang", "en");
        transformer.setParameter("ontology-url", input);
        transformer.setParameter("source", String.format("%ssource", cssLocation));

        StreamSource inputSource = new StreamSource(new StringReader(content));
        transformer.transform(inputSource, new StreamResult(out));

        content = out.toString();

        FileWriter writer = new FileWriter(output);
        writer.write(content);
        writer.close();

        System.out.println("done!");

    }

}
