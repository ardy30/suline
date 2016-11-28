package boeren.com.appsuline.app.bmedical.appsuline.models;


import com.google.gson.Gson;

public class InsulinPompShemaModel {

    public String t1r21;
    public String t1r22;
    public String t1r23;

    public String t1r31;
    public String t1r32;
    public String t1r33;

    public String t1r41;
    public String t1r42;
    public String t1r43;

    public String t1r51;
    public String t1r52;
    public String t1r53;

    public String t1r61;
    public String t1r62;
    public String t1r63;

    public String t1r71;
    public String t1r72;
    public String t1r73;

    public String t2eh11;
    public String t2eh12;
    public String t2eh13;
    public String t2eh14;
    public String t2eh15a;
    public String t2eh15;
    public String t2eh16;

    public String t3eh11;
    public String t3eh12;
    public String t3eh13;
    public String t3eh14;
    public String t3eh15;

    public String t4eh11;
    public String t4eh12;
    public String t4eh13;
    public String t4eh14;

    public String t4eh21;
    public String t4eh22;
    public String t4eh23;
    public String t4eh24;

    public String t4eh31;
    public String t4eh32;
    public String t4eh33;
    public String t4eh34;

    public String t4eh41;
    public String t4eh42;
    public String t4eh43;
    public String t4eh44;

    public String t5eh11;
    public String t5eh12;
    public String t5eh13;
    public String t5eh14;
    public String t5eh15;
    public String t5eh16;
    public String t5eh17;

    public String t6eh11;
    public String t6eh12;
    public String t6eh13;
    public String t6eh14;
    public String t6eh15;
    public String t6eh16;
    public String t6eh17;


    public String toJson(){
        return new Gson().toJson(this);
    }

    public static InsulinPompShemaModel fromJson(String pompModelJson){
        return new Gson().fromJson(pompModelJson,InsulinPompShemaModel.class);
    }
}
