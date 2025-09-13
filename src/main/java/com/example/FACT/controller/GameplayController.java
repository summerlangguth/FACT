package com.example.FACT.controller;

import javafx.application.Platform;
import com.sun.javafx.PlatformUtil;


public class GameplayController {
    private String OS = null;

    /**
     * returns the operating system the application is being run on.
     */
    public void findOS(){
        if(PlatformUtil.isWindows()){
            OS = "Windows";
        } else if (PlatformUtil.isMac()) {
            OS = "Mac";
        } else if (PlatformUtil.isLinux()) {
            OS = "Linux";
        } else if (PlatformUtil.isUnix()) {
            OS = "Unix";
        }
    }

}
