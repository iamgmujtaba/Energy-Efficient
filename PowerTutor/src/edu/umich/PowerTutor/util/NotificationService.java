/*
Copyright (C) 2011 The University of Michigan

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Please send inquiries to powertutor@umich.edu
*/

package edu.umich.PowerTutor.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import edu.umich.PowerTutor.PowerNotifications;

@SuppressWarnings("unchecked")
public class NotificationService {
  private static final String TAG = "NotificationService";

  /* We haven't tried to install the hook yet. */
  private static final int STATE_INIT = 0;
  /* The hook was installed successfully and we should be receiving power
   * related notifications from the battery service.
   */
  private static final int STATE_HOOK_INSTALLED = 1;
  /* The hook failed to install.  This should be the case for most phones as a
   * hack is required to get this to work.
   */
  private static final int STATE_HOOK_FAILED = 2;

  private static int hookState = STATE_INIT;
  private static Binder notifier = new NotificationForwarder();
  private static Vector<PowerNotifications> hooks =
      new Vector<PowerNotifications>();

  private static Method methodGetService;
  
  static {
    try {
      Class classServiceManager = Class.forName("android.os.ServiceManager");
      methodGetService = classServiceManager.getMethod("getService", String.class);
    } catch(NoSuchMethodException e) {
      Log.w(TAG, "Could not find method gerService");
    } catch(ClassNotFoundException e) {
      Log.w(TAG, "Could not find class android.os.ServiceManager");
    }
  }
  
  private static IBinder getBatteryService() {
    if(methodGetService == null) return null;
    try {
      return (IBinder)methodGetService.invoke(null, "batteryhook");
    } catch(InvocationTargetException e) {
      Log.w(TAG, "Call to get service failed");
    } catch(IllegalAccessException e) {
      Log.w(TAG, "Call to get service failed");
    }
    return null;
  }
  
  public static boolean available() {
    synchronized(hooks) {
      if(hookState == STATE_INIT) {
        return getBatteryService() != null;
      }
      return hookState == STATE_HOOK_INSTALLED;
    }
  }

  public static void addHook(PowerNotifications notif) {
    synchronized(hooks) {
      if(hookState == STATE_INIT) {
        installHook();
      }
      if(hookState != STATE_HOOK_INSTALLED) {
        Log.w(TAG, "Attempted to add hook though no " +
                   "notification service available");
      } else {
        hooks.add(notif);
      }
    }
  }

  public static void removeHook(PowerNotifications notif) {
    synchronized(hooks) {
      hooks.remove(notif);
    }
  }

  private static void installHook() {
    Parcel outBinder = Parcel.obtain();
    outBinder.writeStrongBinder(notifier);
    hookState = STATE_HOOK_FAILED;
    try {
      IBinder batteryHook = getBatteryService();
      if(batteryHook == null) {
        /* This should be the case on un-hacked phone.  Maybe one day
         * phones will support this service or similar by default.
         */
        Log.i(TAG, "No power notification hook service installed");
      } else if(!batteryHook.transact(0, outBinder, null, 0)) {
        Log.w(TAG, "Failed to register forwarder");
      } else {
        hookState = STATE_HOOK_INSTALLED;
      }
    } catch(RemoteException e) {
      Log.w(TAG, "Failed to register forwarder");
    }
    outBinder.recycle();
  }

  /* Class responsible for forwarding power notifications to registered
   * hooks.
   */
  private static class NotificationForwarder extends DefaultReceiver {
    @Override
	public boolean onTransact(int code, Parcel data,
                              Parcel reply, int flags) throws RemoteException {
      synchronized(hooks) {
        for(Iterator<PowerNotifications> iter = hooks.iterator();
            iter.hasNext(); ) {
          Parcel junk = Parcel.obtain();
          try {
            iter.next().asBinder().transact(code, data, junk, flags);
          } catch(RemoteException e) {
            iter.remove();
          }
          data.setDataPosition(0);
          junk.recycle();
        }
      }
      return super.onTransact(code, data, reply, flags);
    }
  }

