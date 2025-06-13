package com.confessly.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MamdaniFuzzySystem {
    private List<FuzzyRule> rules;
    private Map<String, FuzzySet> inputSets;
    private Map<String, FuzzySet> outputSets;

    public MamdaniFuzzySystem() {
        this.rules = new ArrayList<>();
        this.inputSets = new HashMap<>();
        this.outputSets = new HashMap<>();
    }

    public void addRule(FuzzyRule rule) {
        rules.add(rule);
    }

    public void addInputSet(String name, FuzzySet set) {
        inputSets.put(name, set);
    }

    public void addOutputSet(String name, FuzzySet set) {
        outputSets.put(name, set);
    }

    public double infer(Map<String, Double> inputs) {
        // Step 1: Fuzzification
        Map<String, Double> fuzzifiedInputs = new HashMap<>();
        for (Map.Entry<String, Double> input : inputs.entrySet()) {
            FuzzySet set = inputSets.get(input.getKey());
            if (set != null) {
                fuzzifiedInputs.put(input.getKey(), set.getMembership(input.getValue()));
            }
        }

        // Step 2: Rule Evaluation
        List<Double> ruleOutputs = new ArrayList<>();
        for (FuzzyRule rule : rules) {
            double ruleOutput = rule.evaluate(fuzzifiedInputs);
            ruleOutputs.add(ruleOutput);
        }

        // Step 3: Aggregation
        double aggregatedOutput = 0.0;
        for (Double output : ruleOutputs) {
            aggregatedOutput = Math.max(aggregatedOutput, output);
        }

        // Step 4: Defuzzification (using Center of Gravity method)
        double numerator = 0.0;
        double denominator = 0.0;
        
        for (FuzzySet outputSet : outputSets.values()) {
            double[] points = outputSet.getPoints();
            for (double point : points) {
                double membership = outputSet.getMembership(point);
                numerator += point * membership;
                denominator += membership;
            }
        }

        return denominator != 0 ? numerator / denominator : 0.0;
    }
}

class FuzzySet {
    private String name;
    private double[] points;
    private double[] membershipValues;

    public FuzzySet(String name, double[] points, double[] membershipValues) {
        this.name = name;
        this.points = points;
        this.membershipValues = membershipValues;
    }

    public double getMembership(double value) {
        // Linear interpolation between points
        for (int i = 0; i < points.length - 1; i++) {
            if (value >= points[i] && value <= points[i + 1]) {
                double ratio = (value - points[i]) / (points[i + 1] - points[i]);
                return membershipValues[i] + ratio * (membershipValues[i + 1] - membershipValues[i]);
            }
        }
        return 0.0;
    }

    public double[] getPoints() {
        return points;
    }
}

class FuzzyRule {
    private List<String> antecedents;
    private String consequent;
    private String operator; // "AND" or "OR"

    public FuzzyRule(List<String> antecedents, String consequent, String operator) {
        this.antecedents = antecedents;
        this.consequent = consequent;
        this.operator = operator;
    }

    public double evaluate(Map<String, Double> fuzzifiedInputs) {
        if (antecedents.isEmpty()) {
            return 0.0;
        }

        if (operator.equals("AND")) {
            double min = Double.MAX_VALUE;
            for (String antecedent : antecedents) {
                Double value = fuzzifiedInputs.get(antecedent);
                if (value != null) {
                    min = Math.min(min, value);
                }
            }
            return min;
        } else if (operator.equals("OR")) {
            double max = Double.MIN_VALUE;
            for (String antecedent : antecedents) {
                Double value = fuzzifiedInputs.get(antecedent);
                if (value != null) {
                    max = Math.max(max, value);
                }
            }
            return max;
        }

        return 0.0;
    }
} 