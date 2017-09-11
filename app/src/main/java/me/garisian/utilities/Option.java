package me.garisian.utilities;

/**
 * Created by garis on 2017-09-10.
 */

public class Option
{
    private String name;
    private boolean used;

    public Option(String name)
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
