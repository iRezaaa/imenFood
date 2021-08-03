package ir.bvar.imenfood.managers;

import java.util.ArrayList;
import java.util.List;

import ir.bvar.imenfood.models.CheckupTimes;
import ir.bvar.imenfood.models.Equipment;
import ir.bvar.imenfood.models.UserInfo;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public class StaticDataManager {

    /**
     * User Info
     */
    private UserInfo currentUserInfo;

    public UserInfo getCurrentUserInfo() {
        return currentUserInfo;
    }

    public void setCurrentUserInfo(UserInfo currentUserInfo) {
        this.currentUserInfo = currentUserInfo;
    }

    /**
     * Equipment List
     */
    private List<Equipment> equipmentList = new ArrayList<>();

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public List<Equipment> getCloneOfEquipmentList() {
        List<Equipment> clone = new ArrayList<>(equipmentList.size());

        for (Equipment item : equipmentList)
            try {
                clone.add(item.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        return clone;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        if (equipmentList != null) {
            this.equipmentList.clear();
            this.equipmentList.addAll(equipmentList);
        }
    }

    /**
     * Checkup times
     */
    private CheckupTimes checkupTimes;

    public void setCheckUpTimes(CheckupTimes checkUpTimes) {
        this.checkupTimes = checkUpTimes;
    }

    public CheckupTimes getCheckupTimes() {
        return checkupTimes;
    }

    /**
     * Links
     */
    private String supportLink;
    private String rulesLink;

    public String getSupportLink() {
        return supportLink;
    }

    public void setSupportLink(String supportLink) {
        this.supportLink = supportLink;
    }

    public String getRulesLink() {
        return rulesLink;
    }

    public void setRulesLink(String rulesLink) {
        this.rulesLink = rulesLink;
    }
}
