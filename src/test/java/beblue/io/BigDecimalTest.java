package beblue.io;

import java.math.BigDecimal;

public class BigDecimalTest {

    public static void main(String... args) {

        double r = 28.8965;
        // BigDecimal bd = new BigDecimal("28.8900000000000005684341886080801486968994140625"); 
        BigDecimal bd = new BigDecimal(r);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        System.out.println(bd.toString());

    }
}
