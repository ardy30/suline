package boeren.com.appsuline.app.bmedical.appsuline.models;

import java.io.Serializable;

/**
 * Created by Jamil on 2-2-2015.
 */
public class User implements Serializable{

    private boolean isActiveUser;
    private long userId;
    private String name;
    private String imageSource;
    private String dateOfBirth;
    private boolean isMale;
    private boolean isNeedle;
    private String nurseEmail;
    private String nurseName;
    private String insulinSpuitInfo;
    private String insulinPompInfo;

    private float minCarbohydrate;
    private float maxCarbohydrate;

    private float minInsulin;
    private float maxInsulin;

    private float minBloodLevel;
    private float maxBloodLevel;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        this.isMale = male;
    }

    public boolean isNeedle() {
        return isNeedle;
    }

    public void setNeedle(boolean needle) {
        this.isNeedle = needle;
    }

    public String getNurseEmail() {
        return nurseEmail;
    }

    public void setNurseEmail(String nurseEmail) {
        this.nurseEmail = nurseEmail;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }
    public float getMinCarbohydrate() {
        return minCarbohydrate;
    }

    public void setMinCarbohydrate(float minCarbohydrate) {
        this.minCarbohydrate = minCarbohydrate;
    }

    public float getMaxCarbohydrate() {
        return maxCarbohydrate;
    }

    public void setMaxCarbohydrate(float maxCarbohydrate) {
        this.maxCarbohydrate = maxCarbohydrate;
    }

    public float getMaxBloodLevel() {
        return maxBloodLevel;
    }

    public void setMaxBloodLevel(float maxBloodLevel) {
        this.maxBloodLevel = maxBloodLevel;
    }

    public float getMinBloodLevel() {
        return minBloodLevel;
    }

    public void setMinBloodLevel(float minBloodLevel) {
        this.minBloodLevel = minBloodLevel;
    }

    public float getMaxInsulin() {
        return maxInsulin;
    }

    public void setMaxInsulin(float maxInsulin) {
        this.maxInsulin = maxInsulin;
    }

    public float getMinInsulin() {
        return minInsulin;
    }

    public void setMinInsulin(float minInsulin) {
        this.minInsulin = minInsulin;
    }

    public boolean isActiveUser() {
        return isActiveUser;
    }

    public void setActiveUser(boolean isActiveUser) {
        this.isActiveUser = isActiveUser;
    }

    public String getInsulinSpuitInfo() {
        return insulinSpuitInfo;
    }

    public void setInsulinSpuitInfo(String insulinSpuitInfo) {
        this.insulinSpuitInfo = insulinSpuitInfo;
    }

    public String getInsulinPompInfo() {
        return insulinPompInfo;
    }

    public void setInsulinPompInfo(String insulinPompInfo) {
        this.insulinPompInfo = insulinPompInfo;
    }
}
