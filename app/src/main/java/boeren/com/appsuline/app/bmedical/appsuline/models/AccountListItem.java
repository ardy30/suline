package boeren.com.appsuline.app.bmedical.appsuline.models;

/**
 * Created by Jamil on 28-1-2015.
 */
public class AccountListItem {
    private String imageSrc;
    private String name;
    private String desc;
    private String nr;
    private String nrtxt;

    public AccountListItem(String imageSrc, String name, String desc, String nr, String nrtxt) {
        this.imageSrc = imageSrc;
        this.name = name;
        this.desc = desc;
        this.nr = nr;
        this.nrtxt = nrtxt;
    }


    public String getImageSrc() {
        return imageSrc;
    }
    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNr(){
        return nr;
    }
    public String getNrTxt(){
        return nrtxt;
    }
    @Override
    public String toString() {
        return name + "\n" + desc;
    }
}