package gr.uoi.cs.pythia.report;

import gr.uoi.cs.pythia.TestsUtilities;
import gr.uoi.cs.pythia.config.SparkConfig;
import gr.uoi.cs.pythia.engine.IDatasetProfiler;
import gr.uoi.cs.pythia.engine.IDatasetProfilerFactory;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.junit.rules.ExternalResource;

import java.io.IOException;

public class ReportResource extends ExternalResource {
    private SparkSession sparkSession;
    private IDatasetProfiler datasetProfiler;
    private String datasetPath;

    public IDatasetProfiler getDatasetProfiler() {
        return datasetProfiler;
    }

    public String getDatasetPath() {
        return datasetPath;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        initializeSpark();
        initializeProfile();
    }

    private void initializeSpark() {
        SparkConfig sparkConfig = new SparkConfig();
        sparkSession =
                SparkSession.builder()
                        .appName(sparkConfig.getAppName())
                        .master(sparkConfig.getMaster())
                        .config("spark.sql.warehouse.dir", sparkConfig.getSparkWarehouse())
                        .getOrCreate();
    }

    private void initializeProfile() throws AnalysisException, IOException {
        StructType schema = TestsUtilities.getPeopleJsonSchema();
        datasetProfiler = new IDatasetProfilerFactory().createDatasetProfiler();
        datasetPath = TestsUtilities.getDatasetPath("people.json");
        datasetProfiler.registerDataset("people", datasetPath, schema);
        datasetProfiler.computeProfileOfDataset();
    }

    @Override
    protected void after() {
        super.after();
        sparkSession.stop();
    }
}
