import java.time.LocalDateTime;
import java.util.HashMap;
import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.apitoken.ApiTokenConfig;
import com.silanis.esl.sdk.apitoken.ApiTokenConfig.TokenType;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;
 
public class UsingClientApp {
  public static final String PACKAGE_TITLE = "UsingClientApp: " + LocalDateTime.now();
  public static final String[] BASE_URL = {"https://apps.esignlive.eu",
                                           "https://sandbox.esignlive.com"}; // EU, US2
  public static final String[] CLIENT_ID = {"1966727ef1854242b10abf2a67f",
                                            "196612ef7352028a20b962b9505"}; // EU, US2
  public static final String[] CLIENT_SECRET = {"6879647261fe954761b1390194dbe6deae2209a250f34d0e49b5733a97e509b4d63733ce10",
                                                "68796472619c9176732ea0c3de0cf1d8f019491fa2cc29cfcc6cf492dc4108f003c228ddcc"}; // EU, US2
  public static final String[] SIGNERS = {"john.cyclist.mcguinness+signer@gmail.com",
                                          "john.cyclist.mcguinness+reviewer@gmail.com"};


  @SuppressWarnings("rawtypes")
  public static void main(String[] args) {
    @SuppressWarnings("unchecked")
    EslClient eslClient = new EslClient(ApiTokenConfig.newBuilder()
      .clientAppId(CLIENT_ID[0])
      .clientAppSecret(CLIENT_SECRET[0])
      .baseUrl(BASE_URL[0])
      .tokenType(TokenType.OWNER)
      .build(),
      BASE_URL[0]+ "/api", false, null, false, new HashMap());
 
      // Build the DocumentPackage object
      DocumentPackage documentPackage = PackageBuilder
        .newPackageNamed(PACKAGE_TITLE)
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNERS[0])
          .withFirstName("SignerFirstName")
          .withLastName("SignerLastName")
          .withCustomId("Signer")
          .signingOrder(0)
          )
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNERS[1])
          .withCustomId("Signer")
          .withFirstName("ReviewerFirstName")
          .withLastName("ReviewerLastName")
          .withCustomId("Reviewer")
          .signingOrder(1)
          )
        .withDocument(DocumentBuilder.newDocumentWithName("sampleAgreement")
          .fromFile("/home/john/Documents/OSS/docs/sampleAgreement.pdf")
          .withSignature(SignatureBuilder
            .signatureFor(SIGNERS[0])
            .onPage(0)
            .atPosition(150, 165)
            )
          .withSignature(SignatureBuilder
            .signatureFor(SIGNERS[0])
              .onPage(0)
              .atPosition(475, 165)
              )
            )
        .build();
 
      // Issue the request to the OneSpan Sign server to create the DocumentPackage
      PackageId packageId = eslClient.createPackageOneStep(documentPackage);
      System.out.println(packageId);
 
      // Send the package to be signed by the participants
      eslClient.sendPackage(packageId);
     
      getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Signer"), SIGNERS[0]);
      getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Reviewer"), SIGNERS[1]);
    }
 
   public static void getSignerLink(String aSigner, String whom) {
       System.out.println("Link for " + whom + ":\n" + aSigner);
   }
}