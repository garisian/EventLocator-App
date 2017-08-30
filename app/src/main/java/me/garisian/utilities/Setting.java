package me.garisian.utilities;

/**
 * Created by garis on 2017-08-30.
 */
/*
    This class is a template for a setting option in the Main Menu.
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

    // Getter and Setters for Setting Variablesf
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

    public boolean isUsed()
    {
        return used;
    }


}
