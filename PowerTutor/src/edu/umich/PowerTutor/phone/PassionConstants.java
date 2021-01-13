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

package edu.umich.PowerTutor.phone;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PassionConstants extends DreamConstants {
  protected int screenWidth;
  protected int screenHeight;

  public PassionConstants(Context context) {
    super(context);
    DisplayMetrics metrics = new DisplayMetrics();
    WindowManager windowManager =
        (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    windowManager.getDefaultDisplay().getMetrics(metrics);
    screenWidth = metrics.widthPixels;
    screenHeight = metrics.heightPixels;
  }

  @Override
public String modelName() {
    return "passion";
  }

  @Override
public double maxPower() {
    return 2800;
  }

  @Override
public double lcdBrightness() {
	return 1.2217;  
}

  @Override
public double lcdBacklight() {
    throw new RuntimeException("lcdBacklight() for passion which has no LCD " +
                               "display");
  }

  @Override
public double oledBasePower() {
    return 365; // This incorporates all the constant component in the model.
  }

  private static final double[] arrayChannelPower = {
      3.0647e-006, 4.4799e-006, 6.4045e-006};
  @Override
public double[] oledChannelPower() {
    return arrayChannelPower;
  }

  @Override
public double oledModulation() {
    return 1.7582e-006;
  }

  private static final double[] arrayCpuPowerRatios = {1.1273, 1.5907, 1.8736,
      2.1745, 2.6031, 2.9612, 3.1373,3.4513, 3.9073 ,4.1959, 4.6492, 5.4818};
  @Override
public double[] cpuPowerRatios() {
    return arrayCpuPowerRatios;
  }

  private static final double[] arrayCpuFreqs = {245, 384, 460, 499, 576, 614,
      653, 691, 768, 806, 845, 998};
  @Override
public double[] cpuFreqs() {
    return arrayCpuFreqs;
  }

  @Override
public double audioPower() {
    return 106.81;
  }

  private static final double[] arrayGpsStatePower = {0.0, 17.5, 268.94};
  @Override
public double[] gpsStatePower() {
    return arrayGpsStatePower;
  }

  @Override
public double gpsSleepTime() {
    return 6.0;
  }

  @Override
public double wifiLowPower() {
    return 34.37;
  }

  @Override
public double wifiHighPower() {
    return 404.46 ;
  }

  @Override
public double wifiLowHighTransition() {
    return 15;
  }

  @Override
public double wifiHighLowTransition() {
    return 8;
  }

  private static final double[] arrayWifiLinkRatios = {
    47.122645, 46.354821, 43.667437, 43.283525, 40.980053, 39.44422, 38.676581,
    34.069637, 29.462693, 20.248805, 11.034917, 6.427122
  };
  @Override
public double[] wifiLinkRatios() {
    return arrayWifiLinkRatios;
  }

  private static final double[] arrayWifiLinkSpeeds = {
    1, 2, 5.5, 6, 9, 11, 12, 18, 24, 36, 48, 54
  };
  @Override
public double[] wifiLinkSpeeds() {
    return arrayWifiLinkSpeeds;
  }

  @Override
public String threegInterface() {
    return "rmnet0";
  }

  @Override
public double threegIdlePower(String oper) {
    if(OPER_TMOBILE.equals(oper)) {
      return 10;
    }
    return 10; // Return the worst case for unknown operators.
  }

  @Override
public double threegFachPower(String oper) {
    if(OPER_TMOBILE.equals(oper)) {
      return 405.81;
    }
    return 405.81; // Return the worst case for unknown operators.
  }

  @Override
public double threegDchPower(String oper) {
    if(OPER_TMOBILE.equals(oper)) {
      return 902.03;
    }
    return 902.03; // Return the worst case for unknown operators.
  }

  @Override
public double getMaxPower(String componentName) {
    if("OLED".equals(componentName)) {
      double[] channel = oledChannelPower();
      return oledBasePower() + 255 * screenWidth * screenHeight *
             (channel[0] + channel[1] + channel[2] - oledModulation());
    }
    return super.getMaxPower(componentName);
  }
}
