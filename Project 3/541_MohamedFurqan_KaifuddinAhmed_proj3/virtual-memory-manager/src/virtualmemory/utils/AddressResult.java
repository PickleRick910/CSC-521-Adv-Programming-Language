package virtualmemory.utils;

public class AddressResult {
    private final int logicalAddress;
    private final int physicalAddress;
    private final byte value;

    public AddressResult(int logicalAddress, int physicalAddress, byte value) {
        this.logicalAddress = logicalAddress;
        this.physicalAddress = physicalAddress;
        this.value = value;
    }

    public int getLogicalAddress() {
        return logicalAddress;
    }

    public int getPhysicalAddress() {
        return physicalAddress;
    }

    public byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AddressResult{" +
                "logicalAddress=" + logicalAddress +
                ", physicalAddress=" + physicalAddress +
                ", value=" + value +
                '}';
    }
}