  /* If you only want to receive a subset of the notifications just extend this
   * class and override the methods you care about.
   */
  public static class DefaultReceiver extends PowerNotifications.Stub {
    @Override
	public void noteSystemMediaCall(int uid) {}
    @Override
	public void noteStartMedia(int uid, int id) {}
    @Override
	public void noteStopMedia(int uid, int id) {}
    @Override
	public void noteVideoSize(int uid, int id, int width, int height) {}
    @Override
	public void noteStartWakelock(int uid, String name, int type) {}
    @Override
	public void noteStopWakelock(int uid, String name, int type) {}
    @Override
	public void noteStartSensor(int uid, int sensor) {}
    @Override
	public void noteStopSensor(int uid, int sensor) {}
    @Override
	public void noteStartGps(int uid) {}
    @Override
	public void noteStopGps(int uid) {}
    @Override
	public void noteScreenOn() {}
    @Override
	public void noteScreenBrightness(int brightness) {}
    @Override
	public void noteScreenOff() {}
    @Override
	public void noteInputEvent() {}
    @Override
	public void noteUserActivity(int uid, int event) {}
    @Override
	public void notePhoneOn() {}
    @Override
	public void notePhoneOff() {}
    @Override
	public void notePhoneDataConnectionState(int dataType, boolean hasData) {}
    @Override
	public void noteWifiOn(int uid) {}
    @Override
	public void noteWifiOff(int uid) {}
    @Override
	public void noteWifiRunning() {}
    @Override
	public void noteWifiStopped() {}
    @Override
	public void noteBluetoothOn() {}
    @Override
	public void noteBluetoothOff() {}
    @Override
	public void noteFullWifiLockAcquired(int uid) {}
    @Override
	public void noteFullWifiLockReleased(int uid) {}
    @Override
	public void noteScanWifiLockAcquired(int uid) {}
    @Override
	public void noteScanWifiLockReleased(int uid) {}
    @Override
	public void noteWifiMulticastEnabled(int uid) {}
    @Override
	public void noteWifiMulticastDisabled(int uid) {}
    @Override
	public void setOnBattery(boolean onBattery, int level) {}
    @Override
	public void recordCurrentLevel(int level) {}
    @Override
	public void noteVideoOn(int uid) {}
    @Override
	public void noteVideoOff(int uid) {}
    @Override
	public void noteAudioOn(int uid) {}
    @Override
	public void noteAudioOff(int uid) {}
  }

  /* Useful for debugging purposes. */
  public static class PrintNotifications extends PowerNotifications.Stub {
    @Override
	public void noteSystemMediaCall(int uid) {
      System.out.println("System media call[uid=" + uid + "]");
    }

    @Override
	public void noteStartMedia(int uid, int id) {
      System.out.println("Start media[uid=" + uid + ", id=" + id + "]");
    }

    @Override
	public void noteStopMedia(int uid, int id) {
      System.out.println("Stop media[uid=" + uid + ", id=" + id + "]");
    }

    @Override
	public void noteVideoSize(int uid, int id, int width, int height) {
      System.out.println("Video size[uid=" + uid + ", id=" + id + 
                         ", width=" + width + ", height=" + height + "]");
    }

    @Override
	public void noteStartWakelock(int uid, String name, int type) {
      System.out.println("Start wakelock[uid=" + uid + ", name=" + name +
                         ", type=" + type + "]");
    }

    @Override
	public void noteStopWakelock(int uid, String name, int type) {
      System.out.println("Stop wakelock[uid=" + uid + ", name=" + name +
                         ", type=" + type + "]");
    }

    @Override
	public void noteStartSensor(int uid, int sensor) {
      System.out.println("noteStartSensor[uid=" + uid + ", sensor=" + sensor +
                         "]");
    }

    @Override
	public void noteStopSensor(int uid, int sensor) {
      System.out.println("noteStopSensor[uid=" + uid + ", sensor=" + sensor +
                         "]");
    }

