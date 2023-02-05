package filesystem.server.Ansewrs;

public record FileAndData(String fileName, byte[] data) {
    @Override
    public byte[] data() {
        return data;
    }

    @Override
    public String fileName() {
        return fileName;
    }
}
