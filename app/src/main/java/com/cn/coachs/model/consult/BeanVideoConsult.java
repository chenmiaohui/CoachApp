package com.cn.coachs.model.consult;

import java.util.List;

/*
 * 视频数据实体
 */
public class BeanVideoConsult {
    private String dateString;
    private List<BeanVideoDetail> WorkTime;

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public List<BeanVideoDetail> getWorkTime() {
        return WorkTime;
    }

    public void setWorkTime(List<BeanVideoDetail> workTime) {
        WorkTime = workTime;
    }

}
