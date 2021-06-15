package tools;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class HashPassword implements EncryptPassword{

    @Override
    public String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new BigInteger(salt).toString(16);
    }

    @Override
    public String createHash(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(),salt.getBytes(),65536,128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return new BigInteger(hash).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashPassword.class.getName()).log(Level.SEVERE, "Не найден алгоритм", ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(HashPassword.class.getName()).log(Level.SEVERE, "Неправильный spec", ex);
        }
        return null;
    }
    
    public String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }
    
    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
}
  
