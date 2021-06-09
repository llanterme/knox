package za.co.knox.restservice.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import za.co.knox.restservice.entity.ScreeningEntity;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PdfUtils {

    private static String FILE = "/Users/luke/Desktop/foo.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public static void generatePdf(List<ScreeningEntity> companyEntityList) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addLogo(document);
            addTitlePage(document);
            addContent(document, companyEntityList);
            document.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    private static void addLogo(Document document) {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("Knox_logo_full_colour.png").toURI());
            Image img = Image.getInstance(path.toAbsolutePath().toString());

            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getWidth()) * 100;

            img.scalePercent(scaler);
            document.add(img);
        } catch (Exception e){

        }
    }

    private static void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Knox Daily Visitor Report For " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()), catFont));
        document.add(preface);


    }

    private static void addContent(Document document, List<ScreeningEntity> companyEntityList) throws DocumentException {



        Paragraph subPara = new Paragraph("", subFont);

        addEmptyLine(subPara,3);
        // add a table
        createTable(subPara,companyEntityList);


        document.add(subPara);






    }

    private static void createTable(Paragraph subCatPart, List<ScreeningEntity> companyEntityList) throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        PdfPCell c1 = new PdfPCell(new Phrase("Name"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Temperature"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Staff"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);
        table.setHeaderRows(1);


        for(ScreeningEntity item: companyEntityList) {
            table.addCell(item.getUserName());
            table.addCell(item.getUserTemperature());
            table.addCell(item.getStaff());


        }
        subCatPart.add(table);




    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
