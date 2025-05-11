import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.ExtractionType;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

// transaction with text tags and sole signer
public class WordTagTransaction {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String FILE_PATH = "/home/john/Documents/OSS/docs/word_tag_transaction.docx";
  public static final String PACKAGE_TITLE = "Word-Tag-Transaction";
  public static final String SIGNER = "john.cyclist.mcguinness+word@gmail.com";
  
  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Build the DocumentPackage object
    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withSettings(DocumentPackageSettingsBuilder.newDocumentPackageSettings()
            .withDefaultTimeBasedExpiry()
            .withRemainingDays(10))
      // Add Signer
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNER)
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Sign_one")
            .withCustomId("Signer1"))
        .withDocument(DocumentBuilder.newDocumentWithName(PACKAGE_TITLE)
            .fromFile(FILE_PATH)
            .enableExtraction()
            .withExtractionType(ExtractionType.TEXT_TAGS))
        // does not complete
        .autocomplete(false)
        .withEmailMessage("Created with .docx file with several Text Tags")
        .build();

    PackageId packageId = eslClient.createAndSendPackage(pkg);
    System.out.println("{\n" + packageId + "\n}");

    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Signer1"), SIGNER);
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