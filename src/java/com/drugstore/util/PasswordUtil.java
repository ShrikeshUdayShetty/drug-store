package com.drugstore.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hashPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(plainPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to hash password", e);
        }
    }

    public static boolean matches(String plainPassword, String existingHash) {
        if (plainPassword == null || existingHash == null) {
            return false;
        }
        return hashPassword(plainPassword).equals(existingHash);
    }
}
