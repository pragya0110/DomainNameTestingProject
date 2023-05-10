package Utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import testrail.APIException;
import testrail.TestRailManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import base.TestBase;


public class ExtentListeners implements ITestListener  {

    static Date d = new Date();
    static String messageBody;
    //static String fileName = "Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
    static String fileName="extent.html";
    public  static ExtentReports extent = ExtentManager.createInstance(System.getProperty("user.dir")+"\\target\\"+fileName);

    public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();


    public void onTestStart(ITestResult result) {


        System.out.println(System.getProperty("user.dir"));
        ExtentTest test = extent.createTest(result.getTestClass().getName()+"     @TestCase : "+result.getMethod().getMethodName());
        testReport.set(test);


    }

    public void onTestSuccess(ITestResult result) {


        String methodName=result.getMethod().getMethodName();
        String logText="<b>"+"TEST CASE:- "+ methodName.toUpperCase()+ " PASSED"+"</b>";
        Markup m=MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        testReport.get().pass(m);
        try {
            TestRailManager.addResultForTestCase(TestBase.getTestCaseID(methodName), TestRailManager.TEST_CASE_PASSED_STATUS, "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (APIException e) {
            throw new RuntimeException(e);
        }


    }

    public void onTestFailure(ITestResult result) {



        String methodName=result.getMethod().getMethodName();
        String excepionMessage=Arrays.toString(result.getThrowable().getStackTrace());
        testReport.get().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
                + "</font>" + "</b >" + "</summary>" +excepionMessage.replaceAll(",", "<br>")+"</details>"+" \n");

        try {
            TestRailManager.addResultForTestCase(TestBase.getTestCaseID(methodName), TestRailManager.TEST_CASE_FAILED_STATUS, "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (APIException e) {
            throw new RuntimeException(e);
        }
		try {

			ExtentManager.captureScreenshot();
			testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(ExtentManager.screenshotPath)
							.build());
		} catch (IOException e) {

		}

        String failureLogg="TEST CASE FAILED";
        Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
        testReport.get().log(Status.FAIL, m);

    }

    public void onTestSkipped(ITestResult result) {
        String methodName=result.getMethod().getMethodName();
        String logText="<b>"+"Test Case:- "+ methodName+ " Skipped"+"</b>";
        Markup m=MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
        testReport.get().skip(m);

    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub

    }

    public void onStart(ITestContext context) {



    }

    public void onFinish(ITestContext context) {

        if (extent != null) {

            extent.flush();
        }

    }

}
