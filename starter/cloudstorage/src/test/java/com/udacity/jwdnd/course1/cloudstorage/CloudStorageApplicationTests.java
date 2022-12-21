package com.udacity.jwdnd.course1.cloudstorage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * Write a Selenium test that verifies that the home page is not accessible without logging in.
	 */
	@Test
	public void testHomeAccessibility() {
		driver.get("http://localhost:" + this.port + "/home");
		// Check if home page is not accessible
		Assertions.assertNotEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
	}

	/**
	 * verifies that the home page is no longer accessible after logout
	 */
	@Test
	public void testLogout(){
		doMockSignUp("Logout", "Test", "LGT", "123");
		doLogIn("LGT", "123");
		// logout
		WebElement logout = driver.findElement(By.id("log-out-button"));
		logout.click();

		driver.get("http://localhost:" + this.port + "/home");
		// Check if home page is not accessible
		Assertions.assertNotEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
	}

	private void navigateNotes(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String xpath = "//a[@id='nav-notes-tab']";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		WebElement notes = driver.findElement(By.id("nav-notes-tab"));
		notes.click();
	}
	private void doEditNote(String title, String description){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.clear();
		noteTitle.sendKeys(title);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.clear();
		noteDescription.sendKeys(description);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit-button")));
		WebElement noteSubmit = driver.findElement(By.id("note-submit-button"));
		noteSubmit.click();

		driver.findElement(By.xpath("/html/body/div/div/span/a")).click();
	}

	/**
	 * logs in an existing user, creates a note and verifies
	 * that the note details are visible in the note list
	 */
	@Test
	public void testExistingNotes() throws InterruptedException {
		doMockSignUp("Logout", "Test", "NT", "123");
		doLogIn("NT", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// add one note
		navigateNotes();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
		driver.findElement(By.id("addNote")).click();
		doEditNote("1", "one");

		// add another note
		navigateNotes();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
		driver.findElement(By.id("addNote")).click();
		doEditNote("2", "two");

		// check if we could find two existing note
		navigateNotes();
		String xpath = "//*[@id=\"userTable\"]";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		WebElement noteTable = driver.findElement(By.xpath(xpath));
		List<WebElement> rows = noteTable.findElements(By.tagName("tbody"));
		Assertions.assertEquals(rows.size(), 2);
	}

	/**
	 *  clicks the edit note button on an existing note, changes the note data,
	 *  saves the changes, and verifies that the changes appear in the note list.
	 */
	@Test
	public void testEditingNotes(){
		doMockSignUp("Logout", "Test", "NT2", "123");
		doLogIn("NT2", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// click add note button
		navigateNotes();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
		WebElement addNote = driver.findElement(By.id("addNote"));
		addNote.click();

		// edit note
		doEditNote("1", "one");

		// click edit note button
		navigateNotes();
		String xpath = "//*[@id=\"userTable\"]/tbody[1]/tr/td[1]/button";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		driver.findElement(By.xpath(xpath)).click();

		// edit existing note
		doEditNote("2", "two");

		// get current note title and description
		navigateNotes();
		String titlePath = "//*[@id=\"userTable\"]/tbody[1]/tr/th";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(titlePath)));
		WebElement title = driver.findElement(By.xpath(titlePath));
		String descriptionPath = "//*[@id=\"userTable\"]/tbody[1]/tr/td[2]";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(descriptionPath)));
		WebElement description = driver.findElement(By.xpath(descriptionPath));

		// assertion
		Assertions.assertEquals(title.getText(), "2");
		Assertions.assertEquals(description.getText(), "two");
	}

	/**
	 * Test with an existing user with existing notes, clicks the
	 * delete note button on an existing note, and verifies that
	 * the note no longer appears in the note list.
	 */
	@Test
	public void deleteExistingNote(){
		doMockSignUp("Logout", "Test", "NT3", "123");
		doLogIn("NT3", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// add a note
		navigateNotes();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
		driver.findElement(By.id("addNote")).click();
		doEditNote("1", "one");

		// add another note
		navigateNotes();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
		driver.findElement(By.id("addNote")).click();
		doEditNote("1", "one");

		// click delete button
		navigateNotes();
		String delete = "//*[@id=\"userTable\"]/tbody[1]/tr/td[1]/a";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(delete)));
		driver.findElement(By.xpath(delete)).click();
		driver.findElement(By.xpath("/html/body/div/div/span/a")).click();

		// check if there's only one note
		navigateNotes();
		String xpath = "//*[@id=\"userTable\"]";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		WebElement noteTable = driver.findElement(By.xpath(xpath));
		List<WebElement> rows = noteTable.findElements(By.tagName("tbody"));
		Assertions.assertEquals(rows.size(), 1);
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a successful sign-up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		WebElement loginLink= driver.findElement(By.id("login-link"));
		loginLink.click();
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));

		// Click homepage, check if the link correctly redirect the user to the homepage
		WebElement homepage = driver.findElement(By.id("home-redirect"));
		homepage.click();
		Assertions.assertEquals("http://localhost:" + this.port + "/home-file", driver.getCurrentUrl());
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	private void navigateCredential(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String xpath = "//a[@id='nav-credentials-tab']";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		WebElement notes = driver.findElement(By.id("nav-credentials-tab"));
		notes.click();
	}

	private void doEditCredential(String url, String username, String password){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.click();
		credentialUrl.clear();
		credentialUrl.sendKeys(url);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.click();
		credentialUsername.clear();
		credentialUsername.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.click();
		credentialPassword.clear();
		credentialPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-submit-button")));
		WebElement credentialSubmit = driver.findElement(By.id("credential-submit-button"));
		credentialSubmit.click();

		driver.findElement(By.xpath("/html/body/div/div/span/a")).click();
	}

	/**
	 * 1. logs in an existing user, creates a note and verifies that the note details are visible in the note list
	 * 2. edit the existing credential and verifies that the changes appear in the credential list.
	 * 3. delete the existing credential and verifies that the credential no longer appears in the credential list.
	 */
	@Test
	public void testCredentials() throws InterruptedException {
		doMockSignUp("Logout", "Test", "CT", "123");
		doLogIn("CT", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// add one note
		navigateCredential();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredential")));
		driver.findElement(By.id("addCredential")).click();
		doEditCredential("1", "user1", "123");

		// add another note
		navigateCredential();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredential")));
		driver.findElement(By.id("addCredential")).click();
		doEditCredential("2", "user2", "234");

		// check if we could find two existing note
		navigateCredential();
		String tablePath = "//*[@id=\"credentialTable\"]";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tablePath)));
		WebElement credentialTable = driver.findElement(By.xpath(tablePath));
		List<WebElement> rows = credentialTable.findElements(By.tagName("tbody"));
		Assertions.assertEquals(rows.size(), 2);

		// edit the first credential
		navigateCredential();
		String editPath = "//*[@id=\"credentialTable\"]/tbody[1]/tr/td[1]/button";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(editPath)));
		driver.findElement(By.xpath(editPath)).click();
		doEditCredential("3", "user3", "123");

		// get current credential url and username
		navigateCredential();
		String urlPath = "//*[@id=\"credentialTable\"]/tbody[1]/tr/th";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(urlPath)));
		WebElement url = driver.findElement(By.xpath(urlPath));
		String usernamePath = "//*[@id=\"credentialTable\"]/tbody[1]/tr/td[2]";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(usernamePath)));
		WebElement username = driver.findElement(By.xpath(usernamePath));

		// assertion
		Assertions.assertEquals(url.getText(), "3");
		Assertions.assertEquals(username.getText(), "user3");

		// delete first credential
		navigateCredential();
		String deletePath = "//*[@id=\"credentialTable\"]/tbody[1]/tr/td[1]/a";
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(deletePath)));
		driver.findElement(By.xpath(deletePath)).click();
		driver.findElement(By.xpath("/html/body/div/div/span/a")).click();

		// check if there's only one credential
		navigateCredential();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tablePath)));
		WebElement credentialTable2 = driver.findElement(By.xpath(tablePath));
		List<WebElement> rows2 = credentialTable2.findElements(By.tagName("tbody"));
		Assertions.assertEquals(rows2.size(), 1);
	}
}
