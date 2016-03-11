package keegancampbell.tentativefinal;


public class Crisis {

    // this class is to be filled with data per API request
    // TODO(kcampbell):
    // 1. Set up a generic picture per type and associate a filename with it to be loaded by the CustomAdapter

    private String location; // latitude + longitude coordinates
    private String url; // news url per API request
    private String type; // type of crisis/title per API request
    private String shortDescription; // short description per API request
    private String id; // id in CrisisNet system

    // constructors
    public Crisis(String type, String id)
    {
        this.type = type;
        this.id = id;
    }
    public Crisis(String location, String url, String type)
    {
        this.location = location;
        this.url = url;
        this.type = type;
    }
    public Crisis(String location, String url, String type, String shortDescription)
    {
        this.location = location;
        this.url = url;
        this.type = type;
        this.shortDescription = shortDescription;
    }

    // basic getter functions
    public String getLocation()
    {
        return location;
    }

    public String url()
    {
        return url;
    }

    public String getType()
    {
        return type;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public String getId()
    {
        return id;
    }

    // basic setter functions
    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
