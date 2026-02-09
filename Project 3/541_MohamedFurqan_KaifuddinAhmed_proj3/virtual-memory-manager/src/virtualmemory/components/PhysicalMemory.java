package virtualmemory.components;

public class PhysicalMemory {
    private final byte[] memory;
    private final int totalFrames;
    private int nextFrame;

    public PhysicalMemory(int totalFrames) {
        this.totalFrames = totalFrames;
        this.memory = new byte[totalFrames * 256]; // Assuming 256 bytes per frame
        this.nextFrame = 0;
    }

    public int allocateFrame() {
        if (nextFrame < totalFrames) {
            return nextFrame++;
        }
        return -1; // No available frames
    }

    public void writeFrame(int frameNumber, byte[] data) {
        System.arraycopy(data, 0, memory, frameNumber * 256, 256); // Copy data into memory
    }

    public byte readByte(int frameNumber, int offset) {
        return memory[frameNumber * 256 + offset];
    }
}
