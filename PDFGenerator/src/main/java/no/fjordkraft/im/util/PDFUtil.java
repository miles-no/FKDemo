package no.fjordkraft.im.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by bhavi on 10/6/2017.
 */
public class PDFUtil {

    public static byte[] rotator(byte[] inputPdf) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputPdf);
        int n = reader.getNumberOfPages();
        PdfDictionary page;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int p = 1; p <= n; p++) {
            page = reader.getPageN(p);
            if(p== 1)
                page.put(PdfName.ROTATE, new PdfNumber(-90));
            if(p==2)
                page.put(PdfName.ROTATE, new PdfNumber(90));
        }
        PdfStamper stamper = new PdfStamper(reader, baos);
        stamper.close();
        reader.close();
        return baos.toByteArray();
    }


    public static byte[] merge(byte[] inputPdf) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputPdf);
        Document doc = new Document(new RectangleReadOnly(842f, 595f), 0, 0, 0, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();
        int totalPages = reader.getNumberOfPages();
        for (int i = 1; i <= totalPages; i = i + 2) {
            doc.newPage();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page = writer.getImportedPage(reader, i+1); // page #1
            float documentWidth = doc.getPageSize().getWidth() / 2;
            float documentHeight = doc.getPageSize().getHeight();
            float pageWidth = page.getWidth();
            float pageHeight = page.getHeight();
            float widthScale = documentWidth / pageWidth;
            float heightScale = documentHeight / pageHeight;
            float scale = Math.min(widthScale, heightScale);

            //float offsetX = 50f;
            float offsetX = (documentWidth - (pageWidth * scale)) / 2;
            float offsetY = 0f;

            cb.addTemplate(page, scale, 0, 0, scale, offsetX, offsetY);

            if (i+1 <= totalPages) {
                PdfImportedPage page2 = writer.getImportedPage(reader, i); // page #2
                pageWidth = page.getWidth();
                pageHeight = page.getHeight();
                widthScale = documentWidth / pageWidth;
                heightScale = documentHeight / pageHeight;
                scale = Math.min(widthScale, heightScale);
                offsetX = ((documentWidth - (pageWidth * scale)) / 2) + documentWidth;
                cb.addTemplate(page2, scale, 0, 0, scale, offsetX, offsetY);
            }
        }
        doc.close();
        return baos.toByteArray();
    }
}
