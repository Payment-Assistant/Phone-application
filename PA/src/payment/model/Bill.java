package payment.model;

import java.util.Date;

/**
 * класс, содержащий информацию о счете плательщика
 */
public class Bill {

    private String idOfBill;        //идентификатор счета
    private String description;     //описание счета (не больше 25 символов)
    private String nameOfSender;    //имя выставителя счета
    private Date dateOfSending;     //дата выставления
    private Date dateOfPay;         //дата оплаты
    private Currency currency;      //валюта
    private int sum;                //сумма
    private boolean isPaid;         //флаг выплачен/не выплачен (default false)
    private String other;           // поле для дополнительной инфы (JSON), чтобы потом было удобнее добавлять новые данные в бд

    public enum Currency{

        RUB("RUB"), USD("USD"), EUR("EUR");
        private String symbol;

        private Currency(String symbol){
            this.symbol = symbol;
        }

        public String getSymbol(){
            return symbol;
        }
    }

    public Bill(String idOfBill,
                String description,
                String nameOfSender,
                Date dateOfSending,
                Date dateOfPay,
                Currency currency,
                int sum){
        this.idOfBill = idOfBill;
        this.description = description;
        this.nameOfSender = nameOfSender;
        this.dateOfSending = dateOfSending;
        this.dateOfPay = dateOfPay;
        this.currency = currency;
        this.sum = sum;
        this.isPaid = false;
    }

    public String getIdOfBill() {
        return idOfBill;
    }

    public String getDescription() {
        return description;
    }

    public String getNameOfSender() {
        return nameOfSender;
    }

    public Date getDateOfSending() {
        return dateOfSending;
    }

    public Date getDateOfPay() {
        return dateOfPay;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getSum() {
        return sum;
    }

    public boolean getStatus(){
        return isPaid;
    }

    public void changePaid(){
        if(!isPaid)
            isPaid = true;
    }

    public String getMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append("Здравствуйте!\n\n");
        builder.append("Ваш счет был оплачен:\n");
        builder.append(String.format("ID: %s\n", idOfBill));
        builder.append(String.format("Описание: %s\n", description));
        builder.append(String.format("Дата выставления: %s\n", dateOfSending));
        builder.append(String.format("Сумма: %d%s\n\n", sum, currency.getSymbol()));
        builder.append("Благодарим за пользование PaymentAssistant");

        return builder.toString();
    }

}
