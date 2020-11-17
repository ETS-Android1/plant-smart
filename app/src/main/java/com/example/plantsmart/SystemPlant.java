package com.example.plantsmart;


public class SystemPlant {
    boolean Online = false;
    String systemName = "";
    double OutPut = 0, IdealValue = 0;
    int progress = 0;

    public String getIdealValue() {
        return round(IdealValue, 2) + getUnit();
    }

    public String getOutPut() {
        return round(OutPut, 2) + getUnit();
    }

    public String getUnit() {
        if (isFound("LDR", systemName)) {
            return " LUX";
        } else if (isFound("Temperature", systemName)) {
            return " °C";
        } else if (isFound("Humidity", systemName) || isFound("Moisture", systemName)) {
            return " %";
        } else if (isFound("Ultra", systemName)) {
            return " cm";
        }
        return "";
    }

    public int getProgress() {
        return progress;
    }

    public boolean isOnline() {
        return Online;
    }

    public void setProgress(int progress) {
        if (progress >= 200) {
            this.progress = 200;
            return;
        }
        if (progress <= 0) {
            this.progress = 0;
            return;
        }
        this.progress = progress;
    }

    public String getSystemName() {
        return systemName;
    }


    public void setVal(Object Firebase, Object IdealVal) {
//        if (OutPut > 0 && getFloat(Firebase) != OutPut) {
            Online = true;
//        }
        if (isFound("Moisture", systemName)) {
            IdealValue = getFloat(IdealVal);
            OutPut = 100f -  (((getFloat(Firebase) - 350f)) / (750f - 350f))*100f;
        } else {
            IdealValue = getFloat(IdealVal);
            OutPut = getFloat(Firebase);
        }
        setProgress((int) ((OutPut / IdealValue) * 100));
    }

    public float getFloat(Object s) {
        try {
            return (float) Double.parseDouble(s.toString());
        } catch (Exception ignored) {
            return 0;
        }
    }

    public SystemPlant(String systemName, int progress) {
        this.systemName = systemName;
        setProgress(progress);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public boolean isFound(String p, String hph) {
        return hph.indexOf(p) != -1 ? true : false;
    }

}
