package vistas.shared;

public class SharedInfo {

	  public  static String username;

      public static void setUsername(String LoginName) {
    	  SharedInfo.username = LoginName;
      }

      public static String getUsername() {
          return username;
      }
}
