/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cn.coachs.util.customgallery.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Desction:图片文件夹
 * Author:kuangtiecheng
 * Date:15/7/30 上午11:28
 */
public class BeanPhotoFolderInfo implements Parcelable {
    private int folderId;
    private String folderName;
    private BeanPhotoInfo coverPhoto;
    private List<BeanPhotoInfo> photoList;

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public BeanPhotoInfo getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(BeanPhotoInfo coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public List<BeanPhotoInfo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<BeanPhotoInfo> photoList) {
        this.photoList = photoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.folderId);
        dest.writeString(this.folderName);
        dest.writeParcelable(this.coverPhoto, 0);
        dest.writeTypedList(photoList);
    }

    public BeanPhotoFolderInfo() {
    }

    protected BeanPhotoFolderInfo(Parcel in) {
        this.folderId = in.readInt();
        this.folderName = in.readString();
        this.coverPhoto = in.readParcelable(BeanPhotoInfo.class.getClassLoader());
        this.photoList = in.createTypedArrayList(BeanPhotoInfo.CREATOR);
    }

    public static final Creator<BeanPhotoFolderInfo> CREATOR = new Creator<BeanPhotoFolderInfo>() {
        public BeanPhotoFolderInfo createFromParcel(Parcel source) {
            return new BeanPhotoFolderInfo(source);
        }

        public BeanPhotoFolderInfo[] newArray(int size) {
            return new BeanPhotoFolderInfo[size];
        }
    };
}
