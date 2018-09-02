package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by Александр on 02.09.2018.
 */
public class LiteBoxLoginPage {
    private WebDriver driver;
    //инициализируем пэйджфэктори
    public LiteBoxLoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }
    //логотип
        @FindBy (xpath = "*//a[@class='logo']")
        public WebElement Logo;

    //корзина
        @FindBy (xpath = "//a[@class='btn-shop']")
        public WebElement btnShop;

    //логин
        @FindBy (xpath = "//*[@id='id_email']")
        public WebElement idEmail;

    //пароль
        @FindBy (id = "id_password")
        public WebElement idPassword;

    //показать
        @FindBy (xpath = "//a[@class='btn eye-btn']")
        public WebElement eyeBtn;

    //запомнить меня
        @FindBy (id = "id_remember")
        public WebElement idRemember;

    //кнопка Войти
        @FindBy (id = "login-submit-btn")
        public WebElement loginSubmitBtn;

    //ссылка на страницу регистрации
        @FindBy (xpath = "//a[text()=\"Регистрация\"]")
        public WebElement registration;

    //ссылка Забыли пароль?
        @FindBy (xpath = "//a[text()=\"Забыли пароль?\"]")
        public WebElement fogotPass;

    //ссылка подтверждение регистрации
        @FindBy (xpath = "//a[text()=\"Подтверждение регистрации\"]")
        public WebElement acceptRegister;

    //ссылка Не приходят email?
        @FindBy (xpath = "//a[text()=\"Не приходят email?\"]")
        public WebElement emailSendFail;

    //ссылка Тарифы
        @FindBy (xpath = "//span[text()=\"Тарифы\"]")
        public WebElement rates;

    //ссылка Центр поддержки
        @FindBy (xpath = "//span[text()=\"Центр поддержки\"]")
        public WebElement support;

    //фэйсбук
        @FindBy (xpath = "//a[@class=\"soc_fb\"]")
        public WebElement facebook;

    //контакт
        @FindBy (xpath = "//a[@class=\"soc_vk\"]")
        public WebElement vk;

    //ютуб
        @FindBy (xpath = "//a[@class=\"soc_yotube\"]")
        public WebElement youtube;

    //поле ввода пароля
        @FindBy (name = "password")
        public WebElement inputPasswordField;

    //Алерт при вводе неверной пары логина и пароля
        @FindBy(xpath = "//div[@class=\"alert alert-danger alert-dismissable alert-link\"]")
        public WebElement loginPassErrorAlert;

    //обработка клика на элемент
        public void click(WebElement webElement) throws InterruptedException{
            //webElement.click();    убрал, т.к. на страничке есть элементы, перекрытые другими элементами и Selenium не может до них добраться
            try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
                }catch (StaleElementReferenceException e) {
                System.out.println("Элемент на который мы кликнули больше не доступен на страничке");
            }
                System.out.println("Кликнули на элемент " + webElement);
                Thread.sleep(1000);
        }

    //обработка ввода текста
        public void sendText(String text, WebElement webElement) throws InterruptedException{
            webElement.click();
            webElement.sendKeys(text);
            System.out.println("ввели в текстовое поле текст:" +text);
        }



}

