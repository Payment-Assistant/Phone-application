package payment.model;

public class BankEmulator {

    public static int payBill(Bill bill, String pack){
        if(bill != null && pack != null){
            return 1;
        }
        else {
            return 0;
        }
    }

}
