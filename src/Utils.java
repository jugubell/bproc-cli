import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    public static String cleanLabel(String str) {
        return str.trim().replace(":", "");
    }

    public static String getLabelFromJmp(String str) {
        return str.trim().split(" ")[1].toUpperCase();
    }

    public static PathType checkPath(String pth) {
        try {
            Path path = Paths.get(pth);

            if(Files.isRegularFile(path)) {
                return PathType.FILE;
            }
            if(Files.isDirectory(path)) {
                return PathType.DIRECTORY;
            }
            return PathType.INVALID;

        } catch (InvalidPathException e) {
            return PathType.INVALID;
        }
    }

    public static List<String> hexFile2binFile(List<String> file) {
        List<String> binFile = new ArrayList<>();
        for (String l: file) {
            int binary_int = Integer.parseInt(l, 16);
            String binary = String.format("%16s", Integer.toBinaryString(binary_int)).replace(' ', '0');
            binFile.add(binary);
        }
        return binFile;
    }

    public static boolean supportsColor() {
        return !System.getProperty("os.name").toLowerCase().contains("win");
    }

}
