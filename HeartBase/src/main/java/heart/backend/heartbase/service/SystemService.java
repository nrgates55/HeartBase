package heart.backend.heartbase.service;

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
            Integer fanRpm = null;

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // CPU cores
                if (line.startsWith("Core")) {
                    double temp = extractNumberBeforeUnit(line, "°C");
                    coreTemps.add(temp);
                    if (temp > maxTemp) {
                        maxTemp = temp;
                    }
                }

                // Power draw
                if (line.startsWith("power1:")) {
                    powerDraw = extractNumberBeforeUnit(line, "W");
                }

                // Fan RPM
                if (line.startsWith("fan")) {
                    fanRpm = (int) extractNumberBeforeUnit(line, "RPM");
                }
            }

            double avgTemp = coreTemps.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            result.put("cpuAverageTemp", avgTemp);
            result.put("cpuMaxTemp", coreTemps.isEmpty() ? null : maxTemp);
            result.put("powerDrawWatts", powerDraw);
            result.put("fanRpm", fanRpm);

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

    private double extractNumberBeforeUnit(String line, String unit) {
        try {
            int colonIndex = line.indexOf(":");
            int unitIndex = line.indexOf(unit);

            if (colonIndex != -1 && unitIndex != -1) {
                String value = line.substring(colonIndex + 1, unitIndex).trim();
                value = value.replace("+", "").trim();
                return Double.parseDouble(value);
            }
        } catch (Exception ignored) {
        }

        return 0.0;
    }
}