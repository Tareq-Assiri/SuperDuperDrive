package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RubricTest {
    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String url;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.url = "http://localhost:" + this.port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    private void doMockSignUp(String firstName, String lastName, String userName, String password){
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
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

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

        Assertions.assertEquals("http://localhost:" + this.port + "/login?success", driver.getCurrentUrl());
    }

    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

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
        try {
            webDriverWait.until(ExpectedConditions.titleContains("Home"));
        }catch(Exception ignored){}
    }

    private void doLogOut(){
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
        WebElement logOutBtn = driver.findElement(By.id("logout"));
        logOutBtn.click();

        //Assert that we were successfully redirected to login screen
        Assertions.assertEquals("http://localhost:" + this.port + "/login?logout", driver.getCurrentUrl());
        //We should be redirected to login again.
        driver.get(url + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());


    }



    /**
     * Test flow from sign up, to login, to logout
     * Rubric:
     * Write a Selenium test that signs up a new user, logs that user in, verifies that they can access the home page,
     * then logs out and verifies that the home page is no longer accessible.
     */
    @Test
    @Order(1)
    void testFlowTillLogout() {
        doMockSignUp("rubric","test","rubricTest","password");
        doLogIn("rubricTest","password");
        doLogOut();
    }

    /**
     * Test that going to home without logging in redirects back to login.
     * Rubric:
     * Write a Selenium test that verifies that the home page is not accessible without logging in.
     */
    @Test
    void testHomeInaccessible(){
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Login"));

        Assertions.assertEquals(url + "/login", driver.getCurrentUrl());
    }

    /**
     * Rubric:
     * Write a Selenium test that logs in an existing user,
     * creates a note and verifies that the note details are visible in the note list.
     */
    @Test
    @Order(2)
    void testNoteCreation(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
        WebElement addNoteBtn = driver.findElement(By.id("addNote"));
        addNoteBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitleTextArea = driver.findElement(By.id("note-title"));
        noteTitleTextArea.click();
        noteTitleTextArea.sendKeys("Title text");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescTextArea = driver.findElement(By.id("note-description"));
        noteDescTextArea.click();
        noteDescTextArea.sendKeys("Desc text");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveChanges")));
        WebElement saveChangesBtn = driver.findElement(By.id("saveChanges"));
        saveChangesBtn.click();

        //Test success url
        Assertions.assertEquals(url + "/result?success=1", driver.getCurrentUrl());
        //test the note is visible
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-display")));
        WebElement noteTitleDisplay = driver.findElement(By.id("note-title-display"));
        WebElement noteDescDisplay = driver.findElement(By.id("note-description-display"));

        Assertions.assertEquals("Title text",noteTitleDisplay.getText());
        Assertions.assertEquals("Desc text", noteDescDisplay.getText());
    }

    /**
     * Rubric:
     * Write a Selenium test that logs in an existing user with existing notes,
     * clicks the edit note button on an existing note, changes the note data,
     * saves the changes, and verifies that the changes appear in the note list.
     */
    @Test
    @Order(3)
    void testChangeNote(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editNote")));
        WebElement noteEditBtn = driver.findElement(By.id("editNote"));
        noteEditBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitleTextArea = driver.findElement(By.id("note-title"));
        noteTitleTextArea.click();
        noteTitleTextArea.sendKeys("Title text");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescTextArea = driver.findElement(By.id("note-description"));
        noteDescTextArea.click();
        noteDescTextArea.sendKeys("Desc text");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveChanges")));
        WebElement saveChangesBtn = driver.findElement(By.id("saveChanges"));
        saveChangesBtn.click();

        //test the note is visible
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-display")));
        WebElement noteTitleDisplay = driver.findElement(By.id("note-title-display"));
        WebElement noteDescDisplay = driver.findElement(By.id("note-description-display"));

        Assertions.assertEquals("Title textTitle text",noteTitleDisplay.getText());
        Assertions.assertEquals("Desc textDesc text", noteDescDisplay.getText());
    }

    /**
     * Rubric:
     * Write a Selenium test that logs in an existing user with existing notes,
     * clicks the delete note button on an existing note,
     * and verifies that the note no longer appears in the note list.
     */
    @Test
    @Order(4)
    void testNoteDelete(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNote")));
        WebElement noteDeleteBtn = driver.findElement(By.id("deleteNote"));
        noteDeleteBtn.click();

        Assertions.assertEquals(url + "/result?success=1", driver.getCurrentUrl());
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        Assertions.assertTrue(driver.findElements(By.id("note-title-display")).isEmpty());
    }

    /**
     * Rubric:
     * Write a Selenium test that logs in an existing user,
     * creates a credential and verifies that the credential details are visible
     * in the credential list.
     */
    @Test
    @Order(5)
    void testCredCreation(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCred")));
        WebElement addCredBtn = driver.findElement(By.id("addCred"));
        addCredBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credUrlTextArea = driver.findElement(By.id("credential-url"));
        credUrlTextArea.click();
        credUrlTextArea.sendKeys("test.url");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credUsernameTextArea = driver.findElement(By.id("credential-username"));
        credUsernameTextArea.click();
        credUsernameTextArea.sendKeys("test user");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credPassTextArea = driver.findElement(By.id("credential-password"));
        credPassTextArea.click();
        credPassTextArea.sendKeys("test pass");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmitBtn")));
        WebElement saveChangesBtn = driver.findElement(By.id("credentialSubmitBtn"));
        saveChangesBtn.click();

        //test the credential is visible
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url-display")));
        WebElement credUrlDisplay = driver.findElement(By.id("credential-url-display"));
        WebElement credUsernameDisplay = driver.findElement(By.id("credential-username-display"));
        WebElement credPassDisplay = driver.findElement(By.id("credential-password-display"));

        Assertions.assertEquals("test.url", credUrlDisplay.getText());
        Assertions.assertEquals("test user", credUsernameDisplay.getText());
        Assertions.assertEquals("test pass", credPassDisplay.getText());

    }

    /**
     * Rubric:
     * Write a Selenium test that logs in an existing user with existing credentials,
     * clicks the edit credential button on an existing credential, changes the credential data,
     * saves the changes, and verifies that the changes appear in the credential list.
     */
    @Test
    @Order(6)
    void testCredEdit(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCred")));
        WebElement editCredBtn = driver.findElement(By.id("editCred"));
        editCredBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credUrlTextArea = driver.findElement(By.id("credential-url"));
        credUrlTextArea.click();
        credUrlTextArea.sendKeys("test.url");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credUsernameTextArea = driver.findElement(By.id("credential-username"));
        credUsernameTextArea.click();
        credUsernameTextArea.sendKeys("test user");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credPassTextArea = driver.findElement(By.id("credential-password"));
        credPassTextArea.click();
        credPassTextArea.sendKeys("test pass");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmitBtn")));
        WebElement saveChangesBtn = driver.findElement(By.id("credentialSubmitBtn"));
        saveChangesBtn.click();

        //Test success url
        Assertions.assertEquals(url + "/result?success=1", driver.getCurrentUrl());
        //test the note is visible
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url-display")));
        WebElement credUrlDisplay = driver.findElement(By.id("credential-url-display"));
        WebElement credUsernameDisplay = driver.findElement(By.id("credential-username-display"));
        WebElement credPassDisplay = driver.findElement(By.id("credential-password-display"));

        Assertions.assertEquals("test.urltest.url",credUrlDisplay.getText());
        Assertions.assertEquals("test usertest user", credUsernameDisplay.getText());
        Assertions.assertEquals("test passtest pass", credPassDisplay.getText());
    }

    @Test
    @Order(7)
    void testCredDelete(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCred")));
        WebElement credDeleteBtn = driver.findElement(By.id("deleteCred"));
        credDeleteBtn.click();

        Assertions.assertEquals(url + "/result?success=1", driver.getCurrentUrl());
        driver.get(url + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        Assertions.assertTrue(driver.findElements(By.id("credential-url-display")).isEmpty());
    }
}
