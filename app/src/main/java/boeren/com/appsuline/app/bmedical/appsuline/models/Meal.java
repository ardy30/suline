package boeren.com.appsuline.app.bmedical.appsuline.models;


public class Meal {
    private int iconResource;
    private LogBookEntry.Type type;
    private String name;

    public Meal(){}
    public Meal(int iconResource,String name,LogBookEntry.Type type){
        this.iconResource=iconResource;
        this.name=name;
        this.type=type;
    }
    public LogBookEntry.Type getType() {
        return type;
    }

    public void setType(LogBookEntry.Type type)
    {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource)
    {
        this.iconResource = iconResource;
    }
}
