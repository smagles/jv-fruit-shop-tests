package core.basesyntax.service.report;

import java.util.Map;

public interface ReportGenerator {
    String getReport(Map<String, Integer> fruits);
}
