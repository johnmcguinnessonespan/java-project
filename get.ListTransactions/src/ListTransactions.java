import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import com.google.common.collect.Sets;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageStatus;
import com.silanis.esl.sdk.Page;
import com.silanis.esl.sdk.PageRequest;

// Get a list of Transactions based on status
public class ListTransactions {
  public static final String PACKAGE_TITLE = "ListPackages";
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";

  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Date START_DATE = new LocalDateTime(2016, 1, 1, 0, 0).toDate();
    // Date END_DATE = LocalDateTime.now().toDate();

    // Page<DocumentPackage> draftPackages = getPackagesByPackageStatus(PackageStatus.DRAFT, START_DATE, END_DATE);
    // Page<DocumentPackage> sentPackages = getPackagesByPackageStatus(PackageStatus.SENT, START_DATE, END_DATE);
    // Page<DocumentPackage> declinedPackages = getPackagesByPackageStatus(PackageStatus.DECLINED, START_DATE, END_DATE);
    // Page<DocumentPackage> archivedPackages = getPackagesByPackageStatus(PackageStatus.ARCHIVED, START_DATE, END_DATE);
    // Page<DocumentPackage> completedPackages = getPackagesByPackageStatus(PackageStatus.COMPLETED, START_DATE, END_DATE);

    PageRequest pageRequest = new PageRequest(1, 100);
    Boolean hasNext = false;

    do {
      Page<Map<String, String>> packages = eslClient.getPackageService().getPackagesFields(PackageStatus.SENT, pageRequest, Sets.newHashSet("id"));
      hasNext = packages.hasNextPage();
      pageRequest = pageRequest.next();

      List<Map<String, String>> results = packages.getResults();

      System.out.println("\nList of completed transactions in " + env + ":");

      for (Map<String, String> map : results) {
        String packageId = map.get("id");
        System.out.println(" -\t" + packageId);
      }
    } while (hasNext);

    System.err.println();
  }

  // private static Page<DocumentPackage> getPackagesByPackageStatus(PackageStatus packageStatus, Date startDate, Date endDate)
  //   {
  //   Page<DocumentPackage> resultPage = eslClient.getPackageService().getUpdatedPackagesWithinDateRange(packageStatus, new PageRequest(1), startDate, endDate);
    
  //   return resultPage;
  //   }

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