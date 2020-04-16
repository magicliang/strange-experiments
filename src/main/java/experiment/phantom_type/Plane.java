package experiment.phantom_type;

/**
 * Plane.java
 * 这个类型可以被用类型参数具体化为任何 FlightStatus 的 飞机。即 Plane<Landed> 与 Plane<Flying>。
 *
 * @author liangchuan
 */
public class Plane<Status extends FlightStatus> {

    private int passenger;

    public int getPassenger() {
        return passenger;
    }

    // 禁掉了除工厂方法和指定的状态构造方法以外的所有其他构造方法。当然，防不了反射攻击（reflection attack）。
    private Plane(int passenger) {
        this.passenger = passenger;
    }

    /**
     * 工厂方法
     *
     * @return
     */
    public static Plane<Landed> newPlane() {
        return new Plane<Landed>(10);
    }

    /**
     * 状态构造方法
     * 在这里每次飞机从一个状态转成另一个飞机状态，都产生了一个新的对象，类似 Value Object 的模式。
     *
     * @param p
     */
    private Plane(Plane<? extends FlightStatus> p) {
        // 在这里，我们可以使用装饰器模式。也可以使用 clone 模式，把乘客（也就是内部状态）移交过去。这取决于我们要不要把旧飞机实例的状态迁移到新飞机实例上。
        this.passenger = p.getPassenger();
        // 做任何想要做的事情
    }

    public static class AirTrafficController {

        public static Plane<Landed> land(Plane<Flying> p) {
            return new Plane<Landed>(p);
        }

        public static Plane<Flying> takeOff(Plane<Landed> p) {
            return new Plane<Flying>(p);
        }
    }

}
