public class Utils {
    public static boolean isValidHexNumber(String str) {
        return !str.isEmpty() && str.toUpperCase().matches("^[0-9A-F]*$");
    }

    public static boolean isIndirect(String str) {
        if(str.startsWith("[") && str.endsWith("]")) {
            return is12bitsHexData(str.toUpperCase());
        } else {
            return false;
        }
    }


    public static boolean is16bitsHexData(String hex) {
        if(hex.startsWith("0X") ^ hex.endsWith("H")) {
            String hexPure = hex.replace("0X", "").replace("H", "");
            return hexPure.length() == 4 && isValidHexNumber(hexPure);
        } else {
            return false;
        }
    }

    public static boolean is12bitsHexData(String hex) {
        if(hex.startsWith("0X") ^ hex.endsWith("H")) {
            String hexPure = hex.replace("0X", "").replace("H", "");
            return hexPure.length() == 3 && isValidHexNumber(hexPure);
        } else {
            return false;
        }
    }

    public static String getDataAddress(String data) {
        return trimAddress(data.split(" ")[1]);
    }

    public static String trimAddress(String address) {
        return address.trim().toUpperCase().replace("0X", " ").replace("H", "").replace("[", "").replace("]", "");
    }

    public static int getDataAddressInt(String data) {
        return Integer.decode("0x" + trimAddress(data.split(" ")[1]));
    }

    public static String getDataData(String data) {
        return trimAddress(data.split(" ")[2]);
    }

}
