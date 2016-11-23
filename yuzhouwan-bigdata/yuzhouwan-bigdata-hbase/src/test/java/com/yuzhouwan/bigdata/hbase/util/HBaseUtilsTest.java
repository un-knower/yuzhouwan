package com.yuzhouwan.bigdata.hbase.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：HBase Utils Test
 *
 * @author Benedict Jin
 * @since 2016/11/21
 */
public class HBaseUtilsTest {

    @Test
    public void generateSplitKeysTest() throws Exception {

        assertEquals("SPLITS => ['-128', '-112', '-96', '-80', '-64', '-48', '-32', '-16', '0', " +
                        "'16', '32', '48', '64', '80', '96', '112']",
                HBaseUtils.generateSplitKeys(-128, 128, 16, 0));

        assertEquals("SPLITS => ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', " +
                        "'14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', " +
                        "'31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', " +
                        "'48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', " +
                        "'65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79']",
                HBaseUtils.generateSplitKeys(0, 80, 1, 2));
    }
}