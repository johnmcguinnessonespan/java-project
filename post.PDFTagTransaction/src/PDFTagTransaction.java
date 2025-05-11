import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.ExtractionType;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.CeremonyLayoutSettingsBuilder;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;
import com.silanis.esl.sdk.internal.EslServerException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

// transaction with text tags and sole signer
public class PDFTagTransaction {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String DOCUMENT_NAME = "PDF with tags";
  public static final String FILE_PATH = "/home/john/Documents/OSS/docs/pdf_tag_transaction.pdf";
  public static final String PACKAGE_TITLE = "PDF-Tag-Transaction";
  public static final String SIGNER = "john.cyclist.mcguinness+pdf@gmail.com";

  public static void main(String[] args) throws IOException, EslServerException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Build the DocumentPackage object
    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNER)
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Signer_one")
            .withCompany("One")
            .withCustomId("robin"))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_NAME)
            .fromFile(FILE_PATH)
            .enableExtraction()
            .withExtractionType(ExtractionType.TEXT_TAGS))
        .withSettings(DocumentPackageSettingsBuilder.newDocumentPackageSettings()
            .withoutLanguageDropDown()
            .withoutWatermark()
            .withRemainingDays(4)
            .withCeremonyLayoutSettings(CeremonyLayoutSettingsBuilder.newCeremonyLayoutSettings()
                .withNavigator()
                .withGlobalNavigation()
                .withSessionBar()
                .withTitle()
                .withProgressBar()
                .withoutGlobalDownloadButton()))
        .withEmailMessage("Using .pdf flie")
        // does not complete
        .autocomplete(false)
        .build();

    PackageId packageId = eslClient.createPackageOneStep(pkg);

    // Send the package to be signed by the participants
    eslClient.sendPackage(packageId);
    System.out.println("{\n" + packageId + "\n}");

    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "robin"), SIGNER);
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