# Navi
An indoor navigation system built on bluetooth for android 🔵📶

## Getting Started
To open this project in android studio goto `File > New > Project From Version Control > Git`. Next Copy and Paste the url `https://github.coventry.ac.uk/eggintod/Navi.git` and click clone.

## Location System

I have defined some global variables to use throughout the program stricture.

``` java
private LocationSystem ls;
private final ArrayList<BLNode> scannedNodes = new ArrayList<>();
```

- ls - This is the location system class it will be initialized in onCreate function.
- scannedNodes - This is an array of `BLNode` objects. These are all the scanned nodes within range of the users device. To update and scan for new nodes call `scan()`, this takes 6 seconds and the scannedNodes array will be repopulated with `BLNode` objects.

### Get the users current location

The locationSystem class is already initialized (Currently only 2 rooms arr added). Just call `.getCurrentLocation()` on the `locationSystem` class to return a `Location` object.

```
Location currentLocation = this.ls.getCurrentLocation(this.scannedNodes);
```

## Notes

I am working on getting a new thread to continuously run the bluetooth scan in the background.