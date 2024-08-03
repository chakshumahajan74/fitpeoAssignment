package fitPeoScript;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FitpeoAssignment {
	
    private static final Logger logger = Logger.getLogger(FitpeoAssignment.class.getName());
    private WebDriver driver;

    
    @BeforeTest
    public void setup() {
	    	logger.info("Starting Fitpeo Assignment script");
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		driver = new ChromeDriver();
    }
    

	@Test
	public void fitpeoTest() throws InterruptedException { 
		driver.get("https://www.fitpeo.com/");
		String title = driver.getTitle();
		driver.manage().window().maximize();
		logger.info("Successfully loaded page with title " + title);
		
		clickRevenueCalculator();
		// Minimum unit of dragging in slider depends upon screen-size/browser, etc.: 820 is not selectable in Mac 14" on Chrome/100% zoom
		Integer valueToSet = 817;
		scrollSlider(valueToSet);
		fillTextboxAndVerifySlider(valueToSet);	
		clickCPTCheckboxes();
		verifyTotalReimbursement();
	}


	private void verifyTotalReimbursement() {
		String totalValueXPath = "/html/body/div[2]/div[1]/header/div/p[4]/p";
		logger.info("Trying to verify 'Total Recurring Reimbursement for all Patients Per Month'");
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			ExpectedCondition<WebElement> totalValueIsVisible = ExpectedConditions.visibilityOfElementLocated(By.xpath(totalValueXPath));
			WebElement totalValue = wait.until(totalValueIsVisible);
			Assert.assertEquals("$75600", totalValue.getText(), "Total Recurring Reimbursement for all Patients Per Month is not same as expected");
	        logger.info("Verified value of 'Total Recurring Reimbursement for all Patients Per Month' successfully");
		} catch (Exception e) {
			Assert.fail("Could NOT find 'Total Recurring Reimbursement for all Patients Per Month'. Exception: " + e.getMessage());
		}
	}


	private void clickCPTCheckboxes() {
		String cpt1XPath = "/html/body/div[2]/div[1]/div[2]/div[1]/label/span[1]";
		String cpt2XPath = "/html/body/div[2]/div[1]/div[2]/div[2]/label/span[1]";
		String cpt3XPath = "/html/body/div[2]/div[1]/div[2]/div[3]/label/span[1]";
		String cpt4XPath = "/html/body/div[2]/div[1]/div[2]/div[8]/label/span[1]";
		
		logger.info("Trying to scroll to CPT checkboxes");
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			ExpectedCondition<WebElement> cpt1IsClickable = ExpectedConditions.visibilityOfElementLocated(By.xpath(cpt1XPath));
			ExpectedCondition<WebElement> cpt2IsClickable = ExpectedConditions.visibilityOfElementLocated(By.xpath(cpt2XPath));
			ExpectedCondition<WebElement> cpt3IsClickable = ExpectedConditions.visibilityOfElementLocated(By.xpath(cpt3XPath));
			ExpectedCondition<WebElement> cpt4IsClickable = ExpectedConditions.visibilityOfElementLocated(By.xpath(cpt4XPath));
			WebElement cpt1Checkbox = wait.until(cpt1IsClickable);
			WebElement cpt2Checkbox = wait.until(cpt2IsClickable);
			WebElement cpt3Checkbox = wait.until(cpt3IsClickable);
			WebElement cpt4Checkbox = wait.until(cpt4IsClickable);
			
			logger.info("Clicking CPT 1 checkbox");
			cpt1Checkbox.click();
			logger.info("Clicking CPT 2 checkbox");
			cpt2Checkbox.click();
			logger.info("Clicking CPT 3 checkbox");
			cpt3Checkbox.click();
			logger.info("Clicking CPT 4 checkbox");
			cpt4Checkbox.click();
	        logger.info("Clicked all CPT checkboxes successfully");
		} catch (Exception e) {
			Assert.fail("Could NOT find CPT checkboxes. Exception: " + e.getMessage());
		}
	}


	private void scrollSlider(Integer valueToSet) {
		String value = valueToSet.toString();
		By sliderClass = By.className("MuiSlider-rail");
		By sliderThumbClass = By.className("MuiSlider-thumb");
		
		WebElement slider = null, thumb = null;
		
		logger.info("Trying to scroll to slider");
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			ExpectedCondition<WebElement> sliderIsVisible = ExpectedConditions.visibilityOfElementLocated(sliderClass);
			ExpectedCondition<WebElement> thumbIsVisible = ExpectedConditions.visibilityOfElementLocated(sliderThumbClass);
			slider = wait.until(sliderIsVisible);
			thumb = wait.until(thumbIsVisible);
			Actions actions = new Actions(driver);			
            actions.moveToElement(slider).perform();
	        logger.info("Scrolled to slider successfully");
		} catch (Exception e) {
			Assert.fail("Could NOT find slider. Exception: " +  e.getMessage());
		}
		
		int maxValue = 2000;
		logger.info("Trying to set slider value to " + value);
		try {
			Actions actions = new Actions(driver);
			int sliderWidth = slider.getSize().getWidth();
			logger.info("Setting slider to 0. Total Width: " + sliderWidth);
			actions.dragAndDropBy(thumb, -sliderWidth, 0).perform();
            int scrollX = (sliderWidth*valueToSet)/maxValue;
            logger.info("Scrolling by: " + scrollX);
            actions.dragAndDropBy(thumb, 123, 0).perform();			
	        logger.info("Slider set to " + value + " successfully");
		} catch (Exception e) {
			Assert.fail("Could NOT set slider to " + value + ". Exception: " + e.getMessage());
		}
	}


	private void fillTextboxAndVerifySlider(Integer valueToSet) {
		String value = valueToSet.toString();
		logger.info("Trying to find text box");
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			By textBoxId = By.id(":r0:");
			ExpectedCondition<WebElement> textBoxIsVisible = ExpectedConditions.visibilityOfElementLocated(textBoxId);
			WebElement textBox = wait.until(textBoxIsVisible);
			
			Assert.assertEquals(value, textBox.getAttribute("value"), "Value of text box doesn't match slider");
			
			String newValue = "560";
			textBox.sendKeys(Keys.BACK_SPACE);
			textBox.sendKeys(Keys.BACK_SPACE);
			textBox.sendKeys(Keys.BACK_SPACE);
			textBox.sendKeys(Keys.BACK_SPACE); // max value is 2000 so 4 backspaces should clear
			textBox.sendKeys(newValue);
	        logger.info("Set text box value to " + newValue + " successfully");
	        
	        By sliderValueCssSelector = By.cssSelector("body > div.MuiBox-root.css-3f59le > div.MuiBox-root.css-rfiegf > div.MuiGrid-root.MuiGrid-container.MuiGrid-spacing-xs-6.css-l0ykmo > div:nth-child(2) > div > div > span.MuiSlider-root.MuiSlider-colorPrimary.MuiSlider-sizeMedium.css-duk49p > span.MuiSlider-thumb.MuiSlider-thumbSizeMedium.MuiSlider-thumbColorPrimary.MuiSlider-thumb.MuiSlider-thumbSizeMedium.MuiSlider-thumbColorPrimary.css-sy3s50 > input[type=range]");
	       
	        ExpectedCondition<WebElement> sliderValueIsVisible = ExpectedConditions.presenceOfElementLocated(sliderValueCssSelector);
			WebElement sliderValue = wait.until(sliderValueIsVisible);
			Assert.assertEquals(sliderValue.getAttribute("value"), newValue, "Value of text box doesn't match slider");
			logger.info("Verified text box value match slider value: " + newValue);
		} catch (Exception e) {
			Assert.fail("Could NOT verify text box value with slider. Exception: " + e.getMessage());
		}
	}


	private void clickRevenueCalculator() {
		logger.info("Trying to find and click Revenue Calculator link");

		try {
			// Explicit Wait of 2 secs
			WebDriverWait wait = new WebDriverWait(driver, 10);
			By revenueCalculatorXPath = By.xpath("/html/body/div[1]/div/header/div/div[3]/div[6]/a");
			ExpectedCondition<WebElement> revenueCalculatorIsClickable = ExpectedConditions.elementToBeClickable(revenueCalculatorXPath);
			WebElement revenueCalculatorLink = wait.until(revenueCalculatorIsClickable);
	        revenueCalculatorLink.click();
	        logger.info("Revenue Calculator link clicked successfully");
		} catch (Exception e) {
			Assert.fail("Could NOT find Revenue Calculator link. Exception: " + e.getMessage());
		}
	}
	
	
	@AfterTest
	public void closeDriver() {
		driver.quit();
	}
	
}
