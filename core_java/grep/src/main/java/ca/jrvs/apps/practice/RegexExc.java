package ca.jrvs.apps.practice;

public interface RegexExc {
    /**
     * Return true if file name extension is jpg or jpeg (case-insensitive)
     * 
     * @param filename
     * @return
     */
    public boolean matchJpeg(String filename);

    /**
     * Return true if ip is valid range (0.0.0.0 -> 999.999.999.999)
     * 
     * @param filename
     * @return
     */
    public boolean matchIp(String ip);

    /**
     * Return true if the line is empty
     * 
     * @param line
     * @return
     */
    public boolean isEmptyLine(String line);
}