package gr.uoi.cs.pythia.decisiontree;

import gr.uoi.cs.pythia.decisiontree.generator.DecisionTreeGeneratorFactory;
import gr.uoi.cs.pythia.decisiontree.input.DecisionTreeParams;
import gr.uoi.cs.pythia.decisiontree.model.DecisionTree;
import gr.uoi.cs.pythia.labeling.Rule;
import gr.uoi.cs.pythia.labeling.RuleSet;
import gr.uoi.cs.pythia.model.Column;
import gr.uoi.cs.pythia.model.DatasetProfile;
import gr.uoi.cs.pythia.model.LabeledColumn;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionTreeManager {

    private final Dataset<Row> dataset;
    private final DatasetProfile datasetProfile;

    public DecisionTreeManager(Dataset<Row> dataset, DatasetProfile datasetProfile) {
        this.dataset = new DecisionTreeOptimizer(dataset).getOptimizedDataset();
        this.datasetProfile = datasetProfile;
    }

    public List<String> extractAllDecisionTrees() {
        List<LabeledColumn> labeledColumns = getLabeledColumns();
        for (LabeledColumn column : labeledColumns) {
            extractAllDecisionTreesForColumn(column, new ArrayList<>());
        }
        return labeledColumns.stream()
                .map(Column::getName)
                .collect(Collectors.toList());
    }

    /**
     * @param allDecisionTreeParams All the user given DecisionTreeParams
     *                              (even multiple for each column).
     */
    public List<String> extractAllDecisionTrees(List<DecisionTreeParams> allDecisionTreeParams) {
        List<LabeledColumn> labeledColumns = getLabeledColumns();
        for (LabeledColumn column : labeledColumns) {
            List<DecisionTreeParams> columnParams = getColumnSpecificParams(column, allDecisionTreeParams);
            extractAllDecisionTreesForColumn(column, columnParams);
        }
        return labeledColumns.stream()
                .map(Column::getName)
                .collect(Collectors.toList());
    }

    private List<LabeledColumn> getLabeledColumns() {
        return datasetProfile.getColumns().stream()
                .filter(column -> column instanceof LabeledColumn)
                .map(column -> (LabeledColumn) column)
                .collect(Collectors.toList());
    }

    private List<DecisionTreeParams> getColumnSpecificParams(Column column,
                                                             List<DecisionTreeParams> allDecisionTreeParams) {
        return allDecisionTreeParams.stream()
                .filter(params -> params.getLabeledColumnName().equals(column.getName()))
                .collect(Collectors.toList());
    }

    private void extractAllDecisionTreesForColumn(LabeledColumn column,
                                                  List<DecisionTreeParams> allColumnParams) {
        if (allColumnParams.isEmpty()) {
            allColumnParams.add(getDefaultDtParams(column.getRuleSet()));
        }
        for (DecisionTreeParams decisionTreeParams : allColumnParams) {
            DecisionTree dt = extractDecisionTree(decisionTreeParams);
            column.addDecisionTree(dt);
        }
    }

    private DecisionTreeParams getDefaultDtParams(RuleSet ruleSet) {
        List<String> targetColumns = ruleSet.getRules().stream()
                .map(Rule::getTargetColumnName)
                .collect(Collectors.toList());
        return new DecisionTreeParams
                .Builder(ruleSet.getNewColumnName(), targetColumns)
                .build();
    }

    private DecisionTree extractDecisionTree(DecisionTreeParams decisionTreeParams) {
        return new DecisionTreeGeneratorFactory(decisionTreeParams, dataset)
                .getDefaultGenerator()
                .computeDecisionTree();
    }
}