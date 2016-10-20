package com.example.yddchsc.baidumap;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[])intent.getExtras().get("pdus");   //接收数据
        for(Object p: pdus){
            byte[]pdu = (byte[])p;
            SmsMessage message = SmsMessage.createFromPdu(pdu); //根据获得的byte[]封装成SmsMessage
            String body = message.getMessageBody();             //发送内容
            //String date = new Date(message.getTimestampMillis()).toLocaleString();//发送时间
            String sender = message.getOriginatingAddress();    //短信发送方

            Pattern pattern = Pattern.compile("[0-9]*\\u002E[0-9]*/[0-9]*\\u002E[0-9]*");
            Matcher matcher = pattern.matcher(body);

            String long_lang = MainActivity.long_lang;

            boolean b= matcher.matches();

            if("where are you?".equals(body)){
                try {
                    SmsManager manager = SmsManager.getDefault();

                    manager.sendTextMessage(sender, null, long_lang, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                abortBroadcast();   //中断广播
            }else if(b){
                File file = new File(context);
                List<friend> datas = (List<friend>) file.getObject("friends.dat");
                for(friend bean:datas){
                    if(("+86"+bean.getText("number")).equals(sender)) {
                        int i = datas.indexOf(bean);
                        bean.setText("long_lang",body);
                        datas.set(i,bean);
                        break;
                    }
                }
                file.saveObject("friends.dat",datas);

                Intent intent0=new Intent(context,MainActivity.class);
                intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent0);
            }
        }
    }
}