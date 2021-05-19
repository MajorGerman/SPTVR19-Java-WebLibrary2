package tools;

public interface EncryptPassword {
    public String createSalt();
    public String createHash(String password, String salt);
}
