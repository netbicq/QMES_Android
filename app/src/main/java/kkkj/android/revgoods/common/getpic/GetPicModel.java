package kkkj.android.revgoods.common.getpic;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

import kkkj.android.revgoods.bean.SamplingDetails;

/**
 * 照片或者录像
 */
public class GetPicModel extends LitePalSupport implements Serializable {
    @Column(unique = true, defaultValue = "unknow")
    String imagePath = "";

    String mp4Path="";

    int type=-1; //0照片 1视频

    String recordId="";

    String content="";

    int isDwon=0;//是否可编辑，默认可编辑

    int isUpload = 0;//0表示不显示上传按钮

    String SubjectID="";

    private int id;

    private SamplingDetails mSamplingDetails;

    public SamplingDetails getSamplingDetails() {
        return mSamplingDetails;
    }

    public void setSamplingDetails(SamplingDetails samplingDetails) {
        mSamplingDetails = samplingDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectID() {
        return SubjectID;
    }

    public void setSubjectID(String subjectID) {
        SubjectID = subjectID;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }

    public int getIsDwon() {
        return isDwon;
    }

    public void setIsDwon(int isDwon) {
        this.isDwon = isDwon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getMp4Path() {
        return mp4Path;
    }

    public void setMp4Path(String mp4Path) {
        this.mp4Path = mp4Path;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
