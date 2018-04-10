package no.fjordkraft.im.util;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 2/28/18
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class SetInvoiceASOnline {



    public static final ThreadLocal threadLocal = new ThreadLocal();

    public static void set(Boolean skipOnline) {
        threadLocal.set(skipOnline);
    }

    public static void unset() {
        threadLocal.remove();
    }
    public static Boolean get() {
        if(threadLocal.get()!=null) {
             return new Boolean(threadLocal.get().toString());
        }
        else {
            return null;
        }
    }
}
