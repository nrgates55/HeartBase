package com.heartbase.heartbase.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
@Service
public class SystemService {

    public Map<String, Object> getSystemStats() {
        Map<String, Object> result = new HashMap<>();

        try {
            Process process = Runtime.getRuntime().exec("sensors");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            List<Double> coreTemps = new ArrayList<>();
            double maxTemp = Double.MIN_VALUE;
            Double powerDraw = null;

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                // CPU cores
                if (line.startsWith("Core")) {
                    double temp = extractTemp(line);
                    coreTemps.add(temp);
                    if (temp > maxTemp) {
                        maxTemp = temp;
                    }
                }

                // Power draw
                if (line.startsWith("power1:")) {
                    powerDraw = extractTemp(line);
                }
            }

            // Average CPU temp
            double avgTemp = coreTemps.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            result.put("cpuAverageTemp", avgTemp);
            result.put("cpuMaxTemp", maxTemp);
            result.put("powerDrawWatts", powerDraw);

            //Battery %
            Process batteryProcess = Runtime.getRuntime().exec(
                    "cat /sys/class/power_supply/BAT0/capacity"
            );

            BufferedReader batteryReader = new BufferedReader(
                    new InputStreamReader(batteryProcess.getInputStream())
            );

            String batteryLine = batteryReader.readLine();
            result.put("batteryPercent", Integer.parseInt(batteryLine));

        } catch (Exception e) {
            result.put("error", e.getMessage());
        }

        return result;
    }

    private double extractTemp(String line) {
        try {
            int plusIndex = line.indexOf("+");
            int cIndex = line.indexOf("°C");

            if (plusIndex != -1 && cIndex != -1) {
                String tempStr = line.substring(plusIndex + 1, cIndex);
                return Double.parseDouble(tempStr);
            }
        } catch (Exception ignored) {}

        return 0.0;
    }
}