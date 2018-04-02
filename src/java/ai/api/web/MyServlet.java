/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.api.web;

import ai.api.model.Fulfillment;
import com.neo.vasgate.VasgateUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author THANG
 */
public class MyServlet extends AIWebhookServlet {

    @Override
    protected void doWebhook(AIWebhookRequest input, Fulfillment output) {
        System.out.println(input.toString());
        // System.out.println("Para location:"+input.getResult().getStringParameter("location"));
        try {
            String action = input.getResult().getAction();
            if (action == null || action.equals("")) {
                output.setSpeech(input.getResult().getFulfillment().getSpeech());
                return;
            }
            if (action.equals("package")) {
                String cmd = input.getResult().getStringParameter("cmdservice");
                String result = VasgateUtil.callVasgate(cmd);
                if (result == null || result.equals("")) {
                    output.setSpeech(input.getResult().getFulfillment().getSpeech());
                    return;
                }
                String[] arrResult = result.split("#");
                if (!arrResult[1].equals("Success")) {
                    output.setSpeech(input.getResult().getFulfillment().getSpeech());
                    return;
                }
                String[] arrPackage = arrResult[2].split("\\|");
                //System.out.println(arrResult[2]);
                String dichvu = input.getResult().getStringParameter("cmdserviceName");
                String out = "Dịch vụ " + dichvu + " có các gói ";
                for (String i : arrPackage) {
                    // System.out.println(i);
                    String[] p = i.split(",");
                    out += "\n" + p[0] + " chu kỳ " + p[1] + " ngày" + " giá " + p[2];
                    
                }
                output.setSpeech(out);
            } else if (action.equals("ktmsisdn")) {
                String msisdn = input.getResult().getStringParameter("msisdn");
                if (!isMobiNumber(msisdn)) {
                    output.setSpeech("Số thuê bao " + msisdn + " không phải số điện thoại của mobifone. Bạn vui lòng kiểm tra lại");
                }
                
                if (msisdn.startsWith("0")) {
                    msisdn = msisdn.substring(1);
                } else if (msisdn.startsWith("84")) {
                    msisdn = msisdn.substring(2);
                }
                String cmd = "KT.DV_DETAIL_NEW -subscriber=" + msisdn;
                String result = VasgateUtil.callVasgate(cmd);
                result = result.trim();
                if (result.equals("no service")) {
                    output.setSpeech("Số thuê bao 0" + msisdn + " không đăng ký sử dụng dịch vụ nào.");
                    return;
                }
                if (result.startsWith("no service|")) result = result.replace("no service|", "");
                String arrResult[] = result.split("\\|");
                String out = "Thuê bao 0" + msisdn + " đang sử dụng dịch vụ :\n";
                for (String dv : arrResult) {
                    String arrDv[] = dv.split(":");
                    String tenDv = arrDv[0];
                    out += "Dịch vụ " + tenDv + " : ";
                    String cacgoi = "";
                    for (int i = 1; i < arrDv.length; i++) {
                        cacgoi += arrDv[i];
                    }
                    String arrGoi[] = cacgoi.split(";");
                    for (String goi : arrGoi) {
                        try {
                            String ttgoi[] = goi.split(",");
                            out += " Gói " + ttgoi[0] + " giá " + ttgoi[1] + " chu kỳ " + ttgoi[4] + " ngày ,";
                        } catch (Exception e) {

                        }
                    }
                    out += "\n";
                }
                output.setSpeech(out);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkWhiteList(String msisdn){
        return true;
    }
    
    public static boolean isMobiNumber(String mobiNumber) {
        String regex = "^(12[01268])[0-9]{7}$|^(9[03])[0-9]{7}$|^(8[9])[0-9]{7}$";
        String s = "";
        if (mobiNumber.startsWith("0")) {
            s = mobiNumber.substring(1);
        }
        if (mobiNumber.startsWith("84")) {
            s = mobiNumber.substring(2);
        }
        CharSequence charSequence = s;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(charSequence);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

}
