package hackerman.notebookmushroom.interactions;



public interface DrawerChangeStateInteraction {
    void openDrawer();

    void closeDrawer();

    boolean isDrawerOpen();

    void invertDrawerState();
}
