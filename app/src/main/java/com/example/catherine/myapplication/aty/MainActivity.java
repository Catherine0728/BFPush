package com.example.catherine.myapplication.aty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.catherine.myapplication.BuildConfig;
import com.example.catherine.myapplication.R;
import com.example.catherine.myapplication.feature.IPush;
import com.example.catherine.myapplication.model.PushMessage;
import com.example.catherine.myapplication.utills.ExampleUtil;
import com.example.catherine.myapplication.utills.L;
import com.example.catherine.myapplication.utills.Push;
import com.example.catherine.myapplication.utills.RomUtil;
import com.example.catherine.myapplication.utills.TimeIntervalDialog;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Activity {

    public static boolean isForeground = false;

    public static List<String> logList = new CopyOnWriteArrayList<String>();

    @BindView(R.id.set_alias)
    Button setAlias;
    @BindView(R.id.set_account)
    Button setAccount;
    @BindView(R.id.subscribe_topic)
    Button subscribeTopic;
    @BindView(R.id.unset_alias)
    Button unsetAlias;
    @BindView(R.id.unset_account)
    Button unsetAccount;
    @BindView(R.id.unsubscribe_topic)
    Button unsubscribeTopic;
    @BindView(R.id.pause_push)
    Button pausePush;
    @BindView(R.id.resume_push)
    Button resumePush;
    @BindView(R.id.set_accept_time)
    Button setAcceptTime;
    @BindView(R.id.btn_huawei)
    Button btnHuawei;
    @BindView(R.id.btn_getALlAlias)
    Button btnGetAllAlias;
    @BindView(R.id.log)
    TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        Push.setIPush(iPush);
        Push.register(this, BuildConfig.DEBUG);

        ButterKnife.bind(this);
        setListener();
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    100);
        }

        //android.permission.WRITE_SETTINGS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_SETTINGS},
                    1300);
        }
    }

    private void setListener() {
        // 设置别名
        setAlias.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.set_alias)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                Push.setAlias(MainActivity.this, alias);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 撤销别名
        unsetAlias.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Push.setAlias(MainActivity.this, null);
            }
        });
        // 设置帐号
        setAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.set_account)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.setUserAccount(MainActivity.this, account, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();

            }
        });
        // 撤销帐号
        unsetAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.unset_account)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.unsetUserAccount(MainActivity.this, account, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 设置标签
        subscribeTopic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.subscribe_topic)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                Push.setTag(MainActivity.this, topic, true);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 撤销标签
        unsubscribeTopic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.unsubscribe_topic)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                Push.setTag(MainActivity.this, topic, false);

                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 设置接收消息时间
        setAcceptTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimeIntervalDialog(MainActivity.this, new TimeIntervalDialog.TimeIntervalInterface() {

                    @Override
                    public void apply(int startHour, int startMin, int endHour,
                                      int endMin) {
                        MiPushClient.setAcceptTime(MainActivity.this, startHour, startMin, endHour, endMin, null);
                    }

                    @Override
                    public void cancel() {
                        //ignore
                    }

                })
                        .show();
            }
        });
        // 暂停推送
        pausePush.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Push.pause(MainActivity.this);
            }
        });

        resumePush.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Push.resume(MainActivity.this);
            }
        });
        btnHuawei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Push.getState(MainActivity.this);
            }
        });
        btnGetAllAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Push.getTags(MainActivity.this);
            }
        });
    }

    /**
     * 刷新日志信息
     */
    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
        log.setText(AllLog);
    }

    @Override
    protected void onResume() {
        isForeground = true;

        super.onResume();
        refreshLogInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Push.unregister(this);
    }

    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    IPush iPush = new IPush() {

        @Override
        public void onRegister(Context context, String registerID) {
            addData("onRegister id:" + registerID + "; target: " + RomUtil.rom().toString());
        }

        @Override
        public void onUnRegister(Context context) {
            addData("onUnRegister");
        }

        @Override
        public void onPaused(Context context) {
            addData("onPaused");
        }

        @Override
        public void onResume(Context context) {
            addData("onResume");
        }


        @Override
        public void onMessage(Context context, PushMessage pushMessage) {

            addData(pushMessage.toString());
        }

        @Override
        public void onMessageClicked(Context context, PushMessage pushMessage) {

            addData("MessageClicked: " + pushMessage.toString());
        }

        @Override
        public void onCustomMessage(Context context, PushMessage pushMessage) {
            if (isForeground) {
                String message = pushMessage.getMessage();
                String extras = pushMessage.getExtra();
                Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
                msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
                if (!ExampleUtil.isEmpty(extras)) {
                    try {
                        JSONObject extraJson = new JSONObject(extras);
                        if (extraJson.length() > 0) {
                            msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                        }
                    } catch (JSONException e) {

                    }

                }
                context.sendBroadcast(msgIntent);
            }
            addData(pushMessage.toString());
        }

        @Override
        public void onAlias(Context context, String alias) {
            addData("alias: " + alias);
        }

        @Override
        public void onTags(Context context, String alias) {
            addData("onTags: " + alias);
        }

        @Override
        public void onLog(Context context, String alias) {
            addData("onLogs: " + alias);
        }
    };

    private void addData(String value) {
        L.line(value);
        logList.add(0, value);
        refreshLogInfo();

    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


}
