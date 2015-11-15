package com.tuanna.main;

public interface Constants {

    int WINDOW_WIDTH = 800;
    int WINDOW_HEIGHT = 600;
    int MAX_FPS = 60;

    int STATE_PLAY = 0;
    int STATE_MENU = 1;

    String CREATE_TEXT = "CREATE GAME";
    String JOIN_TEXT = "JOIN GAME";
    String EXIT_TEXT = "EXIT";

    int DEFAULT_PORT = 6669;

    int sw = 2;

    int LOCAL_PORT = sw == 1 ? 6232 : 6231;
    int DESTINATION_PORT = sw == 1 ? 6231 : 6232;
}
