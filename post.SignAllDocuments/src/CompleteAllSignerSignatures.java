import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.internal.EslServerException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class CompleteAllSignerSignatures {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String PACKAGE_TITLE = "Complete-All-Signer-Signatures";
  public static final String SIGNER_ID = "SignerTwo";
  public static final String TRANSACTION = "2c9OKUPqSjQWEJ3lcgEdQCWhwiw=";

  public static void main(String[] args) throws IOException, EslServerException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    PackageId packageId = new PackageId(TRANSACTION);
	
		eslClient.signDocuments(packageId, SIGNER_ID);

    //System.out.println(packageId.getId());

    System.out.println("All signatures applied for: " + SIGNER_ID);
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