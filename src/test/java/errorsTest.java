import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LiteBoxLoginPage;

/**
 * Created by Александр on 02.09.2018.
 * валидация введенных значений форм логина и пароля начинается со встроенной в HTML5 валидации по аттрибутам тегов:
 * type="email" для поля ввода логина -
 * minlength=6 и maxlength=255 для поля ввода пароля
 * используем метод граничных значений для проверки появления скриптового алерта
 * 1) вводим логин-пароль из данных DataProvider
 * 2) жмем кнопку "Войти"
 * 3) записываем появился ли элемент скриптового алерта на страничке
 * 4) сверяем полученный результат с ожидаемым
 * 5) возвращаем все в исходное состояние
 */
public class errorsTest {

    @Parameters("browser")
    @BeforeTest
    public void init(@Optional("firefox") String browser) throws InterruptedException{
        Helper.init(browser);
    }

    @AfterTest
    public void killAll(){
        Helper.driver.quit();
    }

    @DataProvider
    public Object[][] testData(){
        return new Object[][]{
                {
                    Helper.validLogin, //валидный логин
                    Helper.validPassword, //валидный пароль
                    false //скриптовый алерт не показывается - авторизация
                },
                {
                    Helper.validLogin, //валидный логин
                    Helper.passwordGenerator("eng", 5), //случайный пароль длиной 5 знаков
                    false //скриптовый алерт не показывается - HTML5 ругается на количество символов
                },
                {
                    Helper.validLogin, //валидный логин
                    Helper.passwordGenerator("eng",6), //случайный пароль 6 символов
                    true //скриптовый алерт показывается
                },
                {
                    Helper.validLogin, //валидный логин
                    "", //пустой пароль
                    false //скриптовый алерт не показывается - HTML5 ругается на количество символов
                },
                {
                    Helper.validLogin, //валидный логин
                    Helper.spaceGenerator(248)+Helper.validPassword, //248 пробелов перед валидным паролем
                    false //авторизация пройдена
                },
                {
                    Helper.validLogin, //валидный логин
                    Helper.spaceGenerator(249)+Helper.validPassword, //249 пробелов перед валидным паролем
                    true //скриптовый алерт показывается
                },
                {
                    Helper.validLogin, //валидный логин
                    Helper.validPassword+Helper.spaceGenerator(249), //249 пробелов после валидного пароля
                    false //авторизация пройдена
                },
                {
                    Helper.loginGenerator("eng",10), //случайный валидный логин
                    Helper.passwordGenerator("eng",7), //случайный пароль длиной 7 знаков
                    true //скриптовый алерт показывается
                },
                {
                    Helper.loginGenerator("eng",10), //случайный валидный логин
                    Helper.passwordGenerator("eng",254), //случайный пароль длиной 254 знаков
                    true //скриптовый алерт показывается
                },
                {
                    Helper.loginGenerator("eng",10), //случайный валидный логин
                    Helper.passwordGenerator("eng",255), //случайный пароль длиной 255 знаков
                    true //скриптовый алерт показывается
                },
                {
                    Helper.loginGenerator("eng",10), //случайный валидный логин
                    Helper.passwordGenerator("eng",256), //случайный пароль длиной 256 знаков
                    true //скриптовый алерт показывается
                },
                {
                    Helper.loginGenerator("eng",10), //случайный валидный логин
                    Helper.spaceGenerator(5)+"1", //5 пробелов и один знак
                    true //скриптовый алерт показывается
                },
                {
                    Helper.loginGenerator("eng",10), //случайный валидный логин
                    Helper.spaceGenerator(254)+"1", //254 пробелов и один знак
                    true //скриптовый алерт показывается
                }
        };
    }

    @Test(dataProvider = "testData")
    public void errorAlertsCheck(String login, String password, Boolean expectedResult) throws InterruptedException{
        LiteBoxLoginPage loginPage=new LiteBoxLoginPage(Helper.driver);// новый экземпляр класса с элементами странички авторизации
        Boolean actualResult=false; //по умолчанию примем, что скриптовый алерт не отображается на страничке
        Helper.authorization(login,password ); //авторизуемся
        try {
            if(loginPage.loginPassErrorAlert.isDisplayed()) { //если скриптовый алерт показывается на страничке - выводим его текст
                System.out.println("Показан алерт: ");
                System.out.println("--------------------------------------------------------");
                System.out.println(loginPage.loginPassErrorAlert.getText());
                System.out.println("--------------------------------------------------------");
                actualResult = true; //записываем, что скриптовый алерт появился
            }else System.out.println("Алерт не показывается"); //если скритовый алерт есть, но скрыт - выводим, что  алерт не показывается
        }catch (NoSuchElementException e) {
            System.out.println("Элемент не найден на странице"); // если скриптового алерта вообще нет на страничке
            actualResult = false; //записываем, что алерт не появился
        }
        Helper.driver.get(Helper.basicURL); //обновляем страничку
        Helper.cleanFields(); //чистим поля
        Assert.assertTrue(actualResult.equals(expectedResult)); //сверяем полученный результат с ожидаемым
    }
}
