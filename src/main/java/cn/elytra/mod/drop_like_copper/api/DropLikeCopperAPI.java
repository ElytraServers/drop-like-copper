package cn.elytra.mod.drop_like_copper.api;

public class DropLikeCopperAPI {

    private static boolean libraryMode = false;

    /**
     * Make DropLikeCopper as a library and disable all builtin functionalities.
     */
    public static void setLibraryMode() {
        libraryMode = true;
    }

    public static boolean isLibraryMode() {
        return libraryMode;
    }

}