    @Override
	public void noteStartGps(int uid) {
      System.out.println("noteStartGps[uid=" + uid + "]");
    }

    @Override
	public void noteStopGps(int uid) {
      System.out.println("noteStopGps[uid=" + uid + "]");
    }

    @Override
	public void noteScreenOn() {
      System.out.println("noteScreenOn");
    }

    @Override
	public void noteScreenBrightness(int brightness) {
      System.out.println("noteScreenBrightness[brightness=" + brightness + "]");
    }

    @Override
	public void noteScreenOff() {
      System.out.println("noteScreenOff");
    }

    @Override
	public void noteInputEvent() {
      System.out.println("noteInputEvent");
    }

    @Override
	public void noteUserActivity(int uid, int event) {
      System.out.println("noteUserActivity[uid=" + uid + ", event=" + event +
                         "]");
    }

    @Override
	public void notePhoneOn() {
      System.out.println("notePhoneOn");
    }

    @Override
	public void notePhoneOff() {
      System.out.println("notePhoneOff");
    }

    @Override
	public void notePhoneDataConnectionState(int dataType, boolean hasData) {
      System.out.println("notePhoneDataConnectionState[dataType=" + dataType +
                         ", hasData=" + hasData + "]");
    }

    public void notePhoneState(int phoneState) {
      System.out.println("notePhoneState[phoneState=" + phoneState + "]");
    }

    @Override
	public void noteWifiOn(int uid) {
      System.out.println("noteWifiOn[uid=" + uid + "]");
    }

    @Override
	public void noteWifiOff(int uid) {
      System.out.println("noteWifiOff[uid=" + uid + "]");
    }

    @Override
	public void noteWifiRunning() {
      System.out.println("noteWifiRunning");
    }

    @Override
	public void noteWifiStopped() {
      System.out.println("noteWifiStopped");
    }

    @Override
	public void noteBluetoothOn() {
      System.out.println("noteBluetoothOn");
    }

    @Override
	public void noteBluetoothOff() {
      System.out.println("noteBluetoothOff");
    }

    @Override
	public void noteFullWifiLockAcquired(int uid) {
      System.out.println("noteFullWifiLockAcquired[uid=" + uid + "]");
    }

    @Override
	public void noteFullWifiLockReleased(int uid) {
      System.out.println("noteFullWifiLockReleased[uid=" + uid + "]");
    }

    @Override
	public void noteScanWifiLockAcquired(int uid) {
      System.out.println("noteScanWifiLockAcquired[uid=" + uid + "]");
    }

    @Override
	public void noteScanWifiLockReleased(int uid) {
      System.out.println("noteScanWifiLockReleased[uid=" + uid + "]");
    }

    @Override
	public void noteWifiMulticastEnabled(int uid) {
      System.out.println("noteWifiMulticastEnabled[uid=" + uid + "]");
    }

    @Override
	public void noteWifiMulticastDisabled(int uid) {
      System.out.println("noteWifiMulticastDisabled[uid=" + uid + "]");
      
    }

    @Override
	public void setOnBattery(boolean onBattery, int level) {
      System.out.println("setOnBattery[onBattery=" + onBattery + ", level=" +
                         level + "]");
    }

    @Override
	public void recordCurrentLevel(int level) {
      System.out.println("recordCurrentLevel[level=" + level + "]");
    }

    @Override
	public void noteVideoOn(int uid) {
      System.out.println("noteVideoOn[uid=" + uid + "]");
    }

    @Override
	public void noteVideoOff(int uid) {
      System.out.println("noteVideoOff[uid=" + uid + "]");
    }

    @Override
	public void noteAudioOn(int uid) {
      System.out.println("noteAudioOn[uid=" + uid + "]");
    }

    @Override
	public void noteAudioOff(int uid) {
      System.out.println("noteAudioOff[uid=" + uid + "]");
    }
  }
}
