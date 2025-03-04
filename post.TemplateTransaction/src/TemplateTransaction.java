import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.Placeholder;
import com.silanis.esl.sdk.builder.CeremonyLayoutSettingsBuilder;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;
import com.silanis.esl.sdk.internal.EslServerException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class TemplateTransaction {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties");
  public static final String PACKAGE_TITLE = "Temlate-Transaction";
  public static final String SIGNER = "john.cyclist.mcguinness+template@gmail.com" ;
  public static final String TEMPLATE_ID = "k7TelJtdfWl02dzPFl1LgOt0sHI=" ;

  public static void main(String[] args) throws IOException, EslServerException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    DocumentPackage newPackage = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
    .describedAs("Using a template")
    .withEmailMessage("Pleas sign")
    .withSigner(SignerBuilder.newSignerWithEmail(SIGNER)
        .withFirstName("John")
        .withLastName("Owner")
        .replacing(new Placeholder(SIGNER)))
    .build();

PackageId packageId = eslClient.getTemplateService().createPackageFromTemplate(TEMPLATE_ID, newPackage);

public static void getSignerLink(String aSigner, String whom) {
    System.out.println("Link for " + whom + ":\n" + aSigner);
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