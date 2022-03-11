package ga_complex_planning.pojo;

/**
 * @ClassName Point
 * @Description 受灾点信息
 * @Author faro_z
 * @Date 2022/3/5 2:56 下午
 * @Version 1.0
 **/
public class Point {
    // 受灾点名称
    private String name;
    // 受灾点坐标
    private double x;
    private double y;
    // 受灾点所需物资
    private double need;
    // 受灾点服务时间
    private double serviceTime;
    // 受灾点时间窗
    private double start;
    private double end;
    // 紧急程度
    private int emergency;

    public Point(String name, double x, double y, double need, double serviceTime, double start, double end, int emergency) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.need = need;
        this.serviceTime = serviceTime;
        this.start = start;
        this.end = end;
        this.emergency = emergency;
    }

    public Point() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getNeed() {
        return need;
    }

    public void setNeed(double need) {
        this.need = need;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    @Override
    public String toString() {
        return "Point{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", need=" + need +
                ", serviceTime=" + serviceTime +
                ", start=" + start +
                ", end=" + end +
                ", emergency=" + emergency +
                '}';
    }
}
