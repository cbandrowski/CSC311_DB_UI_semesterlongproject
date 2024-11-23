package service;
import java.util.prefs.Preferences;

public class UserSession {

    private static volatile UserSession instance;
    private String userName;
    private String privileges;
    private String imgNamePath;

    private UserSession(String userName, String privileges) {
        this.userName = userName;
        this.privileges = privileges;

        // Load the image path from Preferences (if available)
        Preferences userPreferences = Preferences.userRoot();
        this.imgNamePath = userPreferences.get("IMG_PATH", null);
    }

    public static UserSession getInstance(String userName, String privileges) {
        if (instance == null) {
            instance = new UserSession(userName, privileges);
        }
        return instance;
    }

    public static UserSession getCurrentSession() {
        Preferences userPreferences = Preferences.userRoot();
        String storedUsername = userPreferences.get("USERNAME", null);
        String storedPrivileges = userPreferences.get("PRIVILEGES", "NONE");

        if (storedUsername != null) {
            return new UserSession(storedUsername, storedPrivileges);
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public String getPrivileges() {
        return privileges;
    }

    public static void saveCredentials(String username, String password, String privileges) {
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.put("USERNAME", username);
        userPreferences.put("PASSWORD", password); // Save the password
        userPreferences.put("PRIVILEGES", privileges);
    }



    public String getImg() {
        return imgNamePath;
    }
    public void setImg(String imgName) {
        this.imgNamePath = imgName;

        // Persist the image path to Preferences
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.put("IMG_PATH", imgName);
    }




    public static void clearCredentials() {
        Preferences userPreferences = Preferences.userRoot();
        userPreferences.remove("USERNAME");
        userPreferences.remove("PRIVILEGES");
        userPreferences.remove("IMG_PATH");

    }

    public static boolean isUsernameCorrect(String username) {
        Preferences userPreferences = Preferences.userRoot();
        String storedUsername = userPreferences.get("USERNAME", "");
        return username.equals(storedUsername);
    }

    public static boolean isPasswordCorrect(String username, String password) {
        Preferences userPreferences = Preferences.userRoot();
        String storedUsername = userPreferences.get("USERNAME", "");
        String storedPassword = userPreferences.get("PASSWORD", "");
        return username.equals(storedUsername) && password.equals(storedPassword);
    }


    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", privileges='" + privileges + '\'' +
                ", imgNamePath='" + imgNamePath + '\'' +
                '}';
    }
}


