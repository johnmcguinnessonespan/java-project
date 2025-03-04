import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.builder.PackageBuilder;

public class UpdateTransactionExpiry {

  public static final String PACKAGE_TITLE = "WSL: ExpiryTransaction";

  public static void main(String[] args) throws IOException
    {
    String env = "US2.SBX";
    Properties prop = readPropertiesFile("/home/john/Documents/OSS/config.properties");
			    
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

	// step1: convert to GMT time
	// local date, expire at 2019-08-23T00:00:00 EDT
	TimeZone localTimeZone = TimeZone.getTimeZone("EDT");
	Calendar localCalender = new GregorianCalendar(2019, 8 - 1, 23, 0, 0, 0);
	Date gmtDate = new Date(localCalender.getTimeInMillis() - localTimeZone.getRawOffset());

	// step2: build package with expiry date
	DocumentPackage pkg = PackageBuilder.newPackageNamed("Create a package with Expiry Date - " + System.currentTimeMillis()).expiresAt(gmtDate)
				.build();
		
	//step3: create package
	PackageId createPackageOneStep = eslClient.createPackageOneStep(pkg);
	System.out.println("{\n" + createPackageOneStep + "\n}");
	}

  public static Properties readPropertiesFile(String fileName) throws IOException
    {
    FileInputStream fis = null;
    Properties prop = null;
  
    try
      {
      fis = new FileInputStream(fileName);
      prop = new Properties();
      prop.load(fis);
      }
    catch(FileNotFoundException fnfe)
      {
      fnfe.printStackTrace();
      }
    catch(IOException ioe)
      {
      ioe.printStackTrace();
      }
    finally
      {
      fis.close();
      }

    return prop;
    }
  }
