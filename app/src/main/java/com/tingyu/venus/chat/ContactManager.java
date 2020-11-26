package com.tingyu.venus.chat;

import android.util.Log;

import com.tingyu.venus.event.ContactListener;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.model.entity.UserInfo;
import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.OkHttpClientUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 联系人管理器
 */
public class ContactManager {

    //定义一个联系人事件监听器容器，管理联系人事件监听器
    private static List<ContactListener> contactListeners= Collections.synchronizedList(new ArrayList<>());


    public List<ContactListener> getContactListeners() {
        return contactListeners;
    }

    /**
     * 添加联系人事件监听器
     * @param contactListener
     */
    public void setContactListener(ContactListener contactListener){
        if(!this.contactListeners.contains(contactListener)){
            this.contactListeners.add(contactListener);
        }
    }





    /**
     * 移除事件监听器
     * @param contactListener
     */
    public void removeContactListener(ContactListener contactListener){
        this.contactListeners.remove(contactListener);
    }

    /**
     * 拉取联系人列表
     * @param phone
     * @return
     */
    public List<UserInfo> asyncGetContactList(String phone){

        List<UserInfo> contactList=new ArrayList<>();
        CountDownLatch countDownLatch=new CountDownLatch(1);
        //向服务器请求数据
        Model.getInstance().getExecutor().execute(()->{
            OkHttpClientUtils.get(Constants.HTTP_BASE_URL + "/client/contact/list/" + phone, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //解析响应数据
                    try {
                        String body = response.body().string();
                        Log.d("response data", body);
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(body);
                        String data = jsonObject.getString("data");
                        org.json.JSONObject jsonData = new org.json.JSONObject(data);
                        JSONArray jsonItems = jsonData.getJSONArray("items");

                        if(jsonItems !=null){
                            for (int i = 0; i < jsonItems.length(); i++) {
                                org.json.JSONObject obj = (JSONObject) jsonItems.get(i);
                                //TODO 为contact 联系人赋值
                                UserInfo contact = new UserInfo();
                                contact.setNickname(obj.getString("username"));
                                contact.setId(obj.getString("phone")); //TODO 用户ID暂时保存手机号
                                contact.setPhone(obj.getString("phone"));
                                contact.setUsername(obj.getString("username"));
                                Log.d("contact",contact.toString());
                                contactList.add(contact);
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        countDownLatch.countDown();
                    }



                }
            });
        });
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);//
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    /**
     * 获取单个联系人
     * @param contactPhone
     * @return
     */
    public UserInfo asyncGetContactFromServer(String contactPhone) {
        UserInfo contact = new UserInfo();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        OkHttpClientUtils.get(Constants.HTTP_BASE_URL + "/client/user/get/" + contactPhone, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //解析返回结果
                try{

                    String body = response.body().string();
                    Log.d("response data", body);
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(body);
                    String data = jsonObject.getString("data");
                    org.json.JSONObject jsonData = new org.json.JSONObject(data);
                    JSONObject item = jsonData.getJSONObject("item");
                    if(item!=null){

                        contact.setUsername(item.getString("username"));
                        contact.setPhone(item.getString("phone"));
                        contact.setNickname(item.getString("username"));
                        contact.setAvatar(item.getString("avatar"));
                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                finally{
                    countDownLatch.countDown();
                }
            }
        });
        //主线程等待数据获取完成
        try{
            countDownLatch.await(5,TimeUnit.SECONDS); //等待
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return contact;
    }

}
