package payment.model;

import java.util.Date;
import java.util.Vector;

/**
 * класс, содержащий информацию о карте плательщика
 */
public class User {

    private String phoneNumber;               //номер телефона
    private String password;                    //пароль
    private String email;                       //почта
    private boolean isPhoneNumberValid;       //флаг проверен/не проверен номер телефона
    private boolean isEmailValid;               //флаг проверен/не проверен email
    private Vector<Bill> billsOutcome;            //исходящие счета
    private Vector<Bill> billsIncome;          //входящие счета

    public User(String phoneNumber,
                String password){
        this.phoneNumber = phoneNumber;
        this.password = password;       //добавить шифрование пароля !!!
        this.isPhoneNumberValid = false;
        this.isEmailValid = false;
        this.billsIncome = new Vector<>(10);
        this.billsOutcome = new Vector<>(10);
    }

    public User(String phoneNumber,
                String password,
                String email) {
        this.phoneNumber = phoneNumber;
        this.password = password;       //добавить шифрование пароля !!!
        this.email = email;             //вставить проверку email с помощью регулярок
        this.isPhoneNumberValid = false;
        this.isEmailValid = false;
        this.billsIncome = new Vector<>(10);
        this.billsIncome = new Vector<>(10);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void addEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void changePhoneNumberValid(){
        if(!isPhoneNumberValid)
            isPhoneNumberValid = true;
    }

    public void changeEmailValid(){
        if(!isEmailValid)
            isEmailValid = true;
    }

    public void changePhoneNumber(String newPhoneNumber){
        phoneNumber = newPhoneNumber;
        isPhoneNumberValid = false;
    }

    public void changeEmail(String email){
        this.email = email;
        isEmailValid = false;
    }

    public void addBillIncome(String idOfBill,
                              String description,
                              String nameOfSender,
                              Date dateOfSending,
                              Date dateOfPay,
                              Bill.Currency currency,
                              int sum){
        billsIncome.add(0, new Bill(idOfBill,
                                    description,
                                    nameOfSender,
                                    dateOfSending,
                                    dateOfPay,
                                    Bill.Currency.RUBLE,
                                    sum));
    }

    public void addBillOutcome(String idOfBill,
                              String description,
                              String nameOfSender,
                              Date dateOfSending,
                              Date dateOfPay,
                              Bill.Currency currency,
                              int sum){
        billsOutcome.add(0, new Bill(idOfBill,
                description,
                nameOfSender,
                dateOfSending,
                dateOfPay,
                Bill.Currency.RUBLE,
                sum));
    }

    public Vector<Bill> getBillsOutcome(){return billsOutcome;}

    public Vector<Bill> getBillsIncome() {return billsIncome;}
}
