package ru.portal_systems.tasknote;

/**
 * Created by 111 on 22.08.2017.
 */

public class CodeHelper {
    //request code for intent
    public static final int C_NEW = 1;
    public static final int C_EDIT = 0;

    //fields of Task for intent extras
    public static final String T_NAME = "0";
    public static final String T_DESCRIPTION = "1";
    public static final String T_TERM = "2";
    public static final String T_PRIOR = "3";
    public static final String T_COMPL = "4";

    //tags for tasksList
    public static final String TL_NUM_EDIT = "5";
    public static final int TL_ALL = 6;
    public static final int TL_ACTUAL = 7;
    public static final int TL_TERMOVER = 8;

}
