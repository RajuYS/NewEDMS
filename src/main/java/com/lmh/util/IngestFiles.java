package com.lmh.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngestFiles
{
  private static final Logger LOGGER = LoggerFactory.getLogger(IngestFiles.class);
  
  public static void moveFilesToVaultDownload(String source)
  {
    File dir1 = new File(source);
    String destinationFolder = Utilities.getProperty("dir.vault.ingest");
    List<String> pdfNames = new ArrayList<String>();
    if (dir1.isDirectory())
    {
      File[] content = dir1.listFiles();
      for (int i = 0; i < content.length; i++) {
        if ((content[i].isFile()) && (content[i].getName().endsWith(".pdf")))
        {
          pdfNames.add(FilenameUtils.removeExtension(content[i].getName()));
          content[i].renameTo(new File(destinationFolder + content[i].getName()));
          LOGGER.info("IngestionModule|{} is moved into vault ingest location ", content[i].getName());
        }
      }
      for (int j = 0; j < content.length; j++) {
        if ((content[j].isFile()) && (content[j].getName().endsWith(".jrn")) && 
          (pdfNames.contains(FilenameUtils.removeExtension(content[j].getName()))))
        {
          content[j].renameTo(new File(destinationFolder + content[j].getName()));
          LOGGER.info("IngestionModule|{} is moved into vault ingest location ", content[j].getName());
        }
      }
    }
  }
}
