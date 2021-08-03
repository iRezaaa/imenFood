package ir.bvar.imenfood.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;
import ir.bvar.imenfood.models.Equipment;

/**
 * Created by rezapilehvar on 14/2/2018 AD.
 */

public class DeviceAdviceViewHolder extends RecyclerView.ViewHolder {
    private List<Equipment> equipmentList;

    @BindView(R.id.viewholder_device_advice_titleTextView)
    AppCompatTextView titleTextView;

    @BindView(R.id.viewholder_device_advice_bodyTextView)
    AppCompatTextView bodyTextView;

    public DeviceAdviceViewHolder(Context context, ViewGroup parent, List<Equipment> equipmentList) {
        super(LayoutInflater.from(context).inflate(R.layout.viewholder_device_advice, parent, false));
        ButterKnife.bind(this, itemView);
        this.equipmentList = equipmentList;
    }

    public void bindViews(Equipment device) {
        titleTextView.setText(device.getEquipmentData().getName());
        bodyTextView.setText(device.getEquipmentData().getAdvice());
    }
}
