/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

/**
 *
 * @author LENOVO
 */
public class UserSessionCache {
    private static String username;
    private static String userId;

    // Menyimpan informasi login pengguna
    public static void login(String username, String userId) {
        UserSessionCache.username = username;
        UserSessionCache.userId = userId;
    }

    // Mengambil informasi username dari sesi
    public static String getUsername() {
        return username;
    }

    // Mengambil informasi userId dari sesi
    public static String getUserId() {
        return userId;
    }
}

