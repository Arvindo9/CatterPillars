package com.solution.catterpillars.ui.home.dashboard.model;

import android.arch.lifecycle.ViewModel;

/**
 * Created by KinG on 10-12-2018.
 * Created by ${EMAIL}.
 */
public class DataType extends ViewModel {

    private final Object data;
    private final String dataType;
    private final String dataName;

    public DataType(Object data, String dataType, String dataName){
        this.data = data;
        this.dataType = dataType;
        this.dataName = dataName;
    }

    public Object getData() {
        return data;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDataName() {
        return dataName;
    }
}
