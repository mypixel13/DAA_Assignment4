# DAA Assignment 4 - Dynamic Programming and String Algorithms

Edit Distance (recursive, table, two rows) and string search (Naive, KMP) for the
"City Search & Spell-Check" scenario, plus my own experiment.

Student: Islam, group SE-2509, Astana IT University

## Project layout

- `src/main/java/daa` - the algorithms, my counters, the input generator and the experiment runner
- `src/test/java/daa` - JUnit 5 tests
- `results/results.csv` - all runs of the experiment
- `results/plots/` - the plots (PNG)
- `plot.py` - draws the plots from results.csv
- `REPORT.md` - Experiment Design Card, plots, prediction vs result, short answers

## Build and test

You need Java 17+ and Maven.

```
mvn test
```

## Run the experiment

```
mvn compile exec:java
python3 plot.py
```

The first command writes `results/results.csv` (takes about 1-2 minutes, the recursive
version with n = 13 is the slow part). The second one needs `matplotlib` and writes
the PNG files to `results/plots/`.

The generator uses a fixed seed (`InputGenerator.SEED = 2026`), so the counters
(operations, memory_cells, result) are always the same. The times depend on the laptop,
so they change a little between runs.

## results.csv columns

| column | meaning |
|---|---|
| family | `edit_distance` or `search` |
| algorithm | recursive, table, two_rows, naive, kmp |
| input | input type (see the Experiment Design Card in REPORT.md) |
| n | string length (edit distance) or text length (search) |
| m | second string length / pattern length |
| time_ms | median of 5 runs |
| operations | recursive: calls, table and two rows: cells computed, search: character comparisons |
| memory_cells | int cells allocated for the DP table(s), 0 for the others |
| result | the distance, or the number of matches |

