package graphic;

import java.util.List;

public class LineGraphicData<T extends Number, U extends Number> {

    private List<T> x;
    private List<U> y;

    private String serialName;

    public LineGraphicData(List<T> x, List<U> y, String serialName) {
        this.x = x;
        this.y = y;
        this.serialName = serialName;
    }

    public List<T> getX() {
        return x;
    }

    public List<U> getY() {
        return y;
    }

    public String getSerialName() {
        return serialName;
    }
}
