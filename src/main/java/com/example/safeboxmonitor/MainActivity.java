package com.example.safeboxmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.safeboxmonitor.common.CCPAppManager;
import com.example.safeboxmonitor.core.ClientUser;
import com.example.safeboxmonitor.ui.helper.IMChattingHelper;
import com.example.safeboxmonitor.ui.helper.SDKCoreHelper;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMChattingHelper.OnMessageReportCallback {

    String mobile = "91407102";
    String pass = "";
    String appKey = "8aaf070858cd982e0158e21ff0000cee";
    String token = "ca8bdec6e6ed3cc369b8122a1c19306d";

    private Button mLogin;
    ECInitParams.LoginAuthType mLoginAuthType = ECInitParams.LoginAuthType.NORMAL_AUTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //save app key/ID and contact number etc. and init rong-lian-yun SDK
        ClientUser clientUser = new ClientUser(mobile);
        clientUser.setAppKey(appKey);
        clientUser.setAppToken(token);
        clientUser.setLoginAuthType(mLoginAuthType);
        clientUser.setPassword(pass);
        CCPAppManager.setClientUser(clientUser);
        SDKCoreHelper.init(MainActivity.this, ECInitParams.LoginMode.FORCE_LOGIN);
        IMChattingHelper.setOnMessageReportCallback(MainActivity.this);

        mLogin = (Button)findViewById(R.id.login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "login succeed!", Toast.LENGTH_SHORT).show();
//                Intent mIntent = new Intent();
//                mIntent.setClass(MainActivity.this, MenuActivity.class);
//                startActivity(mIntent);

            }
        });
    }
    @Override
    public void onMessageReport(ECError error, ECMessage message) {

    }

    @Override
    public void onPushMessage(String sessionId, List<ECMessage> msgs) {
        int msgsSize = msgs.size();
        String message = " ";
        for (int i = 0; i < msgsSize; i++){
            message = ((ECTextMessageBody) msgs.get(i).getBody()).getMessage();
            Log.d("TIEJIANG", "[MainActivity-onPushMessage]" + "i :" + i + ", message = " + message);// add by tiejiang
        }

        Log.d("TIEJIANG", "[MainActivity-onPushMessage]" + ",sessionId :" + sessionId);// add by tiejiang
//mReceiveEditText.setText(message);
        handleSendTextMessage(message + "callback");
    }
    /**
     * 处理文本发送方法事件通知
     * @param text
     */
    public static void handleSendTextMessage(CharSequence text) {
        if(text == null) {
            return ;
        }
        if(text.toString().trim().length() <= 0) {
//canotSendEmptyMessage();
            return ;
        }
// 组建一个待发送的ECMessage
        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
// 设置消息接收者
//msg.setTo(mRecipients);
        msg.setTo("20170419"); // attenionthis number is not the login number! / modified by tiejiang
        ECTextMessageBody msgBody=null;
        Boolean isBQMMMessage=false;
        String emojiNames = null;
//if(text.toString().contains(CCPChattingFooter2.TXT_MSGTYPE)&& text.toString().contains(CCPChattingFooter2.MSG_DATA)){
//try {
//JSONObject jsonObject = new JSONObject(text.toString());
//String emojiType=jsonObject.getString(CCPChattingFooter2.TXT_MSGTYPE);
//if(emojiType.equals(CCPChattingFooter2.EMOJITYPE) || emojiType.equals(CCPChattingFooter2.FACETYPE)){//说明是含有BQMM的表情
//isBQMMMessage=true;
//emojiNames=jsonObject.getString(CCPChattingFooter2.EMOJI_TEXT);
//}
//} catch (JSONException e) {
//e.printStackTrace();
//}
//}
        if (isBQMMMessage) {
            msgBody = new ECTextMessageBody(emojiNames);
            msg.setBody(msgBody);
            msg.setUserData(text.toString());
        } else {
// 创建一个文本消息体，并添加到消息对象中
            msgBody = new ECTextMessageBody(text.toString());
            msg.setBody(msgBody);
            Log.d("TIEJIANG", "[RemoteControlCommandActivity]-handleSendTextMessage" + ", txt = " + text);// add by tiejiang
        }

//String[] at = mChattingFooter.getAtSomeBody();
//msgBody.setAtMembers(at);
//mChattingFooter.clearSomeBody();
        try {
// 发送消息，该函数见上
            long rowId = -1;
//if(mCustomerService) {
//rowId = CustomerServiceHelper.sendMCMessage(msg);
//} else {
            Log.d("TIEJIANG", "[RemoteControlCommandActivity]-SendECMessage");// add by tiejiang
            rowId = IMChattingHelper.sendECMessage(msg);

//}
// 通知列表刷新
//msg.setId(rowId);
//notifyIMessageListView(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TIEJIANG", "[RemoteControlCommandActivity]-send failed");// add by tiejiang
        }
    }

}
