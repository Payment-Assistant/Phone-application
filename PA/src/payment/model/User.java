package payment.model;

import java.util.Date;
import java.util.Vector;

/**
 * класс, содержащий информацию о карте плательщика
 */
public class User {

    // для шифрования пароля (https://javatalks.ru/topics/21642)
    public static byte[] computeHash(String x)
            throws Exception
    {
        java.security.MessageDigest d =null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return  d.digest();
    }

    public static String byteArrayToHexString(byte[] b){
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++){
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    public static void encodingAndSavingPassword(String password){

        try {
            String hash = byteArrayToHexString(computeHash(password));
            System.out.println("the computed hash (hex string) : " + hash);  //TODO hash сохраняем в бд
        } catch (Exception e) {

        }

    }

    public static void passwordIsCorrect(String password) {
        boolean ok = true;
        String inputHash = "";
        while (ok) {
            try {
                inputHash = byteArrayToHexString(computeHash(password));
                if ("".equals(inputHash)) { // TODO replace empty string with hash from DB
                    System.out.println("You got it!");
                    ok = false;
                } else
                    System.out.println("Wrong, try again...!");
            } catch (Exception e) {

            }
        }
    }

    private String name;                        //имя пользователя
    private String phoneNumber;               //номер телефона
    private String password;                    //пароль
    private String email;                       //почта
    private boolean isPhoneNumberValid;       //флаг проверен/не проверен номер телефона
    private Vector<Bill> billsOutcome;            //исходящие счета
    private Vector<Bill> billsIncome;          //входящие счета

    public User(String name,
                String phoneNumber,
                String password){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;       //добавить шифрование пароля !!!
        this.isPhoneNumberValid = false;
        this.billsIncome = new Vector<>(10);
        this.billsOutcome = new Vector<>(10);
        encodingAndSavingPassword(password);
        System.out.println("User " + name + " created!");
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

    public String getName(){return name;}

    public void changePhoneNumberValid(){
        if(!isPhoneNumberValid)
            isPhoneNumberValid = true;
    }

    public void changePhoneNumber(String newPhoneNumber){
        phoneNumber = newPhoneNumber;
        isPhoneNumberValid = false;
    }

    public void changeEmail(String email){
        this.email = email;
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
                                    Bill.Currency.RUB,
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
                Bill.Currency.RUB,
                sum));
    }

    public Vector<Bill> getBillsOutcome(){return billsOutcome;}

    public Vector<Bill> getBillsIncome() {return billsIncome;}
}
