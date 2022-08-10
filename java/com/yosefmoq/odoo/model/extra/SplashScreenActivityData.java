package com.yosefmoq.odoo.model.extra;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.yosefmoq.odoo.BR;

/**
 * Created by shubham.agarwal on 5/6/17.
 */

public class SplashScreenActivityData extends BaseObservable {
    @SuppressWarnings("unused")
    private static final String TAG = "SplashScreenActivityDat";


    private boolean updateAvailable;

    @Bindable
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public void setUpdateAvailable(boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
        notifyPropertyChanged(BR.updateAvailable);
    }
}
