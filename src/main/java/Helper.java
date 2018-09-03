import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import pages.LiteBoxLoginPage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by Александр on 02.09.2018.
 */
public class Helper {
    public static WebDriver driver;
    //ссылка на тестируемую страничку
    public static String basicURL = "https://sa-rc.litebox.ru/accounts/login/";

    //заголовок странички управление магазином
    public static String storePageTitle = "Управление магазином";

    //заголовок странички авторизации
    public static String loginPageTitle = "Войти";

    //тестовый валидный логин для входа
    public static String validLogin = "simple.test.acc@gmail.com";

    //тестовый валидный пароль для входа
    public static String validPassword = "litebox";

    //проверка доступности странички
    public static boolean CheckConnection(){
        Boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(basicURL).openConnection(); //создаем соединения
            con.setRequestMethod("HEAD"); //указываем тип запроса
            result = ((con.getResponseCode()>=200)&&
                    (con.getResponseCode()<300));//проверяем успешный ли ответ, нас устроят 2хх
            System.out.println("Соединение с "+basicURL+" установлено");
        } catch (Exception e){
            //e.printStackTrace();
            System.out.println("к сожалению, невозможно установить соединение с "+ basicURL);
        }finally {
            if (con!=null){
                try{
                    con.disconnect();
                } catch (Exception e){
                    // e.printStackTrace();
                    System.out.println("к сожалению, невозможно установить соединение с "+ basicURL);
                }
            }
        }
        return result;
    }

    //инициализация веб-драйвера
    public static void init(String browser)throws InterruptedException {
        if (Helper.CheckConnection()) {
            //выбираем браузер, в котором будет запущен тест
            if (browser.equals("chrome")) { //выбираем хром
                System.setProperty("webdriver.chrome.driver", "C:\\seleniumgrid\\chromedriver.exe");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless"); //при этом параметре веб-драйвер стартует без открытого браузера
                driver = new ChromeDriver(options);
            }
            if (browser.equals("firefox")) { //выбираем мозиллу
                System.setProperty("webdriver.gecko.driver", "C:\\seleniumgrid\\geckodriver.exe");
                driver = new FirefoxDriver();
            }
            if (browser.equals("ie")) { //выбираем интернет эксплорер
                System.setProperty("webdriver.ie.driver", "C:\\seleniumgrid\\IEDriverServer.exe");
                driver = new InternetExplorerDriver();
            }
            driver.get(Helper.basicURL); //запускаем выбранный вебдрайвер
            Thread.sleep(1000); //ждем 1 с
            System.out.println("WEB драйвер инициализирован");
        }
    }

    //проверка корректности открытия нового окна при клике на элемент(лого или корзина)
    public static void checkNewTabWindowTitle(String expectedTitle) throws InterruptedException{
        String loginWindow = driver.getWindowHandle(); //ловим имя открытой вкладки
        String actualTitle;
        for(String childHandle : driver.getWindowHandles()){ //пробегаем по всем открытым вкладкам браузера
            if (!childHandle.equals(loginWindow)){ //если имя открытой вкладки отличается от имени первоначальной вкладки то
                driver.switchTo().window(childHandle); //переключаемся на эту вкладку
            }
        }
        Thread.sleep(1000); //ждем 1 сек
        actualTitle=driver.getTitle(); //получаем тайтл открытой странички
        Assert.assertEquals(actualTitle,expectedTitle); //сравниваем
        System.out.println("заголовок открытого окна совпадает с ожидаемым");



    }
    //проверка заголовка странички
    public static void checkWindowTitle(String expectedTitle) throws InterruptedException{
        Thread.sleep(1000);
        Assert.assertEquals(driver.getTitle(),expectedTitle);
        System.out.println("страничка по указанной ссылке верная");
        driver.get(basicURL);
    }

    //прохождение авторизации
    public static void authorization(String login, String password) throws InterruptedException{
        LiteBoxLoginPage loginPage= new LiteBoxLoginPage(driver); //создаем новый экземпляр класса с вебэлементами
        loginPage.sendText(login, loginPage.idEmail); //вводим логин
        loginPage.sendText(password, loginPage.idPassword); //вводим пароль
        loginPage.click(loginPage.loginSubmitBtn); //жмем кнопку "войти"
        if (driver.getTitle().equals(storePageTitle)){  //если тайтл открывшейся странички совпадает с тайтлом странички "Управление магазином"
            System.out.println("Авторизация пройдена"); //то авторизация пройдена
        } else System.out.println("Авторизация не пройдена"); //иначе не пройдена
    }

    //генератор паролей нужной длины и раскладки
    public static String passwordGenerator(String charset, int length){
        if (charset.equals("eng")){charset = "abcdefghijklmnopqrstuvwxyz1234567890";} //английская раскладка
        if (charset.equals("rus")){charset = "абвгдеежзийклмнопрстуфхцчшщъыьэюя1234567890";} //русская раскладка
        if (charset.equals("all")){charset = "abcdefghijklmnopqrstuvwxyzабвгдеежзийклмнопрстуфхцчшщъыьэюя1234567890";} //английская и русская раскладка
        String password ="";
        for (int i=0; i<length; i++){
            password = password+charset.charAt(new Random().nextInt(charset.length())); //генерируем пароль заданной длинны и набора символов
        }
        return password;
    }

    //очистка текстовых полей формы
    public static void cleanFields() throws InterruptedException{
        LiteBoxLoginPage loginPage = new LiteBoxLoginPage(driver); //создаем новый экземпляр класса с вебэлементами
        if (!loginPage.idEmail.getText().equals("")){ //если поле ввода логина не пустое
            loginPage.idEmail.clear(); //очищаем поле ввода логина
        }
        if (!loginPage.idPassword.getText().equals("")){ //если поле пароля не пустое
            loginPage.idPassword.clear(); //очищаем поле ввода пароля
        }
    }

    //генератор логинов
    public static String loginGenerator(String charset, int length){
        String login = passwordGenerator(charset,length/2-2)+"@"+passwordGenerator(charset,length/2-2)+".ru";
        return login;
    }

    //генератор пробелов
    public static String spaceGenerator(int length){
        String spaces="";
        for (int x=0;x<length;x++){
            spaces=spaces+" ";
        }
        return spaces;
    }

    //возвращаем значение CSS атрибутов выбранных плейсхолдеров
    public static String[] placeholderCssAttributes(WebElement webElement){
        String[] attributes = {
                webElement.getCssValue("top"),
                webElement.getCssValue("font"),
                webElement.getCssValue("color")
        };
        return attributes;
    }
    //метод печатающий значения CSS атрибутов для проверки плэйсхолдеров
    public static void printResults(String[] placeholderCSS){
        System.out.println("top: "+placeholderCSS[0]);
        System.out.println("font: "+placeholderCSS[1]);
        System.out.println("color: "+placeholderCSS[2]);
        System.out.println("------------------------------------------------------");
    }

}
