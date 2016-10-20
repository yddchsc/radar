package com.example.yddchsc.baidumap;

import java.io.Serializable;

public class friend implements Serializable {

    private static final long serialVersionUID = 1L;

    private int type = 1;          //类型
    private String name ;       //文本信息
    private String number ;
    private String long_lang ;
    private String text4 ;

    public friend(){
    }

    public int getType(){
        return  type ;
    }

    public void setType(int type){
        this.type   =   type ;
    }

    public String getText(String i){
        switch (i){
            case "name":
                return name;
            case "number":
                return number;
            case "long_lang":
                return long_lang;
            case "date":
                return text4;
        }
        return name;
    }

    public void setText(String i,String text){
        switch (i){
            case "name":
                this.name=text;
                break;
            case "number":
                this.number=text;
                break;
            case "long_lang":
                this.long_lang=text;
                break;
            case "date":
                this.text4=text;
                break;
        }
    }
}


