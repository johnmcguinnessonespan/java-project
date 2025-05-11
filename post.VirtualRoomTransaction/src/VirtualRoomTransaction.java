import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
 
import com.silanis.esl.sdk.Document;
import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.DocumentType;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.ExtractionType;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.Signer;
import com.silanis.esl.sdk.VirtualRoom;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.DocumentPackageSettingsBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;
 
public class VirtualRoomTransaction {
  public VirtualRoom packageVirtualRoomRoomAfterUpdate;
 
  public static final String hostUid = "";
  public static Date startDateTime;
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String PACKAGE_TITLE = "Virtual-Room-Transaction";
  public static final String[] SIGNER = {"john.cyclist.mcguinness+word@gmail.com", "john.cyclist.mcguinness+two@gmail.com"};
  public static final String FILE_PATH = "/home/john/Documents/OSS//docs/capture_transaction.pdf";

 
  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);
 
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));
 
    Signer signer = SignerBuilder.newSignerWithEmail(SIGNER[0])
            .build();
 
    hostUid = signer.getId();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 7);
    startDateTime = cal.getTime();
 
    VirtualRoom virtualRoom = new VirtualRoom()
        .withHostUid(hostUid)
        .withVideo(true)
        .withVideoRecording(true)
        .withStartDateTime(startDateTime)
        .build();
 
    DocumentPackage superDuperPackage = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .describedAs("Description")
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNER[1])
            .withFirstName("John")
            .withLastName("Virtual"))
            .withDocument(newDocumentWithName(PACKAGE_TITLE)
            .withId("DocumentId")
            .fromStream(new FileInputStream(FILE_PATH), DocumentType.PDF)

        .build();
 
    PackageId packageId = eslClient.createPackageOneStep(superDuperPackage);
    DocumentPackage retrievedPackage = eslClient.getPackage(packageId);
 
    eslClient.getVirtualRoomService().setVirtualRoom(packageId, virtualRoom);
    packageVirtualRoomRoomAfterUpdate = eslClient.getVirtualRoomService().getVirtualRoom(packageId);
 
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