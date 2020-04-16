package experiment.phantom_type;

/**
 * AirPlaneApp.java
 *
 * @author liangchuan
 */
public class AirPlaneApp {
    public static void main(String[] args) {
        Plane<Landed> p = Plane.newPlane();

        Plane<Flying> fly = Plane.AirTrafficController.takeOff(p);
        Plane<Landed> land = Plane.AirTrafficController.land(fly);

        // 无法编译通过:
        ///Plane<Landed> reallyLanded =  Plane.AirTrafficController.land(land);
        //Plane<Flying> reallyFlying =  Plane.AirTrafficController.takeOff(fly);

    }
}
