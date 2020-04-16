package experiment.proxy.cglib;

import net.sf.cglib.beans.BeanCopier;

/**
 * Created by LC on 2017/6/21.
 */
public class BeanCopierFactory {
    public static <T, U> BeanCopier getBeanCopier(Class<T> clazzT, Class<U> clazzU) {
        return BeanCopier.create(clazzT, clazzU, false);
    }
}
