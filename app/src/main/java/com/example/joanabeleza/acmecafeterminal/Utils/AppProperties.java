package com.example.joanabeleza.acmecafeterminal.Utils;

public class AppProperties {
    private static AppProperties mInstance = null;

    public String hostName = "http://feupcmov-001-site1.htempurl.com/";

    protected AppProperties() {
    }

    public static synchronized AppProperties getInstance() {
        if (null == mInstance) {
            mInstance = new AppProperties();
        }
        return mInstance;
    }

}
