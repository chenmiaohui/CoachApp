package com.cn.coachs.ui.patient.others.myaccount;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.nurse.BeanCheckIndentifyCode;
import com.cn.coachs.ui.chat.common.utils.ToastUtil;
import com.cn.coachs.ui.chat.ui.ECSuperActivity;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.Regex;
import com.cn.coachs.util.RegexNumber;
import com.cn.coachs.util.RegexPwd;
import com.cn.coachs.util.SmsContent;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.coach.ascync.AscynRegister;
import com.cn.coachs.coach.model.BeanRegister;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ResourceAsColor")
@SuppressWarnings("ucd")
public class ActivityRegister extends ECSuperActivity implements
        OnClickListener {
    private static String macAddress;
    private TextView titleText;
    private EditText et1, et2, et3;
    private FButton register_btn, getCode_btn;
    private ImageView iv1, iv2, iv3, iv5;
    /**
     * 获取验证码接口
     */
    private String interface_name;
    /**
     * 注册使用的电话号码
     */
    private String telephonenum;
    // private String name;
    /**
     * 注册时获取的验证码
     */
    private String identifyingCode;
    /**
     * 输入的密码
     */
    private String password1;
    private int CodeResultBack;
    private int RegisterResultBack;
    private String CodeDetialBack;
    private String RegisterDetailBack;
    private TimeCount time;
    private Map<String, String> map;
    private Regex reGexPhone, reGexPwd;
    /**
     * 后台返回的数据
     */
    private String retutrnStr;
    private Message msg;
    private static final int NoCode = 0;
    private static final int GetCode = 1;
    private static final int FailToRegister = 2;
    private static final int SuccessToResgister = 3;
    private static final int PSWnoMatch = 4;
    /**
     * 用户类型，0表示患者
     */
    private static final String userType = "1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initial();
    }

    private void initial() {
        reGexPhone = new RegexNumber();
        reGexPwd = new RegexPwd();
        titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("注册");
        iv1 = (ImageView) findViewById(R.id.delete_Login);
        iv2 = (ImageView) findViewById(R.id.delete1_Login);
        iv3 = (ImageView) findViewById(R.id.delete2_Login);
        iv5 = (ImageView) findViewById(R.id.delete2_Login_l);
        et1 = (EditText) findViewById(R.id.phone_Register);
        et2 = (EditText) findViewById(R.id.code_Register);
        et3 = (EditText) findViewById(R.id.password_Register);

        etClick();
        getCode_btn = (FButton) findViewById(R.id.getCode_Register);
        getCode_btn.setOnClickListener(this);
        register_btn = (FButton) findViewById(R.id.submit_Register);
        register_btn.setOnClickListener(this);
        getCode_btn.setCornerRadius(3);
        register_btn.setCornerRadius(3);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv5.setOnClickListener(this);
    }

    int resultID = 0;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.submit_Register:
                /** 提交注册信息 */
                interface_name = new String();
                interface_name = "regist";
                telephonenum = et1.getText().toString();
                identifyingCode = et2.getText().toString();
                password1 = et3.getText().toString();
                showProgressBar();
                AsyncCheckIdentifyCode acicTask = new AsyncCheckIdentifyCode(telephonenum, identifyingCode) {

                    @Override
                    protected void onPostExecute(
                            BeanCheckIndentifyCode beanCheckIndentifyCode) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(beanCheckIndentifyCode);
                        hideProgressBar();
                        resultID = beanCheckIndentifyCode.getResultID();
                        if (!telephonenum.equals("") & !identifyingCode.equals("")
                                & !password1.equals("")) {

                            if (reGexPwd.judge(password1)) {
                                if (telephonenum.length() == 11 && resultID == 1) {
                                    AscynRegister ascynRegister = new AscynRegister(ActivityRegister.this, telephonenum, password1, identifyingCode) {
                                        @Override
                                        protected void onPostExecute(BeanRegister result) {
                                            super.onPostExecute(result);
                                            if (result == null) {
                                                showToastDialog("注册失败");
                                                return;
                                            }
                                            if (result.getResult() == 1) {
                                                UtilsSharedData.initDataShare(ActivityRegister.this);
                                                UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, telephonenum);
                                                UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, password1);
                                                UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
                                                AscyncLogin async = new AscyncLogin(ActivityRegister.this,
                                                        telephonenum, password1, macAddress);
                                                async.execute();
                                            } else {
                                                showToastDialog("注册失败" + result.getDetail());
                                            }
                                        }
                                    };
                                    ascynRegister.execute();

                                } else {
                                    if (telephonenum.length() != 11) {
                                        showToastDialog("手机号长度只能为11位数字");
                                    } else {
                                        showToastDialog("验证码不对");
                                    }

                                }
                            } else {
                                showToastDialog("密码只能" + reGexPwd.getRule());
                            }
                        } else {
                            showToastDialog("请填写所有信息");
                        }
                    }

                };
                acicTask.execute();


                break;
            case R.id.getCode_Register:
                telephonenum = et1.getText().toString();
                if (reGexPhone.judge(telephonenum)) {
                    if (telephonenum.length() == 11) {
                        AsyncJudgeNumberExsits mTask = new AsyncJudgeNumberExsits(
                                telephonenum) {

                            @Override
                            protected void onPostExecute(String result) {
                                // TODO Auto-generated method stub
                                super.onPostExecute(result);
                                if (result.equals("0")) {
                                    ToastUtil.showMessage("该手机号已存在");
                                } else if (result.equals("1")) {
                                    time = new TimeCount(60000, 1000);
                                    time.start();
                                    interface_name = "getidentifycode";
                                    map = new HashMap<String, String>();
                                    map.put("telephoneNum", telephonenum);
//								map.put("terminal", "ANDROID");

                                    getCode_btn.setEnabled(false);
                                    getCode_btn
                                            .setButtonColor(R.color.fbutton_color_concrete);
                                    new Thread(runnableRegister).start();
                                    // 注册短信监听
                                    SmsContent content = new SmsContent(
                                            ActivityRegister.this, new Handler(),
                                            et2);
                                    // 注册短信变化监听
                                    ActivityRegister.this.getContentResolver()
                                            .registerContentObserver(
                                                    content.SMS_URI_INBOX, true,
                                                    content);
                                }
                            }

                        };
                        mTask.execute();

                    } else {
                        showToastDialog("手机号长度不对");
                    }
                } else {
                    showToastDialog("手机号必须都为数字");
                }
                break;
            case R.id.delete_Login:
                et1.setText("");
                break;
            case R.id.delete1_Login:
                et2.setText("");
                break;
            case R.id.delete2_Login:
                iv3.setVisibility(View.INVISIBLE);
                iv5.setVisibility(View.VISIBLE);
                // 文本以密码形式显示
                et3.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case R.id.delete2_Login_l:
                et3.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                iv3.setVisibility(View.VISIBLE);
                iv5.setVisibility(View.INVISIBLE);
                break;

        }
    }

    public void etClick() {
        et1.setFocusable(true);
        et2.setFocusable(true);
        et3.setFocusable(true);
        et1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv1.setVisibility(View.GONE);
                } else {
                    iv1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                int length = et1.getText().toString().length();
                if (length > 11) {
                    int editEnd = et1.getSelectionEnd();
                    arg0.delete(length - 1, editEnd);
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv2.setVisibility(View.GONE);
                } else {
                    iv2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                int length = et2.getText().toString().length();
                if (length > 4) {
                    int editEnd = et2.getSelectionEnd();
                    arg0.delete(length - 1, editEnd);
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                int length = et3.getText().toString().length();
                if ((length > 64) || !reGexPwd.judge(et3.getText().toString())) {
                    int editEnd = et3.getSelectionEnd();
                    if (length > 0) {
                        arg0.delete(length - 1, editEnd);
                        ToastUtil.showMessage("密码只能" + reGexPwd.getRule());
                    }
                }
            }
        });

    }

    Runnable runnableRegister = new Runnable() {

        @Override
        public void run() {
            String url = AbsParam.getBaseUrl() + "/base/app/" + interface_name;
            if (interface_name.equals("regist")) {

                map = new HashMap<String, String>();
                map.put("telephoneNum", telephonenum);
                map.put("password", password1);
                map.put("identifyingCode", identifyingCode);
                map.put("userType", userType);

            }
            try {
                retutrnStr = NetTool.sendHttpClientPost(url, map, "utf-8");
                if (interface_name.equals("getidentifycode")) {
                    JSONObject getCodeResult = new JSONObject(retutrnStr);
                    CodeResultBack = getCodeResult.getInt("result");
                    CodeDetialBack = getCodeResult.getString("detail");
                    msg = new Message();
                    if (CodeResultBack == 0) {
                        msg.what = NoCode;
                    } else if (CodeResultBack == 1) {
                        msg.what = GetCode;
                    }
                    RegisterHandler.sendMessage(msg);
                } else if (interface_name.equals("regist")) {
                    JSONObject RegisterResult = new JSONObject(retutrnStr);
                    RegisterResultBack = RegisterResult.getInt("result");
                    RegisterDetailBack = RegisterResult.getString("detail");
                    msg = new Message();
                    if (RegisterResultBack == 0) {
                        msg.what = FailToRegister;
                    } else if (RegisterResultBack == 1) {
                        msg.what = SuccessToResgister;
                    }
                    RegisterHandler.sendMessage(msg);
                }
                ;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };
    private Handler RegisterHandler = new Handler() {
        public void handleMessage(Message m) {
            switch (m.what) {
                case NoCode:
                    Toast.makeText(getApplicationContext(), "没有获得验证码", Toast.LENGTH_LONG).show();
                    // time = new TimeCount(60000, 1000);
                    break;
                case GetCode:

                    break;
                case FailToRegister:
                    Toast.makeText(getApplicationContext(), "后台返回失败，请重新试一次" + RegisterDetailBack, Toast.LENGTH_LONG)
                            .show();
                    hideProgressBar();
                    break;
                case SuccessToResgister:

                    Toast.makeText(getApplicationContext(), RegisterDetailBack, Toast.LENGTH_LONG)
                            .show();
                    macAddress = getLocalMacAddress();
                    AscyncLogin async = new AscyncLogin(ActivityRegister.this,
                            telephonenum, password1, macAddress);
                    async.execute();
                    break;
                case PSWnoMatch:
                    Toast.makeText(getApplicationContext(), "您输入的密码不一致，请重新输入", Toast.LENGTH_LONG)
                            .show();
                    break;
                default:
                    hideProgressBar();
                    break;
            }
        }
    };

    public String getLocalMacAddress() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");

        if (result == null) {
            return "网络出错，请检查网络";
        }
        if (result.length() > 0 && result.contains("HWaddr")) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            if (Mac.length() > 1) {
                result = Mac.toLowerCase();
            }
        }
        return result.trim();
    }

    private String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            // 执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                // result += line;
                Log.i("test", "line: " + line);
            }

            result = line;
            Log.i("test", "result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            getCode_btn.setText("重新发送");
            getCode_btn.setButtonColor(getResources().getColor(
                    R.color.blue_second));
            getCode_btn.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            getCode_btn.setEnabled(false);
            getCode_btn.setButtonColor(getResources().getColor(
                    R.color.fbutton_color_concrete));
            getCode_btn.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_register;
    }

}
