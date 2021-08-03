package ir.bvar.imenfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rezapilehvar on 17/1/2018 AD.
 */

public class Equipment implements Serializable, Cloneable {

    @SerializedName("id")
    private
    int ID;

    @SerializedName("equipment")
    private
    EquipmentData equipmentData;

    public class EquipmentData implements Serializable, Cloneable {
        @SerializedName("id")
        private
        int ID;

        @SerializedName("name")
        private
        String name;

        @SerializedName("advice")
        private
        String advice;

        public int getID() {
            return ID;
        }

        public String getName() {
            return name;
        }

        public String getAdvice() {
            return advice;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EquipmentData)) return false;

            EquipmentData that = (EquipmentData) o;

            return getID() == that.getID();
        }

        @Override
        public int hashCode() {
            return getID();
        }
    }

    private boolean checked = false;

    public Equipment(int ID, EquipmentData equipmentData) {
        this.ID = ID;
        this.equipmentData = equipmentData;
    }

    public Equipment() {

    }

    public EquipmentData getEquipmentData() {
        return equipmentData;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public Equipment clone() throws CloneNotSupportedException {
        Equipment clone = null;

        try {
            clone = (Equipment) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clone == null) {
            clone = new Equipment(ID, equipmentData);
        }

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipment)) return false;

        Equipment equipment = (Equipment) o;

        return ID == equipment.ID;
    }

    @Override
    public int hashCode() {
        return ID;
    }
}
