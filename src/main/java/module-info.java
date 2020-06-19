module lib.usb3.ftdi {
    requires java.logging;
    requires lib.javax.usb3;

    exports com.ftdichip.usb;
    exports com.ftdichip.usb.enumerated;
}