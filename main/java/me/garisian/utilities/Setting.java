package me.garisian.utilities;

/**
 * Setting.java
 * Purpose: Template used to structure settings
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-08-30
 */
public class Setting
{
    private String name;
    private boolean used;

    public Setting(String name)
    {
        this.name = name;
        used = false;
    }

    /**
     * Description: A series of getters and setters for the class variables
     */
    public void changeName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setUse()
    {
        used = true;
    }

    public void setUnused()
    {
        used = false;
    }

    public void setTrigger(Boolean s) { used = s; }

    public boolean isUsed() { return used; }
}
