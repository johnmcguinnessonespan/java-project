import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.DocumentType;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;

import static com.silanis.esl.sdk.builder.DocumentBuilder.newDocumentWithName;
import static com.silanis.esl.sdk.builder.PackageBuilder.newPackageNamed;
import static com.silanis.esl.sdk.builder.SignatureBuilder.captureFor;
import static com.silanis.esl.sdk.builder.SignerBuilder.newSignerWithEmail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class CaptureTransaction {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String FILE_PATH = "/home/john/Documents/OSS//docs/capture_transaction.pdf";
  public static final String PACKAGE_TITLE = "Capture-Transaction";
  public static final String SIGNER = "john.cyclist.mcguinness+capture@gmail.com";

  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Build the DocumentPackage object
    DocumentPackage pkg = newPackageNamed(PACKAGE_TITLE)
        .describedAs("Capture package created")
        .withSigner(newSignerWithEmail(SIGNER)
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Signer_one")
            .withCustomId("Capture"))
        .withDocument(newDocumentWithName(PACKAGE_TITLE)
            .fromStream(new FileInputStream(FILE_PATH), DocumentType.PDF)
            .withSignature(captureFor(SIGNER)
                // position signature
                .onPage(0)
                .atPosition(300, 200)
                .setFromFile(true)))
        .build();

    PackageId packageId = eslClient.createPackageOneStep(pkg);
    eslClient.sendPackage(packageId);

    System.out.println("{\n" + packageId + "\n}");
    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Capture"), SIGNER);
  }

  // Print the link to Signing Ceremony
  public static void getSignerLink(String aSigner, String whom) {
    System.out.println("Link for " + whom + ":\n" + aSigner);
  }

  // Read account details from file
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