import org.testng.Assert;
import org.testng.annotations.*;

/**
 * Created by Александр on 02.09.2018.
 * тестовые данные:
 * логин: simple.test.acc@gmail.com
 * пароль: litebox
 * используем наборы данных из DataProvider содержащие пары логин-пароль и ожидаемый результат
 * 1) введем значение логина и пароля
 * 2) пробуем авторизоваться
 * 3) узнаем, совпадает ли заголовок открывшейся странички с заголовком странички управления магазином
 * 4) сверяем ожидаемый и полученный результат
 * 5) возвращаем все в исходное состояние
 */
public class authorizationTest {
    @Parameters("browser")
    @BeforeTest
    public void init(@Optional("chrome") String browser) throws InterruptedException{
        Helper.init(browser);
    }

    @AfterTest
    public void killAll(){
        Helper.driver.quit();
    }

    @DataProvider //набор тестовых данных "логин", "пароль", "успешная ли авторизация"
    public Object[][] authorizationData(){
        return new Object[][]{
                {Helper.validLogin, Helper.validPassword, true}, //валидные значения
                {"", "", false}, //пустые поля
                {Helper.validLogin, "", false},//валидный логин и пустой пароль
                {"", Helper.validPassword, false}, //пустой ллогин и валидный пароль
                {Helper.validPassword, Helper.validLogin, false}, //валидные логин и пароль поменяны местами
                {Helper.validLogin, "' or '1'='1'", false}, //валидный логин и инъекция кода
                {Helper.validLogin.toUpperCase(), Helper.validPassword, true}, //валидный логин заглавными буквами и валидый пароль
                {Helper.validLogin, Helper.validPassword.toUpperCase(), false}, //валидный логин и валидный пароль заглавными буквами
                {" simple.test.acc@gmail.com", Helper.validPassword, true}, //пробел перед валидным логином и валидный пароль
                {Helper.validLogin+" ", Helper.validPassword, true}, // пробел после валидного логина и валидный пароль
                {Helper.validLogin, " "+Helper.validPassword, true}, //валидный логин и пробел перед валидным паролем
                {Helper.validLogin, Helper.validPassword+" ", true}, // валидный логин и пробел после валидного пароля
                {Helper.validLogin, "NULL", false}, //валидный логин и НУЛЛ
                {"NULL", Helper.validPassword, false}, //НУЛЛ и валидный пароль
                {"NULL", "NULL", false}, //нулл и нулл
                {"admin", "admin", false},
                {"<script>alert(123)</script>", Helper.validPassword, false}, //инъекция кода и валидный пароль
                {Helper.validLogin, "<script>alert(123)</script>", false}, // вадидный логин и инъекция кода
                {"<form action='http://live.hh.ru'><input type='submit'></form>", Helper.validPassword, false} //инъекция кода и валидный пароль
        };
    }

    @Test(dataProvider = "authorizationData")
    public void authorizationFormTest(String login, String pass, Boolean expectedResult) throws InterruptedException{
        Helper.authorization(login,pass); //пытаемся авторизоваться используя пару логин и пароль из набора DataProvider
        //узнаем совпадает ли заголовок открывшейся странички с заголовком упраления магазином
        Boolean actualResult = Helper.driver.getTitle().equals(Helper.storePageTitle);
        Helper.driver.get(Helper.basicURL); //возвращаем исходное состояние странички
        Helper.cleanFields(); //очищаем поля
        Assert.assertEquals(actualResult, expectedResult ); //сверяем актуальный и ожидаемый результат сравнения заголовков странички
    }
}
