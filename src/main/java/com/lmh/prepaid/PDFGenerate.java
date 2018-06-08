package com.lmh.prepaid;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lmh.util.Utilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFGenerate {
	private static Logger LOGGER = LoggerFactory.getLogger(PDFGenerate.class);
	private static final Font GENERAL_FONT = new Font(Font.FontFamily.HELVETICA, 10.0F, 0);
	private static final Font TABLE_HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10.0F, 0, BaseColor.WHITE);

	public void generate(PDFObject object, String brand) {
		String imageLocation = "";
		if (brand.equals("Celcom")) {
			imageLocation = Utilities.getProperty("dir.prepaid.logo") + "Celcom.jpg";
		}
		if (brand.equals("Mtrade")) {
			imageLocation = Utilities.getProperty("dir.prepaid.logo") + "Mtrade.jpg";
		}
		if (brand.equals("SPinoy")) {
			imageLocation = Utilities.getProperty("dir.prepaid.logo") + "SPinoy.jpg";
		}
		if (brand.equals("Altel")) {
			imageLocation = Utilities.getProperty("dir.prepaid.logo") + "Altel.jpg";
		}
		if (brand.equals("RedOne")) {
			imageLocation = Utilities.getProperty("dir.prepaid.logo") + "RedOne.jpg";
		}
		String pdfName = object.getA1().split("\\|")[1] + "_" + object.getA1().split("\\|")[2] + ".pdf";
		LOGGER.info("IngestionModule| Generated pdf file name is:{} ", pdfName);
		String generatedFileName = Utilities.getProperty("dir.prepaid.output.images") + brand + "\\" + pdfName;
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(generatedFileName));
			document.open();
			Image img = Image.getInstance(imageLocation);

			img.setAbsolutePosition(433.0F, 710.0F);
			img.scalePercent(60.0F);
			document.add(img);
			addContent(document, object);
			document.close();
			if (brand.equals("Celcom")) {
				Files.copy(new File(generatedFileName).toPath(),
						new File(Utilities.getProperty("dir.prepaid.output.download") + pdfName).toPath(),
						new CopyOption[0]);
			}
		} catch (DocumentException | IOException e) {
			LOGGER.error("IngestionModule| Error: {}", e);
		}
	}

	private static void addContent(Document document, PDFObject object) throws DocumentException {
		document.add(new Paragraph("  "));
		document.add(new Paragraph("CELCOM MOBILE SDN. BHD. (COMPANY NO 27910-A)",
				new Font(Font.FontFamily.HELVETICA, 10.0F, 1)));

		document.add(new Paragraph("MENARA CELCOM, JALAN RAJA MUDA ABDUL AZIZ", GENERAL_FONT));
		document.add(new Paragraph("50300 KUALA LUMPUR", GENERAL_FONT));
		document.add(new Paragraph("GST ID NO: 000549060608", GENERAL_FONT));
		document.add(new Paragraph("  "));
		document.add(new Paragraph("  "));

		document.add(addrTable(object));
		document.add(new Paragraph("  "));
		document.add(createTable(object));
	}

	private static PdfPTable createTable(PDFObject object) throws BadElementException, DocumentException {
		List<String> reloadData = object.getD1();
		int reloadDataSize = reloadData.size();
		String[] totalData1 = object.getD3().split("\\|");
		String[] gstData1 = object.getD4().split("\\|");

		float[] columnWidths = { 40.0F, 74.0F, 42.0F, 24.0F, 42.0F, 66.0F };
		PdfPTable table = new PdfPTable(columnWidths);
		table.setWidthPercentage(100.0F);

		PdfPCell c1 = new PdfPCell(new Phrase(" ", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);

		c1.setHorizontalAlignment(0);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("  ", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(0);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("GROSS", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(1);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("  ", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(1);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("GST", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(1);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("TOTAL BILLED", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(1);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Date", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);

		c1.setHorizontalAlignment(0);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("TRANSACTION TYPE", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(0);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("(RM)", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(1);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("GST", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(1);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("(RM)", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setHorizontalAlignment(1);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("(RM)", TABLE_HEADER_FONT));
		c1.setBorder(0);
		c1.setBackgroundColor(BaseColor.BLUE);
		c1.setFixedHeight(18.0F);
		c1.setHorizontalAlignment(1);
		table.addCell(c1);

		table.setHeaderRows(2);
		for (int i = 0; i < reloadDataSize; i++) {

			String[] reload1 = reloadData.get(i).split("\\|");
			String[] reload = (reload1.length == 7) ? reloadData.get(i).split("\\|")
					: ((reloadData.get(i)) + "@*").split("\\|");

			PdfPCell c2 = new PdfPCell(new Phrase(reload[1], GENERAL_FONT));
			c2.setBorder(0);
			table.addCell(c2);

			c2 = new PdfPCell(new Phrase(reload[2], GENERAL_FONT));
			c2.setBorder(0);
			table.addCell(c2);

			c2 = new PdfPCell(new Phrase(reload[3], GENERAL_FONT));
			c2.setBorder((object.getD2() == null) && (i == reloadDataSize - 1) ? 2 : 0);

			c2.setBorderWidth(1.0F);
			c2.setHorizontalAlignment(1);
			table.addCell(c2);

			c2 = new PdfPCell(new Phrase(reload[4], GENERAL_FONT));
			c2.setBorder(0);
			c2.setHorizontalAlignment(1);
			table.addCell(c2);

			c2 = new PdfPCell(new Phrase(reload[5], GENERAL_FONT));
			c2.setHorizontalAlignment(1);
			c2.setBorder((object.getD2() == null) && (i == reloadDataSize - 1) ? 2 : 0);
			c2.setBorderWidth(1.0F);
			table.addCell(c2);

			c2 = new PdfPCell(new Phrase(reload[6].equals("@*")?reload[6].replace("@*"," "):reload[6], GENERAL_FONT));
			c2.setBorder((object.getD2() == null) && (i == reloadDataSize - 1) ? 2 : 0);
			c2.setBorderWidth(1.0F);
			c2.setFixedHeight(17.0F);
			c2.setHorizontalAlignment(1);
			table.addCell(c2);
		}
		if (object.getD2() != null) {
			String[] Adjustment1= object.getD2().split("\\|");
	
			String[] Adjustment= (Adjustment1.length == 4) ? (object.getD2().split("\\|"))
					: ((object.getD2())+"@*").split("\\|");
			
			PdfPCell c12 = new PdfPCell(new Phrase("", GENERAL_FONT));
			c12.setBorder(0);
			table.addCell(c12);

			c12 = new PdfPCell(new Phrase(Adjustment[1], GENERAL_FONT));
			c12.setBorder(0);
			table.addCell(c12);

			c12 = new PdfPCell(new Phrase(Adjustment[2], GENERAL_FONT));
			c12.setBorder(2);

			c12.setBorderWidth(1.0F);
			c12.setHorizontalAlignment(1);
			table.addCell(c12);

			c12 = new PdfPCell(new Phrase(" ", GENERAL_FONT));
			c12.setBorder(0);
			c12.setHorizontalAlignment(1);
			table.addCell(c12);

			c12 = new PdfPCell(new Phrase("", GENERAL_FONT));
			c12.setHorizontalAlignment(1);
			c12.setBorder(2);
			c12.setBorderWidth(1.0F);
			table.addCell(c12);

			c12 = new PdfPCell(new Phrase(Adjustment[3].equals("@*")?(Adjustment[3].replace("@*", " ")):(Adjustment[3]), GENERAL_FONT));
			c12.setBorder(2);
			c12.setBorderWidth(1.0F);
			c12.setFixedHeight(17.0F);
			c12.setHorizontalAlignment(1);
			table.addCell(c12);
		}
		String[] totalData =(totalData1.length == 5) ? (object.getD3().split("\\|"))
				: ((object.getD3())+"@*").split("\\|");

		PdfPCell c3 = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		c3.setBorder(0);
		table.addCell(c3);

		c3 = new PdfPCell(new Phrase(totalData[1], GENERAL_FONT));
		c3.setBorder(0);
		table.addCell(c3);

		c3 = new PdfPCell(new Phrase((object.getD2() == null) ? totalData[2] : "", GENERAL_FONT));
		c3.setBorder(2);
		c3.setBorderWidth(1.0F);
		c3.setHorizontalAlignment(1);
		table.addCell(c3);

		c3 = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		c3.setBorder(0);
		c3.setHorizontalAlignment(1);
		table.addCell(c3);

		c3 = new PdfPCell(new Phrase((object.getD2() == null) ? totalData[3] : totalData[2], GENERAL_FONT));
		c3.setBorder(2);
		c3.setBorderWidth(1.0F);
		c3.setHorizontalAlignment(1);
		table.addCell(c3);

		c3 = new PdfPCell(new Phrase((object.getD2() == null) ? (totalData[4].equals("@*")?(totalData[4].replace("@*", " ")):(totalData[4])) : totalData[3], GENERAL_FONT));
		//c3 = new PdfPCell(new Phrase(totalData[4].equals("@*")?(totalData[4].replace("@*", " ")):(totalData[4]), GENERAL_FONT));
		c3.setBorder(2);
		c3.setBorderWidth(1.0F);
		c3.setFixedHeight(17.0F);
		c3.setHorizontalAlignment(1);
		table.addCell(c3);

		PdfPCell emptyCell = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		emptyCell.setBorder(0);
		table.addCell(emptyCell);
		emptyCell = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		emptyCell.setBorder(0);
		table.addCell(emptyCell);
		emptyCell = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		emptyCell.setBorder(0);
		table.addCell(emptyCell);
		emptyCell = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		emptyCell.setBorder(0);
		table.addCell(emptyCell);
		emptyCell = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		emptyCell.setBorder(0);
		table.addCell(emptyCell);
		emptyCell = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		emptyCell.setBorder(0);
		table.addCell(emptyCell);
		
		String[] gstData =(gstData1.length == 3) ? (object.getD4().split("\\|"))
				: ((object.getD4())+"@*").split("\\|");
		
		PdfPCell c4 = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		c4.setBorder(0);
		table.addCell(c4);

		c4 = new PdfPCell(new Phrase(gstData[1], GENERAL_FONT));
		c4.setBorder(0);
		table.addCell(c4);
		c4 = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		c4.setBorder(0);
		table.addCell(c4);
		c4 = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		c4.setBorder(0);
		table.addCell(c4);
		c4 = new PdfPCell(new Phrase(" ", GENERAL_FONT));
		c4.setBorder(0);
		table.addCell(c4);
		c4 = new PdfPCell(new Phrase(gstData[2].equals("@*")?(gstData[2].replace("@*", " ")):(gstData[2]), GENERAL_FONT));
		c4.setBorder(0);
		c4.setHorizontalAlignment(1);
		table.addCell(c4);

		return table;
	}

	private static PdfPTable addrTable(PDFObject object) throws BadElementException {
		String[] customerData1 = object.getA1().split("\\|");
		String[] customerInfo1 = object.getH1().split("\\|");
		
		String[] customerData =(customerData1.length == 3) ? (object.getA1().split("\\|"))
				: ((object.getA1())+"@*").split("\\|");
		
		String[] customerInfo =(customerInfo1.length == 4) ? (object.getH1().split("\\|"))
				: ((object.getH1())+"@*").split("\\|");
		
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100.0F);

		PdfPCell c2 = new PdfPCell(new Phrase(customerInfo[1], GENERAL_FONT));
		c2.setBorder(0);
		c2.setColspan(4);
		table.addCell(c2);

		PdfPCell c3 = new PdfPCell(new Phrase(customerInfo[3].equals("@*")?customerInfo[3].replace("@*"," "):customerInfo[3], GENERAL_FONT));
		c3.setBorder(0);
		c3.setColspan(2);
		table.addCell(c3);

		c3 = new PdfPCell(new Phrase("INVOICE NO", GENERAL_FONT));
		c3.setBorder(0);
		table.addCell(c3);

		c3 = new PdfPCell(new Phrase(customerData[1], GENERAL_FONT));
		c3.setBorder(0);
		c3.setHorizontalAlignment(2);
		table.addCell(c3);

		PdfPCell c4 = new PdfPCell(new Phrase(customerInfo[2], GENERAL_FONT));
		c4.setBorder(0);
		c4.setColspan(2);
		table.addCell(c4);

		c4 = new PdfPCell(new Phrase("MONTH OF INVOICE", GENERAL_FONT));
		c4.setBorder(0);
		table.addCell(c4);
		c4 = new PdfPCell(new Phrase(customerData[2].equals("@*")?customerData[2].replace("@*"," "):customerData[2], GENERAL_FONT));
		c4.setBorder(0);
		c4.setHorizontalAlignment(2);
		table.addCell(c4);

		return table;
	}
}
