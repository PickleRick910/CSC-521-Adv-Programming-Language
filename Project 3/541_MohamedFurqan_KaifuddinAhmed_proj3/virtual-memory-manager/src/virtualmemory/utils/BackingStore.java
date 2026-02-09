package virtualmemory.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BackingStore {
    private static final String STORE_FILE = "input/BACKING_STORE.bin";
    private static final int PAGE_SIZE = 256;
    private RandomAccessFile disk;

    public BackingStore() {
        try {
            disk = new RandomAccessFile(new File(STORE_FILE), "r");
        } catch (IOException e) {
            System.err.println("Error opening the backing store: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Reads a specific page from the backing store.
     * @param pageNumber the page number to load
     * @return a byte array containing the page data
     */
    public byte[] readPage(int pageNumber) {
        byte[] pageData = new byte[PAGE_SIZE];
        try {
            disk.seek(pageNumber * PAGE_SIZE);
            disk.readFully(pageData);
        } catch (IOException e) {
            System.err.println("Error reading from backing store: " + e.getMessage());
        }
        return pageData;
    }

    /**
     * Closes the RandomAccessFile to release resources.
     */
    public void close() {
        try {
            if (disk != null) {
                disk.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing the backing store: " + e.getMessage());
        }
    }
}
