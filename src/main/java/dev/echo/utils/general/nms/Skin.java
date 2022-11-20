package dev.echo.utils.general.nms;

import lombok.Getter;

public class Skin {
    //get data
    @Getter
    private final String data;

    //get signature
    @Getter
    private final String signature;


    //skin
    public Skin(String data,String signature){
        this.data = data;
        this.signature = signature;
    }

    //skin
    public Skin(String[] sigData){
        this.data = sigData[0];
        this.signature = sigData[1];
    }
}
