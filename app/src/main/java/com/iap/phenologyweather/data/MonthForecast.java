package com.iap.phenologyweather.data;

import android.content.Context;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.model.HistoryMonth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chenbocong on 2016/3/3.
 */
public class MonthForecast {
    private String monthName;
    private int highTemp, lowTemp;
    private float precip, rainDay, midTemp;
    private int right, tempTop, left, tempBottom, ranfallTop, ranfallBottom, id, rainDayY;

    public static List<MonthForecast> parseMonthForecastData(Context context, HistoryMonth historyMonth) {
        List<MonthForecast> list = new ArrayList<>();
        if (historyMonth == null || historyMonth.getTmax_vmean_month() == null || historyMonth.getTmax_vmean_month().isEmpty()) {
            return null;
        }
        List<String> tempMaxs = historyMonth.getTmax_vmean_month();
        List<String> tempMins = historyMonth.getTmin_vmean_month();
        List<String> rainDays = historyMonth.getPrcp_days_in_month();
        List<String> precips = historyMonth.getPrcp_in_month();
        String[] monthArray = context.getResources().getStringArray(R.array.months_ab);
        for (int i = 0; i < tempMaxs.size()
                && i < tempMins.size()
                && i < rainDays.size()
                && i < precips.size(); i++) {
            MonthForecast f = new MonthForecast();
            //需要处理异常
            f.setHighTemp(Float.valueOf(tempMaxs.get(i)));
            f.setLowTemp(Float.valueOf(tempMins.get(i)));
            f.setMidTemp((f.getHighTemp() + f.getLowTemp()) / 2.0f);
            f.setRainDay(Float.valueOf(rainDays.get(i)));
            f.setPrecip(Float.valueOf(precips.get(i)));

            if (i < 12) {
                f.setMonthName(monthArray[i]);
            }
            f.setId(i);
            list.add(f);
        }

        return list;
    }

    public int getRainDayY() {
        return rainDayY;
    }

