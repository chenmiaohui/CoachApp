package com.cn.coachs.ui.chat.ui.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.cn.coachs.ui.chat.core.ClientUser;
import com.cn.coachs.ui.chat.core.Phone;
import com.cn.coachs.ui.chat.storage.AbstractSQLManager;
import com.cn.coachs.ui.chat.storage.ContactSqlManager;

/**
 * 联系人信息
 * Created by Jorstin on 2015/3/18.
 */
public class ECContacts implements Parcelable {

    public static final Parcelable.Creator<ECContacts> CREATOR = new Creator<ECContacts>() {
        @Override
        public ECContacts[] newArray(int size) {
            return new ECContacts[size];
        }

        @Override
        public ECContacts createFromParcel(Parcel in) {
            return new ECContacts(in);
        }
    };

    private long id;
    /**
     * 联系人账号
     */
    private String contactid;
    /**
     * 联系人昵称
     */
    private String nickname;
    /**
     * 联系人类型
     */
    private int type;
    /**
     * 联系人账号Token
     */
    private String token;
    /**
     * 联系人子账号
     */
    private String subAccount;
    /**
     * 联系人子账号Token
     */
    private String subToken;
    /**
     * 备注
     */
    private String remark;
    private List<Phone> phoneList;
    // Other
    private String[] qpName;
    private String jpNumber; //简拼对应的拨号键盘的数字
    private String jpName;
    private String qpNameStr;
    private String[] qpNumber; //保存拼音对应的拨号键盘的数字
    private long photoId;
    private String imgUrl;
    private int remainServeTime;
    private String age;
    private String sex;
    private String height;
    private String weight;
    private String ill;
    private long paitentID;
    /**
     * 是否该患者的保健秘书,得到的值不能大于1
     */
    private int isMaster;


    public int getIsMaster() {
        return isMaster > 1 ? 0 : isMaster;
    }

    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster > 1 ? 0 : isMaster;
    }

    public long getPaitentID() {
        return paitentID;
    }

    public void setPaitentID(long paitentID) {
        this.paitentID = paitentID;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getIll() {
        return ill;
    }

    public void setIll(String ill) {
        this.ill = ill;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getRemainServeTime() {
        return remainServeTime;
    }

    public void setRemainServeTime(int remainServeTime) {
        this.remainServeTime = remainServeTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the contactid
     */
    public String getContactid() {
        return contactid;
    }

    /**
     * @param contactid the contactid to set
     */
    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    private ECContacts(Parcel in) {
        this.id = in.readLong();
        this.contactid = in.readString();
        this.type = in.readInt();
        this.nickname = in.readString();
        this.subAccount = in.readString();
        this.subToken = in.readString();
        this.token = in.readString();
        this.remark = in.readString();
    }

    public String getSortKey() {
        if (jpName == null || jpName.trim().length() <= 0) {
            return "#";
        }
        if (getIsMaster() == 1) {
            return "*";
        }
        String c = jpName.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()) {
            return c.toUpperCase();
        } else {
            return "#";
        }
    }


    public ContentValues buildContentValues() {
        ContentValues values = new ContentValues();
        values.put(AbstractSQLManager.ContactsColumn.CONTACT_ID, this.contactid);
        values.put(AbstractSQLManager.ContactsColumn.type, this.type);
        values.put(AbstractSQLManager.ContactsColumn.USERNAME, this.nickname);
        values.put(AbstractSQLManager.ContactsColumn.SUBACCOUNT, this.subAccount);
        values.put(AbstractSQLManager.ContactsColumn.SUBTOKEN, this.subToken);
        values.put(AbstractSQLManager.ContactsColumn.TOKEN, this.token);
        values.put(AbstractSQLManager.ContactsColumn.REMARK, this.remark);
        return values;
    }

    public ECContacts() {

    }

    public void setClientUser(ClientUser user) {
        setContactid(user.getUserId());
        setNickname(user.getUserName());
        setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager.getIntRandom(4, 0)]);
    }

    public ECContacts(String contactId) {
        this.contactid = contactId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(this.contactid);
        dest.writeInt(this.type);
        dest.writeString(this.nickname);
        dest.writeString(this.subAccount);
        dest.writeString(this.subToken);
        dest.writeString(this.token);
        dest.writeString(this.remark);
    }

    public void addPhoneList(List<Phone> phoneList) {
        if (this.phoneList == null) {
            this.phoneList = new ArrayList<Phone>();
        }
        this.phoneList.addAll(phoneList);
        phoneList.clear();
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public void addPhone(Phone phone) {
        if (this.phoneList == null) {
            this.phoneList = new ArrayList<Phone>();
            setContactid(phone.getPhoneNum());
        }
        this.phoneList.add(phone);
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public String[] getQpName() {
        return qpName;
    }

    public void setQpName(String[] qpName) {
        this.qpName = qpName;
    }

    public void setQpNumber(String[] qpNumber) {
        this.qpNumber = qpNumber;
    }


    public void setJpNumber(String jpNumber) {
        this.jpNumber = jpNumber;
    }

    public void setJpName(String jpName) {
        this.jpName = jpName;
    }

    public String getJpName() {
        return jpName;
    }

    public String getQpNameStr() {
        return qpNameStr;
    }

    public void setQpNameStr(String qpNameStr) {
        this.qpNameStr = qpNameStr;
    }

}
