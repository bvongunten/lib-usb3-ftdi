# Future Technology Devices International

USB Port Permissions (Linux)

# Check and fix usb / udev permissions

For Debian-flavor systems

Install udev rules to /etc/udev/rules.d

Copy the file 99-libftdi.rules to /etc/udev/rules.d

## Possibly remove competing software

brltty and modemmanager aggressively claim any attached UART. This blocks access to those ports by any other application. Check permissions in the /dev/bus/usb files and delete these two applications:

    % chown root:users /dev/bus/usb/003/ -R
    % sudo apt-get purge brltty
    % sudo apt-get purge modemmanager

ftdi_sio is required if you want logging from FTDI chipsets (which are the only ones that work with non-standard baud rates in linux). If you do not need logging support you can just remove ftdi_sio from your kernel config or disable ftdi_sio:

    % vi /etc/modprobe.d/blacklist.conf
    blacklist ftdi_sio

## LSUSB output:

    Bus 007 Device 013: ID 0403:6001 Future Technology Devices International, Ltd FT232 USB-Serial (UART) IC

## DMSG output:

    Apr 25 18:18:17 kernel: [ 8874.205830] usb 7-2: new full-speed USB device number 15 using uhci_hcd
    Apr 25 18:18:18 mtp-probe: checking bus 7, device 15: "/sys/devices/pci0000:00/0000:00:1d.1/usb7/7-2"
    Apr 25 18:18:18 kernel: [ 8874.407968] ftdi_sio 7-2:1.0: FTDI USB Serial Device converter detected
    Apr 25 18:18:18 kernel: [ 8874.408004] usb 7-2: Detected FT232RL
    Apr 25 18:18:18 kernel: [ 8874.408007] usb 7-2: Number of endpoints 2
    Apr 25 18:18:18 kernel: [ 8874.408009] usb 7-2: Endpoint 1 MaxPacketSize 64
    Apr 25 18:18:18 kernel: [ 8874.408012] usb 7-2: Endpoint 2 MaxPacketSize 64
    Apr 25 18:18:18 kernel: [ 8874.408014] usb 7-2: Setting MaxPacketSize 64
    Apr 25 18:18:18 mtp-probe: bus: 7, device: 15 was not an MTP device
    Apr 25 18:18:18 kernel: [ 8874.410074] usb 7-2: FTDI USB Serial Device converter now attached to ttyUSB0

## UDEVADM output:

    % udevadm info -a -p /sys/devices/pci0000:00/0000:00:1d.1/usb7/7-2

      looking at device '/devices/pci0000:00/0000:00:1d.1/usb7/7-2':
        KERNEL=="7-2"
        SUBSYSTEM=="usb"
        DRIVER=="usb"
        ATTR{configuration}==""
        ATTR{bNumInterfaces}==" 1"
        ATTR{bConfigurationValue}=="1"
        ATTR{bmAttributes}=="a0"
        ATTR{bMaxPower}==" 90mA"
        ATTR{urbnum}=="16"
        ATTR{idVendor}=="0403"
        ATTR{idProduct}=="6001"
        ATTR{bcdDevice}=="0600"
        ATTR{bDeviceClass}=="00"
        ATTR{bDeviceSubClass}=="00"
        ATTR{bDeviceProtocol}=="00"
        ATTR{bNumConfigurations}=="1"
        ATTR{bMaxPacketSize0}=="8"
        ATTR{speed}=="12"
        ATTR{busnum}=="7"
        ATTR{devnum}=="13"
        ATTR{devpath}=="2"
        ATTR{version}==" 2.00"
        ATTR{maxchild}=="0"
        ATTR{quirks}=="0x0"
        ATTR{avoid_reset_quirk}=="0"
        ATTR{authorized}=="1"
        ATTR{manufacturer}=="FTDI"
        ATTR{product}=="FT232R USB UART"
        ATTR{serial}=="A80080VO"

# FAQ
Q: Why do I get "device or resource busy" errors when connecting to the USB-UART device / why do I see strange input in my serial terminal when running it as root?

> This happens with some NetworkManager versions. They ship with a daemon called "modem-manager" enabled by default. This daemon will take control over any UART devices it finds, including the one for the USB->UART connection. If you run your serial terminal as root, and see messages like "AT" commands being printed on the terminal, then this is a sure sign of modem-manager activity. To be able to use the serial console, first shut down this daemon or completely uninstall it. How exactly depends on the distribution. 