    public void setRainDayY(int rainDayY) {
        this.rainDayY = rainDayY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLowTempString(Context context) {
        return lowTemp + "°";
    }

    public String getHighTempString(Context context) {
        return highTemp + "°";
    }

    public float getMidTemp() {
        return midTemp;
    }

    public void setMidTemp(float midTemp) {
        this.midTemp = midTemp;
    }

    public String getRainString() {
        return String.valueOf(Math.round(rainDay));
    }

    public String getRainfallString() {
        return String.valueOf(Math.round(precip));
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(float highTemp) {
        this.highTemp = (int) highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(float lowTemp) {
        this.lowTemp = (int) lowTemp;
    }

    public int getRainDay() {
        return Math.round(rainDay);
    }

    public void setRainDay(float rainDay) {
        this.rainDay = rainDay;
    }

    public float getPrecip() {
        return precip;
    }

    public void setPrecip(float precip) {
        this.precip = precip;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTempTop() {
        return tempTop;
    }

    public void setTempTop(int tempTop) {
        this.tempTop = tempTop;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTempBottom() {
        return tempBottom;
    }

    public void setTempBottom(int tempBottom) {
        this.tempBottom = tempBottom;
    }

    public int getRanfallTop() {
        return ranfallTop;
    }

    public void setRanfallTop(int ranfallTop) {
        this.ranfallTop = ranfallTop;
    }

    public int getRanfallBottom() {
        return ranfallBottom;
    }

    public void setRanfallBottom(int ranfallBottom) {
        this.ranfallBottom = ranfallBottom;
    }


    public static String getIntroductionText(List<MonthForecast> monthForecastList, double lat, Context context) {
        int size = monthForecastList.size();
        if (size != 12) {
            return null;
        }
        String introductionText = null;
        String textList[] = new String[10];
        String tempDiffBigList[] = new String[4];
        int textListId = 0;
        int summer[], spring[], autumn[];
        int highTempList[][] = new int[2][12], lowTempList[][] = new int[2][12], rainDayList[] = new int[12];
        float midTempList[] = new float[12];
        boolean isRainYear = false;
        int maxRainDayId = -1, maxRainDay = -999, minLowTemp = 999, maxHighTemp = -999;
        int springTempDiff = 0, summerTempDiff = 0, autumnTempDiff = 0, winterTempDiff = 0, yearTempDiff;
        int tempDiffBigNum = 0;
        if (lat > 0) {
            spring = new int[]{2, 3, 4};
            summer = new int[]{5, 6, 7};
            autumn = new int[]{8, 9, 10};
        } else {
            spring = new int[]{8, 9, 10};
            summer = new int[]{0, 1, 11};
            autumn = new int[]{2, 3, 4};
        }
        for (int i = 0; i < size; i++) {
            MonthForecast mf = monthForecastList.get(i);
            float midTemp = mf.getMidTemp();
            int rainDay = (int) mf.getRainDay();
            int highTemp = mf.getHighTemp();
            int lowTemp = mf.getLowTemp();
            if (Arrays.binarySearch(spring, i) >= 0) {
                springTempDiff += (highTemp - lowTemp);
            } else if (Arrays.binarySearch(summer, i) >= 0) {
                summerTempDiff += (highTemp - lowTemp);
            } else if (Arrays.binarySearch(autumn, i) >= 0) {
                autumnTempDiff += (highTemp - lowTemp);
            } else {
                winterTempDiff += (highTemp - lowTemp);
            }
            if (rainDay > maxRainDay) {
                maxRainDay = (int) mf.getRainDay();
                maxRainDayId = i;
            }
            if (highTemp > maxHighTemp) {
                maxHighTemp = highTemp;
            }
            if (lowTemp < minLowTemp) {
                minLowTemp = lowTemp;
            }
            midTempList[i] = midTemp;
            highTempList[0][i] = highTemp;
            lowTempList[0][i] = lowTemp;
            highTempList[1][i] = rainDay;
            lowTempList[1][i] = rainDay;
            rainDayList[i] = rainDay;
        }
        yearTempDiff = (springTempDiff + summerTempDiff + autumnTempDiff + winterTempDiff) / size;
        springTempDiff = springTempDiff / 3;
        summerTempDiff = summerTempDiff / 3;
        autumnTempDiff = autumnTempDiff / 3;
        winterTempDiff = winterTempDiff / 3;
        Arrays.sort(midTempList);
        Arrays.sort(rainDayList);
        int change;
        for (int i = 0; i < size - 1; i++) {
            for (int j = size - 1; j > i; j--) {
                if (highTempList[0][j] < highTempList[0][j - 1]) {
                    change = highTempList[0][j];
                    highTempList[0][j] = highTempList[0][j - 1];
                    highTempList[0][j - 1] = change;
                    change = highTempList[1][j];
                    highTempList[1][j] = highTempList[1][j - 1];
                    highTempList[1][j - 1] = change;
                }
                if (lowTempList[0][j] < lowTempList[0][j - 1]) {
                    change = lowTempList[0][j];
                    lowTempList[0][j] = lowTempList[0][j - 1];
                    lowTempList[0][j - 1] = change;
                    change = lowTempList[1][j];
                    lowTempList[1][j] = lowTempList[1][j - 1];
                    lowTempList[1][j - 1] = change;
                }
            }
        }
        if (midTempList[size - 1] - midTempList[0] > 20) {
            textList[textListId] = context.getResources().getString(R.string.four_seasons_diff_big);
            textListId++;
        } else if (midTempList[size - 1] - midTempList[0] < 10) {
            textList[textListId] = context.getResources().getString(R.string.four_seasons_diff_small);
            textListId++;
        }
        if (maxHighTemp <= 26 && minLowTemp >= 0) {
            textList[textListId] = context.getResources().getString(R.string.winter_warm_summer_cool);
            textListId++;
        }
        if (highTempList[0][size - 1] > 32) {
            textList[textListId] = context.getResources().getString(R.string.summer_very_hot);
            textListId++;
        } else if (highTempList[0][size - 1] > 30) {
            textList[textListId] = context.getResources().getString(R.string.summer_hot);
            textListId++;
        }
        if (lowTempList[0][0] < -10) {
            textList[textListId] = context.getResources().getString(R.string.winter_very_cold);
            textListId++;
        } else if (lowTempList[0][0] < 0) {
            textList[textListId] = context.getResources().getString(R.string.winter_cold);
            textListId++;
        }
        if (springTempDiff >= 10) {
            tempDiffBigList[tempDiffBigNum] = context.getResources().getString(R.string.spring);
            tempDiffBigNum++;
        }
        if (summerTempDiff >= 10) {
            tempDiffBigList[tempDiffBigNum] = context.getResources().getString(R.string.summer);
            tempDiffBigNum++;
        }
        if (autumnTempDiff >= 10) {
            tempDiffBigList[tempDiffBigNum] = context.getResources().getString(R.string.autumn);
            tempDiffBigNum++;
        }
        if (winterTempDiff >= 10) {
            tempDiffBigList[tempDiffBigNum] = context.getResources().getString(R.string.winter);
            tempDiffBigNum++;
        }
        if (tempDiffBigNum >= 3) {
            textList[textListId] = context.getResources().getString(R.string.temp_diff_big_0);
            textListId++;
        } else if (tempDiffBigNum == 2) {
            textList[textListId] = String.format(context.getResources().getString(R.string.temp_diff_big_2), tempDiffBigList[0], tempDiffBigList[1].toLowerCase());
            textListId++;
        } else if (tempDiffBigNum == 1) {
            textList[textListId] = String.format(context.getResources().getString(R.string.temp_diff_big_1), tempDiffBigList[0]);
            textListId++;
        } else if (yearTempDiff <= 5) {
            textList[textListId] = context.getResources().getString(R.string.temp_diff_small);
            textListId++;
        }
        if (rainDayList[3] >= 10) {
            textList[textListId] = context.getResources().getString(R.string.rainy_year);
            textListId++;
            isRainYear = true;
        } else if (rainDayList[6] >= 10) {
            int summerNum = 0;
            int winterNum = 0;
            for (int i = 11; i >= 6; i--) {
                if (highTempList[1][i] >= 10) {
                    summerNum++;
                }
                if (lowTempList[1][11 - i] >= 10) {
                    winterNum++;
                }
            }
            if (summerNum >= 5) {
                textList[textListId] = context.getResources().getString(R.string.rainy_summer_half_year);
                textListId++;
            } else if (winterNum >= 5) {
                textList[textListId] = context.getResources().getString(R.string.rainy_winter_half_year);
                textListId++;
            }
        } else if (rainDayList[10] >= 10) {
            int summerNum = 0;
            int winterNum = 0;
            for (int i = 11; i >= 8; i--) {
                if (highTempList[1][i] >= 10) {
                    summerNum++;
                }
                if (lowTempList[1][11 - i] >= 10) {
                    winterNum++;
                }
            }
            if (summerNum >= 3) {
                textList[textListId] = context.getResources().getString(R.string.rainy_summer);
                textListId++;
            } else if (winterNum >= 3) {
                textList[textListId] = context.getResources().getString(R.string.rainy_winter);
                textListId++;
            }
        } else if (rainDayList[8] <= 1 && rainDayList[size - 1] <= 3) {
            textList[textListId] = tempDiffBigList[0] + context.getResources().getString(R.string.scarce_precipitation_year);
            textListId++;
        }
        if (!isRainYear && rainDayList[size - 1] >= 13) {
            textList[textListId] = String.format(context.getResources().getString(R.string.rainy_most_abundant), context.getResources().getStringArray(R.array.months_ab)[maxRainDayId]);
        }
        if (textList.length != 0 && textList != null && textList[0] != null) {
            introductionText = context.getResources().getString(R.string.the_area).replace((char) 160, (char) 32);
            for (int i = 0; true; i++) {
                if (null == textList[i]) {
                        return introductionText.substring(0, introductionText.length() - 1) + context.getResources().getString(R.string.period);
                } else {
                    if (null != textList[i + 1] && textList[i].startsWith("It is") && textList[i + 1].startsWith("It is")) {
                        textList[i] = textList[i].replace(context.getResources().getString(R.string.period), context.getResources().getString(R.string.comma));
                        textList[i + 1] = textList[i + 1].replace("It is", "");
                    }
                    if (i == 0 && textList[i].startsWith("It ")) {
                        introductionText = introductionText + textList[i].substring(3).replace((char) 160, (char) 32);
                    } else {
                        introductionText = introductionText + textList[i].replace((char) 160, (char) 32);
                    }
                }
            }
        } else {
            return null;
        }
    }
}