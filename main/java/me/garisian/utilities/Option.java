package me.garisian.utilities;

/**
 * Option.java
 * Purpose: Template used to structure options which are pulled from a text file
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-09-10
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

    public void flipTrigger() { used = !used; }

    public boolean isUsed()
    {
        return used;
    }
}
