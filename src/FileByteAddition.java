import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FileByteAddition {
  public static void addReallyLargeNumbers(String num1File, String num2File, String sumFile)
      throws IOException {
    RandomAccessFile rand1 = new RandomAccessFile(new File(num1File), "r");
    RandomAccessFile rand2 = new RandomAccessFile(new File(num2File), "r");
    FileOutputStream outFile = new FileOutputStream(sumFile);
    long num1Len = rand1.length();
    long num2Len = rand2.length();
    long totalBytes = Math.max(num1Len, num2Len);
    int remainder = 0;
    for (long i = 0; i < totalBytes; i++) {
      int num1Digit = readDigit(rand1, num1Len - i - 1);
      int num2Digit = readDigit(rand2, num2Len - i - 1);
      int sum = num1Digit + num2Digit + remainder;
      int lastDigit = sum % 10;
      outFile.write(String.valueOf(lastDigit).charAt(0));
      remainder = sum / 10;
    }
    while (remainder != 0) {
      int lastDigit = remainder % 10;
      outFile.write(String.valueOf(lastDigit).charAt(0));
      remainder = remainder / 10;
    }
    rand1.close();
    rand2.close();
    outFile.close();
    reverseFile(sumFile);
  }

  private static void reverseFile(String file)
      throws IOException {
    File tmp = new File(file + "_tmp");
    FileOutputStream tmpFile = new FileOutputStream(tmp);
    RandomAccessFile rand = new RandomAccessFile(new File(file), "r");
    long len = rand.length();
    for (int i = 0; i < len; i++) {
      rand.seek(len - i - 1);
      tmpFile.write(rand.readByte());
    }
    rand.close();
    tmpFile.close();
    FileInputStream stream = new FileInputStream(tmp);
    int available = stream.available();
    FileOutputStream outStream = new FileOutputStream(file);
    for (int i = 0; i < available; i++) {
      outStream.write(stream.read());
    }
    stream.close();
    outStream.close();
    tmp.delete();
  }

  private static int readDigit(RandomAccessFile rand, long byteOffset)
      throws IOException {
    if (byteOffset < 0) {
      return 0;
    }
    rand.seek(byteOffset);
    return Integer.parseInt(String.valueOf((char) rand.readByte()));
  }

  public static void main(String[] args)
      throws IOException {
    addReallyLargeNumbers("./num1.bin", "./num2.bin", "./sum.bin");
  }
}
