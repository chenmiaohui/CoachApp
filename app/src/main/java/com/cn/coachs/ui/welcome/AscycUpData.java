package com.cn.coachs.ui.welcome;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.personinfo.BeanCommonResult;
import com.cn.coachs.model.personinfo.BeanResultUtils;
import com.cn.coachs.service.ServiceUpdate;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.CustomDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

public class AscycUpData extends AsyncTask<Integer, Integer, String> {
    String result;
    private Context act;
    private String version, versionName, description;
    private int ifForce;

    public AscycUpData(Context act) {
        this.act = act;
    }

    @Override
    protected String doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("appType", "1");
        param.put("envType", "2");
        param.put("versionCode", AppMain.verCode + "");
        try {
            result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                            + "/update/app/checknew",
                    param, "utf-8");
            Log.i("result", result);
            jsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void jsonResult(String result2) throws Exception {
        // TODO Auto-generated method stub
        Gson gson = new Gson();
        BeanCommonResult bean = BeanResultUtils.parseResult(result2);
        bean.getData().toString();
        JSONObject jsonObject = new JSONObject(bean.getData().toString());
        version = jsonObject.getString("version");
        versionName = jsonObject.getString("versionName");
        description = jsonObject.getString("description");
        ifForce = jsonObject.getInt("ifForce");
    }

    @Override
    protected void onPostExecute(String result) {
        if (version != null) {
            if (Integer.parseInt(version) > AppMain.verCode) {
                //版本更新
                showDialog();
            } else {
                if (act.getClass().getName().equals("com.cn.coachs.ui.patient.setting.ActivityAboutUs")) {
                    ((ActivityBasic) act).showToastDialog("您当前版本为最新版本");
                }
            }
        }

    }

    private void showDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(act);
        builder.setTitle("提示");
        builder.setMessage("有最新的软件版本:" + versionName + "\n更新描述:" + description);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 开始下载
                String url = AbsParam.getBaseUrl() + "/update/app/downloadnew"
                        + "?appType=1&envType=2&versionCode=" + version;
                // mUpdateManager = new UpdateManager(act);
                // mUpdateManager.setApkUrl(url);
                // mUpdateManager.checkUpdateInfo();
                // 开始下载更新
                Intent intent = new Intent(act, ServiceUpdate.class);
                intent.putExtra("Key_App_Name", "爱练(教练端)");
                intent.putExtra("Key_Down_Url", url);
                act.startService(intent);
            }
        });
        if (ifForce == 0) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

}
