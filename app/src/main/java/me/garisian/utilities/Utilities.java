package me.garisian.utilities;

import android.text.Editable;
import android.widget.EditText;

/**
 * Created by garis on 2017-08-22.
 */

public class Utilities
{
    /*
    * Method written to test Unit Test Implementations
    * */
    public int returnZero()
    {
        return 0;
    }

    /*
    * Check if user input is a valid address
    * Check for format ## Some Address, Postal Code, City, Country
    */
    public boolean validAddress(Editable userInput)
    {
        String stringAddress = userInput.toString();
        String numberRegex = "\\b\\d+\\b";
        return true;
    }
}
