import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.PackageStatus;
import com.silanis.esl.sdk.SigningStatus;
import com.silanis.esl.sdk.io.Files;

public class StatusAndDownload {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String PACKAGE_TITLE = "Status-And-Download";
  public static final String TRANSACTION = "sFT3_f0KL3DF7FEhlHZbs0q64z4=";

  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // define the package
    PackageId packageId = new PackageId(TRANSACTION);
    DocumentPackage sentPackage = eslClient.getPackage(packageId);

    // check and print out status
    PackageStatus status = sentPackage.getStatus();
    System.out.println(status);
    SigningStatus sentSigningStatus = eslClient.getSigningStatus(packageId, null, null);
    System.out.println(sentSigningStatus.getToken());

    if (!sentSigningStatus.getToken().equals("COMPLETED")) {
      System.out.println("Cannot download: signing incomplete");
    } else {
      // download zip file
      byte[] documentZip = eslClient.downloadZippedDocuments(packageId);
      Files.saveTo(documentZip, "documentZip.zip");
      System.out.println("Document zip file downloaded");

      // download evidence material
      byte[] evidenceSummary = eslClient.downloadEvidenceSummary(packageId);
      Files.saveTo(evidenceSummary, "evidenceSummary.pdf");
      System.out.println("Evidence Summary Downloaded");
    }
  }

  public static Properties readPropertiesFile(String fileName) throws IOException {
    FileInputStream fis = null;
    Properties prop = null;

    try {
      fis = new FileInputStream(fileName);
      prop = new Properties();
      prop.load(fis);
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      fis.close();
    }

    return prop;
  }
}