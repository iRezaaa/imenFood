package ir.bvar.imenfood.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.models.Equipment;

/**
 * Created by reza on 1/18/18.
 */

public class DeviceViewHolder extends RecyclerView.ViewHolder implements AppCompatCheckBox.OnCheckedChangeListener {

    @BindView(R.id.viewholder_device_mainCheckBox)
    AppCompatCheckBox mainCheckBox;

    @BindView(R.id.viewholder_device_mainTextView)
    AppCompatTextView mainTextView;

    private List<Equipment> equipmentList;


    public DeviceViewHolder(Context context, ViewGroup parent, List<Equipment> equipmentList) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_device, parent, false));
        ButterKnife.bind(this, itemView);
        this.equipmentList = equipmentList;
        mainCheckBox.setOnCheckedChangeListener(this);
    }

    public void bindViews(Equipment equipment) {
        mainCheckBox.setChecked(equipment.isChecked());
        mainTextView.setText(equipment.getEquipmentData().getName());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
        int adapterPosition = getAdapterPosition();

        if (adapterPosition != -1) {
            Equipment equipment = equipmentList.get(adapterPosition);

            if (equipment != null) {
                equipment.setChecked(check);
            }
        }
    }
}
