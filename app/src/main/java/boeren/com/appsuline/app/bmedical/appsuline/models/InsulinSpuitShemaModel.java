package boeren.com.appsuline.app.bmedical.appsuline.models;


import com.google.gson.Gson;

public class InsulinSpuitShemaModel {
    public String etShortInsuline;
    public String etLongInsuline;
    public String date1;
    public String date2;
    public String date3;
    public String date4;
    public String date5;

    public String t1eh02;
    public String t1eh03;
    public String t1eh04;
    public String t1eh05;

    public String t1eh11;
    public String t1eh12;
    public String t1eh13;
    public String t1eh14;
    public String t1eh15;

    public String t1eh21;
    public String t1eh22;
    public String t1eh23;
    public String t1eh24;
    public String t1eh25;

    public String t1eh31;
    public String t1eh32;
    public String t1eh33;
    public String t1eh34;
    public String t1eh35;

    public String t1eh41;
    public String t1eh42;
    public String t1eh43;
    public String t1eh44;
    public String t1eh45;


    public String t2eh11;
    public String t2eh12;
    public String t2eh13;

    public String t3eh11;
    public String t3eh12;
    public String t3eh13;
    public String t3eh14;

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static InsulinSpuitShemaModel fromJson(String spuitModelJson){
        return new Gson().fromJson(spuitModelJson,InsulinSpuitShemaModel.class);
    }
}
