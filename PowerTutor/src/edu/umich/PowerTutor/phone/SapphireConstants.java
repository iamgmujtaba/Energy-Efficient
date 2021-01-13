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

public class SapphireConstants extends DreamConstants {
  public SapphireConstants(Context context) {
    super(context);
  }

  @Override
public String modelName() {
    return "sapphire";
  }

  @Override
public double maxPower() {
    return 2800;
  }

  @Override
public double lcdBrightness() {
    return 1.72686;
  }

  @Override
public double lcdBacklight() {
    return 340.8305;
  }

  private static final double[] arrayCpuPowerRatios = {1.4169, 2.3997};
  @Override
public double[] cpuPowerRatios() {
    return arrayCpuPowerRatios;
  }

  private static final double[] arrayCpuFreqs = {245.36, 383.38};
  @Override
public double[] cpuFreqs() {
    return arrayCpuFreqs;
  }

  @Override
public double audioPower() {
    return 184.62;
  }

  private static final double[] arrayGpsStatePower = {0.0, 33.577, 284.7624};
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
    return 38.554;
  }

  @Override
public double wifiHighPower() {
    return 733.7631;
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
    return 10;
  }

  @Override
public double threegFachPower(String oper) {
    if(OPER_TMOBILE.equals(oper)) {
      return 413.9689;
    }
    return 413.9689;
  }

  @Override
public double threegDchPower(String oper) {
    if(OPER_TMOBILE.equals(oper)) {
      return 944.3891;
    }
    return 944.3891;
  }
}
