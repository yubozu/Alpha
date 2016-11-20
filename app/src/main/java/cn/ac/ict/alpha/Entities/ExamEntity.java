package cn.ac.ict.alpha.Entities;

import okhttp3.RequestBody;

/**
 * Author: saukymo
 * Date: 11/20/16
 */

public class ExamEntity extends BaseEntity{
    private Integer id;
    private Integer userId;
    private RequestBody file;
    private String result;
    private String createdTime;
    private String examType;
    private Integer medicine;

    public ExamEntity(String result, String examType, RequestBody file, Integer userId, Integer medicine) {
        this.result = result;
        this.userId = userId;
        this.file = file;
        this.createdTime = String.valueOf(System.currentTimeMillis());
        this.examType = examType;
        this.medicine = medicine;
    }

    public Integer getMedicine() {return  medicine; }

    public void setMedicine(Integer medicine) {
        this.medicine = medicine;
    }

    public RequestBody getFile() {
        return file;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getExamType() {
        return examType;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setFile(RequestBody file) {
        this.file = file;
    }
}
