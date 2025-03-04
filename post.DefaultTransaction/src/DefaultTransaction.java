import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
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

public class DefaultTransaction {
  public static final String PACKAGE_TITLE = "DefaultTransaction";
  public static final String[] DOCUMENT_ONE = {"Two Signers", "/home/john/Documents/OSS/docs/default_transaction.pdf" };
  public static final String[] SIGNERS = {"john.cyclist.mcguinness+signer1@gmail.com", "john.cyclist.mcguinness+signer2@gmail.com"};

  public static void main(String[] args) throws IOException, EslServerException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Build the DocumentPackage object
    DocumentPackage pkg = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
    .autocomplete(false)
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNERS[0])
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("One")
            .withCompany("One")
            .withCustomId("signer_one")
            .signingOrder(0)
            .withEmailMessage("You can go first"))
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNERS[1])
            .withFirstName(prop.getProperty("FORENAME"))
            .withLastName("Two")
            .withCompany("Two")
            .withCustomId("signer_two")
            .signingOrder(1)
            .withLanguage(Locale.FRENCH))
        .withDocument(DocumentBuilder.newDocumentWithName(DOCUMENT_ONE[0]).fromFile(DOCUMENT_ONE[1])
            .withSignature(SignatureBuilder.signatureFor(SIGNERS[0]).onPage(0)
                .atPosition(175, 150))
            .withSignature(
                SignatureBuilder.captureFor(SIGNERS[0]).onPage(1).atPosition(100, 210))
            .withSignature(
                SignatureBuilder.captureFor(SIGNERS[0]).onPage(1).atPosition(390, 230))
            .withSignature(SignatureBuilder.initialsFor(SIGNERS[0]).onPage(1)
                .atPosition(100, 450))
            .withSignature(SignatureBuilder.signatureFor(SIGNERS[1]).onPage(1)
                .atPosition(390, 160))
        .withSignature(SignatureBuilder.captureFor(SIGNERS[1]).onPage(0).atPosition(175,
         550))
         .withSignature(SignatureBuilder.initialsFor(SIGNERS[1]).onPage(0).atPosition(175,
         950))
        )
        .withSettings(DocumentPackageSettingsBuilder.newDocumentPackageSettings()
            // .withInPerson()
            // .withOptOutReason("Decline terms.")
            .withoutLanguageDropDown()
            // .hideOwnerInPersonDropDown()
            // .withoutOptOutOther()
            // .disableFirstAffidavit()
            // .disableSecondAffidavit()
            // .withoutDecline()
            .withoutWatermark()
            // .withRemainingDays(2)
            .withHandOverLinkAutoRedirect()
            // .withCaptureText()
            .withHandOverLinkHref("http://www.google.com")
            .withHandOverLinkText("Exit to site")
            .withHandOverLinkTooltip("GO GOOGLE")
            // .withoutDocumentToolbarDownloadButton()
            // .withDialogOnComplete()
            .withCeremonyLayoutSettings(CeremonyLayoutSettingsBuilder.newCeremonyLayoutSettings()
                .withNavigator()
                .withGlobalNavigation()
                // .withoutBreadCrumbs()
                .withSessionBar()
                .withTitle()
                .withProgressBar()
                .withoutGlobalDownloadButton()
            // .withoutGlobalSaveAsLayoutButton()
            // .withLogoSource("http://www.logo-maker.net/images/common/company-logo8.gif")
            ))
        .withEmailMessage(PACKAGE_TITLE)
        .build();

    // Issue the request to the e-SignLive server to create the DocumentPackage
    // try
    // {
    PackageId packageId = eslClient.createPackageOneStep(pkg);
    // }
    // catch (EslServerException serverException)
    // {
    // System.out.println( "The server could not complete the request." );
    // System.out.println( serverException.getMessage() );
    // System.out.println( "HTTP code: " +
    // serverException.getServerError().getCode());
    // System.out.println( "Server message: " +
    // serverException.getServerError().getMessage());
    // }
    // catch (EslException exception)
    // {
    // System.out.println( exception.getMessage() );
    // System.out.println( exception.getCause().getMessage() );
    // }

    // Send the package to be signed by the participants
    eslClient.sendPackage(packageId);
    System.out.println("{\n" + packageId + "\n}");

    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "signer_one"), SIGNERS[0]);
    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "signer_two"), SIGNERS[1]);
    
    // String signingUrlThree =
    // eslClient.getPackageService().getSigningUrl(packageId, "SignerThree");
    // System.out.println("Link for Signer 3:\n" + signingUrlThree);
    // PackageID = new
    // PackageId id = new PackageId("iPWWqb1FZHRKvfvztMEdjQD79Mo=");
    // eslClient.getPackageService().notifySigner(id,
    // "john.cyclist.mcguinness+sone@gmail.com", "");
  }


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