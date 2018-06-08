package com.lmh.prepaid;

import com.lmh.util.Utilities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrepaidIndexFileGenerator
{
  private static final Logger LOGGER = LoggerFactory.getLogger(PrepaidIndexFileGenerator.class);
  
  public static boolean findPdfFile(File pdfFolder, String pdfName)
  {
    Boolean isThere = Boolean.valueOf(false);
    File[] listofPdfFiles = pdfFolder.listFiles();
    for (File pdfFile : listofPdfFiles) {
      if ((pdfFile.isFile()) && (pdfFile.getName().endsWith(".pdf")) && 
        (pdfFile.getName().equals(pdfName))) {
        isThere = Boolean.valueOf(true);
      }
    }
    return isThere.booleanValue();
  }
  
  public void indexGenerator(PDFObject object)
  {
    try
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      DateFormat df = new SimpleDateFormat("MMM-yyyy");
      String fileName = object.getA1().split("\\|")[1] + "_" + object.getA1().split("\\|")[2];
      
      String uniqueID = UUID.randomUUID().toString();
      LOGGER.info("IngestionModule| Generated index file name is: " + fileName + ".jrn");
      File writeFile = new File(Utilities.getProperty("dir.prepaid.output.download") + fileName + ".jrn");
      writeFile.createNewFile();
      try
      {
        FileWriter writer = new FileWriter(writeFile);
        writer.write("J|" + fileName + ".pdf" + "|" + dateFormat
          .format(df.parse(object.getA1().split("\\|")[2])) + "\n" + "A|MBL|" + object
          .getH1().split("\\|")[2] + "\n" + "A|Name|" + object
          .getH1().split("\\|")[1] + "\n" + "A|DRN|" + uniqueID + "\n" + "A|doc.guid|" + uniqueID + "\n" + "D|" + object
          .getH1().split("\\|")[2] + "|" + dateFormat
          .format(df.parse(object.getA1().split("\\|")[2])) + "|1\n");
        writer.flush();
        writer.close();
      }
      catch (IOException|ParseException localIOException1) {}
    }
    catch (IOException ex)
    {
      LOGGER.error("IngestionModule| Error: {}", ex);
    }
  }
}
