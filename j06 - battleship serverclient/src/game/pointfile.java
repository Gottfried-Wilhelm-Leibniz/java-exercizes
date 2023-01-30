package game;

public class XPoint  {
    private String status = "0";

    public XPoint(int x, int y, String s) {
        super(x, y);
        this.status = s;
    }
    @Override
    public String toString() {
        return status;
    }

    public void setStatus(String newStatus) {
        status = newStatus;
    }

}

